package com.vod1.exchange.config.jpa;

import com.google.inject.Inject;

import com.vod1.exchange.dao.AbstractDao;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author ivan
 */
public class DaoMethodInterceptor implements MethodInterceptor {

  private static final Logger log = LoggerFactory.getLogger(DaoMethodInterceptor.class);

  @Inject
  private EntityManagerFactory emf;
  private final boolean useTransaction;

  public DaoMethodInterceptor(boolean useTransaction) {
    this.useTransaction = useTransaction;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    EntityManager entityManager = emf.createEntityManager();
    Object dao = invocation.getThis();
    Method daoMethod = invocation.getMethod();

    if (!AbstractDao.class.isAssignableFrom(dao.getClass())) {
      throw new IllegalStateException(
          dao.getClass().getName() + " must extend " + AbstractDao.class.getName());
    }

    Method setMethod = dao.getClass().getMethod("setEntityManager", EntityManager.class);
    try {
      setMethod.invoke(dao, entityManager);
    } catch (InvocationTargetException e) {
      throw new IllegalStateException(String.format(
          "%s.%s cannot be nested", daoMethod.getDeclaringClass(), daoMethod.getName()), e);
    }

    try {
      if (useTransaction) {
        entityManager.getTransaction().begin();
      }
      Object result = invocation.proceed();
      if (useTransaction) {
        entityManager.getTransaction().commit();
      }
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);

      if (useTransaction) {
        entityManager.getTransaction().rollback();
      }

      throw new IllegalStateException(
          String.format("Failed to execute %s.%s, rolled back",
                        daoMethod.getDeclaringClass(), daoMethod.getName()), e);
    } finally {
      try {
        if (entityManager != null && entityManager.isOpen()) {
          entityManager.close();
        }
      } finally {
        setMethod.invoke(dao, (EntityManager) null);
      }
    }
  }
}
