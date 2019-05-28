package com.projekat.pma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.projekat.pma.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private int newsType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.home_fragment, container, false);
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "";
        int newsType = getActivity().getIntent().getIntExtra("newsType",3);

        if(newsType == 3 ) {
             url = "http://192.168.1.8:9001/pma/news/";
        } else  {
            url = "http://192.168.1.8:9001/pma/news/restrictionType/"+newsType;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            ArrayList<News> newsList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject news = response.getJSONObject(i);
                                News newsItem = new News(news.getString("title"),news.getString("content"));
                                newsItem.setImage(news.getString("image"));
                                newsItem.setId(news.getLong("id"));

                                newsList.add(newsItem);
                            }
                            RecyclerView carRecyclerView = (RecyclerView)view.findViewById(R.id.card_view_recycler_list);
                            // Create the grid layout manager with 2 columns.
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
                            // Set layout manager.
                            carRecyclerView.setLayoutManager(gridLayoutManager);

                            // Create car recycler view data adapter with car item list.
                            CardViewAdapter carDataAdapter = new CardViewAdapter(newsList, getContext());
                            // Set data adapter.
                            carRecyclerView.setAdapter(carDataAdapter);

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

        return  view;
    }

}
