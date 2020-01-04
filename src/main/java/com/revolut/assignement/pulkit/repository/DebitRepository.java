package com.revolut.assignement.pulkit.repository;

import com.revolut.assignement.pulkit.dao.Debit;
import java.util.List;

public interface DebitRepository {

  void insert(final Debit debit);

  void update(final Debit debit);

  List<Debit> getDebitsByAccountNumber(final String accountNumber);

  Debit getDebitByTransactionId(final Long transactionId);

}
