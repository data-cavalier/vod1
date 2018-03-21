package com.vod1.exchange.dao;

import com.google.inject.Singleton;

import com.vod1.exchange.config.jpa.NonTransactional;
import com.vod1.exchange.config.jpa.Transactional;
import com.vod1.exchange.entity.Video;

import java.util.Optional;

/**
 * @author ivan
 */
@Singleton
public class VideoDao extends AbstractDao {

  @Transactional
  public void save(Video video) {
    em().persist(video);
  }

  @NonTransactional
  public Optional<Video> findById(Long videoId) {
    return Optional.ofNullable(em().find(Video.class, videoId));
  }
}
