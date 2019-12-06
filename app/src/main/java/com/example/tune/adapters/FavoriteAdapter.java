package com.example.tune.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tune.R;
import com.example.tune.fragments.SongPlayingFragment;
import com.example.tune.models.FavoriteSongs;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class FavoriteAdapter extends RealmRecyclerViewAdapter<FavoriteSongs, FavoriteAdapter.FavViewHolder> {

    private Realm realm;

    public interface FavoriteListener{

    }

    private final FavoriteListener listener;
    private final Context context;



    public FavoriteAdapter(@Nullable RealmResults<FavoriteSongs> realmResults, Realm realm, FavoriteListener listener, Context context) {
        super(realmResults, true);
        this.realm = realm;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_favorite_adapter, parent, false);

        return new FavoriteAdapter.FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, final int position) {

        final FavoriteSongs songs = getData().get(position);

        final String songTitle = songs.getSongTitle();
        String songArtist = songs.getSongArtist();

        holder.mTitle.setText(songTitle);
        holder.mArtist.setText(songArtist);

        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavorite(position);
            }
        });

//        holder.mFavorite.setOnClickListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    removeFavorite(position);
//                }
//            }
//        });

        holder.favMusicHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                SongPlayingFragment songFrag = new SongPlayingFragment();

                Bundle b = new Bundle();
                b.putString("favTitle", songTitle);
                b.putInt("favPosition", position);
                songFrag.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, songFrag)
                        .addToBackStack("B")
                        .commit();
            }
        });

    }

    public class FavViewHolder extends RecyclerView.ViewHolder{

        TextView mTitle,mArtist;
        CheckBox mFavorite;
        RelativeLayout favMusicHolder;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.favoriteSongTitle);
            mArtist = itemView.findViewById(R.id.favoriteSongArtist);
            mFavorite = itemView.findViewById(R.id.favoriteFavorite);
            favMusicHolder = itemView.findViewById(R.id.contentRowFav);
        }
    }

    protected FavoriteSongs getFavorite(int position){
        return getData().get(position);
    }

    protected void removeFavorite(int position) {

        realm.beginTransaction();
        FavoriteSongs songs = getData().get(position);
//        FavoriteSongs song =  realm.where(FavoriteSongs.class).equalTo("songTitle", getFavorite(position).getSongTitle()).findFirst();
        songs.deleteFromRealm();
        realm.commitTransaction();
        Toast.makeText(context, "Song removed from Favorites", Toast.LENGTH_SHORT).show();
    }
}
