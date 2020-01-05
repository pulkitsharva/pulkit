package com.revolut.assignement.pulkit.service;

import com.revolut.assignement.pulkit.dao.Statement;
import java.util.List;

public interface StatementService {

  void insert(final Statement statement);

  List<Statement> getStatementByAccountNumber(final String accountNumber);

  Statement getStatementByStatementId(final Long statementId);

  void update(final Statement statement);
}
