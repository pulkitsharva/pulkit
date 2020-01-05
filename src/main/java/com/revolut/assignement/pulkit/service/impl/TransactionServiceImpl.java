package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.AccountStatus;
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

    Accounts senderAccountDetails = getAccount(accountNumber);
    validateAccount(senderAccountDetails, accountNumber);

    if (senderAccountDetails.getAvailableBalance().compareTo(requestDto.getAmount()) < 0) {
      log.error("Account doesnt have balance, accountNumber:{}, balance:{}, requestedTransactionAmount:{}",accountNumber, senderAccountDetails.getAvailableBalance(), requestDto.getAmount());
      throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE, accountNumber);
    }

    Accounts receiverAccountDetails = getAccount(requestDto.getReceiverAccountNumber());
    validateAccount(receiverAccountDetails, requestDto.getReceiverAccountNumber());

    SessionManager.executeInSessionInTransaction(
        session -> {
          try{
            Accounts senderAccountWithLock = accountService.getAccountByAccountNumberWithLock(accountNumber);
            Debit debit = debitService.postDebitTransactionAtSourceAccount(accountNumber, requestDto);
            Statement debitStatement = statementService.postStatementAtSourceAccount(accountNumber, requestDto, debit);
            accountService.reduceBalanceOnSuccessfulTransaction(senderAccountWithLock, debit.getAmount());

            Accounts receiverAccountWithLock = accountService.getAccountByAccountNumberWithLock(requestDto.getReceiverAccountNumber());
            Credit credit = creditService.postCreditTransactionAtDestinationAccount(accountNumber, requestDto);
            Statement creditStatement = statementService.postStatementAtDestinationAccount(accountNumber, requestDto, credit);
            accountService.increaseBalanceOnSuccessfulTransaction(receiverAccountWithLock, credit.getAmount());
            log.info("Transfer successfully done, sender:{}, receiver:{}", accountNumber, requestDto.getReceiverAccountNumber());
          }
          catch(Exception e){
            log.error("Unable to take lock or do transfer, rolling back. sender:{}, receiver:{}, error:{}", accountNumber, requestDto.getReceiverAccountNumber(), e);
            throw new RuntimeException("Something went wrong");
          }
          return null;
        },
        sessionFactory);
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
}
