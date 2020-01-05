package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.repository.StatementRepository;
import com.revolut.assignement.pulkit.service.StatementService;
import java.util.List;

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
}
