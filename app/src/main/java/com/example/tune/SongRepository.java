//package com.example.tune;
//
//import android.app.Application;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//
//import com.example.tune.database.SongDatabase;
//
//import java.util.List;
//
//public class SongRepository {
//
//    private SongDAO songDAO;
//    private LiveData<List<Song>> allSongs;
//
//    public SongRepository(Application application){
//        SongDatabase songDatabase = SongDatabase.getInstance(application);
//        songDAO = songDatabase.songDAO();
//        allSongs = songDAO.getAllSongs();
//    }
//
//    public void insert(Song song){
//        new InsertSongAsyncTask(songDAO).execute(song);
//    }
//
//    public void update(Song song){
//        new UpdateSongAsyncTask(songDAO).execute(song);
//    }
//
//    public void delete(Song song){
//        new DeleteSongAsyncTask(songDAO).execute(song);
//    }
//
//    public LiveData<List<Song>> getAllSongs() {
//        return allSongs;
//    }
//
//    private static class InsertSongAsyncTask extends AsyncTask<Song, Void, Void>{
//
//        private SongDAO songDAO;
//        private InsertSongAsyncTask(SongDAO songDAO){
//            this.songDAO = songDAO;
//        }
//        @Override
//        protected Void doInBackground(Song... songs) {
//            songDAO.insert(songs[0]);
//            return null;
//        }
//    }
//
//    private static class UpdateSongAsyncTask extends AsyncTask<Song, Void, Void>{
//
//        private SongDAO songDAO;
//        private UpdateSongAsyncTask(SongDAO songDAO){
//            this.songDAO = songDAO;
//        }
//        @Override
//        protected Void doInBackground(Song... songs) {
//            songDAO.update(songs[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteSongAsyncTask extends AsyncTask<Song, Void, Void>{
//
//        private SongDAO songDAO;
//        private DeleteSongAsyncTask(SongDAO songDAO){
//            this.songDAO = songDAO;
//        }
//        @Override
//        protected Void doInBackground(Song... songs) {
//            songDAO.delete(songs[0]);
//            return null;
//        }
//    }
//}
