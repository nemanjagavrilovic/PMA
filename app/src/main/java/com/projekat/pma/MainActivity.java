package com.projekat.pma;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DatabaseHelper databaseHelper;
    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        System.out.println("Lokacijaa "+locationManager.isProviderEnabled(LOCATION_SERVICE));
        databaseHelper = new DatabaseHelper(this);
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                switch (id) {
                    case R.id.home:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("newsType", 3);
                        startActivity(intent);
                        return true;
                    case R.id.settings:
                        intent = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent);
                        return true;
                    case R.id.contact:
                        intent = new Intent(MainActivity.this, Contact.class);
                        startActivity(intent);
                        return true;
                    case R.id.eletricity_news:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("newsType", 2);
                        startActivity(intent);
                        return true;
                    case R.id.road_news:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("newsType", 0);
                        startActivity(intent);
                        return true;
                    case R.id.water_news:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("newsType", 1);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new MapFragment(), "Map");
        adapter.addFragment(new NotificationFragment(), "Notification");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int[] tabIcons = {
                R.drawable.ic_home_black_24dp,
                R.drawable.ic_place_black_24dp,
                R.drawable.ic_notifications_black_24dp
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected) {
                viewPager.setCurrentItem(tabSelected.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected) {

            }
        });
        scheduleJob();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putFloat("longitude",(float) location.getLongitude());
                                editor.putFloat("latitude",(float) location.getLatitude());
                                editor.commit();
                                System.out.println("---------------INITIAL LONGITUDE----------- "+pref.getFloat("longitude",1));
                                System.out.println("---------------INITIAL LATITUDE----------- "+pref.getFloat("latitude",1));

                            }
                        }
                    });
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putFloat("longitude",(float) location.getLongitude());
                    editor.putFloat("latitude",(float) location.getLatitude());
                    editor.commit();
                    System.out.println("---------------UPDATED LONGITUDE----------- "+pref.getFloat("longitude",1));
                    System.out.println("---------------UPDATED LATITUDE----------- "+pref.getFloat("latitude",1));

                }
            }
        };

    }



    public void scheduleJob() {
        ComponentName componentName=new ComponentName(this, NotificationService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)

                .setMinimumLatency(1000*10)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }



    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback,
                    null /* Looper */);
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        return true;
//    }

}