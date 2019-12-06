package com.example.tune.database;

import com.example.tune.models.FavoriteSongs;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Helper {

    Realm realm;
    RealmResults<FavoriteSongs> favoriteSongs;

    public Helper(Realm realm) {
        this.realm = realm;
    }

    public void selectFromDb(){
        favoriteSongs = realm.where(FavoriteSongs.class).findAll();

    }

    public ArrayList<FavoriteSongs> refresh(){

        ArrayList<FavoriteSongs> favSongs = new ArrayList<>();
        for (FavoriteSongs s : favoriteSongs){
            favSongs.add(s);
        }

        return favSongs;
    }
}
