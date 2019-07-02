package com.projekat.pma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsInfo extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode

        setTheme(pref.getInt("theme",R.style.AppTheme_GREEN));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        TextView text = findViewById(R.id.news_info_text);
        TextView title = findViewById(R.id.news_info_title);
        ImageView image = findViewById(R.id.news_info_image);
        Long identificator = getIntent().getLongExtra("identificator",0);
        if(identificator != 0) {

            String url = "http://192.168.43.30:9001/pma/news/findByIdentificator/"+identificator;

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ImageView image = findViewById(R.id.news_info_image);
                                byte[] encodeByte = Base64.decode(response.getString("image"), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                image.setImageBitmap(bitmap);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            queue.add(request);
        } else {
            byte[] encodeByte = Base64.decode(getIntent().getStringExtra("image"), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            image.setImageBitmap(bitmap);
        }
        text.setText(getIntent().getStringExtra("text"));
        title.setText(getIntent().getStringExtra("title"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dl = (DrawerLayout)findViewById(R.id.news_info);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.news_info_nav);
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
                        return true;
                    case R.id.settings:
                        intent = new Intent(NewsInfo.this, Settings.class);
                        startActivity(intent);
                        return true;
                    case R.id.contact:
                        intent = new Intent(NewsInfo.this, Contact.class);
                        startActivity(intent);
                        return true;
                    case R.id.eletricity_news:
                        intent = new Intent(NewsInfo.this, MainActivity.class);
                        intent.putExtra("newsType", 2);
                        startActivity(intent);
                        finish();

                        return true;
                    case R.id.road_news:
                        intent = new Intent(NewsInfo.this, MainActivity.class);
                        intent.putExtra("newsType", 0);
                        startActivity(intent);
                        finish();

                        return true;
                    case R.id.water_news:
                        intent = new Intent(NewsInfo.this, MainActivity.class);
                        intent.putExtra("newsType", 1);
                        startActivity(intent);
                        finish();
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
