package com.example.tune.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.tune.fragments.BottomPlayerFragment;

public class AudioPlay {



    private static MediaPlayer mediaPlayer;
    public static void playAudio(Context c, Uri id){
        mediaPlayer = MediaPlayer.create(c,id);
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
    public static void stopAudio(Context c, Uri id){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public static int duration(Context c, Uri id){
        mediaPlayer = MediaPlayer.create(c,id);
        return mediaPlayer.getDuration();
    }

    public static int audioSession(Context c, Uri id){
        mediaPlayer = MediaPlayer.create(c,id);
        return mediaPlayer.getAudioSessionId();
    }

    public static int currentPositionx(Context c, Uri id){
        mediaPlayer = MediaPlayer.create(c,id);
        return mediaPlayer.getCurrentPosition();
    }
}
