package com.vod1.exchange.entity;

import com.google.common.base.MoreObjects;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author ivan
 */
@Entity
@Table(name = "videos")
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "download_url", nullable = false)
  private String downloadUrl;

  @Column(name = "creation_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Video)) {
      return false;
    }
    Video video = (Video) o;
    return Objects.equals(id, video.id) &&
           Objects.equals(description, video.description) &&
           Objects.equals(downloadUrl, video.downloadUrl) &&
           Objects.equals(creationDate, video.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description, downloadUrl, creationDate);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("description", description)
        .add("downloadUrl", downloadUrl)
        .add("creationDate", creationDate)
        .toString();
  }
}
