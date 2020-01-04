package com.revolut.assignement.pulkit.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import com.revolut.assignement.pulkit.repository.CreditRepository;
import com.revolut.assignement.pulkit.repository.StatementRepository;
import com.revolut.assignement.pulkit.repository.UserRepository;
import com.revolut.assignement.pulkit.repository.impl.AccountsRepositoryImpl;
import com.revolut.assignement.pulkit.repository.impl.CreditRepositoryImpl;
import com.revolut.assignement.pulkit.repository.impl.StatementRepositoryImpl;
import com.revolut.assignement.pulkit.repository.impl.UserRepositoryImpl;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

public class DependencyInjector extends AbstractModule {

  private final HibernateBundle hibernateBundle;

  public DependencyInjector(final HibernateBundle hibernateBundle) {
    this.hibernateBundle = hibernateBundle;
  }

  @Provides
  @Singleton
  public SessionFactory provideSessionFactory() {
    return this.hibernateBundle.getSessionFactory();
  }

  public void configure() {
    this.bind(UserRepository.class).to(UserRepositoryImpl.class).in(Singleton.class);
    this.bind(AccountsRepository.class).to(AccountsRepositoryImpl.class).in(Singleton.class);
    this.bind(StatementRepository.class).to(StatementRepositoryImpl.class).in(Singleton.class);
    this.bind(CreditRepository.class).to(CreditRepositoryImpl.class).in(Singleton.class);
  }
}
