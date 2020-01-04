package com.revolut.assignement.pulkit.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
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

  }
}
