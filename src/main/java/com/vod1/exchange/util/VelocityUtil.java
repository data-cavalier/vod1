package com.vod1.exchange.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ivan
 */
public class VelocityUtil {

  public static VelocityContext buildContext(HttpServletRequest req) {
    VelocityContext context = new VelocityContext();
    //context path is always available to the template
    context.put("contextPath", req.getContextPath());
    return context;
  }

  public static String merge(String templateName, VelocityContext context) {
    Template template;
    StringWriter sw = new StringWriter();
    template = Velocity.getTemplate(templateName, "UTF-8");
    template.merge(context, sw);
    return sw.toString();
  }
}
