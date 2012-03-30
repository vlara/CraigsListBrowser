package com.vlara.craigslist;

import com.actionbarsherlock.app.SherlockActivity;
import com.vlara.craigslist.db.DBAdapter;
import com.vlara.craigslist.net.CategoryAsyncTask;
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
	public final static int Location = 1;
	public static DBAdapter db;
	public static Context ctx;
	public static Dialog mSplashDialog;
	public static Spinner citySpin;
	public static Spinner stateSpin;
	public static Spinner groupSpin;
	public static Spinner categorySpin;
	public static Cursor stateCursor, cityCursor, groupCursor, categoryCursor;

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
			// Populate Database
			Log.d(TAG, "creating async task");
			// LocationAsyncTask lat = new LocationAsyncTask();
			CategoryAsyncTask cat = new CategoryAsyncTask();
			cat.execute("");
			// lat.execute("");
			Intent splash = new Intent(this, SplashActivity.class);
			startActivityForResult(splash, Location);
		} else {
			setContentView(R.layout.main);
			populateSpinners();
		}
		// setContentView(R.layout.main);
		db.close();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent i) {
		Log.d(TAG, "in on Activity Result");
		if (requestCode == Location) {
			if (resultCode == 1) {
				setContentView(R.layout.main);
				// Returned From Location Succesfully
				// populateSpinners
				Log.d(TAG, "Activity Result Location");
				populateSpinners();
			}
		}
	}

	private void populateSpinners() {
		db.open();
		groupCursor = db.getAllGroups();
		stateCursor = db.getAllStates();
		Log.d(TAG, "POP COUNT: " + stateCursor.getCount());
		citySpin = (Spinner) findViewById(R.id.City);
		stateSpin = (Spinner) findViewById(R.id.State);
		groupSpin = (Spinner) findViewById(R.id.Group);
		categorySpin = (Spinner) findViewById(R.id.Category);

		String[] from = new String[] { "stateName" };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, stateCursor, from, to);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stateSpin.setAdapter(mAdapter);
		stateSpin.setOnItemSelectedListener(stateClick);

		String[] from2 = new String[] { "catgroup" };
		int[] to2 = new int[] { android.R.id.text1 };
		SimpleCursorAdapter mAdapter2 = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, groupCursor, from2, to2);
		mAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpin.setAdapter(mAdapter2);
		groupSpin.setOnItemSelectedListener(groupClick);
	}

	private OnItemSelectedListener stateClick = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
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
			m2Adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			citySpin.setAdapter(m2Adapter);
			db.close();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};
	
	private OnItemSelectedListener groupClick = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Cursor c1 = (Cursor) parent.getItemAtPosition(pos);
			String catGroup = c1.getString(c1
					.getColumnIndexOrThrow("catgroup"));
			Log.d(TAG, "catgroup: " + catGroup);
			// populate City Spinner Here
			db.open();
			categoryCursor = db.getAllCategorys(catGroup);
			Log.d(TAG, "Sub-CATEGORY: " + categoryCursor.getCount());
			String[] from = new String[] { "category" };
			int[] to = new int[] { android.R.id.text1 };
			SimpleCursorAdapter m2Adapter = new SimpleCursorAdapter(
					getApplicationContext(),
					android.R.layout.simple_spinner_item, categoryCursor, from, to);
			m2Adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			categorySpin.setAdapter(m2Adapter);
			db.close();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

}