package com.vod1.exchange.service;

import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.vod1.exchange.dao.VideoDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author ivan
 */
public class VideoServiceTest {

  private VideoService videoService;
  private VideoOSSUploader videoOSSUploader;
  private VideoDao videoDao;

  @Before
  public void setUp() throws Exception {
    videoOSSUploader = mock(VideoOSSUploader.class);
    videoDao = mock(VideoDao.class);
    Injector injector = Guice.createInjector(binder -> {
      binder.bind(VideoOSSUploader.class).toProvider(() -> videoOSSUploader);
      binder.bind(VideoDao.class).toProvider(() -> videoDao);
    });
    videoService = injector.getInstance(VideoService.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void saveVideo_success() {
    File propFile = new File(Resources.getResource("vod_manager.properties").getFile());

    videoService.saveVideo(propFile);

    verify(videoOSSUploader,times(1)).uploadVideo(propFile);
    verify(videoDao,times(1)).save(any());
  }
}