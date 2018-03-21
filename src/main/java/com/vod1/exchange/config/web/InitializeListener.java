package com.vod1.exchange.config.web;

import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author ivan
 */
public class InitializeListener implements ServletContextListener {

  private static Logger log = LoggerFactory.getLogger(InitializeListener.class); //NOPMD

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    Properties prop = new Properties();
    InputStream is = InitializeListener.class.getResourceAsStream("/velocity.properties");
    try {
      prop.load(is);

      String logFile = servletContextEvent.getServletContext().getRealPath("/WEB-INF") + "/velocity.log";
      log.info("logFile=" + logFile);
      prop.setProperty("runtime.log", logFile);

      log.info("Loaded velocity properties: " + prop);
      Velocity.init(prop);
      log.info("Initialized singleton velocity instance");

    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
