package com.elixar.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.elixar.R;

/**
 * Created by BMPComp2 on 25-01-2017.
 */

public class VerificationActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   TextView verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verify=(TextView)findViewById(R.id.tvSubmit);
        verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(VerificationActivity.this, Activity_Resetpass.class);
                startActivity(i);


            }
        });
    }


}
