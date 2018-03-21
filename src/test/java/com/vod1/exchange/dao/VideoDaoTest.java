package com.vod1.exchange.dao;

import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.github.javafaker.Faker;
import com.vod1.exchange.config.Constants;
import com.vod1.exchange.config.VodShareModule;
import com.vod1.exchange.config.jpa.VodPersistModule;
import com.vod1.exchange.entity.Video;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ivan
 */
public class VideoDaoTest {

  private VideoDao videoDao;
  private Faker faker;

  @Before
  public void setUp() throws Exception {
    //make sure we load config props from test resources
    String path = Resources.getResource("vod_manager.properties").getFile();
    System.setProperty(Constants.CONFIG_PROP_FILE_PATH, path);
    faker = new Faker();
    //use guice to inject all the depenencies
    Injector injector = Guice.createInjector(new VodPersistModule(), new VodShareModule());
    videoDao = injector.getInstance(VideoDao.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void save_and_find_success() {
    Video video = new Video();
    video.setDescription(faker.superhero().name());
    video.setDownloadUrl(faker.numerify("https://acme.com/###"));
    videoDao.save(video);
    assertThat(video.getId()).isNotNull();
    assertThat(videoDao.findById(video.getId())).isPresent();
    assertThat(videoDao.findById(video.getId()).get().getDescription()).isEqualTo(video.getDescription());
    assertThat(videoDao.findById(video.getId()).get().getDownloadUrl()).isEqualTo(video.getDownloadUrl());
    assertThat(videoDao.findById(video.getId()).get().getCreationDate()).isNull();
  }

  @Test
  public void save_and_find_failed() {
    Video video = new Video();
    video.setDescription(faker.superhero().name());
    video.setDownloadUrl(faker.numerify("https://acme.com/###"));
    videoDao.save(video);
    assertThat(video.getId()).isNotNull();
    assertThat(videoDao.findById(video.getId() + 1)).isNotPresent();
  }
}