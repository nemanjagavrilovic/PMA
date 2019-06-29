package com.projekat.pma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.util.HashMap;
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

                        double centerLatitude = calculate(restriction.getLatFrom(),restriction.getLatTo());
                        double centerLongitude = calculate (restriction.getLonFrom(),restriction.getLonTo());
                        LatLng center = new LatLng(centerLatitude,centerLongitude);
                        googleMap.addCircle(new CircleOptions()
                                .center(center)
                                .radius((calculateDistance(restriction.getLatFrom(),restriction.getLonFrom(),restriction.getLatTo(),restriction.getLonTo()))/2)
                                .fillColor(0x44ff0000)
                                .strokeColor(0xffff0000)
                                .strokeWidth(5));
                        googleMap.addMarker(new MarkerOptions().position(center).title(news.getTitle()).snippet(news.getText()));

                         //For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(14).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        /*RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        String url ="https://maps.googleapis.com/maps/api/directions/json?origin=45.248996,19.849229&destination=45.258996,19.839229&key=AIzaSyA1A_yHNBiCoP-8U73yc-S6FPErRFG0A4Y";
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        List<List<HashMap<String, String>>> routes = new ArrayList<>();
                                        routes = parser.parse(response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });*/

                        //queue.add(request);
                    }
                }

            }
        });
    }

    private double calculate(double latFrom, double latTo) {
        return (latFrom+latTo)/2;
    }

    public double calculateDistance(double x1, double y1, double x2, double y2) {
        Location locationA = new Location("point A");
        locationA.setLatitude(x1);
        locationA.setLongitude(y1);

        Location locationB = new Location("point B");
        locationB.setLatitude(x2);
        locationB.setLongitude(y2);

        double distance = locationA.distanceTo(locationB);
        return distance/2;
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "";
        int newsType = getActivity().getIntent().getIntExtra("newsType",3);
        final String date = getActivity().getIntent().getStringExtra("date");

        if(newsType == 3) {
            url = "http://192.168.43.30:9001/pma/news/";
        } else {
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
}
