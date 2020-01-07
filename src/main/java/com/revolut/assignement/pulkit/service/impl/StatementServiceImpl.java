package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.TransactionType;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.Transactions;
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

  public Statement postStatementAtAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Transactions transactions, final
      TransactionType transactionType) {
    Statement statement =
        Statement.builder()
            .accountNumber(transactions.getAccountNumber())
            .referenceId(transactions.getTransactionId())
            .type(transactionType)
            .status(transactions.getStatus())
            .amount(transactions.getAmount())
            .build();
    statementRepository.insert(statement);
    log.info(
        "Statement entry done for transactionId:{}, type:{}, amount:{}, account:{}",
        transactions.getTransactionId(),
        statement.getType(),
        statement.getAmount(),
        statement.getAccountNumber());
    return statement;
  }

}
