package com.revolut.assignement.pulkit.repository;

import com.revolut.assignement.pulkit.dao.Transactions;
import java.util.List;

public interface TransactionsRepository {

  void insert(final Transactions transactions);

  void update(final Transactions transactions);

  List<Transactions> getTransactionsByAccountNumber(final String accountNumber);

  Transactions getTransactionByTransactionId(final Long transactionId);

  Transactions getTransactionsByPaymentGatewayTransactionId(final String paymentGatewayTransactionId);
}
