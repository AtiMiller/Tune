package com.example.tune.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tune.BlurBuilder;
import com.example.tune.R;
import com.example.tune.adapters.AudioPlay;
import com.example.tune.models.AllSongs;
import com.example.tune.models.FavoriteSongs;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    private MediaPlayer player;

    //Declarations for views
    private TextView startTimeTv, endTimeTV, songArtistTv, songTitleTv;
    private ImageButton playPauseBtn, nextBtn, previousBtn;
    private SeekBar seekBar;
    private CheckBox shuffleBtn,loopBtn, favoriteButton;
    private WaveVisualizer visualizer;
    private Toolbar sToolbar;
    private ImageView mNowPlayingIV, mControlBackgroundIV;

    //Declarations for receiving the data
    private int songPosition;
    private int currentPosition;
    private boolean playing;
    private ArrayList<AllSongs> fetchSongs;
    private String songData, songTitle, songArtist;

//    private int favPosition;
//    private int favSize;

//    private FavoriteSongs favSong;
//    , favData, favTitle, favArtist;
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
    private MediaMetadataRetriever metaRetriever;


    //Declarations saving data
    private SharedPreferences.Editor saveShuffle, saveLoop, saveBar;
    private SharedPreferences sharedPrefsShuffle, sharedPrefsLoop, sharedPrefsBar;

    private Context playerContext;



    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        playerContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getData();
        super.onCreate(savedInstanceState);
    }

    private void getData(){

        //With Bundle I will receive all the data from the Adapter and I will create a Model
        //Get data from the BottomPlayer
        Bundle b = getArguments();

        if (b != null) {
            songPosition = b.getInt("songPosition");
            currentPosition = b.getInt("currentPosition");
            fetchSongs = b.getParcelableArrayList("songData");
            playing = b.getBoolean("playing", false);
            checkPosition();
        }
    }

    private void checkPosition(){

        /*This method will be called to check the current song's position*/

        AllSongs songModel;

        if(songPosition == fetchSongs.size()){
            songModel = fetchSongs.get(fetchSongs.size() - 1);
            createModel(songModel);
        }else if (songPosition == -1){
            songModel = fetchSongs.get(0);
            createModel(songModel);
        }else {
            songModel = fetchSongs.get(songPosition);
            createModel(songModel);
        }

        player =  MediaPlayer.create(playerContext, Uri.parse(songData));
    }

    private AllSongs createModel(AllSongs songModel){

        /*This method will be called to create the object model*/

        songData = songModel.getSongData();
        songArtist = songModel.getSongArtist();
        songTitle = songModel.getSongTitle();
        songId = songModel.getSongId();
        dateAdded = songModel.getSongDateAdded();
        return  songModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_playing, container, false);

        metaRetriever = new MediaMetadataRetriever();
        setHasOptionsMenu(true);
        viewInit(view);
        clickHandler();
        updateViews();



//        rand = new Random();
//        Realm.init(getContext());

        return view;
    }



    private View viewInit (View view){
        seekBar = view.findViewById(R.id.seekBar);
        startTimeTv = view.findViewById(R.id.start);
        endTimeTV = view.findViewById(R.id.endTime);
        songArtistTv = view.findViewById(R.id.Artist);
        songTitleTv = view.findViewById(R.id.Title);
        playPauseBtn = view.findViewById(R.id.playPauseButton);
        nextBtn = view.findViewById(R.id.nextButton);
        previousBtn = view.findViewById(R.id.previousButton);
        loopBtn = view.findViewById(R.id.loopButton);
        shuffleBtn = view.findViewById(R.id.shuffleButton);
        visualizer = view.findViewById(R.id.blast);
        favoriteButton = view.findViewById(R.id.favoriteSongPlaying);
        sToolbar = view.findViewById(R.id.StoolBar);
        mNowPlayingIV = view.findViewById(R.id.mPNowPlayingImage);
        mControlBackgroundIV= view.findViewById(R.id.mPcontrolBackground);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(playing){
            playPauseBtn.setBackgroundResource(R.drawable.pause_circle);
            player.seekTo(currentPosition);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!player.isPlaying()){
                        player.start();
                    }
                }
            });
        }
        super.onActivityCreated(savedInstanceState);
    }

    private void returnSavedData(){

/*    This method will return the user inputs. If the user pressed the loop or the shuffle button, then
      this method will be executed and will return the click events with the sharer preferences class.*/

        sharedPrefsShuffle = PreferenceManager.getDefaultSharedPreferences(playerContext);
        boolean shuffle = sharedPrefsShuffle.getBoolean("shuffleOn", true);
        if(shuffle){
            shuffleBtn.setChecked(true);
        }
        sharedPrefsLoop = PreferenceManager.getDefaultSharedPreferences(playerContext);
        boolean loop = sharedPrefsLoop.getBoolean("loopOn", true);
        if(loop){
            loopBtn.setChecked(true);
        }

        getData();

    }

    private void clickHandler(){

        /*This method will be called, whenever the user pressed a button in this fragment.*/

        sToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomFrag();

            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked){
                    favoriteButton.setChecked(false);
                }else if (!isChecked) {
                    favoriteButton.setChecked(true);
                }
            }
        });

        shuffleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                saveShuffle = PreferenceManager.getDefaultSharedPreferences(playerContext).edit();

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

                saveLoop = PreferenceManager.getDefaultSharedPreferences(playerContext).edit();

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
        checkPosition();
    }

    private void nextAndPrevLoop(){
        checkPosition();
    }

    private void nextNormal(){
        songPosition++;
        if(player.isPlaying()){
            player.stop();
            player.release();
            checkPosition();
            updateViews();
            player.start();
        }else{
            checkPosition();
            updateViews();
        }
    }

    private void prevNormal(){
        songPosition--;
        if(player.isPlaying()){
            player.stop();
            player.release();
            checkPosition();
            updateViews();
            player.start();
        }else{
            checkPosition();
            updateViews();
        }
    }

    private void updateViews(){

        metaRetriever.setDataSource(songData);
        final byte [] art = metaRetriever.getEmbeddedPicture();
        mNowPlayingIV.setImageResource(R.drawable.music);

        try {
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            if(songImage != null){
                mNowPlayingIV.setImageBitmap(songImage);}
            Bitmap blur = BlurBuilder.blur(playerContext, songImage);
            mControlBackgroundIV.setImageBitmap(blur);
        } catch (Exception e) {
            e.printStackTrace();
        }
        songTitleTv.setText(songTitle);
        songArtistTv.setText(songArtist);

        updateProgress();
    }

    private void updateProgress() {

        handler = new Handler();

        int duration = player.getDuration();
        int audio = player.getAudioSessionId();
        if(audio != -1){
            visualizer.setAudioSessionId(audio);
        }

                String durationFormat = String.format(Locale.UK, "%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) duration),
                TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                duration)));

        endTimeTV.setText(durationFormat);
        seekBar.setMax(duration/1000);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    int position = player.getCurrentPosition()/1000; // In milliseconds
                    seekBar.setProgress(position);
                    startTimeTv.setText(String.format(Locale.UK, "%d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) position*1000),
                            TimeUnit.MILLISECONDS.toSeconds((long) position*1000) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) position*1000))));
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
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


    private void playAutomatically(){

        /*This method will be called to play a song automatically*/

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {

                if(!player.isPlaying()){
                    if(shuffle){
                        randomPosition = rand.nextInt(fetchSongs.size());
                        songPosition = randomPosition;
//                        playSong();
                    } else if(loop){
//                        playSong();
                    }else{
                        songPosition = songPosition +1;
//                        playSong();
                    }
                }
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

        saveBar = PreferenceManager.getDefaultSharedPreferences(playerContext).edit();

        switch (item.getItemId()){
            case R.id.playlist:

//                saveBar.putBoolean("openBottomFrag", true).apply();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openBottomFrag(){

//        openBottomFrag = true;

//        player.pause();
//        int lenght = player.getCurrentPosition();
//        Bundle b = new Bundle();
//        b.putString("songData", songData);
//        b.putInt("seekPosition", lenght);
//        barFragment.setArguments(b);

        Bundle b = new Bundle();
        BottomPlayerFragment barFrag = new BottomPlayerFragment();

        if(player.isPlaying()){
            playing = true;
            b.putBoolean("playing", playing);
            b.putInt("songPosition", songPosition);
            b.putInt("currentPosition", currentPosition);
            b.putParcelableArrayList("songData", fetchSongs);
        }else{
            b.putInt("songPosition", songPosition);
            b.putInt("currentPosition", currentPosition);
            b.putParcelableArrayList("songData", fetchSongs);
        }

        barFrag.setArguments(b);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.bottomBar_container, barFrag)
                .commit();

        player.stop();
    }

    @Override
    public void onDestroyView() {
//        stopAudio();
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
