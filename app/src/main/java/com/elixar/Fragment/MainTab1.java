package com.elixar.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elixar.Activity.Act_TabBar;
import com.elixar.Activity.Activity_Address;
import com.elixar.Activity.Activity_Doctortype;
import com.elixar.Activity.Activity_Insureance;
import com.elixar.Activity.Activity_Place;
import com.elixar.Activity.Activity_SerchResult;
import com.elixar.Activity.mylocation;
import com.elixar.R;


public class MainTab1 extends Act_TabBar {

    LinearLayout layout_doctypetype,layout_location,layout_insuarance;
    TextView serch_data;
    View  myFragmentView;
    AutoCompleteTextView searchView_text;
    TextView reset_pass;
    Context mcontext;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainTab1.this);
            builder.setMessage("Are you sure you want close application?");
            builder.setCancelable(false);
            // builder.setTitle("New Order");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        // Inflate the layout for this fragment
        mcontext=this;
        initialize_Controls();
        init();

        layout_doctypetype.setOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(MainTab1.this,Activity_Doctortype.class);

                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        layout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainTab1.this,mylocation.class);

                startActivity(i);

                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        layout_insuarance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainTab1.this,Activity_Insureance.class);

                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        serch_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainTab1.this,Activity_SerchResult.class);

                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

    }
    private void initialize_Controls()
    {
        // TODO Auto-generated method stub
        super.initialize_Bottombar(1);
    }
    public  void init() {

        layout_doctypetype = (LinearLayout) findViewById(R.id.layout_doctortype);
        layout_location = (LinearLayout) findViewById(R.id.layout_location);
        layout_insuarance = (LinearLayout) findViewById(R.id.layout_ins);
        layout_insuarance = (LinearLayout)findViewById(R.id.layout_ins);
        serch_data = (TextView) findViewById(R.id.tvSubmit);
    }

}
