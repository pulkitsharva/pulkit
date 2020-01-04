package com.revolut.assignement.pulkit.repository.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.repository.UserRepository;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class UserRepositoryImpl extends AbstractDAO<User> implements UserRepository {

  /**
   * Creates a new DAO with a given session provider.
   *
   * @param sessionFactory a session provider
   */
  @Inject
  public UserRepositoryImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void create(final User user) {
    this.currentSession().save(user);
  }

  @Override
  public User getUserByUserId(final Long userId) {
    Criteria criteria = this.currentSession().createCriteria(User.class);
    criteria.add(Restrictions.eq("userId", userId));
    return (User) criteria.uniqueResult();
  }
}
