package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.common.CommonsUtil;
import com.revolut.assignement.pulkit.common.Imapper;
import com.revolut.assignement.pulkit.common.SubTransactionType;
import com.revolut.assignement.pulkit.common.TransactionMetadata;
import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.common.TransactionType;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.exception.AccountNotFoundException;
import com.revolut.assignement.pulkit.exception.AccountStatusNotValidException;
import com.revolut.assignement.pulkit.exception.ErrorCode;
import com.revolut.assignement.pulkit.exception.InsufficientBalanceException;
import com.revolut.assignement.pulkit.service.AccountService;
import com.revolut.assignement.pulkit.service.CreditService;
import com.revolut.assignement.pulkit.service.DebitService;
import com.revolut.assignement.pulkit.service.StatementService;
import com.revolut.assignement.pulkit.service.TransactionService;
import com.revolut.assignement.pulkit.util.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

@Slf4j
public class TransactionServiceImpl implements TransactionService {

  @Inject
  private SessionFactory sessionFactory;

  @Inject
  private AccountService accountService;

  @Inject
  private DebitService debitService;

  @Inject
  private CreditService creditService;

  @Inject
  private StatementService statementService;

  @Override
  public void doTransfer(final String accountNumber, final MoneyTransferRequestDto requestDto)
      throws AccountNotFoundException, AccountStatusNotValidException,
          InsufficientBalanceException {
    Accounts senderAccountDetails =
        (Accounts)
            SessionManager.executeInSession(
                session -> {
                  return accountService.getAccountByAccountNumber(accountNumber);
                },
                sessionFactory);
    if (senderAccountDetails == null) {
      throw new AccountNotFoundException(ErrorCode.INVALID_ACCOUNT, accountNumber);
    }
    if (!AccountStatus.ACTIVE.equals(senderAccountDetails.getAccountStatus())) {
      throw new AccountStatusNotValidException(ErrorCode.INVALID_ACCOUNT_STATUS, accountNumber);
    }
    if (senderAccountDetails.getAvailableBalance().compareTo(requestDto.getAmount()) < 0) {
      throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE, accountNumber);
    }

    Accounts receiverAccountDetails =
        (Accounts)
            SessionManager.executeInSession(
                session -> {
                  return accountService.getAccountByAccountNumber(
                      requestDto.getReceiverAccountNumber());
                },
                sessionFactory);
    if (receiverAccountDetails == null) {
      throw new AccountNotFoundException(
          ErrorCode.INVALID_ACCOUNT, requestDto.getReceiverAccountNumber());
    }
    if (!AccountStatus.ACTIVE.equals(receiverAccountDetails.getAccountStatus())) {
      throw new AccountStatusNotValidException(
          ErrorCode.INVALID_ACCOUNT_STATUS, requestDto.getReceiverAccountNumber());
    }
    SessionManager.executeInSessionInTransaction(
        session -> {
          Accounts senderAccountWithLock = accountService.getAccountByAccountNumberWithLock(accountNumber);
          Debit debit = postDebitTransactionAtSourceAccount(accountNumber, requestDto);
          Statement debitStatement = postStatementAtSourceAccount(accountNumber, requestDto, debit);
          accountService.reduceBalanceOnSuccessfulTransaction(senderAccountWithLock, debit.getAmount());

          Accounts receiverAccountWithLock = accountService.getAccountByAccountNumberWithLock(requestDto.getReceiverAccountNumber());
          Credit credit = postCreditTransactionAtDestinationAccount(accountNumber, requestDto);
          Statement creditStatement = postStatementAtDestinationAccount(accountNumber, requestDto, credit);
          accountService.increaseBalanceOnSuccessfulTransaction(receiverAccountWithLock, credit.getAmount());
          return null;
        },
        sessionFactory);
  }

  public Debit postDebitTransactionAtSourceAccount(final String accountNumber, final MoneyTransferRequestDto requestDto){
    try{
      TransactionMetadata transactionMetadata = Imapper.INSTANCE.transferRequestToTransactionMetadata(requestDto);
      String transactionMetadataString = Imapper.INSTANCE.fromDtotoJsonString(transactionMetadata);
      Debit debit = Imapper.INSTANCE.transferRequestToDebitEntity(requestDto, accountNumber, SubTransactionType.MONEY_TRANSFER, TransactionStatus.SUCCESS, transactionMetadataString);
      debitService.insert(debit);
      String externalReferenceId = CommonsUtil.getExternalReferenceId(debit.getTransactionId());
      debit.setExternalReferenceId(externalReferenceId);
      debitService.update(debit);
      return debit;
    }
    catch (Exception e){
      log.error("Unable to post transaction for account:{}, rolling back. Error:{}", accountNumber, e);
      throw new RuntimeException("Unable to post transaction for account:{}, rolling back.");
    }

  }

  public Credit postCreditTransactionAtDestinationAccount(final String accountNumber, final MoneyTransferRequestDto requestDto){
    try{
      TransactionMetadata transactionMetadata = Imapper.INSTANCE.transferRequestToTransactionMetadata(requestDto);
      transactionMetadata.setSenderAccountNumber(accountNumber);
      String transactionMetadataString = Imapper.INSTANCE.fromDtotoJsonString(transactionMetadata);
      Credit credit = Imapper.INSTANCE.transferRequestToCreditEntity(requestDto, accountNumber, SubTransactionType.MONEY_TRANSFER, TransactionStatus.SUCCESS, transactionMetadataString);
      creditService.insert(credit);
      String externalReferenceId = CommonsUtil.getExternalReferenceId(credit.getTransactionId());
      credit.setExternalReferenceId(externalReferenceId);
      creditService.update(credit);
      return credit;
    }
    catch (Exception e){
      log.error("Unable to post transaction for account:{}, rolling back. Error:{}", accountNumber, e);
      throw new RuntimeException("Unable to post transaction for account:{}, rolling back.");
    }

  }

  public Statement postStatementAtSourceAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Debit debit) {
    Statement statement =
        Statement.builder()
            .accountNumber(accountNumber)
            .referenceId(debit.getTransactionId())
            .type(TransactionType.DEBIT)
            .status(TransactionStatus.SUCCESS)
            .amount(debit.getAmount())
            .build();
    statementService.insert(statement);
    return statement;
  }

  public Statement postStatementAtDestinationAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Credit credit) {
    Statement statement =
        Statement.builder()
            .accountNumber(requestDto.getReceiverAccountNumber())
            .referenceId(credit.getTransactionId())
            .type(TransactionType.CREDIT)
            .status(TransactionStatus.SUCCESS)
            .amount(credit.getAmount())
            .build();
    statementService.insert(statement);
    return statement;
  }
}
