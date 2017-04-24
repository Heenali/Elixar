package com.elixar.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.elixar.Adapter.Doctortype_Adapter;
import com.elixar.Helper.ConnectionDetector;
import com.elixar.Helper.Constants;
import com.elixar.Helper.UserFunctions;
import com.elixar.Method.Doctortype_method;
import com.elixar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Ravi on 29/07/15.
 */
public class Activity_Doctortype extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    //class object

    UserFunctions UF;
    ConnectionDetector cd;
    //string variable
    String android_id;
    String json_save;
    LinearLayout norecord;
    //controls all
    ListView notification_listview;
    ArrayList<Doctortype_method> actorsList;
    Doctortype_Adapter adapter;
    ImageView back;
    View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctortype);
        Log.e("********************", "******************************");
        Log.e("********************", "Notification page 1 api call");
        Log.e("********************", "******************************");
        init();

back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});
        swipeRefreshLayout.setOnRefreshListener(this);
        // swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        if (cd.isConnectingToInternet())
                                        {

                                            SyncMethod("http://bmptechnologies.com/eelixar_api/get_speciality_category.php");

                                        } else {
                                            UF.msg("Check Your Internet Connection.");
                                        }


                                    }
                                }
        );
        notification_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String postloadid = actorsList.get(position).gettitle();
                String source_addoc=actorsList.get(position).gettitle();
                Constants.store_doctor_type=actorsList.get(position).gettitle();
                Toast.makeText(getApplicationContext(),"Selecting "+ Constants.store_doctor_type+" Doctor Type", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    public void init()
    {
        UF = new UserFunctions(Activity_Doctortype.this);
        cd = new ConnectionDetector(Activity_Doctortype.this);
        norecord=(LinearLayout)findViewById(R.id.norecord);
        back=(ImageView)findViewById(R.id.backbtn);
        notification_listview=(ListView)findViewById(R.id.listView_notification);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(false);
         if(cd.isConnectingToInternet())
            {
                SyncMethod("http://bmptechnologies.com/eelixar_api/get_speciality_category.php");
            }
            else
            {
                UF.msg("Check Your Internet Connection.");
            }



    }
    private static String pad(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        swipeRefreshLayout.setRefreshing(false);
        if(cd.isConnectingToInternet())
        {
            SyncMethod("http://bmptechnologies.com/eelixar_api/get_speciality_category.php");

        }
        else
        {
            UF.msg("Check Your Internet Connection.");
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
                    try {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);

                        swipeRefreshLayout.setRefreshing(false);

                        JSONObject jobj = new JSONObject(aResponse);
                        Log.e("Home Get draft--", jobj.toString());
                        String status = jobj.getString("status");

                        Log.e("Myorder Homestatusdraft",status);
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("true"))
                        {
                            actorsList = new ArrayList<Doctortype_method>();
                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("response");
                            if(actorsList.size()>0){
                                actorsList.clear();
                            }
                            for(int i=0;i<array.length();i++)
                            {


                                actorsList.add(new Doctortype_method(array.getJSONObject(i).getString("category_images"),array.getJSONObject(i).getString("category_name"),array.getJSONObject(i).getString("category_description"),array.getJSONObject(i).getString("created_date")));

                            }
                            if (getApplicationContext() != null)
                            {

                                if (adapter == null)
                                {
                                    adapter = new Doctortype_Adapter(getApplicationContext(),actorsList);
                                    notification_listview.setAdapter(adapter);

                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            if(actorsList.size()==0)
                            {
                                norecord.setVisibility(View.VISIBLE);
                            }


                        }
                        else
                        {
                            swipeRefreshLayout.setRefreshing(false);
                            norecord.setVisibility(View.VISIBLE);
                            // UF.msg(message + "");
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
