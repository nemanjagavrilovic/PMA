package com.projekat.pma;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CancelCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode

        setTheme(pref.getInt("theme",R.style.AppTheme_GREEN));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
    }

}
