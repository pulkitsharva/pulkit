package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.repository.StatementRepository;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class StatementRepositoryImpl extends AbstractDAO<Statement> implements StatementRepository {

  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public StatementRepositoryImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void insert(final Statement statement) {
    this.currentSession().save(statement);
  }

  @Override
  public List<Statement> getStatementByAccountNumber(String accountNumber) {
    Criteria criteria = this.currentSession().createCriteria(Statement.class);
    criteria.add(Restrictions.eq("accountNumber", accountNumber));
    criteria.add(Restrictions.eq("visibleToUI", Boolean.TRUE));
    return criteria.list();
  }

  @Override
  public Statement getStatementByStatementId(Long statementId) {
    Criteria criteria = this.currentSession().createCriteria(Statement.class);
    criteria.add(Restrictions.eq("statementId", statementId));
    return (Statement) criteria.uniqueResult();
  }

  @Override
  public void update(Statement statement) {
    this.currentSession().update(statement);
  }

  @Override
  public List<Statement> getAll() {
    Criteria criteria = this.currentSession().createCriteria(Statement.class);
    return criteria.list();
  }
}
