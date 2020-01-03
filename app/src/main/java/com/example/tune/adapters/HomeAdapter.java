package com.example.tune.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tune.R;
//import com.example.tune.Song;
import com.example.tune.fragments.BottomPlayerFragment;
import com.example.tune.models.AllSongs;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<AllSongs> allSongs;
    private Context context;

    public HomeAdapter(ArrayList<AllSongs> allSongs, Context context) {
        this.allSongs = allSongs;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, final int position) {

        final String songTitle = allSongs.get(position).getSongTitle();
        String songArtist = allSongs.get(position).getSongArtist();
        String songPath = allSongs.get(position).getSongData();
        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(songPath);
        final byte [] art = metaRetriver.getEmbeddedPicture();

        holder.hMusicTitle.setText(songTitle);
        holder.hMusicArtist.setText(songArtist);
        holder.hMusicImage.setImageResource(R.drawable.music);
        holder.hMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.hMoreOptions);
                popup.inflate(R.menu.home_item_options);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addPlaylist:
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.addFavorite:
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.share:
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.artist:
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.album:
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.editInfo:
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        holder.hContentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                BottomPlayerFragment barFrag = new BottomPlayerFragment();

                Bundle b = new Bundle();
                b.putInt("songPosition", position);
                b.putParcelableArrayList("songData", allSongs);
                barFrag.setArguments(b);

                activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottomBar_container, barFrag)
                    .commit();

            }
        });

        try {
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            if(songImage != null){
                holder.hMusicImage.setImageBitmap(songImage); }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
//        return songs.size();
        int limit = 9;
        if(allSongs.size() > limit){
            return limit;
        } else {
            return allSongs.size();
        }

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView hMusicTitle, hMusicArtist;
        ImageView hMusicImage, hMoreOptions;
        CardView hContentHome;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            hMusicTitle = itemView.findViewById(R.id.musicTitle);
            hMusicArtist = itemView.findViewById(R.id.musicArtist);
            hMusicImage = itemView.findViewById(R.id.musicImage);
            hMoreOptions = itemView.findViewById(R.id.moreOptions);
            hContentHome = itemView.findViewById(R.id.contentHome);

        }
    }

//    public void setSongs(List<Song> songs){
//        this.songs = songs;
//
//    }

}
