package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import com.revolut.assignement.pulkit.service.AccountService;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountServiceImpl implements AccountService {

  @Inject private AccountsRepository accountsRepository;

  @Override
  public void insert(final Accounts accounts) {
    accountsRepository.insert(accounts);
  }

  @Override
  public List<Accounts> getAllAccounts() {
    return accountsRepository.getAllAccounts();
  }

  @Override
  public Accounts getAccountByAccountNumber(final String accountNumber) {
    return accountsRepository.getAccountByAccountNumber(accountNumber);
  }

  @Override
  public void update(final Accounts accounts) {
    accountsRepository.update(accounts);
  }

  @Override
  public Accounts getAccountByAccountNumberWithLock(final String accountNumber) {
    return accountsRepository.getAccountByAccountNumberWithLock(accountNumber);
  }

  @Override
  public void reduceBalanceOnSuccessfulTransaction(Accounts accounts,
      final BigDecimal amount) {
    accounts.setAvailableBalance(accounts.getAvailableBalance().subtract(amount).setScale(2));
    update(accounts);
  }

  @Override
  public void increaseBalanceOnSuccessfulTransaction(Accounts accounts,
      final BigDecimal amount) {
    accounts.setAvailableBalance(accounts.getAvailableBalance().add(amount).setScale(2));
    update(accounts);
  }
}
