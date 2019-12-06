package com.example.tune.fragments;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
public class BottomBarFragment extends Fragment {

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
    private byte[] art;

    private MediaPlayer player;



    private FragmentManager fm;


    private ImageButton playBtn;
    private TextView titleTv, artistTv;
    private RelativeLayout mBottomMusic;

    private Fragment songFrag;



    public BottomBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        getData();
        player = MediaPlayer.create(context, Uri.parse(songData));
        player.start();
        super.onAttach(context);
    }

    private void getData(){
        /*In this method the  bundle receives the data, transferred form the Home Adapter.*/

        //Get data from the HomeAdapter
        Bundle b = getArguments();
        try{
            if (b != null) {
                songPosition = b.getInt("songPosition");
                fetchSongs = b.getParcelableArrayList("songData");
                art = b.getByteArray("songImage");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        createModel();
    }

    private void createModel(){

        /*This method will be called to create the object model*/
        songModelB = fetchSongs.get(songPosition);

        songData = songModelB.getSongData();
        songArtist = songModelB.getSongArtist();
        songTitle = songModelB.getSongTitle();
        songId = songModelB.getSongId();
        dateAdded = songModelB.getSongDateAdded();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

//        if(player.isPlaying()){
//            Toast.makeText(getActivity(), "PLaying", Toast.LENGTH_SHORT).show();
//            player.stop();
//            player.release();
//            player.start();
//        }else if(!player.isPlaying()){
//            Toast.makeText(getActivity(), "NOTPLaying", Toast.LENGTH_SHORT).show();
//            player.start();
//        }
        super.onCreate(savedInstanceState);
    }



    private void isPlaying (){

        /*This method checks if it's playing the audio in the media player or not.*/

        if (player.isPlaying()){
            player.stop();
            player.release();
//            Toast.makeText(getActivity(), "isPlaying", Toast.LENGTH_SHORT).show();
            getData();

        }else {
//            Toast.makeText(getActivity(), "isNOTPlaying", Toast.LENGTH_SHORT).show();
            getData();
        }
    }



    private void playSong(){

        /* This method will be executed, to play the audio with the media player.*/

        player.reset();
        try{
            player.setDataSource(getActivity(), Uri.parse(songData));
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
//        int audio = player.getAudioSessionId();
//        if(audio != -1){
//            visualizer.setAudioSessionId(audio);
//        }
//        updateViews();
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

        bMusicProgress = view.findViewById(R.id.musicProgress);
        bNowPlayingIV = view.findViewById(R.id.nowPlayingIV);
        bNowPlayingTitleTv = view.findViewById(R.id.nowPlayingTitleTv);
        bNowPlayingArtistTv = view.findViewById(R.id.nowPlayingArtistTv);
        bNowPlayingPreviousBtn = view.findViewById(R.id.nowPlayingPreviousBtn);
        bNowPlayingPlayBtn = view.findViewById(R.id.nowPlayingPlayBtn);
        bNowPlayingNextBtn = view.findViewById(R.id.nowPlayingNextBtn);
        bBottomMusic = view.findViewById(R.id.bottomMusic);


        clickHandler();
//
//        checkPlaying();



//        mBottomMusic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                fm.beginTransaction()
//                        .show(songFrag)
//                        .hide(BottomBarFragment.this)
//                        .commit();
//
//            }
//        });
//
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(player.isPlaying()){
//                    player.pause();
//                    playBtn.setBackgroundResource(R.drawable.play_button);
//                }else{
//                    player.start();
//                    playBtn.setBackgroundResource(R.drawable.pause_button);
//                }
//            }
//        });

        return view;
    }



    private void checkPlaying(){

/*     This method will check if the user clicked on another songModelB on the list populated in the HomeFragment
       while the music was playing or while the music was not playing.*/

        /*TODO check later the player if its playing or not*/

        if(player != null && player.isPlaying()){
            player.stop();
            player.release();
//            bNowPlayingPlayBtn.setBackgroundResource(R.drawable.pause_circle);
            getData();
        }else {
            getData();
//            bNowPlayingPlayBtn.setBackgroundResource(R.drawable.pause_circle);
        }

    }


    private void checkPosition(){

        /*This method will be called to check the current songModelB's position*/

        if(songPosition == fetchSongs.size()){
            songModelB = fetchSongs.get(fetchSongs.size() - 1);
//            createModel();
        }else if (songPosition == -1){
            songModelB = fetchSongs.get(0);
//            createModel();
        }else {
            songModelB = fetchSongs.get(songPosition);
//            createModel();
        }

    }





    private void clickHandler() {


        bNowPlayingPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

                if(player.isPlaying()){
                    player.pause();
                    bNowPlayingPlayBtn.setBackgroundResource(R.drawable.play_circle);
                }else{
                    player.start();
                    bNowPlayingPlayBtn.setBackgroundResource(R.drawable.pause_circle);
                }
            }
        });

        bNowPlayingNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bNowPlayingPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onStop() {
        Toast.makeText(getContext(), "Stop", Toast.LENGTH_SHORT).show();
        player.stop();
        player.release();
        super.onStop();
    }
}
