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
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.dto.MoneyTransferResponseDto;
import com.revolut.assignement.pulkit.exception.AccountNotFoundException;
import com.revolut.assignement.pulkit.exception.AccountStatusNotValidException;
import com.revolut.assignement.pulkit.exception.DuplicateTransferRequestException;
import com.revolut.assignement.pulkit.exception.ErrorCode;
import com.revolut.assignement.pulkit.exception.InsufficientBalanceException;
import com.revolut.assignement.pulkit.repository.TransactionsRepository;
import com.revolut.assignement.pulkit.service.AccountService;
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
  private StatementService statementService;

  @Inject
  private TransactionsRepository transactionsRepository;

  @Override
  //initiateMoneyTransfer
  public MoneyTransferResponseDto doTransfer(final String accountNumber, final MoneyTransferRequestDto requestDto)
      throws AccountNotFoundException, AccountStatusNotValidException,
      InsufficientBalanceException, DuplicateTransferRequestException {

    Accounts senderAccountDetails = getAccount(accountNumber);
    validateAccount(senderAccountDetails, accountNumber);

    //read guava precondition
    if (senderAccountDetails.getAvailableBalance().compareTo(requestDto.getAmount()) < 0) {
      log.error("Account doesnt have balance, accountNumber:{}, balance:{}, requestedTransactionAmount:{}",accountNumber, senderAccountDetails.getAvailableBalance(), requestDto.getAmount());
      throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE, accountNumber);
    }

    Accounts receiverAccountDetails = getAccount(requestDto.getReceiverAccountNumber());
    validateAccount(receiverAccountDetails, requestDto.getReceiverAccountNumber());

    Transactions existingTransferRequest = (Transactions) SessionManager.executeInSession(session ->{
      return transactionsRepository.getTransactionsByPaymentGatewayTransactionId(requestDto.getPaymentGatewayTransactionId());
    },sessionFactory);

    if(existingTransferRequest!=null && TransactionStatus.SUCCESS.equals(existingTransferRequest.getStatus())){
      throw new DuplicateTransferRequestException(ErrorCode.DUPLICATE_TRANSFER_REQUEST, requestDto.getPaymentGatewayTransactionId());
    }

    Transactions transactions = (Transactions) SessionManager.executeInSessionInTransaction(
        session -> {
          try{
            Accounts senderAccountWithLock = accountService.getAccountByAccountNumberWithLock(accountNumber);
            Transactions debit = postTransactionAtAccount(accountNumber, requestDto, SubTransactionType.MONEY_TRANSFER);
            Statement debitStatement = statementService.postStatementAtAccount(accountNumber, requestDto, debit,
                TransactionType.DEBIT);
            accountService.reduceBalanceOnSuccessfulTransaction(senderAccountWithLock, debit.getAmount());

            Accounts receiverAccountWithLock = accountService.getAccountByAccountNumberWithLock(requestDto.getReceiverAccountNumber());
            Transactions credit = postTransactionAtAccount(requestDto.getReceiverAccountNumber(), requestDto, SubTransactionType.MONEY_TRANSFER);
            Statement creditStatement = statementService.postStatementAtAccount(accountNumber, requestDto, credit, TransactionType.CREDIT);
            accountService.increaseBalanceOnSuccessfulTransaction(receiverAccountWithLock, credit.getAmount());
            log.info("Transfer successfully done, sender:{}, receiver:{}", accountNumber, requestDto.getReceiverAccountNumber());
            return debit;
          }
          catch(Exception e){
            log.error("Unable to take lock or do transfer, rolling back. sender:{}, receiver:{}, error:{}", accountNumber, requestDto.getReceiverAccountNumber(), e);
            throw new RuntimeException(e);
          }
        },
        sessionFactory);
    MoneyTransferResponseDto moneyTransferResponseDto = MoneyTransferResponseDto.builder()
        .paymentGatewayTransactionId(requestDto.getPaymentGatewayTransactionId())
        .externalReferenceId(transactions.getExternalReferenceId())
        .timestamp(transactions.getCreatedAt().getTime())
        .status(transactions.getStatus())
        .build();
    return moneyTransferResponseDto;
  }

  private void validateAccount(final Accounts account, final String accountNumber)
      throws AccountNotFoundException, AccountStatusNotValidException {
    if (account == null) {
      log.error("Account not found, accountNumber:{}",accountNumber);
      throw new AccountNotFoundException(ErrorCode.INVALID_ACCOUNT, accountNumber);
    }
    if (!AccountStatus.ACTIVE.equals(account.getAccountStatus())) {
      log.error("Account status is not active, accountNumber:{}, status:{}",accountNumber, account.getAccountStatus());
      throw new AccountStatusNotValidException(ErrorCode.INVALID_ACCOUNT_STATUS, accountNumber);
    }


  }
  private Accounts getAccount(final String accountNumber){
    Accounts account =
        (Accounts)
            SessionManager.executeInSession(
                session -> {
                  return accountService.getAccountByAccountNumber(accountNumber);
                },
                sessionFactory);
    return account;
  }

  private Transactions postTransactionAtAccount(final String accountNumber, final MoneyTransferRequestDto requestDto, final SubTransactionType subTransactionType){
    try{
      TransactionMetadata transactionMetadata = Imapper.INSTANCE.transferRequestToTransactionMetadata(requestDto, accountNumber);
      String transactionMetadataString = Imapper.INSTANCE.fromDtotoJsonString(transactionMetadata);
      Transactions transactions = Imapper.INSTANCE.transferRequestToDebitEntity(requestDto, accountNumber, subTransactionType, TransactionStatus.SUCCESS, transactionMetadataString);
      transactionsRepository.insert(transactions);
      String externalReferenceId = CommonsUtil.getExternalReferenceId(transactions.getTransactionId());
      transactions.setExternalReferenceId(externalReferenceId);
      transactionsRepository.update(transactions);
      log.info(
          "Posted {} transaction of amount:{} posted successfully at account:{}",
          transactions.getType(),
          transactions.getAmount(),
          transactions.getAccountNumber());
      return transactions;
    }
    catch (Exception e){
      log.error("Unable to post transaction for account:{}, rolling back. Error:{}", accountNumber, e);
      throw new RuntimeException("Unable to post transaction for account:{}, rolling back.");
    }
  }

}
