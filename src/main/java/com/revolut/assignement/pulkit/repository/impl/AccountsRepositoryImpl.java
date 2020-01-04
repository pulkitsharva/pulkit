package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class AccountsRepositoryImpl extends AbstractDAO<Accounts> implements AccountsRepository {

  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public AccountsRepositoryImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void insert(Accounts accounts) {
    this.currentSession().save(accounts);
  }

  @Override
  public List<Accounts> getAllAccounts() {
    Criteria criteria = this.currentSession().createCriteria(Accounts.class);
    return criteria.list();
  }

  @Override
  public Accounts getAccountByAccountNumber(String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Accounts.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    return (Accounts) criteria.uniqueResult();
  }

  @Override
  public void update(Accounts accounts) {
    this.currentSession().update(accounts);
  }
}
