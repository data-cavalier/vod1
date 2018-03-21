package com.vod1.exchange.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.vod1.exchange.dao.VideoDao;
import com.vod1.exchange.entity.Video;

import java.io.File;
import java.util.List;

/**
 * @author ivan
 */
@Singleton
public class VideoService {

  @Inject
  private VideoOSSUploader videoOSSUploader;
  @Inject
  private VideoDao videoDao;

  //upload to oss and save the content to rds
  public void saveVideo(File uploadFile) {
    final String downloadUrl = videoOSSUploader.uploadVideo(uploadFile);
    Video video = new Video();
    video.setDescription(uploadFile.getName());
    video.setDownloadUrl(downloadUrl);
    videoDao.save(video);
  }

  //just a layer or separation, not useful for such simple app
  public List<Video> list(int start, int max) {
    return videoDao.list(start, max);
  }
}
