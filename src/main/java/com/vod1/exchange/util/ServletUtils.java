package com.vod1.exchange.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ivan
 */
public class ServletUtils {

  private static Logger log = LoggerFactory.getLogger(ServletUtils.class); //NOPMD

  public static void renderHtml(
      HttpServletRequest req, HttpServletResponse resp, VelocityContext context,
      String templateFile) throws IOException {
    resp.setContentType("text/html; charset=UTF-8");
    try {
      String merged = VelocityUtil.merge(templateFile, context);

      // As general practice, if you didn't open the stream then don't close
      resp.getWriter().write(merged);
    } catch (ResourceNotFoundException e) {
      log.error("Couldn't find the template", e);
      handleException(req, resp, e);
    } catch (ParseErrorException e) {
      log.error("Syntax error: problem parsing the template", e);
      handleException(req, resp, e);
    } catch (MethodInvocationException e) {
      log.error("Something invoked in the template threw an exception", e);
      handleException(req, resp, e);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handleException(req, resp, e);
    }
  }

  public static void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) throws IOException {
    log.warn(e.getMessage(), e);
    resp.sendRedirect(req.getContextPath() + "/unknownerror");
  }
}
