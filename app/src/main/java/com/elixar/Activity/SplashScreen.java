package com.elixar.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.elixar.Fragment.MainTab1;
import com.elixar.Helper.Constants;
import com.elixar.Helper.SessionManager;
import com.elixar.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SplashScreen extends Activity {

    SessionManager sm;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        GPSTracker gps;
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            SharedPreferences prefs1;
            String prefName1 = "location_store";
            prefs1 = getSharedPreferences(prefName1, MODE_PRIVATE);
            String store = prefs1.getString("Address", "");

            if (store.equalsIgnoreCase("")) {


                gps = new GPSTracker(SplashScreen.this);
                Geocoder geocoder = new Geocoder(SplashScreen.this, Locale.getDefault());
                List<Address> addresses;
                String cityName = "";
                try {

                    addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        cityName = addresses.get(0).getAddressLine(0);
                        String stateName = addresses.get(0).getAddressLine(1);
                        String countryName = addresses.get(0).getAddressLine(2);
                        cityName = cityName + " , " + stateName;
                        Constants.savelocation = cityName;
                        SharedPreferences prefs;
                        String prefName = "savelocation";
                        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("savelocation", cityName);
                        editor.putString("lat", gps.getLatitude() + "");
                        editor.putString("long", gps.getLongitude() + "");
                        editor.commit();

                    } else {
                        cityName = "";
                    }


                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                // Toast.makeText(getApplicationContext(),cityName,Toast.LENGTH_LONG).show();
                SharedPreferences prefs;
                String prefName = "location_store";
                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Address", cityName);
                editor.commit();
                sm = new SessionManager(SplashScreen.this);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        if (sm.isLoggedIn()) {
                            Intent i = new Intent(SplashScreen.this, MainTab1.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashScreen.this, LoginActvity.class);
                            startActivity(i);
                        }
                        // close this activity
                        finish();
                    }
                }, SPLASH_TIME_OUT);


            } else {
                sm = new SessionManager(SplashScreen.this);

                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        if (sm.isLoggedIn()) {
                            Intent i = new Intent(SplashScreen.this, MainTab1.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(SplashScreen.this, LoginActvity.class);
                            startActivity(i);
                            finish();
                        }
                        // close this activity

                    }
                }, SPLASH_TIME_OUT);
            }

        }
    }
}

