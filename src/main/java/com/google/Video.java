package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video implements Comparable<Video>{

  private final String title;
  private final String videoId;
  private final List<String> tags;

  private boolean isFlagged;
  private String reason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    isFlagged = false;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public void flag(String motive){
    isFlagged = true;
    reason = motive.substring(0);
  }

  public void unflag(){
    isFlagged = false;
  }

  public boolean isFlagged(){
    return isFlagged;
  }

  public String getReason(){
    return reason;
  }

  // Overriding compareTo() method
  @Override
  public int compareTo(Video o) {
    return this.title.compareTo(o.getTitle());
  }
}
