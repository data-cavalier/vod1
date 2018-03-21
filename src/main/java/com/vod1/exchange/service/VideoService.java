package com.vod1.exchange.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.vod1.exchange.dao.VideoDao;
import com.vod1.exchange.entity.Video;

import java.io.File;

/**
 * @author ivan
 */
@Singleton
public class VideoService {

  @Inject
  private VideoOSSUploader videoOSSUploader;
  @Inject
  private VideoDao videoDao;

  public void saveVideo(File uploadFile) {
    final String downloadUrl = videoOSSUploader.uploadVideo(uploadFile);
    Video video = new Video();
    video.setDescription(uploadFile.getName());
    video.setDownloadUrl(downloadUrl);
    videoDao.save(video);
  }
}
