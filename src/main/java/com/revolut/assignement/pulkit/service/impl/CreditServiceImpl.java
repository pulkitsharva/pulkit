package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.repository.CreditRepository;
import com.revolut.assignement.pulkit.service.CreditService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreditServiceImpl implements CreditService {

  @Inject
  private CreditRepository creditRepository;

  @Override
  public void insert(final Credit credit) {
    creditRepository.insert(credit);
  }

  @Override
  public void update(final Credit credit) {
    creditRepository.update(credit);
  }

  @Override
  public List<Credit> getCreditsByAccountNumber(final String accountNumber) {
    return creditRepository.getCreditsByAccountNumber(accountNumber);
  }

  @Override
  public Credit getCreditByTransactionId(final Long transactionId) {
    return creditRepository.getCreditByTransactionId(transactionId);
  }
}
