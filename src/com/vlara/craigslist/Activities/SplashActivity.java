package com.vlara.craigslist.Activities;

import com.vlara.craigslist.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class SplashActivity extends Activity {
	public static Context ctx;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		ctx = this;
	}
}
