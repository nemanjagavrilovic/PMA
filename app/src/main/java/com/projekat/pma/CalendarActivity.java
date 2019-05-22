package com.projekat.pma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.projekat.pma.MainActivity;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Button cancelDateButton = (Button) findViewById(R.id.buttonCancel);
        cancelDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}
