package com.example.tune.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tune.adapters.FavoriteAdapter;
import com.example.tune.database.Helper;
import com.example.tune.models.FavoriteSongs;
import com.example.tune.R;
//import com.example.tune.adapters.FavoriteScreenAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements FavoriteAdapter.FavoriteListener {

//    private FavoriteScreenAdapter adapter;
    private FavoriteAdapter adapter;
    private LinearLayoutManager manager;
    private DefaultItemAnimator animator;
    private RecyclerView recyclerView;
    private Activity myActivity;
    private Helper helper;
    private Realm realm;
    private RealmChangeListener realmChangeListener;



    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

//        helper = new Helper(realm);
//        helper.selectFromDb();

        recyclerView = view.findViewById(R.id.contentFavorite);
//        adapter = new FavoriteScreenAdapter(helper.refresh(), getContext());
        adapter = new FavoriteAdapter(realm.where(FavoriteSongs.class).findAll(), realm, this, getContext());
        manager = new LinearLayoutManager(myActivity);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);

//        Refresh();
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

//    private void Refresh(){
//
//        realmChangeListener = new RealmChangeListener() {
//            @Override
//            public void onChange(Object o) {
//                adapter = new FavoriteScreenAdapter(helper.refresh(), getContext());
//                recyclerView.setAdapter(adapter);
//            }
//        };
//        realm.addChangeListener(realmChangeListener);
//
//    }



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



}
