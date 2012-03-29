package com.vlara.craigslist;

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
