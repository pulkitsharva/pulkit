package com.revolut.assignement.pulkit.service;

import com.revolut.assignement.pulkit.common.TransactionType;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import java.util.List;

public interface StatementService {

  void insert(final Statement statement);

  List<Statement> getStatementByAccountNumber(final String accountNumber);

  Statement getStatementByStatementId(final Long statementId);

  void update(final Statement statement);

  Statement postStatementAtAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Transactions transactions, final
      TransactionType transactionType);

}
