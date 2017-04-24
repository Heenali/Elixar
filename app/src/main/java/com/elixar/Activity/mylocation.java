package com.elixar.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Adapter.Doctortype_Adapter;
import com.elixar.Helper.Constants;
import com.elixar.Method.Doctortype_method;
import com.elixar.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class mylocation extends AppCompatActivity
{
    LinearLayout container;
    LinearLayout container_nearplace;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Button  searchView_text_text;
    public static final String TAG = "SampleActivityBase";
    RadioButton r1;
    LinearLayout delete;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_location);
        searchView_text_text= (Button) findViewById(R.id.searchView_text_text);
        delete= (LinearLayout) findViewById(R.id.delete);

        back=(ImageView)findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        container = (LinearLayout) findViewById(R.id.container);
        container_nearplace = (LinearLayout) findViewById(R.id.container_nearlocation);

        String lats="",longs="";
        SharedPreferences prefs1l;
        String prefName1l = "savelocation";
        prefs1l = getSharedPreferences(prefName1l, MODE_PRIVATE);

        lats= prefs1l.getString("lat", lats);
        longs= prefs1l.getString("long", longs);

         SyncMethod("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lats+","+longs+"&radius=10000&types=location&key=AIzaSyB3Cb1Jp7yQv6c_e5xpZAHx23yjGrrOHYs");

        String store="";
        SharedPreferences prefs1;
        String prefName1 = "location_store";
        prefs1 = getSharedPreferences(prefName1, MODE_PRIVATE);
        store= prefs1.getString("Address", store);
        if(store.contains(",,"))
        {
            final ArrayList items= new ArrayList(Arrays.asList(store.split(",,")));
           // Toast.makeText(getApplicationContext(),items.get(0).toString(),Toast.LENGTH_LONG).show();
            for(int i=0;i<items.size();i++)
            {
                LayoutInflater layoutInflater =(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.list_recentlocation, null);
                TextView textOut = (TextView) addView.findViewById(R.id.location_name);
                LinearLayout whole_layout=(LinearLayout)addView.findViewById(R.id.whole_layout);
                RadioButton r1= (RadioButton) addView.findViewById(R.id.r1);
                final String istore=items.get(i).toString();

                textOut.setText(items.get(i).toString());

                //////////////////
                String store_save="";
                SharedPreferences prefs1_save;
                String prefName1_save = "savelocation";
                prefs1_save = getSharedPreferences(prefName1_save, MODE_PRIVATE);
                Constants.savelocation= prefs1_save.getString("savelocation",Constants.savelocation);
                //////////////////////

                if(Constants.savelocation.equalsIgnoreCase(items.get(i).toString()))
                {
                    r1.setChecked(true);
                    searchView_text_text.setText(Constants.savelocation.toString());
                }
                else
                {
                    r1.setVisibility(View.GONE);
                    r1.setChecked(false);
                }

                whole_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        Constants.savelocation=istore;
                        SharedPreferences prefs;
                        String prefName = "savelocation";
                        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("savelocation", Constants.savelocation);
                        editor.commit();
                        container.removeAllViews();
                       // Toast.makeText(getApplication(),Constants.savelocation,Toast.LENGTH_SHORT).show();
                        listcalling();

                    }
                });
                container.addView(addView);

            }

        }
        else
        {
            LayoutInflater layoutInflater =(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.list_recentlocation, null);
            TextView textOut = (TextView) addView.findViewById(R.id.location_name);
            RadioButton r1= (RadioButton) addView.findViewById(R.id.r1);
            textOut.setText(store);
            r1.setChecked(true);
            container.addView(addView);

        }

        searchView_text_text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openAutocompleteActivity();
            }
        });
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                String store="";
                SharedPreferences prefs1;
                String prefName1 = "location_store";
                prefs1 = getSharedPreferences(prefName1, MODE_PRIVATE);
                store= prefs1.getString("Address", store);
                if(store.contains(",,")) {
                    final ArrayList items = new ArrayList(Arrays.asList(store.split(",,")));
                    // Toast.makeText(getApplicationContext(),items.get(0).toString(),Toast.LENGTH_LONG).show();
                    Constants.savelocation = items.get(0).toString();

                }
                      //  Toast.makeText(getApplicationContext(),Constants.savelocation,Toast.LENGTH_LONG).show();
                        SharedPreferences prefs;
                        String prefName = "savelocation";
                        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("savelocation", Constants.savelocation);
                        editor.commit();


               Toast.makeText(getApplicationContext(),"Delete current location",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void openAutocompleteActivity()
    {
        try
        {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK)
            {
                container.removeAllViews();
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
              /*  mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*/
               // searchView_text_text.setText(place.getAddress()+"");
                String store1="";
                SharedPreferences prefs1;
                String prefName1 = "location_store";
                prefs1 = getSharedPreferences(prefName1, MODE_PRIVATE);
                store1= prefs1.getString("Address", store1);


                 String ans= store1+",,"+place.getAddress();

                SharedPreferences prefs;
                String prefName = "location_store";
                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Address", ans);
                editor.commit();

               // Toast.makeText(getApplicationContext(), place.getAddress()+"",Toast.LENGTH_LONG).show();

                final ArrayList items= new ArrayList(Arrays.asList(ans.split(",,")));
                for(int i=0;i<items.size();i++)
                {
                    LayoutInflater layoutInflater =(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.list_recentlocation, null);
                    TextView textOut = (TextView) addView.findViewById(R.id.location_name);
                    RadioButton r1= (RadioButton) addView.findViewById(R.id.r1);
                    LinearLayout whole_layout=(LinearLayout)addView.findViewById(R.id.whole_layout);
                    textOut.setText(items.get(i).toString());
                    final String istore=items.get(i).toString();

                    //////////////////
                    String store_save="";
                    SharedPreferences prefs1_save;
                    String prefName1_save = "savelocation";
                    prefs1_save = getSharedPreferences(prefName1_save, MODE_PRIVATE);
                    Constants.savelocation= prefs1_save.getString("savelocation",Constants.savelocation);
                    //////////////////////

                    if(Constants.savelocation.equalsIgnoreCase(istore))
                    {
                        r1.setChecked(true);
                        searchView_text_text.setText(Constants.savelocation.toString());
                    }
                    else
                    {
                        r1.setVisibility(View.GONE);
                        r1.setChecked(false);
                    }

                    whole_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {

                            Constants.savelocation=istore;
                            SharedPreferences prefs;
                            String prefName = "savelocation";
                            prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("savelocation", Constants.savelocation);
                            editor.commit();
                            container.removeAllViews();
                           // Toast.makeText(getApplication(),Constants.savelocation,Toast.LENGTH_SHORT).show();
                            listcalling();

                        }
                    });




                    container.addView(addView);

                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }
    public void listcalling()
    {
        String store="";
        SharedPreferences prefs1;
        String prefName1 = "location_store";
        prefs1 = getSharedPreferences(prefName1, MODE_PRIVATE);
        store= prefs1.getString("Address", store);
        if(store.contains(",,"))
        {
            final ArrayList items = new ArrayList(Arrays.asList(store.split(",,")));
            //  Toast.makeText(getApplicationContext(), items.get(0).toString(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < items.size(); i++) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.list_recentlocation, null);
                TextView textOut = (TextView) addView.findViewById(R.id.location_name);

                RadioButton r1 = (RadioButton) addView.findViewById(R.id.r1);

                LinearLayout whole_layout=(LinearLayout)addView.findViewById(R.id.whole_layout);
                textOut.setText(items.get(i).toString());
                final String istore=items.get(i).toString();

                //////////////////
                String store_save="";
                SharedPreferences prefs1_save;
                String prefName1_save = "savelocation";
                prefs1_save = getSharedPreferences(prefName1_save, MODE_PRIVATE);
                Constants.savelocation= prefs1_save.getString("savelocation",Constants.savelocation);
                //////////////////////

                if (Constants.savelocation.equalsIgnoreCase(items.get(i).toString()))
                {
                    r1.setChecked(true);
                    searchView_text_text.setText(Constants.savelocation.toString());
                }
                else
                {
                    r1.setChecked(false);
                    r1.setVisibility(View.GONE);
                }
                whole_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {


                        Constants.savelocation=istore;
                        SharedPreferences prefs;
                        String prefName = "savelocation";
                        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("savelocation", Constants.savelocation);
                        editor.commit();
                        container.removeAllViews();
                        // Toast.makeText(getApplication(),Constants.savelocation,Toast.LENGTH_SHORT).show();
                        listcalling();

                    }
                });
                container.addView(addView);
            }


        }
    }
    public void SyncMethod(final String GetUrl)
    {

        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
                    // document all_stuff = null;

                    SetServerString = fetchResult(url);
                    threadMsg(SetServerString);
                } catch (Throwable t) {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler() {
                public void handleMessage(Message msg) {
                    try
                    {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);

                        JSONArray array=new JSONArray();
                        JSONObject get_res=new JSONObject(aResponse);
                        array=get_res.getJSONArray("results");
                        for(int aa=0;aa<array.length();aa++)
                        {
                            LayoutInflater layoutInflater =
                                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View addView = layoutInflater.inflate(R.layout.list_nearlocation, null);
                            final TextView textOut = (TextView)addView.findViewById(R.id.place);
                            final LinearLayout playout = (LinearLayout) addView.findViewById(R.id.place_layout);
                            textOut.setText(array.getJSONObject(aa).getString("name"));
                            playout.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v)
                                {
                                    String store1="";
                                    SharedPreferences prefs1;
                                    String prefName1 = "location_store";
                                    prefs1 = getSharedPreferences(prefName1, MODE_PRIVATE);
                                    store1= prefs1.getString("Address", store1);


                                    String ans= store1+",,"+textOut.getText().toString();

                                    SharedPreferences prefs;
                                    String prefName = "location_store";
                                    prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("Address", ans);
                                    editor.commit();


                                    Constants.savelocation=textOut.getText().toString();
                                    SharedPreferences prefs11;
                                    String prefName11 = "savelocation";
                                    prefs11 = getSharedPreferences(prefName11, MODE_PRIVATE);
                                    SharedPreferences.Editor editor11 = prefs11.edit();
                                    editor11.putString("savelocation", Constants.savelocation);
                                    editor11.commit();
                                    Toast.makeText(getApplicationContext(),textOut.getText().toString(),Toast.LENGTH_LONG).show();
                                    finish();

                                }});
                            container_nearplace.addView(addView, 0);
                        }


                        } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public String fetchResult(String urlString) throws JSONException {
        StringBuilder builder;
        BufferedReader reader;
        URLConnection connection = null;
        URL url = null;
        String line;
        builder = new StringBuilder();
        reader = null;
        try {
            url = new URL(urlString);
            connection = url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //Log.d("DATA", builder.toString());
        } catch (Exception e) {

        }
        //JSONArray arr=new JSONArray(builder.toString());
        return builder.toString();
    }

}
