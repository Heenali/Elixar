package com.elixar.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.elixar.Fragment.MainTab1;
import com.elixar.Fragment.MainTab2;
import com.elixar.Fragment.MainTab3;
import com.elixar.Fragment.MainTab4;
import com.elixar.Fragment.MainTab5;
import com.elixar.R;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;


    //Fragments

    MainTab2 MainTab2;
    MainTab1 MainTab1;
    MainTab3 MainTab3;
    MainTab4 MainTab4;
    MainTab5 MainTab5;

    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.menu2:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.menu3:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.menu4:
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.menu5:
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe

       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        */

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        MainTab1=new MainTab1();
        MainTab2=new MainTab2();
        MainTab3=new MainTab3();
        MainTab4=new MainTab4();
        MainTab5=new MainTab5();
       // adapter.addFragment(MainTab1);
      //  adapter.addFragment(MainTab2);
       // adapter.addFragment(MainTab3);
        //adapter.addFragment(MainTab4);
        //adapter.addFragment(MainTab5);
        //viewPager.setAdapter(adapter);
    }
}
