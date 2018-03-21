package com.vod1.exchange.config.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import javax.persistence.EntityManagerFactory;

/**
 * @author ivan
 */
public class VodPersistModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(EntityManagerFactory.class)
        .toProvider(EntityManagerFactoryProvider.class)
        .in(Singleton.class);

    DaoMethodInterceptor transactionalInterceptor = new DaoMethodInterceptor(true);
    requestInjection(transactionalInterceptor);
    bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionalInterceptor);

    DaoMethodInterceptor nonTransactionalInterceptor = new DaoMethodInterceptor(false);
    requestInjection(nonTransactionalInterceptor);
    bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(NonTransactional.class),
        nonTransactionalInterceptor);
  }
}
