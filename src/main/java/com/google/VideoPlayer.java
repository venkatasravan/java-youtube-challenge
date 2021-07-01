package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  private final VideoPlaylist videoPlaylist;


  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.videoPlaylist = new VideoPlaylist();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = videoLibrary.getVideos();
    videos.sort(Comparator.comparing(Video::getTitle));
    for (Video video: videos) {
      printVideo(video);
    }
  }

  private void printVideo(Video video) {
      System.out.printf(video.getTitle() +" ("+video.getVideoId()+") ");
      System.out.printf("[");
      for( int i=0; i< video.getTags().size();i++){
        System.out.printf(video.getTags().get(i));
        if((i+1)!= video.getTags().size()){
          System.out.printf(" ");
        }
      }
      if(video.getCurrentState()!=Video.video_options.DEFAULT){
        String flagged_content="";
        if(video.getCurrentState()==Video.video_options.FLAGGED) {
          flagged_content=" (reason: "+video.getFlag_reason()+")";
        }
        System.out.println("] - "+video.getCurrentState().toString()+flagged_content);
      } else {
        System.out.println("]");
      }
    }

  public void playVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if(video!=null) {
      if(!video.isFlagged()){
        if(videoPlaylist.getCurrentVideo()!=null){
          stopVideo();
        }
        videoPlaylist.playVideo(video);
      } else {
        System.out.printf("Cannot play video: Video is currently flagged (reason: %s)%n",video.getFlag_reason());
      }
    } else {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if(videoPlaylist.getCurrentVideo()!=null){
      System.out.println("Stopping video: "+videoPlaylist.getCurrentVideo().getTitle());
      videoPlaylist.removeVideo();
    } else {
      System.out.println("Cannot stop video: No video is currently playing");
    }

  }

  public void playRandomVideo() {
    int randomNumber= (int)(Math.random()*videoLibrary.getVideos().size());
    if(!videoLibrary.getVideos().get(randomNumber).isFlagged()) {
      playVideo(videoLibrary.getVideos().get(randomNumber).getVideoId());
    } else {
      System.out.println("No videos available");
    }
  }

  public void pauseVideo() {
    if(videoPlaylist.getCurrentVideo()!=null){
      if(videoPlaylist.getCurrentVideo().getCurrentState()!=Video.video_options.PAUSED){
        videoPlaylist.getCurrentVideo().updateState(Video.video_options.PAUSED);
        System.out.printf("Pausing video: %s%n",videoPlaylist.getCurrentVideo().getTitle());
      } else {
        System.out.printf("Video already paused: %s%n",videoPlaylist.getCurrentVideo().getTitle());
      }
    } else {
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    if(videoPlaylist.getCurrentVideo()!=null){
      if(videoPlaylist.getCurrentVideo().getCurrentState()==Video.video_options.PAUSED){
        videoPlaylist.getCurrentVideo().updateState(Video.video_options.CONTINUE);
        System.out.printf("Continuing video: %s%n",videoPlaylist.getCurrentVideo().getTitle());
      } else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    } else {
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    if(videoPlaylist.getCurrentVideo()!=null){
      System.out.printf("Currently playing: ");
      printVideo(videoPlaylist.getCurrentVideo());
    } else {
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    if(videoPlaylist.addNewPlayList(playlistName)){
      System.out.printf("Successfully created new playlist: %s%n",playlistName);
    } else {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if(videoPlaylist.getPlayLists().contains(playlistName.toLowerCase())) {
      if(video!=null) {
        if(!video.isFlagged()) {
          videoPlaylist.addVideoToPlayList(playlistName,video);
        } else {
          System.out.printf("Cannot add video to %s: Video is currently flagged (reason: %s)%n",playlistName,video.getFlag_reason());
        }
      } else {
        System.out.println("Cannot add video to my_playlist: Video does not exist");
      }
    } else {

      System.out.printf("Cannot add video to %s: Playlist does not exist%n",playlistName);
    }

  }

  public void showAllPlaylists() {
    Set<String> playLists = videoPlaylist.getPlayLists();
    List<String> tempList = new ArrayList<>(playLists);
    Collections.sort(tempList);

    if(!playLists.isEmpty()) {
      System.out.println("Showing all playlists:");
      for(String playList: tempList) {
        int videoCount= videoPlaylist.getVideo_playlist().get(playList).size();
        String videoCountString = videoCount>1? videoCount+" videos":videoCount+" video";
        System.out.println(playList+ " ("+videoCountString+")");
      }
    } else {
      System.out.println("No playlists exist yet");
    }

  }

  public void showPlaylist(String playlistName) {

    if(videoPlaylist.getPlayLists().contains(playlistName.toLowerCase())) {
      System.out.println("Showing playlist: "+playlistName);
      if(!videoPlaylist.getVideo_playlist().get(playlistName.toLowerCase()).isEmpty()) {
        for(String video_id: videoPlaylist.getVideo_playlist().get(playlistName.toLowerCase())) {
          printVideo(videoLibrary.getVideo(video_id));
        }
      } else {
        System.out.println("No videos here yet");
      }
    } else {
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n",playlistName);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if(videoPlaylist.getPlayLists().contains(playlistName.toLowerCase())) {
      if(videoLibrary.getVideo(videoId)!=null) {
        if(videoPlaylist.getVideo_playlist().get(playlistName.toLowerCase()).contains(videoId)){
          videoPlaylist.getVideo_playlist().get(playlistName.toLowerCase()).remove(videoId);
          System.out.printf("Removed video from %s: %s%n",playlistName,videoLibrary.getVideo(videoId).getTitle());
        } else {
          System.out.printf("Cannot remove video from %s: Video is not in playlist%n",playlistName);
        }
      } else {
        System.out.printf("Cannot remove video from %s: Video does not exist%n",playlistName);
      }
    } else {
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n",playlistName);
    }
  }

  public void clearPlaylist(String playlistName) {
    if(videoPlaylist.getPlayLists().contains(playlistName.toLowerCase())) {
      if(!videoPlaylist.getVideo_playlist().get(playlistName.toLowerCase()).isEmpty()) {
        videoPlaylist.getVideo_playlist().get(playlistName.toLowerCase()).clear();
        System.out.printf("Successfully removed all videos from %s%n",playlistName);
      }
    } else {
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n",playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    if(videoPlaylist.getPlayLists().contains(playlistName.toLowerCase())) {
      videoPlaylist.getPlayLists().remove(playlistName.toLowerCase());
      System.out.printf("Deleted playlist: %s%n",playlistName);
    } else {
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n",playlistName);
    }

  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> videoResult = new ArrayList<>();

    for(Video video: videos) {
      if(video.isFlagged()) {
        continue;
      }
      if(video.getTitle().contains(searchTerm) || video.getTags().contains(searchTerm) ||
              video.getVideoId().contains(searchTerm)) {
        videoResult.add(video);
      }
    }
    videoResult.sort(Comparator.comparing(Video::getTitle));
    if(videoResult.isEmpty()) {
      System.out.printf("No search results for %s%n",searchTerm);
    }
    else {
      System.out.printf("Here are the results for %s:%n",searchTerm);
      for (int i=0;i<videoResult.size();i++) {
        System.out.printf((i+1)+") ");
        printVideo(videoResult.get(i));
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");


      Scanner scanner = new Scanner(System.in);
      String input = scanner.next();
      int value=0;
      try{
        value = Integer.parseInt(input);
      } catch (NumberFormatException e){

      }



      if(videoResult.size()>(value-1) && value!=0){
        playVideo(videoResult.get(value-1).getVideoId());
      }
    }

  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> videoResult = new ArrayList<>();

    for(Video video: videos) {
      if(video.isFlagged()) {
        continue;
      }
      if(video.getTags().contains(videoTag)) {
        videoResult.add(video);
      }
    }
    videoResult.sort(Comparator.comparing(Video::getTitle));
    if(videoResult.isEmpty()) {
      System.out.printf("No search results for %s%n",videoTag);
    }
    else {
      System.out.printf("Here are the results for %s:%n",videoTag);
      for (int i=0;i<videoResult.size();i++) {
        System.out.printf((i+1)+") ");
        printVideo(videoResult.get(i));
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");


      Scanner scanner = new Scanner(System.in);
      String input = scanner.next();
      int value=0;
      try{
        value = Integer.parseInt(input);
      } catch (NumberFormatException e){
        e.getMessage();
      }

      if(videoResult.size()>(value-1) && value!=0){
        playVideo(videoResult.get(value-1).getVideoId());
      }
    }
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId,"Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if(video != null) {
      if(!video.isFlagged()) {
        video.setIs_flagged(true);
        video.updateState(Video.video_options.FLAGGED);
        video.setFlag_reason(reason);
        if(videoPlaylist.getCurrentVideo()!=null && videoPlaylist.getCurrentVideo().getCurrentState()!=Video.video_options.DEFAULT) {
          stopVideo();
        }
        System.out.printf("Successfully flagged video: %s (reason: %s)%n",video.getTitle(),video.getFlag_reason());
      } else {
        System.out.println("Cannot flag video: Video is already flagged");
      }

    } else {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if(video!=null) {
      if(video.isFlagged()) {
        video.setIs_flagged(false);
        video.updateState(Video.video_options.DEFAULT);
        System.out.printf("Successfully removed flag from video: %s%n",video.getTitle());
      } else {
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
    } else {
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }
}