package com.vlara.craigslist;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockListActivity;
import com.threetaps.model.Posting;
import com.vlara.craigslist.net.PostAsyncTask;

public class listActivity extends SherlockListActivity {
	public final static String TAG = "CraigsApp";
	public static SharedPreferences preferences;
	public static Context ctx;
	public static List<Posting> posts;
	public static PostListAdapter psa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		ctx = this;
		setContentView(R.layout.list);
		PostAsyncTask pat = new PostAsyncTask();
		String location = preferences.getString("locationCode", "");
		Log.d(TAG, "Group CODE " + preferences.getString("groupCode", ""));
		String category = preferences.getString("categoryCode", "");
		String searchTerm = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			searchTerm = extras.getString("searchTerm");
		}
		pat.execute(new String[] { location, category, searchTerm });
		psa = new PostListAdapter(this, posts, this);
		getListView().setAdapter(psa);
	}
	
	public void test(){
		
	}
}
