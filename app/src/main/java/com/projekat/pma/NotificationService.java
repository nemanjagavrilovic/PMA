package com.projekat.pma;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.projekat.pma.model.Notification;
import android.provider.Settings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends JobService {

    private DatabaseHelper helper;


    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // doBackgroundWork(params);
        return false;
    }

    public void doBackgroundWork(final JobParameters params) {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.10:9001/pma/news/";
                if (Settings.Secure.getInt(getApplicationContext().getContentResolver()
                        ,Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF) != Settings.Secure.LOCATION_MODE_OFF) {

                    queue.add(checkForUpdates(url));
                }



            }
        }, 0, 5000);



    }

    public JsonArrayRequest checkForUpdates(String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            helper = new DatabaseHelper(getApplicationContext());
                            Random generator = new Random();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject news = response.getJSONObject(i);
                                Notification notification = helper.getNotificationByIdentificator(news.getLong("identificatior"));

                                if (notification.getIdentificator() == null) {

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    float longitude = pref.getFloat("longitude",1);
                                    System.out.println(longitude + "LONGITUDE U SERVISU ------------------");
                                    float latitude = pref.getFloat("latitude",1);


                                    JSONArray restriction = news.getJSONArray("restriction");
                                    JSONObject container = (JSONObject) restriction.get(0);
                                    double longitudeFrom = container.getDouble("lonFrom");
                                    double latitudeFrom = container.getDouble("latFrom");
                                    double longitudeTo=container.getDouble("lonTo");
                                    double latitudeTo = container.getDouble("latTo");

                                    int seekBarValue =  1000*pref.getInt("seekBarValue",0);

                                    double resultFrom = calculateDistance(longitude,latitude,longitudeFrom,latitudeFrom);
                                    double resultTo = calculateDistance(longitude,latitude,longitudeTo,latitudeTo);

                                    if(seekBarValue == 0) {
                                        notification.setTitle(news.getString("title"));
                                        notification.setText(news.getString("content"));
                                        notification.setIdentificator(news.getLong("identificatior"));

                                        helper.addData(notification);
                                        Intent intent = new Intent(getApplicationContext(), NewsInfo.class);
                                        intent.putExtra("title", notification.getTitle());
                                        intent.putExtra("text", notification.getText());
                                        intent.putExtra("image", news.getString("image"));

                                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                                generator.nextInt(), intent,
                                                PendingIntent.FLAG_CANCEL_CURRENT);

                                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(getApplicationContext())
                                                        .setContentIntent(contentIntent)
                                                        .setSmallIcon(R.drawable.common_full_open_on_phone)
                                                        .setContentTitle(news.getString("title"))
                                                        .setAutoCancel(true)
                                                        .setContentText(news.getString("content"));


                                        if (pref.getBoolean("vibration", false) == true) {
                                            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                                        }

                                        if (pref.getBoolean("sound", false) == true) {
                                            mBuilder.setSound(soundUri);
                                        }

                                        NotificationManager mNotificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(i, mBuilder.build());

                                    } else {
                                        if (resultFrom < seekBarValue && resultTo < seekBarValue) {

                                            notification.setTitle(news.getString("title"));
                                            notification.setText(news.getString("content"));
                                            notification.setIdentificator(news.getLong("identificatior"));

                                            helper.addData(notification);
                                            Intent intent = new Intent(getApplicationContext(), NewsInfo.class);
                                            intent.putExtra("title", notification.getTitle());
                                            intent.putExtra("text", notification.getText());
                                            intent.putExtra("image", news.getString("image"));

                                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                                    generator.nextInt(), intent,
                                                    PendingIntent.FLAG_CANCEL_CURRENT);

                                            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            NotificationCompat.Builder mBuilder =
                                                    new NotificationCompat.Builder(getApplicationContext())
                                                            .setContentIntent(contentIntent)
                                                            .setSmallIcon(R.drawable.common_full_open_on_phone)
                                                            .setContentTitle(news.getString("title"))
                                                            .setAutoCancel(true)
                                                            .setContentText(news.getString("content"));


                                            if (pref.getBoolean("vibration", false) == true) {
                                                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                                            }

                                            if (pref.getBoolean("sound", false) == true) {
                                                mBuilder.setSound(soundUri);
                                            }

                                            NotificationManager mNotificationManager =
                                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            mNotificationManager.notify(i, mBuilder.build());
                                        }
                                    }
                                }
                            }

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

        return request;
    }
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        Location locationA = new Location("point A");
        locationA.setLatitude(x1);
        locationA.setLongitude(y1);

        Location locationB = new Location("point B");
        locationB.setLatitude(x2);
        locationB.setLongitude(y2);

        double distance = locationA.distanceTo(locationB);
        System.out.println("DISTANCAAAA" + distance);
        return distance;
    }


}
