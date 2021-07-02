package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist implements Comparable<VideoPlaylist>{
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

    public void removeVideo(Video video){
        videos.remove(video);
    }

    @Override
    public int compareTo(VideoPlaylist o) {
        return this.name.compareTo(o.getName());
    }
}
