package com.projekat.pma;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projekat.pma.model.News;
import com.projekat.pma.model.Restriction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapFragment extends Fragment {

    private View inflatedView;
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       this.inflatedView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = (MapView) this.inflatedView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getData();
        Button selectedDateButton = (Button) inflatedView.findViewById(R.id.button);
        selectedDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        return this.inflatedView;
    }
    public void setNewsLocations(final List<News> newsList) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                for (News news:newsList) {
                    for(Restriction restriction : news.getRestriction()) {
                        LatLng location = new LatLng(restriction.getLat(), restriction.getLon());
                        googleMap.addMarker(new MarkerOptions().position(location).title(news.getTitle()).snippet(news.getText()));

                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }

            }
        });
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "";
        int newsType = getActivity().getIntent().getIntExtra("newsType",3);
        final String date = getActivity().getIntent().getStringExtra("date");

        if(newsType == 3) {
            url = "http://192.168.1.8:9001/pma/news/";
        } else {
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
                                newsItem.setRestriction(getRestrictions(news.getJSONArray("restriction"),date));
                                if(newsItem.getRestriction().size() != 0 ) {
                                    newsList.add(newsItem);
                                }
                            }
                            setNewsLocations(newsList);
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

    public List<Restriction> getRestrictions(JSONArray array, String  date) {
        ArrayList<Restriction> restrictionsList = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject restriction = array.getJSONObject(i);
                Restriction restrictionItem = new Restriction();
                restrictionItem.setId(restriction.getLong("id"));
                restrictionItem.setLat(restriction.getDouble("lat"));
                restrictionItem.setLon(restriction.getDouble("lon"));
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
}
