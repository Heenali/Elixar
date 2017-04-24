package com.elixar.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class AddInsuranceActivity extends AppCompatActivity {

    ImageView image,select_btn;

    File mypath;
    String final_image="";
    int status_final=0;
    ///////////////
    private String userChoosenTask;
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    TextView btn_signup;
    ProgressDialog pDialog;
    SessionManager sm;
    public String current = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addinsurance);
        pDialog = new ProgressDialog(AddInsuranceActivity.this);
        init();
        mypath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+current);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signup();
            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });




    }

    public void init()
    {
        image=(ImageView)findViewById(R.id.image);
        select_btn=(ImageView) findViewById(R.id.select_btn);
        btn_signup=(TextView)findViewById(R.id.tvuplode);
        sm= new SessionManager(AddInsuranceActivity.this);
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddInsuranceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddInsuranceActivity.this);

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

        if (resultCode == Activity.RESULT_OK) {
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

        image.setImageBitmap(thumbnail);
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

                    //  Toast.makeText(getApplicationContext(), f.getAbsolutePath(), 20).show();

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

                    image.setImageBitmap(bm);

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
        // Toast.makeText(getBaseContext(), "Uploding failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }
    public void onSignupSuccess()
    {
        btn_signup.setEnabled(true);
        setResult(RESULT_OK, null);

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
            Toast.makeText(getApplicationContext(),final_image,Toast.LENGTH_LONG).show();
            multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("patient_id", new StringBody(""+sm.getUserId()));
            multipartEntity.addPart("plan_id", new StringBody(""+ 1));
            multipartEntity.addPart("insurence_card_image",  new FileBody(f));
            SyncMethodSubmit("http://bmptechnologies.com/eelixar_api/add_insurence_card.php");
        }
        catch (Exception e)
        {

        }



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
                            Toast.makeText(getApplicationContext(),"Successfully Uploading ", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        finish();
                        status_final=1;

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
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
    public boolean validate()
    {
        boolean valid = true;


        if(final_image.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Please Select Image", Toast.LENGTH_LONG).show();
            valid = false;
        }
        else
        {

        }


        return valid;
    }

}
