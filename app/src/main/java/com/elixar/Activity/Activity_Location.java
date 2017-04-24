package com.elixar.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elixar.Helper.MapWrapperLayout;
import com.elixar.Helper.OnInfoWindowElemTouchListener;
import com.elixar.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_Location extends Activity {
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private TextView mno;
    private ImageView image,filterbtn;
    GoogleMap map;
    List<NameValuePair> params11;
    private OnInfoWindowElemTouchListener infoButtonListener;
    ProgressDialog loading;
    FloatingActionButton float_map;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        map = mapFragment.getMap();

        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));

        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.info_window_layout, null);
        this.infoTitle = (TextView) infoWindow.findViewById(R.id.title1);
        this.infoSnippet = (TextView) infoWindow.findViewById(R.id.title2);
        // this.infoButton = (Button)infoWindow.findViewById(R.id.button);
        this.mno = (TextView) infoWindow.findViewById(R.id.title3);
        this.image = (ImageView) infoWindow.findViewById(R.id.ivProfilePic);

        filterbtn = (ImageView)findViewById(R.id.button);
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Location.this, FIlterActvity.class);
                startActivity(i);
            }
        });

        float_map=(FloatingActionButton) findViewById(R.id.fab_navigation);
        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Location.this, Activity_SerchResult.class);
                startActivity(i);
            }
        });
        map.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker)
            {

              //  infoTitle.setText(marker.getTitle());

                //infoButtonListener.setMarker(marker);
                String[] allIdsArray = TextUtils.split(marker.getSnippet(), ",");
                ArrayList<String> idsList = new ArrayList<String>(Arrays.asList(allIdsArray));

               // infoSnippet.setText(idsList.get(0));
               // mno.setText(idsList.get(1));

                //  Picasso.with(getApplicationContext()).load(idsList.get(2).toString()).into(image);
               // UrlImageViewHelper.setUrlDrawable(image, idsList.get(2).toString(), R.drawable.pro);

                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);


                return infoWindow;
            }
        });

        //calling post method//////
        params11 = new ArrayList<NameValuePair>();
        params11.add(new BasicNameValuePair("location", "" + ""));
        params11.add(new BasicNameValuePair("doctor_type", "" + ""));
        params11.add(new BasicNameValuePair("insurance_card", "" + ""));
        new background().execute();

        /////////////////////////

        //SyncMethod("http://byteofearth.com/TestMyShop/APIS/list_Myshop.php");


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void SyncMethod(final String GetUrl) {
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
                    try {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);


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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Activity_Location Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class background extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {
            String obj;//new JSONArray();
            try {
                // obj=getJSONFromUrl("Your posting path", params11);
                obj = getJSONFromUrl("http://bmptechnologies.com/eelixar_api/get_my_search.php", params11);
                return obj;
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            Log.e("Result of geting data", "" + result);
            try {

                Log.e("Exam", "screen>>" + result);
                JSONObject get_res = new JSONObject(result);

                JSONArray array = new JSONArray();

                array = get_res.getJSONArray("response");
                Log.e("mess", "screen>>" + array.toString());


                for (int aa = 0; aa <= array.length(); aa++) {
                    Bitmap imagedisplay = null;
                    Double d = null, d1 = null;
                    if (aa == 0) {
                        d = new Double(21.7644725);
                        d1 = new Double(72.1519304);
                    } else if (aa == 1) {
                        d = new Double(22.3038945);
                        d1 = new Double(70.8021599);
                    } else if (aa == 2) {

                        d = new Double(21.8757175);
                        d1 = new Double(73.5594128);
                    }

                    map.addMarker(new MarkerOptions()
                            // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                            .icon((BitmapDescriptorFactory.fromResource(R.drawable.mapin_large)))
                            .title(array.getJSONObject(aa).getString("doctor_name").toString())
                            .snippet(array.getJSONObject(aa).getString("doctor_experience").toString() + "," + array.getJSONObject(aa).getString("doctor_education").toString() + "," + array.getJSONObject(aa).getString("doctor_profile_pic").toString())

                            .position(new LatLng(d, d1)));

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(d, d1), 8.0f));

                }

            } catch (Exception e) {

            }

        }
    }

    public String getJSONFromUrl(String url, List<NameValuePair> params) {
        InputStream is = null;
        String json = "";

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                //sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;

    }
}


