package com.vod1.exchange.config.jpa;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.vod1.exchange.config.Constants;

import org.apache.commons.configuration2.Configuration;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author ivan
 */
@Singleton
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

  @Inject
  private Configuration config;

  @Override
  public EntityManagerFactory get() {
    return Persistence.createEntityManagerFactory("vodPU", createHibernateProps());
  }

  private Map<String, Object> createHibernateProps() {
    final Map<String, Object> propertyMap = Maps.newHashMap();

    // This must remain as false, to support the legacy tables created under previous allocation method
    // See Hibernate 3 to 4 migration guide for more info
    propertyMap.put("hibernate.id.new_generator_mappings", "false");

    // enable this to see the raw sql queries in output
    propertyMap.put("hibernate.show_sql", "true");
    propertyMap.put("hibernate.format_sql", "true");
    propertyMap.put("hibernate.use_sql_comments", "true");

    propertyMap.put(
        "javax.persistence.spi.PersistenceProvider",
        HibernatePersistenceProvider.class.getName()
    );
    propertyMap.put("hibernate.connection.provider_class", "com.vod1.exchange.config.jpa.HikariConnectionProvider");

    if (isInMemoryDB()) {
      inMemoryDBSettings(propertyMap);
    } else {
      mySqlDBSettings(propertyMap);
    }

    return propertyMap;
  }

  private void mySqlDBSettings(Map<String, Object> propertyMap) {
    // do not set hibernate.dialect
    // let hakariCP to detect it
    propertyMap.put("hibernate.dialect.storage_engine", "innodb");
    propertyMap.put("hibernate.hbm2ddl.auto", "validate");
  }

  private void inMemoryDBSettings(Map<String, Object> propertyMap) {
    propertyMap.put("hibernate.dialect", H2Dialect.class.getName());
    propertyMap.put("hibernate.hbm2ddl.auto", "update");
  }


  private boolean isInMemoryDB() {
    String jdbcUrl = config.getString(Constants.RDS_URL);
    return jdbcUrl.startsWith("jdbc:h2:mem");
  }
}
