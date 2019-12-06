package com.example.tune.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AllSongs extends ArrayList implements Parcelable {

        long songId, songDateAdded;
        String songTitle, songArtist, songData;

    public AllSongs(long songId, long songDateAdded, String songTitle, String songArtist, String songData) {
        this.songId = songId;
        this.songDateAdded = songDateAdded;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songData = songData;
    }

    protected AllSongs(Parcel in) {
        songId = in.readLong();
        songDateAdded = in.readLong();
        songTitle = in.readString();
        songArtist = in.readString();
        songData = in.readString();
    }

    public static final Creator<AllSongs> CREATOR = new Creator<AllSongs>() {
        @Override
        public AllSongs createFromParcel(Parcel in) {
            return new AllSongs(in);
        }

        @Override
        public AllSongs[] newArray(int size) {
            return new AllSongs[size];
        }
    };

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getSongDateAdded() {
        return songDateAdded;
    }

    public void setSongDateAdded(long songDateAdded) {
        this.songDateAdded = songDateAdded;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongData() {
        return songData;
    }

    public void setSongData(String songData) {
        this.songData = songData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(songId);
        parcel.writeLong(songDateAdded);
        parcel.writeString(songTitle);
        parcel.writeString(songArtist);
        parcel.writeString(songData);
    }
}
