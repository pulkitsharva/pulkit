package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import com.revolut.assignement.pulkit.service.AccountService;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class AccountServiceImpl implements AccountService {

  @Inject private AccountsRepository accountsRepository;

  @Override
  public void insert(final Accounts accounts) {
    if(accounts == null){
      throw new RuntimeException("entity cannot be null");
    }
    accountsRepository.insert(accounts);
  }

  @Override
  public List<Accounts> getAllAccounts() {
    return accountsRepository.getAllAccounts();
  }

  @Override
  public Accounts getAccountByAccountNumber(final String accountNumber) {
    if(StringUtils.isEmpty(accountNumber)){
      throw new RuntimeException("accountNumber cannot be null");
    }
    return accountsRepository.getAccountByAccountNumber(accountNumber);
  }

  @Override
  public void update(final Accounts accounts) {
    if(accounts == null){
      throw new RuntimeException("entity cannot be null");
    }
    accountsRepository.update(accounts);
  }

  @Override
  public Accounts getAccountByAccountNumberWithLock(final String accountNumber) {
    return accountsRepository.getAccountByAccountNumberWithLock(accountNumber);
  }

  @Override
  public void reduceBalanceOnSuccessfulTransaction(Accounts accounts,
      final BigDecimal amount) {
    try{
      accounts.setAvailableBalance(accounts.getAvailableBalance().subtract(amount).setScale(2));
    }
    catch(Exception e){
      log.error("Available balance is null, error:{}", e);
      throw new RuntimeException("available balance is null");
    }
    update(accounts);
  }

  @Override
  public void increaseBalanceOnSuccessfulTransaction(Accounts accounts,
      final BigDecimal amount) {
    try{
      accounts.setAvailableBalance(accounts.getAvailableBalance().add(amount).setScale(2));
    }
    catch(Exception e){
      log.error("Available balance/amount is null, error:{}", e);
      throw new RuntimeException("available balance/amount is null");
    }
    update(accounts);
  }
}
