package com.vod1.exchange.dao;

import com.google.inject.Singleton;

import com.vod1.exchange.config.jpa.NonTransactional;
import com.vod1.exchange.config.jpa.Transactional;
import com.vod1.exchange.entity.Video;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;

/**
 * @author ivan
 */
@Singleton
public class VideoDao extends AbstractDao {

  @Transactional
  public void save(Video video) {
    if (video.getCreationDate() == null) {
      video.setCreationDate(new Date());
    }
    em().persist(video);
  }

  @NonTransactional
  public Optional<Video> findById(Long videoId) {
    return Optional.ofNullable(em().find(Video.class, videoId));
  }

  @NonTransactional
  public List<Video> list(int start, int max) {

    TypedQuery<Video> query = em().createQuery("FROM Video ORDER BY creationDate DESC", Video.class);
    query.setFirstResult(start);
    query.setMaxResults(max);

    return query.getResultList();
  }
}
