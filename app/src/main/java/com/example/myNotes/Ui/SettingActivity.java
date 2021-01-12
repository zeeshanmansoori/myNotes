package com.example.myNotes.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myNotes.R;

public class SettingActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener {

    //widget
    RadioGroup radioGroup;
    RadioButton on,off,auto;
    public static final String PREFERENCES_NIGHTMODE = "nightModepref";
    public static final String NIGHT_MODE = "isNightMode";
    SharedPreferences sharedPreferences;
    private int Mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences(PREFERENCES_NIGHTMODE, Context.MODE_PRIVATE);
        Mode = sharedPreferences.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        // find view by ids
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);
        auto = findViewById(R.id.auto);
        radioGroup = findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);

        // setting listeners

        switch (Mode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                off.setChecked(true);
                break;

            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                auto.setChecked(true);
                break;

            case AppCompatDelegate.MODE_NIGHT_YES:
                on.setChecked(true);
                break;

            default:
                Log.d("default", "going to default");
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.auto:
                Mode = (AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                Toast.makeText(this, "aoto + press back to apply changes", Toast.LENGTH_SHORT).show();
                break;

            case R.id.off:
                Mode = (AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(this, "off + press back to apply changes", Toast.LENGTH_SHORT).show();
                break;

            case R.id.on:
                Mode = (AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(this, "on + press back to apply changes", Toast.LENGTH_SHORT).show();
                break;
        }

    }
    private void saveSettings(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NIGHT_MODE,Mode);
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
    }
}