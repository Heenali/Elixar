package com.elixar.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Helper.SessionManager;
import com.elixar.Helper.Utility;
import com.elixar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_Profileupdate extends Activity {

    //class object declaration
    String selectedImagePath_uplode="",selectedImagePath;
    int status_final=0;
    File mypath;
    String final_image;
    ///////////////
    private String userChoosenTask;
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    //all reqired string declartion
    String json_save;
    /////////////////////
    View myFragmentView;
    ImageView propic;
    //all control declartion
    EditText firstname,lastname,emailid,pass,conpass,mobno;
    TextView btn_signup;
    String android_id;
    TextView link_login;
    public String current = null;
    ///////////////////
    ImageView getimage;
    ProgressDialog pDialog;
    SessionManager sm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);
        // Inflate the layout for this fragment
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //init() contains all control declaration
        init();
        pDialog = new ProgressDialog(Activity_Profileupdate.this);
        mypath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+current);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signup();
            }
        });
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });


    }
    public  void init()
    {

        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_Profileupdate.this);

        propic=(ImageView)findViewById(R.id.propic);
        firstname=(EditText)findViewById(R.id.r_et_fname);
        lastname=(EditText)findViewById(R.id.r_et_lname);
        emailid=(EditText)findViewById(R.id.r_et_email);
        mobno=(EditText)findViewById(R.id.r_et_phone);
        pass=(EditText)findViewById(R.id.r_et_password);
        conpass=(EditText)findViewById(R.id.r_et_repassword);
        btn_signup=(TextView)findViewById(R.id.tvSubmit);
        getimage=(ImageView) findViewById(R.id.addimage);
        //links clicks

/*
        firstname.setText(sm.getUserName());
        lastname.setText(sm.getUserNamel());
        emailid.setText(sm.getemailid());
        // mobno.setText(sm.getemailid());

        getimage.setVisibility(View.GONE);
        String image=sm.getimage();
        UrlImageViewHelper.setUrlDrawable(propic, image.toString(), R.drawable.no_img);*/

    }

    //validation function
    public boolean validate()
    {
        boolean valid = true;
        String fname = firstname.getText().toString().trim();
        String lname = lastname.getText().toString().trim();
        String email = emailid.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String mobile_number = mobno.getText().toString();
        String confirmPassword = conpass.getText().toString().trim();
        if (fname.isEmpty() || fname.length() < 3) {
            firstname.setError("at least 3 characters");

            valid = false;
        } else {
            firstname.setError(null);
        }
        if (lname.isEmpty() || lname.length() < 3) {
            lastname.setError("at least 3 characters");

            valid = false;
        } else {
            lastname.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid.setError("enter a valid email address");
            valid = false;
        } else {
            emailid.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            pass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pass.setError(null);
        }

        if (confirmPassword.isEmpty() || confirmPassword.length() < 4 || confirmPassword.length() > 10) {
            conpass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            conpass.setError(null);
        }
        if(final_image.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Please Select Image", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else
        {

        }


        if (mobile_number.isEmpty() || mobile_number.length() < 10) {
            mobno.setError("Please enter valid mobile number");
            valid = false;
        } else {
            mobno.setError(null);
        }

        return valid;
    }
    public void signup()
    {
        Log.d("mess", "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btn_signup.setEnabled(false);


        onSignupSuccess();

    }
    public void onSignupFailed() {
        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }
    public void onSignupSuccess()
    {
        btn_signup.setEnabled(true);
       setResult(RESULT_OK, null);

        String password = pass.getText().toString().trim();

        String confirmPassword = conpass.getText().toString().trim();

        if (confirmPassword.equalsIgnoreCase(password))
        {
            try
            {
                pDialog.setMessage("Please Wait...");
                pDialog.setCancelable(false);
                pDialog.show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap image = null;

                if(final_image.equalsIgnoreCase(""))
                {

                    Toast.makeText(getApplicationContext(), "select Image.....", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Log.e(" path",""+final_image);
                    image = BitmapFactory.decodeFile(final_image,options);
                }
                File f = null;
                if(image != null)
                {
                    FileOutputStream mFileOutStream = new FileOutputStream(mypath);
                    image.compress(Bitmap.CompressFormat.PNG, 60, mFileOutStream);
                    mFileOutStream.flush();
                    mFileOutStream.close();
                    f = new File(final_image);

                }
                //  Toast.makeText(getApplicationContext(),selectedImagePath_uplode,Toast.LENGTH_LONG).show();
                multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("patient_firstname", new StringBody(""+firstname.getText().toString()));
                multipartEntity.addPart("patient_lastname", new StringBody(""+lastname.getText().toString()));
                multipartEntity.addPart("patient_email", new StringBody(""+emailid.getText().toString()));
                multipartEntity.addPart("patient_password", new StringBody(""+pass.getText().toString()));
                multipartEntity.addPart("patient_mobile_number", new StringBody(""+emailid.getText().toString()));
                multipartEntity.addPart("patient_profile_image",  new FileBody(f));
                SyncMethodSubmit("http://bmptechnologies.com/eelixar_api/patient_register.php");
            }
            catch (Exception e)
            {

            }


        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password not matched", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        /*firstname.setText("");
        lastname.setText("");
        emailid.setText("");
        mobno.setText("");
        pass.setText("");
        conpass.setText("");*/

    }
    public void SyncMethodSubmit(final String GetUrl)
    {

        final Thread background = new Thread(new Runnable()
        {

            public void run()
            {
                try
                {

                    String url=GetUrl;//"http://demo.webeet.net/mobex/api/webservices/move_offer/code_id/"+code_id+"/from/"+move_from+"/to/"+to_folder+"/flag/"+flagged_;
                    url=url.trim();
                    // Log.e("Url",""+url);
                    String SetServerString = "";
                    SetServerString=getJSONFromUrlSubmit(url);
                    threadMsg(SetServerString);
                }
                catch (Throwable t)
                {

                    Log.e("Animation", "Thread  exception " + t);
                }
            }
            private void threadMsg(String msg)
            {

                if (!msg.equals(null) && !msg.equals(""))
                {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }

            private final Handler handler11 = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    String aResponse = msg.getData().getString("message");
                    Log.e("visi add",">>"+aResponse);
                    try
                    {
                        JSONObject jobj = new JSONObject(aResponse);
                        String status = jobj.getString("status");
                        if(status.equalsIgnoreCase("true"))
                            Toast.makeText(getApplicationContext(),"Successfully Registration ", Toast.LENGTH_LONG).show();

                        status_final=1;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            };
        });
        // Start Thread
        background.start();
    }
    public String getJSONFromUrlSubmit(String url)//, List<NameValuePair> params)
    {
        InputStream is = null;
        String json = "";

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(multipartEntity);
            // httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e)
        {
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
        // try parse the string to a JSON object
//		        try {
//		            jObj = new JSONObject(json);
//		        } catch (JSONException e) {
//		            Log.e("JSON Parser", "Error parsing data " + e.toString());
//		        }
        //
        // return JSON String
        return json;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library jjjj"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_Profileupdate.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Activity_Profileupdate.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        final_image=destination.toString();
        //  Toast.makeText(getApplicationContext(), destination.toString(), Toast.LENGTH_SHORT).show();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        propic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data)
    {

        Bitmap bm=null;
        if (data != null) {
            try {

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                OutputStream fOut = null;
                String strDirectory = Environment.getExternalStorageDirectory().toString();
                Log.e("Original   dimensions", bm.getWidth()+" "+bm.getHeight());

                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                File f = new File(strDirectory, String.valueOf(seconds));
                try
                {
                    fOut = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    Log.e("Compressed dimensions", bm.getWidth()+" "+bm.getHeight());
                    fOut.flush();
                    fOut.close();

                    // Toast.makeText(getApplicationContext(), f.getAbsolutePath(), 20).show();

                    MediaStore.Images.Media.insertImage(getContentResolver(),f.getAbsolutePath(), f.getName(), f.getName());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(f.getAbsolutePath().toString(), options);
                    final int REQUIRED_SIZE = 100;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    final_image=f.getAbsolutePath().toString();
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath().toString() ,options);

                    propic.setImageBitmap(bm);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
