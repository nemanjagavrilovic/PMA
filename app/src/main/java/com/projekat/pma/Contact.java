package com.projekat.pma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Contact extends AppCompatActivity {

    private Spinner mailTo;
    private EditText mailSubject;
    private EditText mailMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode

        setTheme(pref.getInt("theme",R.style.AppTheme_GREEN));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mailTo = findViewById(R.id.spinner1);
        mailSubject = findViewById(R.id.mailSubject);
        mailMessage = findViewById(R.id.mailMessage);

        Button buttonSendMail = findViewById(R.id.send_mail);

        buttonSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        Button buttonWaterCall = findViewById(R.id.call_water);
        Button buttonEletricityCall  = findViewById(R.id.call_electricity);

        buttonWaterCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+381655236602"));
                startActivity(intent);
            }
        });

        buttonEletricityCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+381655236602"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void sendMail() {

        String []  mailToString  =  new String[1];
        mailToString[0] = mailTo.getSelectedItem().toString();
        String mailSubjectString = mailSubject.getText().toString();
        String mailContent = mailMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,mailToString);
        intent.putExtra(Intent.EXTRA_SUBJECT,mailSubjectString);
        intent.putExtra(Intent.EXTRA_TEXT,mailContent);


        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));

    }

}
