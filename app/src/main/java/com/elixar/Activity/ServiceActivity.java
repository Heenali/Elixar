package com.elixar.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elixar.R;

import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {


    //    private RecyclerView rvGallery;
    private ListView lvServices;
    private GridLayoutManager lLayout;
    private LinearLayout llNoData;
    private TextView tvReload;
    private TextView tvSubmit;

    private LinearLayout llCategoryServices;
    private LinearLayout llPhoto;


    private LinearLayout llPhotoPreview;
    private LinearLayout llPhoto1;
    private ImageView ivPhoto1;

    private LinearLayout llPhoto2;
    private ImageView ivPhoto2;

    private LinearLayout llPhoto3;
    private ImageView ivPhoto3;
    private TextView tvTotalPhotos;


//    private LinearLayout llFirstRow;
//    private LinearLayout llSecondRow;
//    private HorizontalScrollView hsvFirst;
//    private HorizontalScrollView hsvSecond;


    private boolean editMode;

    public ArrayList<Integer> listService;



    private BroadcastReceiver myReceiver;
    private IntentFilter mIntentFilter;
TextView cancel;
    //Task
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        cancel=(TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();



            }
        });


    }


    @Override
    public void onClick(View v) {

    }
}
