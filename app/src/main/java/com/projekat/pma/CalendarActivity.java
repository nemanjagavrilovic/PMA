package com.projekat.pma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.projekat.pma.MainActivity;

public class CalendarActivity extends AppCompatActivity {

    private String currentDate= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode

        setTheme(pref.getInt("theme",R.style.AppTheme_GREEN));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        final CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        Button cancelDateButton = (Button) findViewById(R.id.buttonCancel);
        cancelDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        calendarView.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                month+=1;
                currentDate = year+"-"+month+"-"+dayOfMonth;
            }//met
        });
        Button confirmButton = (Button) findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.putExtra("date",currentDate);
                startActivity(intent);
                finish();
            }
        });
    }

}
