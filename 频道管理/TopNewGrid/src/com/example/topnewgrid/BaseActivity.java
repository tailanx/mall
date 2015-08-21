package com.example.topnewgrid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseActivity  extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		findViewById(R.id.textView1).setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChannelActivity channelActivity = new ChannelActivity(BaseActivity.this);
				channelActivity.showAsDropDown(findViewById(R.id.textView1));
				
			}
		});
	}

}
