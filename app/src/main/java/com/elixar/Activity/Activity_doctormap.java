package com.elixar.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Adapter.searchtype_Adapter;
import com.elixar.Helper.ConnectionDetector;
import com.elixar.Helper.MapWrapperLayout;
import com.elixar.Helper.UserFunctions;
import com.elixar.Method.Doctortype_method;
import com.elixar.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.elixar.Activity.Activity_Location.getPixelsFromDp;

public class Activity_doctormap extends Activity {
    //class object
    double latitude = 0.000;
    double longitude = 0.000;
    UserFunctions UF;
    ConnectionDetector cd;
    //string variable

    String android_id;
    String json_save;
    LinearLayout norecord;
    //controls all
    ListView notification_listview;
    ArrayList<Doctortype_method> actorsList;
    searchtype_Adapter adapter;
    View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<NameValuePair> params11;
    TextView test_display, tvfilter, tvmap;
    LinearLayout searchView_text;
    Button searchView_text_text;
    private ViewGroup infoWindow;
    private  RatingBar  ratingBar;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private TextView mno;
    private ImageView image;

    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
    GoogleMap map;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctormap);
        Log.e("********************", "******************************");
        Log.e("********************", "Notification page 1 api call");
        Log.e("********************", "******************************");


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
        this.ratingBar = (RatingBar) infoWindow.findViewById(R.id.ratingBar);

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                infoTitle.setText(" My Location");
                infoSnippet.setVisibility(View.GONE);
                mno.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                ratingBar.setVisibility(View.GONE);


                return infoWindow;
            }
        });
        init();
        searchView_text_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();
            }
        });

        searchView_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();
            }
        });
      /*  mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng latLng = new LatLng(13.05241, 80.25082);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Raj Amal"));*/


    }


// this is method for create and load map with Support Map Fragment


    public void init()
    {
        searchView_text_text= (Button) findViewById(R.id.searchView_text_text);
        searchView_text = (LinearLayout)findViewById(R.id.searchView_text);
        UF = new UserFunctions(Activity_doctormap.this);
        cd = new ConnectionDetector(Activity_doctormap.this);
        // norecord=(LinearLayout)findViewById(R.id.norecord);
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation())
        {
            tracker.showSettingsAlert();
        }
        else
        {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            //  Toast.makeText(getApplicationContext(),latitude+"",Toast.LENGTH_LONG).show();

            map.addMarker(new MarkerOptions()
                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.mapin_large)))
                    .title("My Location")
                    .snippet("")

                    .position(new LatLng(latitude, longitude )));

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude ), 8.0f));
        }

        // notification_listview=(ListView)findViewById(R.id.listView_notification);
        // swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

    }


    class background extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }
        protected String doInBackground(Void... params)
        {
            String obj;//new JSONArray();
            try
            {
                // obj=getJSONFromUrl("Your posting path", params11);
                obj=getJSONFromUrl("http://bmptechnologies.com/eelixar_api/get_my_search.php", params11);
                return obj;
            }
            catch(Exception e)
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result)
        {
            super.onPostExecute(result);

            Log.e("Result of geting data",""+result);
            try {

                Log.e("Exam", "screen>>" + result);
                JSONObject get_res = new JSONObject(result);

                JSONArray array = new JSONArray();

                array = get_res.getJSONArray("response");
                Log.e("mess", "screen>>" + array.toString());
                actorsList = new ArrayList<Doctortype_method>();
                if(actorsList.size()>0){
                    actorsList.clear();
                }
                for (int aa = 0; aa <array.length();aa++)
                {
                    Log.e("fffffffff",array.getJSONObject(aa).getString("doctor_profile_pic").toString());
                    actorsList.add(new Doctortype_method(array.getJSONObject(aa).getString("doctor_profile_pic").toString(),array.getJSONObject(aa).getString("doctor_name").toString(),array.getJSONObject(aa).getString("doctor_experience").toString(),array.getJSONObject(aa).getString("doctor_education")));

                }

                if (adapter == null)
                {
                    adapter = new searchtype_Adapter(getApplicationContext(),actorsList);
                    notification_listview.setAdapter(adapter);

                } else {
                    adapter.notifyDataSetChanged();
                }

                if(actorsList.size()==0)
                {
                    norecord.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);

            } catch (Exception e) {
                swipeRefreshLayout.setRefreshing(false);
            }

        }
    }
    public String getJSONFromUrl(String url, List<NameValuePair> params)
    {
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
        }  catch (IOException e) {
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
        } catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;

    }
    class GooglePlacesAutocompleteAdapter_so extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter_so(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete_so(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
    public static ArrayList<String> autocomplete_so(String input) {
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8") );


            URL url = new URL(sb.toString());
            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {

        }

        return resultList;
    }
    private void openAutocompleteActivity() {
        try {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK)
            {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
              /*  mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*/
                searchView_text_text.setText(place.getAddress()+"");
                Toast.makeText(getApplicationContext(), place.getAddress()+"",Toast.LENGTH_LONG).show();


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



}
