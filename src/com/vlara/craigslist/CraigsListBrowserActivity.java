package com.vlara.craigslist;

import com.actionbarsherlock.app.SherlockActivity;
import com.vlara.craigslist.db.DBAdapter;
import com.vlara.craigslist.net.LocationAsyncTask;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class CraigsListBrowserActivity extends SherlockActivity {
	public final static String TAG = "CraigsApp";
	public static DBAdapter db;
	public static Context ctx;
	public static Dialog mSplashDialog;
	public static Spinner citySpin;
	public static Spinner stateSpin;
	public static Cursor stateCursor, cityCursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		db = new DBAdapter(this);
		db.open();
		stateCursor = db.getAllStates();
		Log.d(TAG, "Count: " + stateCursor.getCount());
		if (stateCursor.getCount() <= 0) {
			//Populate Database
			Log.d(TAG, "creating async task");
			LocationAsyncTask lat = new LocationAsyncTask();
			lat.execute("");
			Intent splash = new Intent(this, SplashActivity.class);
			startActivity(splash);
		} else {
			setContentView(R.layout.main);
			citySpin = (Spinner) findViewById(R.id.City);
			stateSpin = (Spinner) findViewById(R.id.State);
			String[] from = new String[] { "stateName" };
			int[] to = new int[] { android.R.id.text1 };
			SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_spinner_item, stateCursor, from, to);
			mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			stateSpin.setAdapter(mAdapter);

			stateSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					Cursor c1 = (Cursor) parent.getItemAtPosition(pos);
					String stateName = c1.getString(c1
							.getColumnIndexOrThrow("stateName"));
					Log.d(TAG, "State Name: " + stateName);
					// populate City Spinner Here
					db.open();
					cityCursor = db.getAllCities(stateName);
					Log.d(TAG, "CITY: " + cityCursor.getCount());
					String[] from = new String[] { "city" };
					int[] to = new int[] { android.R.id.text1 };
					SimpleCursorAdapter m2Adapter = new SimpleCursorAdapter(
							getApplicationContext(),
							android.R.layout.simple_spinner_item, cityCursor, from, to);
					m2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					citySpin.setAdapter(m2Adapter);
					db.close();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		}
		db.close();

	}

}