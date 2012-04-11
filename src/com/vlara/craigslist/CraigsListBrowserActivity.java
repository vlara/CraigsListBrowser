package com.vlara.craigslist;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.vlara.craigslist.db.DBAdapter;
import com.vlara.craigslist.net.CategoryAsyncTask;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
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
	public static SharedPreferences preferences;
	public static Editor edit;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ActionBar setup
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setTitle("Search");

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		edit = preferences.edit();
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
		db.open();
		db.clearPosts();
		db.close();
		// reset posts db
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

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.searchmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.SearchButton:
			search();
			return true;
		case R.id.FavsButton:
			favs();
			return true;
		case android.R.id.home:
			Log.d(TAG, "TOUCHED HOME");
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void search() {
		Log.d(TAG, "STATE CODE " + preferences.getString("stateCode", ""));
		Log.d(TAG, "Location CODE " + preferences.getString("locationCode", ""));
		Log.d(TAG, "Group CODE " + preferences.getString("groupCode", ""));
		Log.d(TAG, "Category CODE " + preferences.getString("categoryCode", ""));
		EditText searchTerm = (EditText) findViewById(R.id.Search);

		Intent i = new Intent(getApplicationContext(), listActivity.class);
		i.putExtra("searchTerm", searchTerm.getText().toString());
		startActivityForResult(i, Location);
	}

	public void favs() {
		Log.d(TAG, "Clicked Favs");
	}

	private void populateSpinners() {
		// setup button first
		db.open();
		db.clearPosts();
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
		mAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
			String stateCode = c1.getString(c1
					.getColumnIndexOrThrow("stateCode"));
			Log.d(TAG, "State Code: " + stateCode);
			// save state code
			// populate City Spinner Here
			edit.putString("stateCode", stateCode);
			edit.putString("stateName", stateName);
			edit.commit();
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
			citySpin.setOnItemSelectedListener(cityClick);
			db.close();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	private OnItemSelectedListener cityClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Cursor c1 = (Cursor) parent.getItemAtPosition(pos);
			String cityName = c1.getString(c1.getColumnIndexOrThrow("city"));
			String locationCode = c1
					.getString(c1.getColumnIndexOrThrow("code"));
			Log.d(TAG, "CITY: " + cityName + " Code: " + locationCode);
			edit.putString("locationCode", locationCode);
			edit.putString("cityName", cityName);
			edit.commit();
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
			String catGroup = c1
					.getString(c1.getColumnIndexOrThrow("catgroup"));
			String catCode = c1.getString(c1.getColumnIndexOrThrow("code"));
			Log.d(TAG, "catCode: " + catCode);
			edit.putString("categoryCode", catCode);
			edit.putString("categoryGroup", catGroup);
			edit.commit();
			// populate City Spinner Here
			db.open();
			categoryCursor = db.getAllCategorys(catGroup);
			Log.d(TAG, "Sub-CATEGORY: " + categoryCursor.getCount());
			String[] from = new String[] { "category" };
			int[] to = new int[] { android.R.id.text1 };
			SimpleCursorAdapter m2Adapter = new SimpleCursorAdapter(
					getApplicationContext(),
					android.R.layout.simple_spinner_item, categoryCursor, from,
					to);
			m2Adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			categorySpin.setAdapter(m2Adapter);
			categorySpin.setOnItemSelectedListener(categoryClick);
			db.close();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	private OnItemSelectedListener categoryClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Cursor c1 = (Cursor) parent.getItemAtPosition(pos);
			String categoryName = c1.getString(c1
					.getColumnIndexOrThrow("category"));
			String categoryCode = c1
					.getString(c1.getColumnIndexOrThrow("code"));
			Log.d(TAG, "CATEGORY: " + categoryName + " Code: " + categoryCode);
			edit.putString("categoryName", categoryName);
			edit.putString("categoryCode", categoryCode);
			edit.commit();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

}