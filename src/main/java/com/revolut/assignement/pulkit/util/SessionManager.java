package com.revolut.assignement.pulkit.util;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

@Slf4j
public class SessionManager {

  public static Object executeInSessionInTransaction(final Function callback,
      final SessionFactory sessionFactory) {
    Session newSession = null;
    Session prevSession = null;
    try {
      newSession = sessionFactory.openSession();
      newSession.setFlushMode(FlushMode.COMMIT);
      /* Get the existing session, and store it to bind it back for the caller */
      prevSession = ManagedSessionContext.bind(newSession);
      Transaction transaction = newSession.beginTransaction();
      try {
        Object retVal = callback.apply(newSession);
        newSession.flush();
        transaction.commit();
        return retVal;
      } catch (Exception e) {
        transaction.rollback();
        throw e;
      }
    } catch (Exception e) {
      log.error("Error in SessionManager, error:{}", e);
      throw e;
    } finally {
      if (newSession != null && newSession.isOpen()) {
        newSession.clear();
        newSession.close();
      } else {
        log.error("mysql connection found null while closing");
      }
      ManagedSessionContext.unbind(sessionFactory);
      if (prevSession != null) {
        ManagedSessionContext.bind(prevSession);
      }
    }
  }

  public static Object executeInSession(final Function callback,
      final SessionFactory sessionFactory) {
    Session session = null;
    try {
      session = sessionFactory.openSession();
      ManagedSessionContext.bind(session);
      return callback.apply(session);
    } finally {
      if (session != null) {
        session.close();
      }
      ManagedSessionContext.unbind(sessionFactory);
    }
  }
}