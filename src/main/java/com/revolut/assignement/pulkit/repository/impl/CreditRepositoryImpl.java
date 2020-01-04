package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.repository.CreditRepository;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class CreditRepositoryImpl extends AbstractDAO<Credit> implements CreditRepository {

  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public CreditRepositoryImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void insert(final Credit credit) {
    this.currentSession().save(credit);
  }

  @Override
  public void update(final Credit credit) {
    this.currentSession().update(credit);
  }

  @Override
  public List<Credit> getCreditsByAccountNumber(String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Credit.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    return criteria.list();
  }

  @Override
  public Credit getCreditByTransactionId(Long transactionId) {
    Criteria criteria = this.currentSession().createCriteria(Credit.class);
    criteria.add(Restrictions.eq("transactionId", transactionId));
    return (Credit) criteria.uniqueResult();
  }
}
