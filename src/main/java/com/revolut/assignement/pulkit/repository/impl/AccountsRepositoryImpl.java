package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class AccountsRepositoryImpl extends AbstractDAO<Accounts> implements AccountsRepository {

  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public AccountsRepositoryImpl(final SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void insert(final Accounts accounts) {
    this.currentSession().save(accounts);
  }

  @Override
  public List<Accounts> getAllAccounts() {
    Criteria criteria = this.currentSession().createCriteria(Accounts.class);
    return criteria.list();
  }

  @Override
  public Accounts getAccountByAccountNumber(final String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Accounts.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    return (Accounts) criteria.uniqueResult();
  }

  @Override
  public Accounts getAccountByAccountNumberWithLock(final String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Accounts.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    criteria.setLockMode(LockMode.PESSIMISTIC_WRITE);
    return (Accounts) criteria.uniqueResult();
  }

  @Override
  public void update(final Accounts accounts) {
    this.currentSession().update(accounts);
  }
}
