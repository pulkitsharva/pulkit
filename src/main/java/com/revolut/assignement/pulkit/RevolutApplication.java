package com.revolut.assignement.pulkit;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.revolut.assignement.pulkit.common.DependencyInjector;
import com.revolut.assignement.pulkit.common.RevolutApplicationConfiguration;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.exception.RevolutExceptionMapperImpl;
import com.revolut.assignement.pulkit.exception.RuntimeExceptionMapper;
import com.revolut.assignement.pulkit.repository.UserRepository;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.jersey.sessions.SessionFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RevolutApplication extends Application<RevolutApplicationConfiguration> {

  private GuiceBundle<RevolutApplicationConfiguration> guiceBundle;

  public static void main(String args[]) throws Exception {
    new RevolutApplication().run(args);
  }

  private final HibernateBundle<RevolutApplicationConfiguration> hibernate =
      new ScanningHibernateBundle<RevolutApplicationConfiguration>(
          "com.revolut.assignement.pulkit") {
        public DataSourceFactory getDataSourceFactory(
            final RevolutApplicationConfiguration configuration) {
          return configuration.getDataSourceFactory();
        }
      };

  public void initialize(final Bootstrap<RevolutApplicationConfiguration> bootstrap) {
    this.guiceBundle =
        GuiceBundle.<RevolutApplicationConfiguration>newBuilder()
            .addModule(new DependencyInjector(hibernate))
            .enableAutoConfig(getClass().getPackage().getName())
            .setConfigClass(RevolutApplicationConfiguration.class)
            .build(Stage.DEVELOPMENT);
    bootstrap.addBundle(hibernate);
    bootstrap.addBundle(this.guiceBundle);
  }

  public void run(RevolutApplicationConfiguration configuration, Environment environment)
      throws Exception {
    environment.jersey().register(new SessionFactoryProvider.Binder());
    environment.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
    environment.jersey().register(new RuntimeExceptionMapper());
    environment.jersey().register(new RevolutExceptionMapperImpl());
  }
}
