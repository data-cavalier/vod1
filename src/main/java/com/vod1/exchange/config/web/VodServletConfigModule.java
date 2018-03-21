package com.vod1.exchange.config.web;

import com.google.inject.servlet.ServletModule;

import com.vod1.exchange.page.HomePage;

/**
 * @author ivan
 */
public class VodServletConfigModule extends ServletModule {

  @Override
  protected void configureServlets() {
    serve("/").with(HomePage.class);
  }
}
