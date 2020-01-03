package com.example.tune.fragments;


import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tune.R;
//import com.example.tune.Song;
//import com.example.tune.SongViewModel;
import com.example.tune.adapters.HomeAdapter;
import com.example.tune.models.AllSongs;
import com.example.tune.adapters.MainScreenAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //Declarations for views
    private RecyclerView hNewlyAddedRV, hRecentlyPlayedRV, hPlayQueRV;
    private TextView hPlayQueTV,hRecentlyPlayedTV, hNewlyAddedTV;
    private Button hOpenQue;
    private LinearLayout hPlayQueLinLay, hRecentlyPlayedLinLay, hNewlyAddedLinLay;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);
        fvb(view);
        populateRecycler();
        setUpViews();

        return view;
    }

    private View fvb(View view){

        //FindVewByIds
        hPlayQueRV = view.findViewById(R.id.playQueRV);
        hRecentlyPlayedRV = view.findViewById(R.id.RecentlyPlayedRV);
        hNewlyAddedRV = view.findViewById(R.id.NewlyAddedRV);

        hPlayQueTV = view.findViewById(R.id.playQueTV);
        hRecentlyPlayedTV = view.findViewById(R.id.recentlyPlayedTV);
        hNewlyAddedTV = view.findViewById(R.id.newlyAddedTV);

        hPlayQueLinLay = view.findViewById(R.id.playQueLinLay);
        hRecentlyPlayedLinLay = view.findViewById(R.id.recentlyPlayedLinLay);
        hNewlyAddedLinLay = view.findViewById(R.id.newlyAddedLinLay);
        return view;
    }

    private void setUpViews(){

        hPlayQueTV.setText("Play Que");
        hRecentlyPlayedTV.setText("Recently Played");
        hNewlyAddedTV.setText("Newly Added");
        hPlayQueLinLay.setVisibility(View.GONE);
        hRecentlyPlayedLinLay.setVisibility(View.GONE);
    }

    private void populateRecycler(){

        ArrayList<AllSongs> hSongs = new ArrayList<>();
        getSongsFromPhone(hSongs);
        HomeAdapter hAdapter = new HomeAdapter(hSongs, getContext());
        GridLayoutManager hGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        hNewlyAddedRV.setLayoutManager(hGridLayoutManager);
        hNewlyAddedRV.setAdapter(hAdapter);
    }

    private ArrayList<AllSongs> getSongsFromPhone(ArrayList<AllSongs> arrayList){

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selectionArgsMp3 = new String[]{ mimeType };

        ContentResolver hResolver = getActivity().getContentResolver();
        Uri hUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor hCursor = hResolver.query(hUri, null, selectionMimeType, selectionArgsMp3, null);
        if(hCursor !=null && hCursor.moveToFirst()){
            //get columns
            int songId = hCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = hCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = hCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songData = hCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDateAdded = hCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            //add data to the array
            do {
                long currentId = hCursor.getLong(songId);
                long currentDate = hCursor.getLong(songDateAdded);
                String currentTitle = hCursor.getString(songTitle);
                String currentArtist = hCursor.getString(songArtist);
                String currentData = hCursor.getString(songData);

                arrayList.add(new AllSongs(currentId, currentDate, currentTitle, currentArtist, currentData));
            }
            while (hCursor.moveToNext());
        }
        return arrayList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.song_playing_menu, menu);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.playlist);
        if(item!=null)
            item.setVisible(false);
    }

}
