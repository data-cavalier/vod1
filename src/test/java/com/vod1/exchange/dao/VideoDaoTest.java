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

import java.util.stream.IntStream;

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
    //do nothing for now
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
    assertThat(videoDao.findById(video.getId()).get().getCreationDate()).isNotNull();
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

  @Test
  public void list_and_pagination() {
    IntStream.range(0, 9).forEach(
        index -> {
          Video video = new Video();
          video.setDescription(faker.superhero().name());
          video.setDownloadUrl(faker.numerify("https://acme.com/###"));
          videoDao.save(video);
        }
    );

    assertThat(videoDao.list(0, 5).size()).isEqualTo(5);
    assertThat(videoDao.list(5, 10).size()).isEqualTo(4);
  }
}