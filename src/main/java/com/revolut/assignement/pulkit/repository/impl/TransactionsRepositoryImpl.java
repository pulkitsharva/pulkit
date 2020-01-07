package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.repository.TransactionsRepository;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class TransactionsRepositoryImpl extends AbstractDAO<Transactions> implements
    TransactionsRepository {

  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public TransactionsRepositoryImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void insert(Transactions transactions) {
    this.currentSession().save(transactions);
  }

  @Override
  public void update(Transactions transactions) {
    this.currentSession().update(transactions);
  }

  @Override
  public List<Transactions> getTransactionsByAccountNumber(String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Transactions.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    return criteria.list();
  }

  @Override
  public Transactions getTransactionByTransactionId(Long transactionId) {
    Criteria criteria = this.currentSession().createCriteria(Transactions.class);
    criteria.add(Restrictions.eq("transactionId", transactionId));
    return (Transactions) criteria.uniqueResult();
  }

  @Override
  public Transactions getTransactionsByPaymentGatewayTransactionId(
      String paymentGatewayTransactionId) {
    Criteria criteria = this.currentSession().createCriteria(Transactions.class);
    criteria.add(Restrictions.eq("paymentGatewayTransactionId", paymentGatewayTransactionId));
    List<Transactions> transactions = criteria.list();
    if(transactions!=null && !transactions.isEmpty()){
      return transactions.get(0);
    }
    return null;
  }
}
