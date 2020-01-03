//package com.example.tune;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import java.util.List;
//
//public class SongViewModel extends AndroidViewModel {
//
//    private SongRepository songRepository;
//    private LiveData<List<Song>> allSongs;
//
//    public SongViewModel(@NonNull Application application) {
//        super(application);
//
//        songRepository = new SongRepository(application);
//        allSongs = songRepository.getAllSongs();
//    }
//
//    public void insert(Song song){
//        songRepository.insert(song);
//    }
//
//    public void update(Song song){
//        songRepository.update(song);
//    }
//
//    public void delete(Song song){
//        songRepository.delete(song);
//    }
//
//    public LiveData<List<Song>> getAllSongs() {
//        return allSongs;
//    }
//}
