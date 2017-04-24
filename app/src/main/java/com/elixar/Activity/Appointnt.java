package com.elixar.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elixar.R;

import java.util.Calendar;

/**
 * Created by HEENALI on 10-02-2017.
 */
public class Appointnt extends Activity
{
    LinearLayout tab1_l,tab2_l;
    TextView tab1,tab2;
    private String openTimeString, closeTimeString;
    private TextView edOnTime, edCloseTime;
    private Calendar startTime;
    private Calendar endTime;
    ImageView map;
    ImageView  back,mapdoc;
    LinearLayout next,calander,datepicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_two);
        calander = (LinearLayout) findViewById(R.id.calender_layout);
        datepicker = (LinearLayout) findViewById(R.id.date);
        back=(ImageView)findViewById(R.id.backbtn);
        tab1=(TextView)findViewById(R.id.tab1);
        tab2=(TextView)findViewById(R.id.tab2);

        tab1_l=(LinearLayout) findViewById(R.id.tab1_layout);
        tab2_l=(LinearLayout) findViewById(R.id.tab2_layout);

        tab2_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                tab1_l.setBackgroundColor(Color.parseColor("#ffffff"));
                tab2_l.setBackgroundColor(Color.parseColor("#f75d7f"));


                tab1.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
                tab2.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));
            }
        });
        tab1_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                tab1_l.setBackgroundColor(Color.parseColor("#f75d7f"));
                tab2_l.setBackgroundColor(Color.parseColor("#ffffff"));

                tab1.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));
                tab2.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
            }
        });
        LinearLayout next;

        next = (LinearLayout) findViewById(R.id.next_layout);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Appointnt.this, Appointment_two.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        mapdoc=(ImageView)findViewById(R.id.doctormap);
        mapdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(Appointnt.this,Activity_doctormap.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               finish();


            }
        });


    }

}
