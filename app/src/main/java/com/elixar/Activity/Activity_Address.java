package com.elixar.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.elixar.Helper.Utility;
import com.elixar.R;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by HEENALI on 16-02-2017.
 */
public class Activity_Address extends AppCompatActivity
{

    LinearLayout home,url,add,phone_layout1;
    LinearLayout home1,url1,add1,phone_layout;
    ImageView h,a,u,p;
    View phone_layout_side,home_layout_side,work_layout_side;
    ImageView userimage;
    ImageView back;
    EditText firstname;
    String final_image="";
    ///////////////
    private String userChoosenTask;
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_address);
        back=(ImageView)findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firstname=(EditText)findViewById(R.id.firstname_edittext);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(firstname.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        userimage=(ImageView) findViewById(R.id.userimage);
        phone_layout_side=(View) findViewById(R.id.phone_layout_side);
        work_layout_side=(View) findViewById(R.id.work_layout_side);
        home_layout_side=(View) findViewById(R.id.home_layout_side);

        home1=(LinearLayout)findViewById(R.id.home1layout);
        url1=(LinearLayout)findViewById(R.id.url1_layout);
        add1=(LinearLayout)findViewById(R.id.add1_layout);
        phone_layout1=(LinearLayout)findViewById(R.id.phone1_layout);

        home=(LinearLayout)findViewById(R.id.home_layout);
        url=(LinearLayout)findViewById(R.id.url_layout);
        add=(LinearLayout)findViewById(R.id.work_layout);
        phone_layout=(LinearLayout)findViewById(R.id.phone_layout);

        h=(ImageView)findViewById(R.id.imag_home1);
        u=(ImageView)findViewById(R.id.imag_url_btn);
        a=(ImageView)findViewById(R.id.imag_address1);
        p=(ImageView)findViewById(R.id.imag_phone);
        home_layout_side.setVisibility(View.GONE);
        phone_layout_side.setVisibility(View.GONE);
        work_layout_side.setVisibility(View.GONE);
        phone_layout1.setVisibility(View.GONE);
        home.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        url.setVisibility(View.GONE);

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                selectImage();
            }
        });
        home1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                if (home.getVisibility() == View.VISIBLE)
                {
                    home.setVisibility(View.GONE);
                    home_layout_side.setVisibility(View.GONE);
                    h.setBackgroundDrawable(getResources().getDrawable(R.drawable.add));

                }
                else
                {
                    home.setVisibility(View.VISIBLE);
                    home_layout_side.setVisibility(View.VISIBLE);
                    h.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
                }
            }
        });

        url1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                if (url.getVisibility() == View.VISIBLE)
                {
                    url.setVisibility(View.GONE);

                    u.setBackgroundDrawable(getResources().getDrawable(R.drawable.add));
                }
                else
                {
                    url.setVisibility(View.VISIBLE);
                    u.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
                }
            }
        });

        add1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {


                if (add.getVisibility() == View.VISIBLE)
                {
                    add.setVisibility(View.GONE);
                    work_layout_side.setVisibility(View.GONE);
                    a.setBackgroundDrawable(getResources().getDrawable(R.drawable.add));
                }
                else
                {
                    add.setVisibility(View.VISIBLE);
                    work_layout_side.setVisibility(View.VISIBLE);
                    a.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
                }
            }
        });
        phone_layout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

               //
                // Toast.makeText(getApplicationContext(),"ffff",Toast.LENGTH_SHORT).show();
                if (phone_layout1.getVisibility() == View.VISIBLE)
                {
                    phone_layout1.setVisibility(View.GONE);
                    phone_layout_side.setVisibility(View.GONE);
                    p.setBackgroundDrawable(getResources().getDrawable(R.drawable.add));
                }
                else
                {
                    phone_layout1.setVisibility(View.VISIBLE);
                    phone_layout_side.setVisibility(View.VISIBLE);
                    p.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_Address.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Activity_Address.this);

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

        userimage.setImageBitmap(thumbnail);
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

                    userimage.setImageBitmap(bm);

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
