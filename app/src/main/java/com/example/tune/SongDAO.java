//package com.example.tune;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import java.util.List;
//
//@Dao
//public interface SongDAO {
//
//    @Insert
//    void insert(Song song);
//
//    @Update
//    void update(Song song);
//
//    @Delete
//    void delete(Song song);
//
//    @Query("SELECT * FROM song_table ORDER BY id DESC")
//    LiveData<List<Song>> getAllSongs();
//}
