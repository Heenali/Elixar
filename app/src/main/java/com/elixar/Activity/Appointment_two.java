package com.elixar.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.elixar.R;

/**
 * Created by HEENALI on 10-02-2017.
 */
public class Appointment_two extends Activity
{

LinearLayout tab1_l,tab2_l;
    TextView tab1,tab2;
    ImageView back,mapdoc;
    RadioButton radioButton1,radioButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);
        radioButton1=(RadioButton)findViewById(R.id.radioButton1);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);



        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                radioButton1.setChecked(false);
                radioButton2.setChecked(true);
            }
        });

        tab1=(TextView)findViewById(R.id.tab1);
        tab2=(TextView)findViewById(R.id.tab2);

        tab1_l=(LinearLayout) findViewById(R.id.tab1_layout);
        tab2_l=(LinearLayout) findViewById(R.id.tab2_layout);

        tab2_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tab1_l.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_white));
                tab2_l.setBackgroundDrawable(getResources().getDrawable(R.drawable.border1_appcolor));


                tab1.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
                tab2.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));
            }
        });
        tab1_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tab1_l.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
                tab2_l.setBackgroundDrawable(getResources().getDrawable(R.drawable.border1));


                tab1.setTextColor(ContextCompat.getColor(getApplication(), R.color.white));
                tab2.setTextColor(ContextCompat.getColor(getApplication(), R.color.app_theme_color));
            }
        });


        back=(ImageView)findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               finish();

            }
        });

    }

}
