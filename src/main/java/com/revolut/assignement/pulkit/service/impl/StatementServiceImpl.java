package com.revolut.assignement.pulkit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.Imapper;
import com.revolut.assignement.pulkit.common.TransactionType;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.dto.StatementMetadata;
import com.revolut.assignement.pulkit.dto.StatementResponseDto;
import com.revolut.assignement.pulkit.repository.StatementRepository;
import com.revolut.assignement.pulkit.service.StatementService;
import com.revolut.assignement.pulkit.util.SessionManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

@Slf4j
public class StatementServiceImpl implements StatementService {

  @Inject
  private StatementRepository statementRepository;

  @Inject
  private SessionFactory sessionFactory;

  public void insert(final Statement statement){
    statementRepository.insert(statement);
  }

  public StatementResponseDto getStatementByAccountNumber(final String accountNumber){
    StatementResponseDto statements =
        (StatementResponseDto)
            SessionManager.executeInSession(
                session -> {
                  try {
                    List<Statement> statementList =
                        statementRepository.getStatementByAccountNumber(accountNumber);
                    StatementResponseDto statementResponseDtos =
                        Imapper.INSTANCE.map(statementList);
                    System.out.println(statementResponseDtos);
                    return statementResponseDtos;
                  } catch (Exception exception) {
                    log.error(
                        "Unable to get user's statement, account:{}, exception:{}",
                        accountNumber,
                        exception);
                    throw new RuntimeException("Unable to get user's statement");
                  }
                },
                this.sessionFactory);
    return statements;
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
    StatementMetadata statementMetadata = Imapper.INSTANCE.transferTransactionToStatementMetadata(transactions);
    String metadataString = null;
    try{
      metadataString = Imapper.INSTANCE.fromDtotoJsonString(statementMetadata);
    } catch (JsonProcessingException exception) {
      log.error("Unable to store metadata, accountNumber:{}, error:{}",transactions.getAccountNumber(), exception);
    }
    Statement statement =
        Statement.builder()
            .accountNumber(transactions.getAccountNumber())
            .referenceId(transactions.getTransactionId())
            .type(transactionType)
            .status(transactions.getStatus())
            .amount(transactions.getAmount())
            .metadata(metadataString)
            .visibleToUI(Boolean.TRUE)
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
