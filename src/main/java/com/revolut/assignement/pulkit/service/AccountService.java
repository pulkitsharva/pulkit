package com.revolut.assignement.pulkit.service;

import com.revolut.assignement.pulkit.dao.Accounts;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

  void insert(final Accounts accounts);

  List<Accounts> getAllAccounts();

  Accounts getAccountByAccountNumber(final String accountNumber);

  void update(final Accounts accounts);

  Accounts getAccountByAccountNumberWithLock(final String accountNumber);

  void reduceBalanceOnSuccessfulTransaction(final Accounts accounts, final BigDecimal amount);

  void increaseBalanceOnSuccessfulTransaction(final Accounts accounts, final BigDecimal amount);
}
