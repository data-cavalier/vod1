package com.vod1.exchange.config.jpa;

import com.vod1.exchange.config.ConfigurationProvider;
import com.vod1.exchange.config.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.commons.configuration2.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

/**
 * this class is not controlled by Guice, so we could not use injection here
 *
 * @author ivan
 */
public class HikariConnectionProvider implements ConnectionProvider, Configurable, Stoppable {

  private static final Logger log = LoggerFactory.getLogger(HikariConnectionProvider.class);

  /**
   * Ensure VM singleton for this class.
   */
  private static final AtomicBoolean initCompleted = new AtomicBoolean(false);
  private static boolean initDone = false;

  /**
   * HikariCP data source.
   */
  private static HikariDataSource hds;

  // *************************************************************************
  //
  // *************************************************************************

  // *************************************************************************
  // Configurable
  // *************************************************************************

  @SuppressWarnings("rawtypes")
  @Override
  public void configure(Map props) throws HibernateException {
    try {
      getHikariDataSource();
      log.info("HikariCP Configured");
    } catch (Exception e) {
      throw new HibernateException(e);
    }
  }

  private static HikariDataSource getHikariDataSource() {
    if (!initDone && initCompleted.compareAndSet(false, true)) {
      HikariConfig hikariConfig = new HikariConfig();
      Configuration config = new ConfigurationProvider().get();
      log.info("Configuring HikariCP......");

      String jdbcUrl = config.getString(Constants.RDS_URL);
      log.info("using jdbcUrl '{}'", jdbcUrl);
      hikariConfig.setJdbcUrl(jdbcUrl);

      if (!jdbcUrl.startsWith("jdbc:h2:mem")) {
        hikariConfig.setUsername(config.getString(Constants.RDS_USER));
        hikariConfig.setPassword(config.getString(Constants.RDS_PASSWORD));
      }

      // do not use connections unless they are needed
      hikariConfig.setMinimumIdle(0);

      // maximum wait time for a connection to lay idle in pool - 5 minutes
      hikariConfig.setIdleTimeout(30000);
      // maximum wait time for a connection from the pool - 5 minutes
      hikariConfig.setConnectionTimeout(300000);
      hikariConfig.setInitializationFailTimeout(-1);

      hikariConfig.setMaximumPoolSize(20);

      // after how many seconds should leak detection check be triggered?   default to off (0)
      int numOfSecondsForLeak = 60;
      log.info("set leak detection at {}", numOfSecondsForLeak);
      hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(numOfSecondsForLeak));

      HikariConnectionProvider.hds = new HikariDataSource(hikariConfig);
      HikariConnectionProvider.initDone = true;
    }
    return HikariConnectionProvider.hds;
  }

  // *************************************************************************
  // ConnectionProvider
  // *************************************************************************

  @Override
  public Connection getConnection() throws SQLException {
    return getHikariDataSource().getConnection();
  }

  @Override
  public void closeConnection(Connection conn) throws SQLException {
    conn.close();
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean isUnwrappableAs(Class unwrapType) {
    return ConnectionProvider.class.equals(unwrapType) || HikariConnectionProvider.class.isAssignableFrom(unwrapType);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T unwrap(Class<T> unwrapType) {
    if (ConnectionProvider.class.equals(unwrapType) ||
        HikariConnectionProvider.class.isAssignableFrom(unwrapType)) {
      return (T) this;
    } else if (DataSource.class.isAssignableFrom(unwrapType)) {
      return (T) getHikariDataSource();
    } else {
      throw new UnknownUnwrapTypeException(unwrapType);
    }
  }

  // *************************************************************************
  // Stoppable
  // *************************************************************************

  @Override
  public void stop() {
    log.info("Allow VM cleanup to shutdown connection - adding hook");
    Runtime.getRuntime().addShutdownHook(new Thread(() -> getHikariDataSource().close()));
  }

  @SuppressWarnings("WeakerAccess")
  public static HikariDataSource getPooledDataSource() {
    return getHikariDataSource();
  }
}
