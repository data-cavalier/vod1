package com.vod1.exchange.page;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.vod1.exchange.entity.Video;
import com.vod1.exchange.service.VideoService;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ivan
 */
@Singleton
public class HomePage extends AbstractPage {

  private static Logger log = LoggerFactory.getLogger(HomePage.class); //NOPMD
  private static final int PAGE_SIZE = 5;
  @Inject
  private VideoService videoService;


  @Override
  protected VelocityContext buildContext(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    VelocityContext context = super.buildContext(req, resp);
    final String pageNoParam = req.getParameter("pageNo");
    final int pageNo = Optional.ofNullable(pageNoParam)
        .map(Integer::parseInt)
        .orElse(0);
    final int start = pageNo * PAGE_SIZE;

    List<Video> videos = videoService.list(start, PAGE_SIZE);
    context.put("videos", videos);
    return context;
  }

  protected String getTemplateFile(HttpServletRequest req) {
    return "velocity/pages/home.vm";
  }
}
