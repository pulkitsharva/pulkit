package com.revolut.assignement.pulkit.service;

import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import java.util.List;

public interface StatementService {

  void insert(final Statement statement);

  List<Statement> getStatementByAccountNumber(final String accountNumber);

  Statement getStatementByStatementId(final Long statementId);

  void update(final Statement statement);

  Statement postStatementAtSourceAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Debit debit);

  Statement postStatementAtDestinationAccount(
      final String accountNumber, final MoneyTransferRequestDto requestDto, final Credit credit);
}
