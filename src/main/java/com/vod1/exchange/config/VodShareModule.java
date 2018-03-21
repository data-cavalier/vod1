package com.vod1.exchange.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.vod1.exchange.config.ConfigurationProvider;

import org.apache.commons.configuration2.Configuration;

/**
 * @author ivan
 */
public class VodShareModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Configuration.class).toProvider(ConfigurationProvider.class).in(Singleton.class);
  }
}
