package com.vod1.exchange.page;

import com.google.inject.Singleton;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ivan
 */
@Singleton
public class UploadPage extends AbstractPage {

  @Override
  protected String getTemplateFile(HttpServletRequest req) {
    return "velocity/pages/upload.vm";
  }
}
