package com.elixar.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.elixar.Activity.Act_TabBar;
import com.elixar.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTab2 extends Act_TabBar {

    Context mcontext;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainTab2.this);
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
        setContentView(R.layout.fragment_appoitment);
        // Inflate the layout for this fragment
        mcontext=this;
        initialize_Controls();

    }

    private void initialize_Controls()
    {
        // TODO Auto-generated method stub
        super.initialize_Bottombar(2);
    }

}
