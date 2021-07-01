package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean is_flagged = false;
  private String flag_reason = "Not supplied";

  private video_options videoOptions=video_options.DEFAULT;

  enum video_options {
    PAUSED,
    CONTINUE,
    DEFAULT,
    FLAGGED,
  }

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
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

  void updateState(video_options options){
    videoOptions=options;
  }

  video_options getCurrentState() {
    return videoOptions;
  }

  boolean isFlagged() {
    return is_flagged;
  }

  void setIs_flagged(boolean is_flagged) {
    this.is_flagged=is_flagged;
  }

  void setFlag_reason(String flag_reason) {
    this.flag_reason=flag_reason;
  }

  String getFlag_reason() {
    return flag_reason;
  }
}
