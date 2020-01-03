//package com.example.tune;
//
//
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "song_table")
//public class Song {
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    private long songId, songDateAdded;
//
//    private String songTitle, songArtist, songData;
//
//    public Song(long songId, long songDateAdded, String songTitle, String songArtist, String songData) {
//        this.songId = songId;
//        this.songDateAdded = songDateAdded;
//        this.songTitle = songTitle;
//        this.songArtist = songArtist;
//        this.songData = songData;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public long getSongId() {
//        return songId;
//    }
//
//    public long getSongDateAdded() {
//        return songDateAdded;
//    }
//
//    public String getSongTitle() {
//        return songTitle;
//    }
//
//    public String getSongArtist() {
//        return songArtist;
//    }
//
//    public String getSongData() {
//        return songData;
//    }
//}
