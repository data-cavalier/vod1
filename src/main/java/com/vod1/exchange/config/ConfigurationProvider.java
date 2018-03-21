package com.vod1.exchange.config;

import com.google.inject.Provider;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author ivan
 */
public class ConfigurationProvider implements Provider<Configuration> {

  private static Logger log = LoggerFactory.getLogger(ConfigurationProvider.class); //NOPMD
  private String propertyFilePath;

  public ConfigurationProvider() {
    SystemConfiguration sysConfig = new SystemConfiguration();
    propertyFilePath = sysConfig.getString(Constants.CONFIG_PROP_FILE_PATH, Constants.CONFIG_PROP_FILE_PATH_DEFAULT);
  }

  @Override
  public Configuration get() {
    Configurations configs = new Configurations();
    try {
      return configs.properties(new File(getPropertyFilePath()));
    } catch (ConfigurationException e) {
      log.error(" error on build configuration", e);
      throw new IllegalStateException(e);
    }
  }

  public String getPropertyFilePath() {
    return propertyFilePath;
  }
}
