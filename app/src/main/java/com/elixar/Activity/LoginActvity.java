package com.elixar.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Fragment.MainTab1;
import com.elixar.Helper.ConnectionDetector;
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
 * Created by BMPComp2 on 25-01-2017.
 */

public class LoginActvity extends AppCompatActivity
{
    EditText email,pass;
    TextView submit;
    ConnectionDetector cd;
    SessionManager sm;
    String jsonLogin;
    List<NameValuePair> params11;
    TextView txt_regi,txt_forgot,txt_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
        txt_regi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LoginActvity.this, RegisterActvity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    CustomDialogClass_hometype hometypeclass = new CustomDialogClass_hometype(LoginActvity.this);
                    hometypeclass.show();




            }
        });



    }

    public boolean validate() {
        boolean valid = true;

        String mobile_number = email.getText().toString();
        String password = pass.getText().toString();

        if (mobile_number.isEmpty() ) {
            email.setError("Please enter Email Address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            pass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }
    public void login() {
        Log.d("mess", "Login");

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        submit.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        onLoginSuccess();
    }
    public void onLoginFailed()
    {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();

        submit.setEnabled(true);
    }
    public void init()
    {
        cd = new ConnectionDetector(LoginActvity.this);
        sm = new SessionManager(LoginActvity.this);
        email=(EditText)findViewById(R.id.r_et_email);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        pass=(EditText)findViewById(R.id.r_et_password);
        submit=(TextView) findViewById(R.id.tvLogin);
        txt_regi=(TextView) findViewById(R.id.link_registration);
        txt_forgot=(TextView) findViewById(R.id.link_forgot);


    }
    public void onLoginSuccess()
    {
        submit.setEnabled(true);

        if (cd.isConnectingToInternet())
        {

            params11 = new ArrayList<NameValuePair>();
            params11.add(new BasicNameValuePair("patient_email", ""+email.getText().toString()));
            params11.add(new BasicNameValuePair("patient_password", ""+pass.getText().toString()));
            new background().execute();
        } else {
            Toast.makeText(getApplicationContext(),"Check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    }
    class background extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pDialog = new ProgressDialog(LoginActvity.this);
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

                obj=getJSONFromUrl("http://bmptechnologies.com/eelixar_api/patient_login.php", params11);


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

                    JSONObject jArray = jobj.getJSONObject("response");

                    jArray.getString("patient_firstname");
                    sm.createLoginSession( jArray.getString("patient_id").toString());
                    sm.setUserId(jArray.getString("patient_id").toString(), jArray.getString("patient_firstname").toString(), jArray.getString("patient_lastname").toString(), jArray.getString("patient_password").toString(), jArray.getString("patient_profile_image").toString(), "", "", "", jArray.getString("patient_email").toString());


                    Intent i = new Intent(getApplicationContext(), MainTab1.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);//
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Email add or password Wrong", Toast.LENGTH_LONG).show();
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
    class CustomDialogClass_hometype extends Dialog {

        public Context context;
        public Dialog d;
        ListView listview_hometype;
        ProgressDialog loading;
        List<NameValuePair> params11_for;
        EditText email_txt;
        TextView submit;

        public CustomDialogClass_hometype(Context a) {
            super(a);
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_forgotpass);
            context = this.getContext();
            email_txt = (EditText) findViewById(R.id.email_edittext);
            submit = (TextView) findViewById(R.id.tvreset);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cd.isConnectingToInternet())
                    {
                        InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(submit.getWindowToken(), 0);
                        params11_for = new ArrayList<NameValuePair>();
                        params11_for.add(new BasicNameValuePair("patient_email", ""+email_txt.getText().toString()));

                        new background_for().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),"Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }
        ///
        class background_for extends AsyncTask<Void, Void, String>
        {
            ProgressDialog pDialog = new ProgressDialog(LoginActvity.this);
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

                    obj=getJSONFromUrl("http://bmptechnologies.com/eelixar_api/forgot_password.php", params11_for);


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

                     //   Toast.makeText(getApplicationContext(),"Request send successfully Now Check Your Mail", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActvity.this,VerificationActivity.class);
                        startActivity(i);

                    }
                    else
                        Toast.makeText(getApplicationContext(),"Email Address Wrong", Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {

                }

                pDialog.dismiss();

            }
        }

    }
}
