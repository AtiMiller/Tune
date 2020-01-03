package com.example.tune.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tune.R;
import com.example.tune.models.AllSongs;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomPlayerFragment extends Fragment {

    //Declarations for views
    private ProgressBar bMusicProgress;
    private ImageView bNowPlayingIV;
    private TextView bNowPlayingTitleTv, bNowPlayingArtistTv;
    private ImageButton bNowPlayingPreviousBtn, bNowPlayingPlayBtn, bNowPlayingNextBtn;
    private LinearLayout bBottomMusic;


    //Declarations for storing data
    private ArrayList<AllSongs> fetchSongs;
    private int songPosition;
    private Realm realm;
    private AllSongs songModelB;
    private String songData, songTitle, songArtist, favData, favTitle, favArtist;
    private long songId, dateAdded, favId, favDateAdded;

    private MediaPlayer player;
    private Handler handler;
    private Context bottomContext;
    private FragmentManager fm;
    private boolean playing = false;

    private int currentPosition;


    private ImageButton playBtn;
    private TextView titleTv, artistTv;
    private RelativeLayout mBottomMusic;


    public BottomPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bottomContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getData();
        super.onCreate(savedInstanceState);
    }

    private void getData(){

        /*In this method the  bundle receives the data, transferred form the Home Adapter.*/

        Bundle b = getArguments();
        try{
            if (b != null) {

                //Get data from the HomeAdapter
                playing = b.getBoolean("playing", false);
                currentPosition = b.getInt("currentPosition");
                songPosition = b.getInt("songPosition");
                fetchSongs = b.getParcelableArrayList("songData");
                Toast.makeText(bottomContext, "getCurrentPosition"+currentPosition, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(bottomContext, "NoData", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_bar, container, false);

        //This is function is called when you want to switch to this fragment and you want
        //your songModelB to be played where it was left...
//        player =  MediaPlayer.create(getActivity(), Uri.parse(songData));
//        player.seekTo(seekPosition);
//        player.start();
        viewInit(view);
        checkPosition();
        clickHandler();


//
//        checkPlaying();

        return view;
    }

    private View viewInit(View view){

        bMusicProgress = view.findViewById(R.id.musicProgress);
        bNowPlayingIV = view.findViewById(R.id.nowPlayingIV);
        bNowPlayingTitleTv = view.findViewById(R.id.nowPlayingTitleTv);
        bNowPlayingArtistTv = view.findViewById(R.id.nowPlayingArtistTv);
        bNowPlayingPreviousBtn = view.findViewById(R.id.nowPlayingPreviousBtn);
        bNowPlayingPlayBtn = view.findViewById(R.id.nowPlayingPlayBtn);
        bNowPlayingNextBtn = view.findViewById(R.id.nowPlayingNextBtn);
        bBottomMusic = view.findViewById(R.id.bottomMusic);

        return view;
    }

    private void clickHandler() {

        bNowPlayingPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.isPlaying()){
                    player.pause();
                    bNowPlayingPlayBtn.setBackgroundResource(R.drawable.play_circle);
                }else{
                    playSong();
                    bNowPlayingPlayBtn.setBackgroundResource(R.drawable.pause_circle);
                }
            }
        });

        bNowPlayingNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        bNowPlayingPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSong();
            }
        });

        bBottomMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerFrag();
            }
        });
    }

    private void openPlayerFrag() {

        currentPosition = player.getCurrentPosition();

        PlayerFragment songFrag = new PlayerFragment();
        Bundle b = new Bundle();

        fm = getFragmentManager();
        fm.beginTransaction()
            .add(R.id.bottomBar_container, songFrag)
            .hide(this)
            .addToBackStack("A")
            .commit();

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

        songFrag.setArguments(b);

        player.stop();
    }

    private void nextSong(){
        songPosition++;
       checkPosition();
    }

    private void previousSong(){
        songPosition--;
       checkPosition();
    }

    private void checkPosition(){

        /*This method will be called to check the current songModelB's position*/

        if(songPosition == fetchSongs.size()){
            songModelB = fetchSongs.get(fetchSongs.size() - 1);
            createModel();
        }else if (songPosition == -1){
            songModelB = fetchSongs.get(0);
            createModel();
        }else {
            songModelB = fetchSongs.get(songPosition);
            createModel();
        }
    }

    private void createModel(){

        /*This method will be called to create the object model*/

        songData = songModelB.getSongData();
        songArtist = songModelB.getSongArtist();
        songTitle = songModelB.getSongTitle();
        songId = songModelB.getSongId();
        dateAdded = songModelB.getSongDateAdded();

        updateViews();
    }

    private void updateViews() {

        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(songData);
        final byte [] art = metaRetriver.getEmbeddedPicture();

        bNowPlayingTitleTv.setText(songTitle);
        bNowPlayingArtistTv.setText(songArtist);
        bNowPlayingIV.setImageResource(R.drawable.music);

        try {
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            if(songImage != null){
                bNowPlayingIV.setImageBitmap(songImage);}
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkPlaying();
    }

    private void checkPlaying(){

/*     This method will check if the user clicked on another song on the list populated in the HomeFragment
       while the music was playing or while the music was not playing.*/

        if(player != null && player.isPlaying()) {
            Toast.makeText(bottomContext, "player != null && player.isPlaying", Toast.LENGTH_SHORT).show();
            player.stop();
            player.release();
            playSong();
        }else if (playing) {
            player = MediaPlayer.create(bottomContext, Uri.parse(songData));
            player.seekTo(currentPosition);
            player.start();
        }else if (player == null) {
            playSong();
        }else {
            Toast.makeText(bottomContext, "else", Toast.LENGTH_SHORT).show();
            player = MediaPlayer.create(bottomContext, Uri.parse(songData));
            player.seekTo(currentPosition);
            player.pause();
        }
    }

    private void playSong() {

        /* This method will be executed, to play the audio with the media player.*/

        player = new MediaPlayer();

        player.reset();
        try {
            player.setDataSource(bottomContext, Uri.parse(songData));
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateProgress();
//        currentPosition = player.getCurrentPosition();

    }

    private void updateProgress() {

        handler = new Handler();

        int  duration = player.getDuration();

        bMusicProgress.setMax(duration/1000);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(player!=null){
                    int position = player.getCurrentPosition()/1000; // In milliseconds
                    bMusicProgress.setProgress(position);
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }


    @Override
    public void onStop() {
        player.pause();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
