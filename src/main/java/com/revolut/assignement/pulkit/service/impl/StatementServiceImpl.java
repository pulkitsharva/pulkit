package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.SubTransactionType;
import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.common.TransactionType;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.repository.StatementRepository;
import com.revolut.assignement.pulkit.service.StatementService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatementServiceImpl implements StatementService {

  @Inject
  private StatementRepository statementRepository;

  public void insert(final Statement statement){
    statementRepository.insert(statement);
  }

  public List<Statement> getStatementByAccountNumber(final String accountNumber){
    return statementRepository.getStatementByAccountNumber(accountNumber);
  }

  public Statement getStatementByStatementId(final Long statementId){
    return statementRepository.getStatementByStatementId(statementId);
  }

  public void update(final Statement statement){
    statementRepository.update(statement);
  }

  public Statement postStatementAtSourceAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Debit debit) {
    Statement statement =
        Statement.builder()
            .accountNumber(accountNumber)
            .referenceId(debit.getTransactionId())
            .type(debit.getType().getTransactionType())
            .status(debit.getStatus())
            .amount(debit.getAmount())
            .build();
    statementRepository.insert(statement);
    log.info(
        "Statement entry done for transactionId:{}, type:{}, amount:{}, account:{}",
        debit.getTransactionId(),
        statement.getType(),
        statement.getAmount(),
        statement.getAccountNumber());
    return statement;
  }

  public Statement postStatementAtDestinationAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Credit credit) {
    Statement statement =
        Statement.builder()
            .accountNumber(requestDto.getReceiverAccountNumber())
            .referenceId(credit.getTransactionId())
            .type(credit.getType().getTransactionType())
            .status(credit.getStatus())
            .amount(credit.getAmount())
            .build();
    statementRepository.insert(statement);
    log.info(
        "Statement entry done for transactionId:{}, type:{}, amount:{}, account:{}",
        credit.getTransactionId(),
        statement.getType(),
        statement.getAmount(),
        statement.getAccountNumber());
    return statement;
  }
}
