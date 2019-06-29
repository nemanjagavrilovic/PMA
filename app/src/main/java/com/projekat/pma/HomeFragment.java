package com.projekat.pma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.projekat.pma.model.Restriction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {

    public void update(final SwipeRefreshLayout view) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "";
        int newsType = getActivity().getIntent().getIntExtra("newsType",3);
        final String date = getActivity().getIntent().getStringExtra("date");

        if(newsType == 3 ) {
            url = "http://192.168.43.30:9001/pma/news/";
        } else  {
            url = "http://192.168.43.30:9001/pma/news/restrictionType/"+newsType;
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
                                newsItem.setRestriction(getRestrictions(news.getJSONArray("restriction"),date));
                                if(newsItem.getRestriction().size() != 0 ) {
                                    newsList.add(newsItem);
                                }
                            }
                            RecyclerView carRecyclerView = (RecyclerView)view.findViewById(R.id.card_view_recycler_list);
                            // Create the grid layout manager with 2 columns.
                            int layout = 0;
                            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                            System.out.println("layout" + pref.getString("layout","NULL"));
                            if(pref.getString("layout","list").equals("grid")) {
                                layout = 2;
                            } else {
                                layout = 1;
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), layout);
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final SwipeRefreshLayout view = (SwipeRefreshLayout) inflater.inflate(R.layout.home_fragment, container, false);
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                view.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setRefreshing(false);
                        update(view);
                    }
                },3000);
            }
        });

        update(view);
        return  view;
    }


    public List<Restriction> getRestrictions(JSONArray array, String  date) {
        ArrayList<Restriction> restrictionsList = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject restriction = array.getJSONObject(i);
                Restriction restrictionItem = new Restriction();
                restrictionItem.setId(restriction.getLong("id"));
                restrictionItem.setLatFrom(restriction.getDouble("latFrom"));
                restrictionItem.setLonFrom(restriction.getDouble("lonFrom"));
                restrictionItem.setLatTo(restriction.getDouble("latTo"));
                restrictionItem.setLonTo(restriction.getDouble("lonTo"));
                restrictionItem.setFromDate(convertDate(restriction.getString("from")));
                restrictionItem.setToDate(convertDate(restriction.getString("to")));

                if(date != null && !date.isEmpty()) {
                    Date selectedDate = convertDate(date);

                    if (selectedDate.after(restrictionItem.getFromDate()) && selectedDate.before(restrictionItem.getToDate())) {

                        restrictionsList.add(restrictionItem);
                    }
                } else {
                    restrictionsList.add(restrictionItem);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return restrictionsList;
    }

    private Date convertDate(String stringDate) {

        if(stringDate==null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onResume(){
        super.onResume();

    }
}
