package com.elixar.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Helper.SessionManager;
import com.elixar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEENALI on 28-01-2017.
 */
public class Activity_Resetpass extends Activity
{
EditText pass,conf_pass;
    TextView submit;
    List<NameValuePair> params11;   SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);

        init();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(pass.getText().toString().equalsIgnoreCase("") || conf_pass.getText().toString().equalsIgnoreCase(""))
{
    pass.setError("Enter password");
    conf_pass.setError("Enter Confo password");
}
else
{
    if(pass.getText().toString().equalsIgnoreCase(conf_pass.getText().toString()))
    {
        params11 = new ArrayList<NameValuePair>();
        params11.add(new BasicNameValuePair("patient_id", ""+sm.getUserId()));
        params11.add(new BasicNameValuePair("patient_password", ""+pass.getText().toString()));

        new background().execute();

    }
    else
    {
        Toast.makeText(getApplicationContext(),"Password and Conform Pass not match",Toast.LENGTH_LONG).show();

    }


}




            }
        });

    }
    public void init()
    {    sm = new SessionManager(Activity_Resetpass.this);
        pass=(EditText) findViewById(R.id.pass_edittext);
        conf_pass=(EditText) findViewById(R.id.conpass_edittext);
        submit=(TextView) findViewById(R.id.tvreset);
    }
    class background extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pDialog = new ProgressDialog(Activity_Resetpass.this);
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String doInBackground(Void... params)
        {
            String obj;//new JSONArray();
            try
            {

                obj=getJSONFromUrl("http://bmptechnologies.com/eelixar_api/reset_password_varification.php", params11);


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

            Log.e("Result",""+result);

            /*{"status":"true","response":{"patient_id":"29","patient_firstname":"Heenali","patient_lastname":"Lakhani","patient_username":"","patient_email":"heena.jaypal@gmail.com","patient_password":"1234","patient_dob":"0000-00-00","patient_mobile_number":"9409174780","patient_profile_image":"http:\/\/www.bmptechnologies.com\/eelixar_api\/images\/profile_images\/Chrysanthemum.jpg","patient_weight":"","patient_height":"","patient_organ_donor":"","patient_blood_type":"","patient_medical_condition":"","patient_allegories_reaction":"","patient_fcm_id":"","forgot_pwd_code":"","verify_code":"0","is_varified":"0","created_date":"2017-01-26 08:53:31","is_active":"0"}}
            */
            try
            {
                JSONObject jobj = new JSONObject(result);
                String status = jobj.getString("status");
                if(status.equalsIgnoreCase("true"))
                {



                    Toast.makeText(getApplicationContext(),"Password change successfully ",Toast.LENGTH_LONG).show();
                    finish();

                }
                else
                    Toast.makeText(getApplicationContext(),"Somthing Wrong",Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {

            }

            pDialog.dismiss();

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
        } catch (ClientProtocolException e) {
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
        } catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;

    }
}
