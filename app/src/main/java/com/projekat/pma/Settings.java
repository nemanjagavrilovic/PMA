package com.projekat.pma;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;

public class Settings extends AppCompatActivity {

    //news layout
    RadioGroup radioNewsLayout;
    RadioButton radioGrid;
    RadioButton radioList;

    //theme
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_BLUE_THEME = "blue_theme";

    //notification
    CheckBox checkBoxSound;
    CheckBox checkBoxVibration;

    private Context mContext;
    private NotificationManager mNotificationManager;
    private AudioManager mAudioManager;
    Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //use the choosen theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useBlueTheme = preferences.getBoolean(PREF_BLUE_THEME, false);

        if(useBlueTheme) {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //news layout
        radioNewsLayout=findViewById(R.id.radioNewsLayout);
        radioGrid=findViewById(R.id.radioGrid);
        radioList=findViewById(R.id.radioList);

        radioNewsLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(radioGrid.isChecked())
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "selected news layout: GRID", Toast.LENGTH_LONG);
                    toast.show();

                    //tek treba da implementiram...
                    /*private void setLayoutManager() {
                        if (mColumnCount <= 1) {
                            mColumnCount = 1;
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }
                    }*/
                }
                else if(radioList.isChecked())
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "selected news layout: LIST", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        //theme
        Switch toggle = (Switch) findViewById(R.id.toggleTheme);
        toggle.setChecked(useBlueTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        //notification
        mContext = getApplicationContext();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        checkBoxSound=(CheckBox) findViewById(R.id.checkBoxSound);
        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "checked: SOUND", Toast.LENGTH_LONG);
                    toast.show();

                    // If do not disturb mode on, then off it first
                    turnOffDoNotDisturbMode();

                    // Get the ringer current volume level
                    int current_volume_level = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);

                    // Get the ringer maximum volume
                    int max_volume_level = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

                    // Get a random volume level in specified range
                    int random_volume = mRandom.nextInt(((max_volume_level-0)+1)+0);

                    // Set the ringer volume
                    mAudioManager.setStreamVolume(AudioManager.STREAM_RING, random_volume, AudioManager.FLAG_SHOW_UI);
                }
            }
        });

        checkBoxVibration=(CheckBox) findViewById(R.id.checkBoxVibration);
        checkBoxVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                }
            }
        });
    }

    private void toggleTheme(boolean blueTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_BLUE_THEME, blueTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    protected void turnOffDoNotDisturbMode(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23
            // If notification policy access granted for this package
            if(mNotificationManager.isNotificationPolicyAccessGranted()){{
                if(mAudioManager.getStreamVolume(AudioManager.STREAM_RING)== 0){
                    // If do not disturb mode on, then off it
                    // Set the interruption filter to all, allow all notification
                    mNotificationManager.setInterruptionFilter(mNotificationManager.INTERRUPTION_FILTER_NONE);
                    // Show a toast
                    Toast.makeText(mContext,"Turn OFF Do Not Disturb Mode",Toast.LENGTH_SHORT).show();
                }
            }
            }else {
                // Show a toast
                Toast.makeText(mContext,"Going to get grant access",Toast.LENGTH_SHORT).show();

                // If notification policy access not granted for this package
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

}

//settings:
//news layout (listView, gridView)
//tema (ne menja na nivou cele aplikacije)

//ostalo:
//TODO: promena orijentacije ekrana -> promena view-a
//ikonicu aplikacije mozemo promeniti opet :D
