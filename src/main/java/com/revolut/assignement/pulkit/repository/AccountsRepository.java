package com.revolut.assignement.pulkit.repository;

import com.revolut.assignement.pulkit.dao.Accounts;
import java.util.List;

public interface AccountsRepository {

  void insert(final Accounts accounts);

  List<Accounts> getAllAccounts();

  Accounts getAccountByAccountNumber(final String accountNumber);

  Accounts getAccountByAccountNumberWithLock(final String accountNumber);

  void update(final Accounts accounts);

}
