package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.repository.DebitRepository;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class DebitRepositoryImpl extends AbstractDAO<Debit> implements DebitRepository {


  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public DebitRepositoryImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void insert(final Debit debit) {
    this.currentSession().save(debit);
  }

  @Override
  public void update(Debit debit) {
    this.currentSession().update(debit);
  }

  @Override
  public List<Debit> getDebitsByAccountNumber(String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Debit.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    return criteria.list();
  }

  @Override
  public Debit getDebitByTransactionId(Long transactionId) {
    Criteria criteria = this.currentSession().createCriteria(Debit.class);
    criteria.add(Restrictions.eq("transactionId", transactionId));
    return (Debit) criteria.uniqueResult();
  }
}
