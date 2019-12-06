package com.example.tune.fragments;


import android.app.Activity;
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

import com.example.tune.R;
import com.example.tune.adapters.HomeAdapter;
import com.example.tune.models.AllSongs;
import com.example.tune.adapters.MainScreenAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //Declarations for views
    private RecyclerView hNewlyAddedRV, hRecentlyPlayedRV, hPlayQueRV;
    private TextView hPlayQueTV,hRecentlyPlayedTV, hNewlyAddedTV;
    private Button hOpenQue;
    private LinearLayout hPlayQueLinLay, hRecentlyPlayedLinLay, hNewlyAddedLinLay;

    //Declarations for data
    private ArrayList<AllSongs> hSongs;
    private ContentResolver hResolver;
    private Uri hUri;
    private Cursor hCursor;
    private HomeAdapter hAdapter;

    //Declarations for the adapter
    private GridLayoutManager hGridLayoutManager;

    //Declarations for fragments
    private FragmentManager hFragmentManager;
    private HomeFragment hHome;

    private RecyclerView recyclerView;
//    private Activity myActivity;



//    private MainScreenAdapter adapter;
//    private LinearLayoutManager linearLayoutManager;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        setHasOptionsMenu(true);

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

        hFragmentManager = getFragmentManager();
        populateRecycler();
        setUpViews();

        return view;
    }

    private void populateRecycler(){

        hSongs = getSongsFromPhone();
        hAdapter = new HomeAdapter(hSongs, getContext());
        hGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        hNewlyAddedRV.setLayoutManager(hGridLayoutManager);
        hNewlyAddedRV.setAdapter(hAdapter);

    }

    private void setUpViews(){

        hPlayQueTV.setText("Play Que");
        hRecentlyPlayedTV.setText("Recently Played");
        hNewlyAddedTV.setText("Newly Added");
        hPlayQueLinLay.setVisibility(View.GONE);
        hRecentlyPlayedLinLay.setVisibility(View.GONE);
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        adapter = new MainScreenAdapter(hSongs, getContext());
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(adapter);
//
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        myActivity = (Activity) context;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        myActivity = activity;
//    }
    

    private ArrayList<AllSongs> getSongsFromPhone(){

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selectionArgsMp3 = new String[]{ mimeType };

        hSongs = new ArrayList<AllSongs>();
        hResolver = getActivity().getContentResolver();
        hUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        hCursor = hResolver.query(hUri, null, selectionMimeType, selectionArgsMp3, null);
        if(hCursor!=null && hCursor.moveToFirst()){
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

                hSongs.add(new AllSongs(currentId, currentDate, currentTitle, currentArtist, currentData));
            }
            while (hCursor.moveToNext());
        }
        return hSongs;
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
