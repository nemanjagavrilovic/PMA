package com.projekat.pma;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class NewsInfo extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dl = (DrawerLayout)findViewById(R.id.news_info);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;

                switch(id)
                {
                    case R.id.home:
                        intent = new Intent(NewsInfo.this, MainActivity.class);
                        startActivity(intent);
//                        NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
//                        FragmentManager manager = getSupportFragmentManager();
//                        manager.beginTransaction()
//                                .replace(R.id.main,newsInfoFragment)
//                                .commit();
                        return true;
                    case R.id.settings:
                        intent = new Intent(NewsInfo.this, Settings.class);
                        startActivity(intent);
                        return true;
                    case R.id.contact:
                        intent = new Intent(NewsInfo.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


}
