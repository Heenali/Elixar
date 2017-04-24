package com.elixar.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Adapter.searchtype_Adapter;
import com.elixar.Helper.ConnectionDetector;
import com.elixar.Helper.Constants;
import com.elixar.Helper.UserFunctions;
import com.elixar.Method.Doctortype_method;
import com.elixar.R;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Activity_SerchResult extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    //class object
/////

    private static final String tag = "MyCalendarActivity";

    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private LinearLayout prevMonth;
    private LinearLayout nextMonth;
    private GridView calendarView;
    //  private GridCellAdapter adapter;
    private Calendar _calendar;
    int day;
    private int month, year;


    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

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
    TextView test_display,tvfilter,tvmap;
    AutoCompleteTextView searchView_text;
    FloatingActionButton float_map;
    ImageView back;
    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdata);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.e("********************", "******************************");
        Log.e("********************", "Notification page 1 api call");
        Log.e("********************", "******************************");


        back=(ImageView)findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        day = _calendar.get(Calendar.DAY_OF_MONTH);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                + year);

        selectedDayMonthYearButton = (Button) this
                .findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        prevMonth = (LinearLayout) this.findViewById(R.id.prevMonth);

        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                String dt = currentMonth.getText().toString();  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(dt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, -1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy");
                String output = sdf1.format(c.getTime());
                currentMonth.setText(output);
            }
        });
        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText( day+" "+DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (LinearLayout) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Click action
                String dt = currentMonth.getText().toString();  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(dt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy");
                String output = sdf1.format(c.getTime());
                currentMonth.setText(output);

            }
        });

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        // adapter = new GridCellAdapter(getApplicationContext(),  R.id.calendar_day_gridcell, month, year);
        // adapter.notifyDataSetChanged();
        // calendarView.setAdapter(adapter);


        init();

        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Activity_SerchResult.this, Activity_Location.class);
              startActivity(i);
            }
        });
        try
        {
            searchView_text.setThreshold(1);
           // searchView_text.setAdapter(new Activity_SerchResult.GooglePlacesAutocompleteAdapter_so(Activity_SerchResult.this, R.layout.item));

        }
        catch (Exception e) {

        }
        notification_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Intent i = new Intent(Activity_SerchResult.this, Appointnt.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });
        searchView_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  Toast.makeText(getApplicationContext(),"from",Toast.LENGTH_SHORT).show();

                Toast.makeText(Activity_SerchResult.this,"Selected Location "+searchView_text.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });
        tvfilter = (TextView)findViewById(R.id.filter);
        tvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(Activity_SerchResult.this, FIlterActvity.class);
                startActivity(intent);
            }
        });


        tvmap = (TextView)findViewById(R.id.map);
        tvmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(Activity_SerchResult.this, Activity_Location.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        params11 = new ArrayList<NameValuePair>();
                                        params11.add(new BasicNameValuePair("location", ""+ Constants.store_location));
                                        params11.add(new BasicNameValuePair("doctor_type", ""+Constants.store_doctor_type));
                                        params11.add(new BasicNameValuePair("insurance_card", ""+Constants.store_insuarance_type));
                                        new background().execute();

                                    }
                                }
        );
       /* float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_SerchResult.this, Activity_Location.class);
                startActivity(i);
            }
        });
*/
    }
    public void init()
    {

        searchView_text = (AutoCompleteTextView)findViewById(R.id.searchView_text);
        UF = new UserFunctions(Activity_SerchResult.this);
        cd = new ConnectionDetector(Activity_SerchResult.this);
        norecord=(LinearLayout)findViewById(R.id.norecord);
        float_map=(FloatingActionButton) findViewById(R.id.fab_navigation);
        notification_listview=(ListView)findViewById(R.id.listView_notification);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow( searchView_text.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(false);

        params11 = new ArrayList<NameValuePair>();
        params11.add(new BasicNameValuePair("location", ""+Constants.store_location));
        params11.add(new BasicNameValuePair("doctor_type", ""+Constants.store_doctor_type));
        params11.add(new BasicNameValuePair("insurance_card", ""+Constants.store_insuarance_type));
        new background().execute();

    }
    @Override
    public void onResume()
    {
        super.onResume();

        swipeRefreshLayout.setRefreshing(false);

        params11 = new ArrayList<NameValuePair>();
        params11.add(new BasicNameValuePair("location", ""+Constants.store_location));
        params11.add(new BasicNameValuePair("doctor_type", ""+Constants.store_doctor_type));
        params11.add(new BasicNameValuePair("insurance_card", ""+Constants.store_insuarance_type));
        new background().execute();


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
                   // Log.e("fffffffff",array.getJSONObject(aa).getString("doctor_profile_pic").toString());
                    actorsList.add(new Doctortype_method(array.getJSONObject(aa).getString("doctor_name").toString(),array.getJSONObject(aa).getString("doctor_name").toString(),array.getJSONObject(aa).getString("doctor_experience").toString(),array.getJSONObject(aa).getString("doctor_education")));

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
                    FilterResults filterResults = new Filter.FilterResults();
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
    public static ArrayList<String> autocomplete_so(String input)
    {
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
    private void setGridCellAdapterToDate(int month, int year) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat DesiredFormat  = new SimpleDateFormat("MMM, yyyy");
        String s=month+"/"+year;
        Date date1 = null;
        try {
            date1 = sourceFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = DesiredFormat.format(date1);
        currentMonth.setText(day+" "+formattedDate.toString());

    }

}
