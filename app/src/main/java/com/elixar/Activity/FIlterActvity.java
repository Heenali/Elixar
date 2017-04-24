package com.elixar.Activity;


import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elixar.Fragment.MainTab1;
import com.elixar.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.util.Calendar;



public class FIlterActvity extends AppCompatActivity {


    private int REQUEST_CAMERA = 100, SELECT_FILE = 101;

    private TextView toolbarTitle;
    private TextView tvAction;
    private EditText _CompanyName, _ContactPhone, _ContactEmail, _Website, edAbout;
    private FloatingActionButton fab;
    private ImageView ivBusiness;

    private LinearLayout llAddBusiness;
    private LinearLayout llBusinessPreview;
    private LinearLayout llBasicInfo;
    private LinearLayout llAddress;
    private LinearLayout llTiming;
    private LinearLayout llPhoto;
    private LinearLayout llServices;

    TextView text_all,text_male,text_female;
    private LinearLayout llPostAnOffer;
    private LinearLayout llDeals;
    private LinearLayout llReviews;
    private LinearLayout llAnalytics;
    private String openTimeString, closeTimeString;
    private Bitmap issueBitmap;
    private String issueImagePath;
    private File imageFile;

    //Task
    private ProgressDialog pd;

    private EditText edOnTime, edCloseTime;
    private Calendar startTime;
    private Calendar endTime;
    RangeSeekBar seekBarInteger;
    LinearLayout all_gender,female_gender,male_gender;
    ImageView  back;
    //Business

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_filter);



        initControls();
        seekBarInteger.setRangeValues(0, 30); // if we want to set progrmmatically set range of seekbar
        seekBarInteger.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {


            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                //   minTextInt.setText("Min Value " + minValue);
                // maxtextInt.setText("Max value " + maxValue);

            }

        });
    }

    private void initControls() {
        seekBarInteger = (RangeSeekBar) findViewById(R.id.seekbar);
        // minTextInt = (TextView) findViewById(R.id.seekValuemin);
        //  maxtextInt = (TextView) findViewById(R.id.seekValuemax);
        llAddBusiness = (LinearLayout) findViewById(R.id.lininsurance);
        llBusinessPreview = (LinearLayout) findViewById(R.id.linlocation);
        llBasicInfo = (LinearLayout)findViewById(R.id.linservices);
        llAnalytics=(LinearLayout)findViewById(R.id.linlanguage);
        all_gender = (LinearLayout)findViewById(R.id.all_gender);
        female_gender = (LinearLayout)findViewById(R.id.female_gender);
        male_gender = (LinearLayout)findViewById(R.id.male_gender);
        back=(ImageView)findViewById(R.id.backbtn);
        text_all = (TextView) findViewById(R.id.text_all);
        text_male = (TextView) findViewById(R.id.text_male);
        text_female = (TextView) findViewById(R.id.text_female);

        all_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                all_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
                male_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border2));
                female_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border1));
                text_all.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));
                text_male.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
                text_female.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
            }
        });
        male_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                all_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_white));
                male_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border2_appcolor));
                female_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border1));
                text_all.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
                text_male.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));
                text_female.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));

            }
        });
        female_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                all_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_white));
                male_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border2));
                female_gender.setBackgroundDrawable(getResources().getDrawable(R.drawable.border1_appcolor));
                text_all.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
                text_male.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
                text_female.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               finish();


            }
        });
        llAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(FIlterActvity.this,Activity_Insureance.class);
                startActivity(i);


            }
        });
        llBusinessPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(FIlterActvity.this,MainTab1.class);
                startActivity(i);


            }
        });

        llBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(FIlterActvity.this,ServiceActivity.class);
                startActivity(i);


            }
        });
        llAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(FIlterActvity.this,LanguageActivity.class);
                startActivity(i);


            }
        });


        edOnTime = (EditText) findViewById(R.id.bt_on_time);
        edOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startTime == null){
                    startTime = Calendar.getInstance();
                    startTime.set(Calendar.SECOND, 0);
                }


                int mHour = startTime.get(Calendar.HOUR_OF_DAY);
                int mMinute = startTime.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(FIlterActvity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        startTime.set(Calendar.HOUR_OF_DAY ,  hourOfDay);
                        startTime.set(Calendar.MINUTE, minute);
                        onTimeSetUI(hourOfDay, minute, "on_time");

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        edCloseTime = (EditText) findViewById(R.id.bt_off_time);
        edCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (endTime == null){
                    endTime = Calendar.getInstance();
                    endTime.set(Calendar.SECOND, 0);
                }


                int mHour = endTime.get(Calendar.HOUR_OF_DAY);
                int mMinute = endTime.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(FIlterActvity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Calendar temp = Calendar.getInstance();
                        temp.set(Calendar.HOUR_OF_DAY ,  hourOfDay);
                        temp.set(Calendar.MINUTE, minute);
                        temp.set(Calendar.SECOND, 0);

                        if (startTime.after(temp))
                        {
                            Toast.makeText(FIlterActvity.this, "Closing time should be after open time", Toast.LENGTH_LONG).show();
                            return;
                        }

                        endTime.set(Calendar.HOUR_OF_DAY ,  hourOfDay);
                        endTime.set(Calendar.MINUTE, minute);
                        onTimeSetUI(hourOfDay, minute, "close_time");

                    }
                }, mHour, mMinute, false);

                if (startTime!=null)
                    timePickerDialog.show();
            }
        });

    }
    private String pad(int value) {
        return (value > 9 ? String.valueOf(value) : "0" + value);
    }
    private void onTimeSetUI(int hourOfDay, int minute, String tag) {

        if (tag.equalsIgnoreCase("on_time")) {
            openTimeString = pad(hourOfDay) + ":" + pad(minute);
            edOnTime.setText(openTimeString);
        } else {
            closeTimeString = pad(hourOfDay) + ":" + pad(minute);
            edCloseTime.setText(closeTimeString);
        }
    }
}