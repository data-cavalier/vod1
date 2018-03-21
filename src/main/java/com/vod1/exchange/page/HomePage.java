package com.vod1.exchange.page;

import com.google.inject.Singleton;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ivan
 */
@Singleton
public class HomePage extends AbstractPage {

  protected String getTemplateFile(HttpServletRequest req) {
    return "velocity/pages/home.vm";
  }
}
