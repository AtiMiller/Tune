package com.example.tune.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.example.tune.R;
import com.example.tune.fragments.AllSongFragment;
import com.example.tune.fragments.BottomPlayerFragment;
import com.example.tune.fragments.FavoriteFragment;
import com.example.tune.fragments.InfoFragment;
import com.example.tune.fragments.HomeFragment;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

import com.example.tune.fragments.SettingsFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    /*This activity works with the drawer layout and the toolbar layout together*/

    //Declarations for views
    private AppBarLayout hAppBarLayout;
    private CollapsingToolbarLayout hCToolbar;
    private Toolbar hToolbar;
    private LinearLayout hPLayQueLinLay, hRecentPlayLinLay, hNewAddLinLay;
    private Button hOpenQue;
    private RecyclerView hPlayQueRV, hRecentPlayRV, hNewAddRV;
    private DrawerLayout hDrawer;
    private NavigationView hNavigationView;
    private NestedScrollView hNestedFrag;


    private FragmentManager fragmentManager = getSupportFragmentManager(); ;
    private InfoFragment info;
    private BottomPlayerFragment bar;

    AppBarLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        viewInit();
        navView();
        toolbarView();
    }

    private void viewInit(){

        hDrawer = findViewById(R.id.drawer_layout_m);
        hNavigationView = findViewById(R.id.nav_view);
        hAppBarLayout = findViewById(R.id.home_appbar);
        hCToolbar = findViewById(R.id.collapsing_toolbar);
        hToolbar = findViewById(R.id.toolbar_home);
    }

    private void navView(){

        hNavigationView.setItemIconTintList(null);
        hNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:

                            openHome();
//                        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//                        CollapsingToolbarLayout.LayoutParams layoutParams=new CollapsingToolbarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT );

//                        AppBarLayout.LayoutParams params= new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
//                        app.setLayoutParams(params);

//                        collapsingToolbar.setLayoutParams(p);
//                        TypedValue tv = new TypedValue();
//                        int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getApplication().getResources().getDisplayMetrics());
//                            AppBarLayout.LayoutParams layoutParams=new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT );
//                            hAppBarLayout.setLayoutParams(layoutParams);



//                        ViewCompat.setNestedScrollingEnabled(hNestedFrag, false);
//                        AppBarLayout.LayoutParams params = hAppBarLayout;
//                        params.set
//                        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);// clear all scroll flags
//                        TypedValue tv = new TypedValue();
//                        if (getApplication().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//                        {
//                            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getApplication().getResources().getDisplayMetrics());
//                            AppBarLayout.LayoutParams layoutParams=new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, actionBarHeight );
//                            hAppBarLayout.setLayoutParams(layoutParams);
//                        }
//                        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)hAppBarLayout.getLayoutParams();
//                        lp.height = (int)heightDp;

                        break;
                    case R.id.nav_que_music:
                        Toast.makeText(HomeActivity.this, "Clicked on the Play queue", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_playlist:
                        Toast.makeText(HomeActivity.this, "Clicked on the Playlist", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_song:
                        openAllSongs();
                        break;
                    case R.id.nav_genres:
                        Toast.makeText(HomeActivity.this, "Clicked on Genres", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_favorite:
                        openFavorite();
                        break;
                    case R.id.nav_account:
                        Toast.makeText(HomeActivity.this, "Clicked on Account", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_settings:
                        openSetting();
                        break;
                    case R.id.nav_info:
                        fragmentManager.beginTransaction()
                                .replace(R.id.container_fragment, info)
                                .commit();
                        hDrawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nav_share:
                        Toast.makeText(HomeActivity.this, "Under Building...", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }

    private void openHome(){

        final HomeFragment main = new HomeFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, main, "HomeFargment")
                .commit();
        hDrawer.closeDrawer(Gravity.LEFT);

        lockToolbar();
        hCToolbar.setTitleEnabled(false);
        hToolbar.setTitle("FragmentActivity Title");
    }

    private void openFavorite(){

        FavoriteFragment favorite = new FavoriteFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, favorite)
                .commit();
        hDrawer.closeDrawer(Gravity.LEFT);
    }

    private void  openSetting(){

        SettingsFragment setting = new SettingsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, setting)
                .commit();
        hDrawer.closeDrawer(Gravity.LEFT);
    }

    private void openAllSongs(){

        AllSongFragment allSong = new AllSongFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, allSong)
                .commit();
        hDrawer.closeDrawer(Gravity.LEFT);
    }

    private void lockToolbar(){

        AppBarLayout app = (AppBarLayout) findViewById(R.id.home_appbar);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) app.getLayoutParams();
        layoutParams.height = AppBarLayout.LayoutParams.WRAP_CONTENT;
        /*^This code will lock the toolbar for another */
    }



    private void toolbarView(){

        setSupportActionBar(hToolbar);
//        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) hCToolbar.getLayoutParams();
//        p.setScrollFlags(AppBarLayout.LayoutParams.WRAP_CONTENT);
//        hCToolbar.setLayoutParams(p);

//        TypedValue tv = new TypedValue();
//        int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getApplication().getResources().getDisplayMetrics());
//        AppBarLayout.LayoutParams layoutParams=new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, actionBarHeight );
//        hAppBarLayout.setLayoutParams(layoutParams);

        hToolbar.setNavigationIcon(R.drawable.navigation_icon);

        hToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hDrawer.openDrawer(Gravity.LEFT);
            }
        });


        int color = getResources().getColor(R.color.colorPrimaryLight);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.nulshockbd);
        hAppBarLayout.setExpanded(true);
        hCToolbar.setTitle("Tune");
        hCToolbar.setCollapsedTitleTextColor(Color.BLACK);
        hCToolbar.setStatusBarScrimColor(color);
        hCToolbar.setExpandedTitleColor(Color.WHITE);
        hCToolbar.setExpandedTitleTypeface(typeface);
        hCToolbar.setCollapsedTitleTypeface(typeface);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed(){

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}
