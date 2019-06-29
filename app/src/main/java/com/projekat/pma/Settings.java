package com.projekat.pma;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class Settings extends AppCompatActivity {

    private LocationManager manager;
    private SwitchCompat switchLocation;
    static boolean isTouched = false;
    private SeekBar simpleSeekBar;
    private RadioGroup radioNewsLayout;
    private RadioButton radioGrid;
    private RadioButton radioList;
    private RadioGroup radioThemeLayout;
    private RadioButton radioDark;
    private RadioButton radioLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode

        setTheme(pref.getInt("theme",R.style.AppTheme_GREEN));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final SharedPreferences.Editor editor = pref.edit();

        radioNewsLayout=findViewById(R.id.radioLayout);
        radioGrid=findViewById(R.id.radioGrid);
        radioList=findViewById(R.id.radioList);

        if(pref.getString("layout","").equals("list")) {
            radioList.setChecked(true);
        } else {
            radioGrid.setChecked(true);
        }

        radioNewsLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(radioGrid.isChecked())
                {
                    editor.putString("layout","grid");

                }
                else if(radioList.isChecked())
                {

                    editor.putString("layout","list");
                }
                editor.commit();
            }
        });
        switchLocation = (SwitchCompat) findViewById(R.id.switchLocation);
        CheckBox soundCheckBox = (CheckBox) findViewById(R.id.sound);
        CheckBox vibrationCheckBox = (CheckBox) findViewById(R.id.vibration);
        simpleSeekBar = (SeekBar) findViewById(R.id.simpleSeekBar);

        radioDark = (RadioButton) findViewById(R.id.radioDark);
        radioLight = (RadioButton) findViewById(R.id.radioLight);

        if(pref.getInt("theme",0) == R.style.AppTheme_RED) {
            radioDark.setChecked(true);
        }


        radioDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                if(isChecked) {
                    editor.putInt("theme",R.style.AppTheme_RED);
                    editor.commit();
                }
            }
        });

        if(pref.getInt("theme",0) == R.style.AppTheme_GREEN) {
            radioLight.setChecked(true);
        }
        radioLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                if(isChecked) {
                    editor.putInt("theme",R.style.AppTheme_GREEN);
                    editor.commit();

                }
            }
        });
       soundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                if(isChecked) {
                    editor.putBoolean("sound", true);
                    editor.commit();
                    System.out.println(pref.getBoolean("sound",false));
                } else {
                    editor.putBoolean("sound", false);
                    editor.commit();
                }
            }
        });

        vibrationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                if(isChecked) {
                    editor.putBoolean("vibration", true);
                    editor.commit();
                } else {
                    editor.putBoolean("vibration", false);
                    editor.commit();
                }
            }
        });
        switchLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched=false;
                    buildAlertMessageNoGps();
                }
            }
        });

        simpleSeekBar.setProgress(pref.getInt("seekBarValue",50));
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.simpleSeekBar);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                simpleSeekBar.setProgress(progress);
                int seekBarValue= simpleSeekBar.getProgress();
                editor.putInt("seekBarValue",seekBarValue);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "";
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                message="Your GPS seems to be disabled, do you want to enable it?";
            }else{
                message="Your GPS seems to be enabled,but necessary for receiving notifications, do you want to disable it?";
            }
                builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();

        if (android.provider.Settings.Secure.getInt(getApplicationContext().getContentResolver()
                ,android.provider.Settings.Secure.LOCATION_MODE, android.provider.Settings.Secure.LOCATION_MODE_OFF) != android.provider.Settings.Secure.LOCATION_MODE_OFF) {
            switchLocation.setChecked(true);
            simpleSeekBar.setEnabled(true);

        }else{
            switchLocation.setChecked(false);
            simpleSeekBar.setEnabled(false);
        }

    }



}
