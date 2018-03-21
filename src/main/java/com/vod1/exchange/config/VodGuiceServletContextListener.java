package com.vod1.exchange.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ivan
 */
public class VodGuiceServletContextListener extends GuiceServletContextListener {

  private static Logger log = LoggerFactory.getLogger(VodGuiceServletContextListener.class); //NOPMD

  @Override
  protected Injector getInjector() {
    try {
      Injector injector = Guice.createInjector(
          new VodServletConfigModule()
      );
      return injector;
    } catch (Exception e) {
      log.error("can't create guice injector due to error {}", e.getMessage(), e);
      throw new IllegalStateException("can't create guice injector " + e.getMessage());
    }

  }
}
