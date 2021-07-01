package com.google;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private Video video;
    private Set<String> playlists = new HashSet<String>();
    private HashMap<String,List<String>> video_playlist= new HashMap<>();


    void playVideo(Video video){
        System.out.println("Playing video: "+video.getTitle());
        video.updateState(Video.video_options.DEFAULT);
        this.video=video;
    }

    Video getCurrentVideo() {
        return video;
    }

    boolean addNewPlayList(String name) {
        if(!video_playlist.containsKey(name.toLowerCase())) {
            video_playlist.put(name.toLowerCase(),new ArrayList<>());
        }
        return playlists.add(name.toLowerCase());
    }

    void removeVideo() {
        this.video=null;
    }

    Set<String> getPlayLists(){
        return playlists;
    }

    HashMap<String,List<String>> getVideo_playlist() {
        return video_playlist;
    }

    void addVideoToPlayList(String name,Video video) {
        if(video!=null) {
            if(!video_playlist.get(name.toLowerCase()).contains(video.getVideoId())){
                video_playlist.get(name.toLowerCase()).add(video.getVideoId());
                System.out.printf("Added video to %s: %s%n",name,video.getTitle());
            } else {
                System.out.printf("Cannot add video to %s: Video already added%n",name);
            }

        } else {
            System.out.printf("Cannot add video to %s: Video does not exist%n",name);
        }
    }


}
