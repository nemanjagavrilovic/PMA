package com.projekat.pma;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.projekat.pma.model.News;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.home:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
//                        NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
//                        FragmentManager manager = getSupportFragmentManager();
//                        manager.beginTransaction()
//                                .replace(R.id.main,newsInfoFragment)
//                                .commit();
                        return true;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.contact:
                        Toast.makeText(MainActivity.this, "Contact",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return true;
                }
            }
        });

        News news1 = new News("Title 1","Text 1");
        News news2 = new News("Title 2","Text 2");
        News news3 = new News("Title 3","Text 3");
        News news4 = new News("Title 4","Text 4");
        News news5 = new News("Title 5","Text 5");

        ArrayList<News> news = new ArrayList<>();
        news.add(news1);
        news.add(news2);
        news.add(news3);
        news.add(news4);
        news.add(news5);

        NewsListAdapter adapter = new NewsListAdapter(this,R.layout.adapter_view_layout,news);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewsInfo.class);
                startActivity(intent);
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