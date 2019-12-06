package com.example.tune.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tune.R;
import com.example.tune.models.AllSongs;
import com.example.tune.models.FavoriteSongs;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongPlayingFragment extends Fragment {

    private Activity myActivity;
    private MediaPlayer player;

    //Declarations for views
    private TextView startTimeTv, endTime, songArtistTv, songTitleTv;
    private ImageButton playPauseBtn, nextBtn, previousBtn;
    private SeekBar seekBar;
    private CheckBox shuffleBtn,loopBtn, favoriteButton;
    private WaveVisualizer visualizer;

    //Declarations for fragment
    private FragmentManager fm;
    private Fragment barFrag;

    //Declarations for receiving the data
    private int currentPosition;
    private int favPosition;
    private int favSize;
    private ArrayList<AllSongs> fetchSongs;
    private AllSongs songModel;
    private FavoriteSongs favSong;
    private String songData, songTitle, songArtist, favData, favTitle, favArtist;
    private long songId, dateAdded, favId, favDateAdded;

    //Conditional declarations
    private String link = "";
    private boolean openBar = false;
    private boolean favourite = false;
    private boolean shuffle = false;
    private boolean loop = false;
    private boolean isChecked;
    private Random rand;
    private int randomPosition;
    private Handler handler;

    //Declarations saving data
    private Realm realm;
    private SharedPreferences.Editor saveShuffle, saveLoop, saveBar;
    private SharedPreferences sharedPrefsShuffle, sharedPrefsLoop, sharedPrefsBar;

    public SongPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_playing, container, false);

        setHasOptionsMenu(true);

        seekBar = view.findViewById(R.id.seekBar);
        startTimeTv = view.findViewById(R.id.start);
        endTime = view.findViewById(R.id.endTime);
        songArtistTv = view.findViewById(R.id.Artist);
        songTitleTv = view.findViewById(R.id.Title);
        playPauseBtn = view.findViewById(R.id.playPauseButton);
        nextBtn = view.findViewById(R.id.nextButton);
        previousBtn = view.findViewById(R.id.previousButton);
        loopBtn = view.findViewById(R.id.loopButton);
        shuffleBtn = view.findViewById(R.id.shuffleButton);
        visualizer = view.findViewById(R.id.blast);
        favoriteButton = view.findViewById(R.id.favoriteSongPlaying);


        player = new MediaPlayer();
        rand = new Random();
        handler = new Handler();
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();


        clickHandler();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myActivity = activity;
    }

    /*When the user pressed a song on the MainScreen, through the Adapter this fragment will be
    attached to the main activity. After this fragment is attached, the onActivityCreated method
    will be executed.*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fm = getFragmentManager();

            checkPlaying();
            playAutomatically();


        /*I remove this function until the bottom bar will be synchronized properly with the
        * song playing fragment, like knowing which song was played last time, knowing
          how to switch between the two fragments while the music is playing...etc.*/
//        sharedPrefsBar = PreferenceManager.getDefaultSharedPreferences(myActivity);
//
//        boolean bar = sharedPrefsBar.getBoolean("openBar", false);
//        if(bar){
//            openBar();
//        }else{
//            checkPlaying();
//            playAutomatically();
//        }


    }

    private void checkPlaying(){

/*     This method will check if the user clicked on another song on the list populated in the HomeFragment
       while the music was playing or while the music was not playing.*/

        if(player != null && player.isPlaying()){
            player.stop();
            player.release();
            playPauseBtn.setBackgroundResource(R.drawable.pause_button);
            returnSavedData();
        }else {
            playPauseBtn.setBackgroundResource(R.drawable.pause_button);
            returnSavedData();
        }

    }

    private void returnSavedData(){

/*    This method will return the user inputs. If the user pressed the loop or the shuffle button, then
      this method will be executed and will return the click events with the sharer preferences class.*/

        sharedPrefsShuffle = PreferenceManager.getDefaultSharedPreferences(myActivity);
        boolean shuffle = sharedPrefsShuffle.getBoolean("shuffleOn", true);
        if(shuffle){
            shuffleBtn.setChecked(true);
        }
        sharedPrefsLoop = PreferenceManager.getDefaultSharedPreferences(myActivity);
        boolean loop = sharedPrefsLoop.getBoolean("loopOn", true);
        if(loop){
            loopBtn.setChecked(true);
        }

        getData();

    }

    private void getData(){

        //With Bundle I will receive all the data from the Adapter and I will create a Model
        //Get data from the MainScreenAdapter
        Bundle b = getArguments();
        try {
            fetchSongs = b.getParcelableArrayList("songData");
            currentPosition = b.getInt("songPosition");
            songTitle = b.getString("songTitle");
            if(fetchSongs.size() != 0){
                favoriteCheck();
                checkPosition();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //Get data from the favoriteAdapter
        try{
            favTitle = b.getString("favTitle");
            favPosition = b.getInt("favPosition");
            if(favTitle != null){
                favoriteCheck();
                favourite = true;
                checkFavoritePosition();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clickHandler(){

        /*This method will be called, whenever the user pressed a button in this fragment.*/

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteCheck();
                if(isChecked){
                    favoriteButton.setChecked(false);
                    removeFavoriteSong();
                }else if (!isChecked) {
                    favoriteButton.setChecked(true);
                    addFavoriteSong();
                }
            }
        });

        shuffleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                saveShuffle = PreferenceManager.getDefaultSharedPreferences(myActivity).edit();

                if(b){
                    link = "ShuffleON";
                    saveShuffle.putBoolean("shuffleOn", true).apply();
                    loopBtn.setChecked(false);
                }else {
                    link = "ShuffleOFF";
                    saveShuffle.putBoolean("shuffleOn", false).apply();
                }
            }
        });

        loopBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                saveLoop = PreferenceManager.getDefaultSharedPreferences(myActivity).edit();

                if(b){
                    link = "LoopON";
                    saveLoop.putBoolean("loopOn", true).apply();
                    shuffleBtn.setChecked(false);
                }else{
                    link = "LoopOFF";
                    saveLoop.putBoolean("loopOn", false).apply();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (link){
                    case "ShuffleON":
                        nextAndPrevShuffle();
                        break;
                    case "LoopON":
                       nextAndPrevLoop();
                        break;
                        default:
                            nextNormal();
                }
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (link){
                    case "ShuffleON":
                        nextAndPrevShuffle();
                        break;
                    case "LoopON":
                        nextAndPrevLoop();
                        break;
                        default:
                            prevNormal();
                }
            }
        });

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying()){
                    player.pause();
                    playPauseBtn.setBackgroundResource(R.drawable.play_button);
                }else{
                    player.start();
                    playPauseBtn.setBackgroundResource(R.drawable.pause_button);
                }
            }
        });

    }

    private void nextAndPrevShuffle(){

        if(favourite){
            randomPosition = rand.nextInt(favSize);
            favPosition = randomPosition;
            checkFavoritePosition();
        }else {
            randomPosition = rand.nextInt(fetchSongs.size());
            currentPosition = randomPosition;
            checkPosition();
        }
    }

    private void nextAndPrevLoop(){

        if(favourite){
            checkFavoritePosition();
        }else {
            checkPosition();
        }

    }

    private void nextNormal(){

        if(favourite){
            favPosition++;
            checkFavoritePosition();
        }else {
            currentPosition++;
            checkPosition();
        }
    }

    private void prevNormal(){

        if(favourite){
            favPosition--;
            checkFavoritePosition();
        }else{
            currentPosition--;
            checkPosition();
        }
    }

    private void checkPosition(){

        /*This method will be called to check the current song's position*/
        playPauseBtn.setBackgroundResource(R.drawable.pause_button);

        if(currentPosition == fetchSongs.size()){
            songModel = fetchSongs.get(fetchSongs.size() - 1);
            createModel();
        }else if (currentPosition == -1){
            songModel = fetchSongs.get(0);
            createModel();
        }else {
            songModel = fetchSongs.get(currentPosition);
            createModel();
        }

    }

    private void createModel(){

        /*This method will be called to create the object model*/

        songData = songModel.getSongData();
        songArtist = songModel.getSongArtist();
        songTitle = songModel.getSongTitle();
        songId = songModel.getSongId();
        dateAdded = songModel.getSongDateAdded();
        favoriteCheck();
        playSong();
    }

    private void playSong(){
        /* This method will be executed, to play the audio with the media player.*/

        player.reset();
        try{
            player.setDataSource(myActivity, Uri.parse(songData));
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        int audio = player.getAudioSessionId();
        if(audio != -1){
            visualizer.setAudioSessionId(audio);
        }
        updateViews();
    }

    private void checkFavoritePosition () {

        playPauseBtn.setBackgroundResource(R.drawable.pause_button);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                favSize = realm.where(FavoriteSongs.class).findAll().size();
                if(favPosition == favSize || favPosition > favSize){
                    favSong = realm.where(FavoriteSongs.class).findAll().last();
                    createFavoriteModel();
                } else if(favPosition == -1 || favPosition == 0){
                    favSong = realm.where(FavoriteSongs.class).findAll().get(0);
                    createFavoriteModel();
                } else if (favPosition > 0) {
                    favSong = realm.where(FavoriteSongs.class).findAll().get(favPosition);
                    createFavoriteModel();
                }
            }
        });
    }

    private void createFavoriteModel() {

        favData = favSong.getSongData();
        favArtist = favSong.getSongArtist();
        favTitle = favSong.getSongTitle();
        favId = favSong.getSongId();
        favDateAdded = favSong.getDateAdded();
        favoriteCheck();
        playFavorite();
    }

    private void playFavorite(){
        /* This method will be executed, to play the audio with the media player.*/

        player.reset();
        try{
            player.setDataSource(myActivity, Uri.parse(favData));
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        int audio = player.getAudioSessionId();
        if(audio != -1){
            visualizer.setAudioSessionId(audio);
        }
        updateViews();
    }

    private void playAutomatically(){

        /*This method will be called to play a song automatically*/

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {

                if(!player.isPlaying()){
                    if(shuffle){
                        randomPosition = rand.nextInt(fetchSongs.size());
                        currentPosition = randomPosition;
                        playSong();
                    } else if(loop){
                        playSong();
                    }else{
                        currentPosition = currentPosition +1;
                        playSong();
                    }
                }
            }
        });
    }

    private void updateViews(){

        /*This method, will update the views with data.*/
        if(songTitle != null){
            songTitleTv.setText(songTitle);
            songArtistTv.setText(songArtist);
        }else if (favTitle != null){
            songTitleTv.setText(favTitle);
            songArtistTv.setText(favArtist);
        }
        int finalTime = player.getDuration();

        String duration = String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)));

        endTime.setText(duration);
        seekBar.setMax(finalTime/1000);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(player!=null){
                    int mCurrentPosition = player.getCurrentPosition()/1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                    startTimeTv.setText(String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) mCurrentPosition*1000),
                    TimeUnit.MILLISECONDS.toSeconds((long) mCurrentPosition*1000) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) mCurrentPosition*1000))));
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(player != null && b){
                    player.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void favoriteCheck(){

        /*This method will check if the current song is added to the favourites or not.
        * If the current song is added to the favourites, then the favourite ckeck box will be checked.*/

        FavoriteSongs phoneDataQuery = realm.where(FavoriteSongs.class)
                .equalTo("songTitle", songTitle).findFirst();

        FavoriteSongs favoriteDataQuery = realm.where(FavoriteSongs.class)
                .equalTo("songTitle", favTitle).findFirst();

        if(phoneDataQuery != null || favoriteDataQuery != null){
            favoriteButton.setChecked(true);
        }else if (phoneDataQuery == null || favoriteDataQuery == null){
            favoriteButton.setChecked(false);
        }
        isChecked = favoriteButton.isChecked();
    }

    private void addFavoriteSong() {

        /*THis method will be called when the user wants to add a song to Favorites*/

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FavoriteSongs song = realm.createObject(FavoriteSongs.class);
                song.setSongData(songData);
                song.setSongArtist(songArtist);
                song.setSongTitle(songTitle);
                song.setSongId(songId);
                song.setDateAdded(dateAdded);
            }
        }, new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess() {
                Toast.makeText(myActivity, "Song added to favorites" , Toast.LENGTH_SHORT).show();
            }
        }
        );

    }

    private void removeFavoriteSong() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FavoriteSongs music = realm.where(FavoriteSongs.class).equalTo("songTitle", songTitle).or().equalTo("songTitle", favTitle).findFirst();
                music.deleteFromRealm();
                Toast.makeText(myActivity, "Song removed to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.song_playing_menu, menu);
    }

    /*TODO Create a bottom bar fragment for playing music in the background 
    *   */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        saveBar = PreferenceManager.getDefaultSharedPreferences(myActivity).edit();

        switch (item.getItemId()){
            case R.id.playlist:
                openBar();
//                saveBar.putBoolean("openBar", true).apply();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openBar(){

//        openBar = true;

//        player.pause();
//        int lenght = player.getCurrentPosition();
//        Bundle b = new Bundle();
//        b.putString("songData", songData);
//        b.putInt("seekPosition", lenght);
//        barFragment.setArguments(b);


        fm.beginTransaction()
                .hide(this)
                .show(barFrag)
                .commit();
    }

    @Override
    public void onDestroyView() {
//        stopAudio();
        realm.close();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void stopAudio() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
