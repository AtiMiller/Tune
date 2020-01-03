package com.example.tune.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tune.R;
import com.example.tune.fragments.BottomPlayerFragment;
import com.example.tune.models.AllSongs;
import com.example.tune.fragments.PlayerFragment;

import java.util.ArrayList;

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.MyViewHolder> {

    private ArrayList<AllSongs> songs;
    private Context context;


    public MainScreenAdapter(ArrayList<AllSongs> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_mainscreen_adapter, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final String songTitle = songs.get(position).getSongTitle();
        String songArtist = songs.get(position).getSongArtist();

        holder.trackTitle.setText(songTitle);
        holder.trackArtist.setText(songArtist);
        holder.contentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PlayerFragment songFrag = new PlayerFragment();
                BottomPlayerFragment barFrag = new BottomPlayerFragment();

                Bundle b = new Bundle();
                b.putInt("songPosition", position);
                b.putString("songTitle", songTitle);
                b.putParcelableArrayList("songData", songs);
                songFrag.setArguments(b);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, songFrag, "SongFrag")
                        .add(R.id.container_fragment, barFrag,"BarFrag")
                        .hide(barFrag)
                        .commit();



            }
        });
    }


    @Override
    public int getItemCount() {

            return songs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trackTitle, trackArtist;
        RelativeLayout contentHolder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            trackTitle = itemView.findViewById(R.id.trackTitle);
            trackArtist = itemView.findViewById(R.id.trackArtist);
            contentHolder = itemView.findViewById(R.id.contentRow);

        }
    }
}
