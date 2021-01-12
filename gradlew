package com.example.app20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.app20.fragments.MessageFragment;
import com.example.app20.fragments.SettingFragment;
import com.google.android.material.navigation.NavigationView;

import static com.example.app20.fragments.SettingFragment.MY_PREFERENCES;
import static com.example.app20.fragments.SettingFragment.NIGHT_MODE;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // widget
    private DrawerLayout drawer;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    // variables
    private static final int REQUESTCODE = 100;

    SharedPreferences sharedPreferences;

    private ActionMode mActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);

        //setting themes
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt(NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        setContentView(R.layout.activity_home);

        //find views
        mToolbar = findViewById(R.id.mytoolbar);
        drawer = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);


        // setting supports actionbar
        setSupportActionBar(mToolbar);


        // setting up drawers
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_open);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        // Listeners
        navigationView.setNavigationItemSelectedListener(this);


        // TP
        TextView textView = findViewById(R.id.textview);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode!= null){
                    return false;
                }
                mActionMode = startSupportActionMode(mActionModeCallback);
                return true;

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()==R.id.menu_settings){
//            Intent intent = new Intent(this, Settings.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppCompatDelegate.getDefaultNightMode()!=sharedPreferences.getInt(NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))
                AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt(NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
    }

    //actionmode callback
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu,menu);
            mode.setTitle("choose your options");
            mode.setSubtitle("this is our subtitle");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.share_menu:
                    Toast.makeText(home.this, "share", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.delete_menu:
                    Toast.makeText(home.this, "delete", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;

            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @Override
    public void onBackPressed() {

        //if drawer open closing it first
        if (drawer.isDrawerOpen(Gravi