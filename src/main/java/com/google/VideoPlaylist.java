package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private List<Video> videos;

    VideoPlaylist(String name){
        this.name = name;
        videos = new ArrayList<Video>();
    }

    public String getName(){
        return name;
    }

    public List<Video> getVideos(){
        return videos;
    }

    public void addVideo(Video video){
        videos.add(video);
    }
}
