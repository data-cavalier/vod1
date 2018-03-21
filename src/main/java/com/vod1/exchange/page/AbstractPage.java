package com.vod1.exchange.page;

import com.vod1.exchange.util.ServletUtils;
import com.vod1.exchange.util.VelocityUtil;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ivan
 */
public abstract class AbstractPage extends HttpServlet {

  private static Logger log = LoggerFactory.getLogger(AbstractPage.class); //NOPMD

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      ServletUtils.renderHtml(req, resp, buildContext(req, resp), getTemplateFile(req));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      ServletUtils.handleException(req, resp, e);
    }
  }

  /**
   * Build context for the page
   */
  protected VelocityContext buildContext(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    VelocityContext context = VelocityUtil.buildContext(req);
    return context;
  }

  /**
   * @return Template file related to classpath
   */
  protected abstract String getTemplateFile(HttpServletRequest req);


}
