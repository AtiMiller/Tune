package com.example.tune.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;

public class AudioPlay {

    public static MediaPlayer mediaPlayer;
    public static void playAudio(Context c, Uri id){
        mediaPlayer = MediaPlayer.create(c,id);
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
    public static void stopAudio(Context c, Uri id){
        mediaPlayer.stop();
    }
}
