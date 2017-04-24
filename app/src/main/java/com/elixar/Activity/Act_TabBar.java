package com.elixar.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.elixar.Fragment.MainTab1;
import com.elixar.Fragment.MainTab2;
import com.elixar.Fragment.MainTab3;
import com.elixar.Fragment.MainTab4;
import com.elixar.Fragment.MainTab5;
import com.elixar.R;


public class Act_TabBar extends Activity implements OnClickListener
{

	//private LinearLayout lin_wack,lin_radiostream,lin_wackTV,lin_shoutbox,lin_RssFeed;
	private ImageView img_one,img_two,img_three,img_four,img_five ;
	private TextView txt_one,txt_two,txt_three,txt_four,txt_five;
	private int tab_id = 0;
	Context mcontext;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mcontext=this;
		//	SetLanguageToAll();
	}

	@TargetApi(Build.VERSION_CODES.M)
	public void initialize_Bottombar(int tab)
	{

		tab_id=tab;

		img_one=(ImageView) findViewById(R.id.ivHome);
		img_two=(ImageView) findViewById(R.id.ivDeal);
		img_three=(ImageView) findViewById(R.id.ivReport);
		img_four=(ImageView) findViewById(R.id.ivNotifications);
		img_five=(ImageView) findViewById(R.id.ivAccount);


		txt_one=(TextView) findViewById(R.id.tvHome);
		txt_two=(TextView) findViewById(R.id.tvDeal);
		txt_three=(TextView) findViewById(R.id.tvReport);
		txt_four=(TextView) findViewById(R.id.tvNotifications);
		txt_five=(TextView) findViewById(R.id.tvAccount);

		txt_one.setOnClickListener(this);
		txt_two.setOnClickListener(this);
		txt_three.setOnClickListener(this);
		txt_four.setOnClickListener(this);
		txt_five.setOnClickListener(this);

		img_one.setOnClickListener(this);
		img_two.setOnClickListener(this);
		img_three.setOnClickListener(this);
		img_four.setOnClickListener(this);
		img_five.setOnClickListener(this);

		switch (tab)
		{
			case 1:
				img_one.setImageResource(R.drawable.search_red);
				txt_one.setTextColor(getResources().getColor(R.color.colorPrimary));
				break;
			case 2:
				img_two.setImageResource(R.drawable.appointment_red);
				txt_two.setTextColor(getResources().getColor(R.color.colorPrimary));
				break;

			case 3:
				img_three.setImageResource(R.drawable.report_red);
				txt_three.setTextColor(getResources().getColor(R.color.colorPrimary));
				break;

			case 4:
				img_four.setImageResource(R.drawable.notification_red);
				txt_four.setTextColor(getResources().getColor(R.color.colorPrimary));
				break;
			case 5:
				img_five.setImageResource(R.drawable.acc_red);
				txt_five.setTextColor(getResources().getColor(R.color.colorPrimary));
				break;
			default:
				break;
		}

	}





	@Override
	public void onClick(View v)
	{
		///if(v==lin_wack||v==imgV_wack)
		if(v==txt_one||v==img_one)
		{
			if(tab_id != 1)
			{
				Intent i=new Intent(Act_TabBar.this,MainTab1.class);
				startActivity(i);
				//overridePendingTransition(android.R.anim.slide_in_left,
				//        android.R.anim.slide_out_right);
				finish();
				//SetLanguageToAll();


			}
			else
			{

			}
		}
		else if (v==txt_two||v==img_two)
		{
			if(tab_id != 2)
			{

				Intent i=new Intent(Act_TabBar.this,MainTab2.class);
				startActivity(i);
				//overridePendingTransition(android.R.anim.slide_in_left,
				//        android.R.anim.slide_out_right);
				finish();
				//SetLanguageToAll();
			}
			else
			{

			}

		}
		else if (v==txt_three||v==img_three)
		{
			if(tab_id != 3)
			{

				Intent i=new Intent(Act_TabBar.this,MainTab3.class);
				startActivity(i);
				//overridePendingTransition(android.R.anim.slide_out_right,
				//        android.R.anim.slide_in_left);
				finish();
				//SetLanguageToAll();
			}
			else
			{

			}
		}
		else if (v==txt_four||v==img_four)
		{
			if(tab_id != 4)
			{

				Intent i=new Intent(Act_TabBar.this,MainTab4.class);
				startActivity(i);
				//overridePendingTransition(android.R.anim.slide_out_right,
				//        android.R.anim.slide_in_left);
				finish();
				//SetLanguageToAll();
			}
			else
			{

			}
		}
		else if (v==txt_five||v==img_five)
		{
			if(tab_id != 5)
			{

				Intent i=new Intent(Act_TabBar.this,MainTab5.class);
				startActivity(i);
				//overridePendingTransition(android.R.anim.slide_out_right,
				//        android.R.anim.slide_in_left);
				finish();
				//SetLanguageToAll();
			}else
			{

			}
		}

	}


}

