package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.repository.DebitRepository;
import com.revolut.assignement.pulkit.service.DebitService;
import java.util.List;

public class DebitServiceImpl implements DebitService {

  @Inject
  private DebitRepository debitRepository;

  @Override
  public void insert(final Debit debit) {
    debitRepository.insert(debit);
  }

  @Override
  public void update(Debit debit) {
    debitRepository.update(debit);
  }

  @Override
  public List<Debit> getDebitsByAccountNumber(String accountNumber) {
    return debitRepository.getDebitsByAccountNumber(accountNumber);
  }

  @Override
  public Debit getDebitByTransactionId(Long transactionId) {
    return debitRepository.getDebitByTransactionId(transactionId);
  }
}
