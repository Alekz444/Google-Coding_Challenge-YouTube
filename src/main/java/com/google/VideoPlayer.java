package com.google;

import org.apache.maven.shared.utils.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  private Video videoPlayed = null;
  private boolean isPaused = false;
  private List<VideoPlaylist>  videoPlaylists;
  private List<String> videoIds;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    videoPlaylists = new ArrayList<VideoPlaylist>();
    videoIds = videosToIds(videoLibrary.getVideos());
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> videos;
    videos = videoLibrary.getVideos();
    Collections.sort(videos);
    System.out.println("Here's a list of all available videos:");
    for(Video v : videos){
      String line = v.getTitle() + " (" + v.getVideoId() + ") ";
      List<String> tags = v.getTags();
      line += tagsToString(tags);
      System.out.println(line);
    }
  }

  public void playVideo(String videoId) {
    List<Video> videos = videoLibrary.getVideos();
    String title = null;
    Video videoSearched = null;
    for (Video v : videos) {
      if (v.getVideoId().equals(videoId)) {
        title = v.getTitle();
        videoSearched = v;
      }
    }
    if (title == null) {
      System.out.println("Cannot play video: Video does not exist");
      return;
    }
    stopVideoPlayed();
    System.out.println("Playing video: " + title);
    videoPlayed = videoSearched;
    isPaused = false;
  }


  public void stopVideo() {
    if (videoPlayed == null) {
      System.out.println("Cannot stop video: No video is currently playing");
      return;
    }
    System.out.println("Stopping video: " + videoPlayed.getTitle());
    videoPlayed = null;
    isPaused = false;
  }

  public void playRandomVideo() {
    List<Video> videos = videoLibrary.getVideos();
    if(videos.isEmpty()){
      System.out.println("No videos available");
      return;
    }
    stopVideoPlayed();
    Random random = new Random();
    Video video = videos.get(random.nextInt(videos.size()));
    System.out.println("Playing video: " + video.getTitle());
    videoPlayed = video;
    isPaused = false;
  }

  public void pauseVideo() {
    if(videoPlayed == null){
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }
    if(isPaused == true){
      System.out.println("Video already paused: " + videoPlayed.getTitle());
      return;
    }
    System.out.println("Pausing video: " + videoPlayed.getTitle());
    isPaused = true;
  }

  public void continueVideo() {
    if(videoPlayed == null){
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }
    if(!isPaused){
      System.out.println("Cannot continue video: Video is not paused");
      return;
    }
    System.out.println("Continuing video: " + videoPlayed.getTitle());
    isPaused = false;
  }

  public void showPlaying() {
    if(videoPlayed == null){
      System.out.println("No video is currently playing");
      return;
    }

    String tagString = tagsToString(videoPlayed.getTags());
    String output = "Currently playing: ";
    output += videoPlayed.getTitle() + " ";
    output += "(" + videoPlayed.getVideoId() + ") ";
    output += tagString;
    if(isPaused){
      output += " - PAUSED";
    }
    System.out.println(output);
  }

  public void createPlaylist(String playlistName) {
    if(videoPlaylists != null){
      for(VideoPlaylist v : videoPlaylists){
        if(v.getName().trim().equalsIgnoreCase(playlistName.trim())){
          System.out.println("Cannot create playlist: A playlist with the same name already exists");
          return;
        }
      }
    }
    VideoPlaylist videoPlaylist = new VideoPlaylist(playlistName);
    videoPlaylists.add(videoPlaylist);
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist vp = searchedPlaylist(playlistName);
    if(vp ==  null){
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      return;
    }
    Video v = searchedVideo(videoId);
    if(v == null){
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      return;
    }
    if(vp.getVideos().contains(v)){
      System.out.println("Cannot add video to " + playlistName + ": Video already added");
      return;
    }
    vp.addVideo(v);
    System.out.println("Added video to " + playlistName + ": " + v.getTitle());
  }

  public void showAllPlaylists() {
    if(videoPlaylists.isEmpty()){
      System.out.println("No playlists yet");
      return;
    }
    System.out.println("Showing all playlists:");
    Collections.sort(videoPlaylists);
    for(VideoPlaylist vp : videoPlaylists){
      System.out.println(vp.getName());
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylistByName(playlistName);
    if(playlist == null){
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
      return;
    }

    if(playlist.getVideos().isEmpty()){
      System.out.println("Showing playlist: " + playlistName);
      System.out.println("No videos here yet");
      return;
    }
    System.out.println("Showing playlist: " + playlistName);
    for(Video v : playlist.getVideos()){
      String output = v.getTitle() + " (" + v.getVideoId() + ") ";
      List<String> tags = v.getTags();
      output += tagsToString(tags);
      System.out.println(output);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist  vp = searchedPlaylist(playlistName);
    if(vp == null){
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
      return;
    }
    Video v = searchedVideo(videoId);
    if(v == null){
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      return;
    }
    boolean found = false;
    for(Video video : vp.getVideos()){
      if(video == v){
        found = true;
      }
    }
    if(found == false){
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      return;
    }
    vp.removeVideo(v);
    System.out.println("Removed video from " + playlistName + ": " + v.getTitle());
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist vp = searchedPlaylist(playlistName);
    if(vp == null){
      System.out.println("Cannot clear playlist " +  playlistName + ": Playlist does not exist");
      return;
    }
    List<Video> list = vp.getVideos();
    Iterator it = list.iterator();
    while(it.hasNext()){
      it.next();
      it.remove();
    }
    System.out.println("Successfully removed all videos from " + playlistName);
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist vp = searchedPlaylist(playlistName);
    if(vp == null){
      System.out.println("Cannot delete playlist " +  playlistName + ": Playlist does not exist");
      return;
    }
    videoPlaylists.remove(vp);
    System.out.println("Deleted playlist: " + vp.getName());
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }


  /**
   * If there is a video that is currently being played,
   * it will be stopped and an appropriate message displayed.
   */
  private void stopVideoPlayed() {
    if (videoPlayed != null) {
      System.out.println("Stopping video: " + videoPlayed.getTitle());
      videoPlayed = null;
      isPaused = false;
    }
  }

  /**
   * Takes a list of the video's  tags and returns a well-formatted
   * string
   * @param tags The tags for a video
   */
  private String tagsToString(List<String> tags){
    if(tags.isEmpty()){
      return "[]";
    }
    String output = "[";
    for(String t : tags){
      output = output + t + " ";
    }
    //StringBuffer sb= new StringBuffer(output);
    //sb.deleteCharAt(sb.length()-1);
    output = StringUtils.chop(output);

    output = output + "]";
    return output;
  }

  /**
   * Makes an Array List of the names of the playlists in the given list.
   * @param videoPlaylists The given list of playlists
   * @return playlistsToNames The returning Array List
   */
  private ArrayList<String> playlistsToNames(List<VideoPlaylist> videoPlaylists){
    ArrayList<String> names = new ArrayList<>();
    for(VideoPlaylist v : videoPlaylists){
      names.add(v.getName());
    }
    return names;
  }

  private List<String> videosToIds(List<Video> videos){
    List<String>  ids = new ArrayList<String>();
    for(Video v : videos){
      ids.add(v.getVideoId());
    }
    return ids;
  }

  private VideoPlaylist getPlaylistByName(String name){
    for(VideoPlaylist v : videoPlaylists){
      if(v.getName().trim().equalsIgnoreCase(name.trim())){
        return v;
      }
    }
    return null;
  }

  /**
   * Returns a playlist by its name
   * @param playlistName
   * @return The playlist or NULL
   */
  private VideoPlaylist searchedPlaylist(String playlistName){
    for(VideoPlaylist vp :  videoPlaylists){
      if(vp.getName().trim().equalsIgnoreCase(playlistName.trim())){
        return vp;
      }
    }
    return null;
  }

  /**
   * Returns a video by its id
   * @param videoId
   * @return A video or NULL
   */
  private Video searchedVideo(String videoId){
    for(Video v : videoLibrary.getVideos()){
      if(v.getVideoId().equals(videoId)){
        return v;
      }
    }
    return null;
  }
}


