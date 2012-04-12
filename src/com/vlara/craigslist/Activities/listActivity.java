package com.vlara.craigslist.Activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.db.DBAdapter;
import com.vlara.craigslist.net.PostAsyncTask;

public class listActivity extends SherlockListActivity {
	public final static String TAG = "CraigsApp";
	public static SharedPreferences preferences;
	public static Context ctx;
	public static List<Posting> posts;
	public static DBAdapter db;
	public static SimpleCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Post List");
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		ctx = this;
		setContentView(R.layout.list);
		PostAsyncTask pat = new PostAsyncTask();
		Log.d(TAG, "STATE CODE " + preferences.getString("stateCode", ""));
		Log.d(TAG, "Location CODE " + preferences.getString("locationCode", ""));
		Log.d(TAG, "Group CODE " + preferences.getString("groupCode", ""));
		Log.d(TAG, "Category CODE " + preferences.getString("categoryCode", ""));
		
		String location = preferences.getString("locationCode", "");
		String category = preferences.getString("categoryCode", "");
		String searchTerm = "";
		Bundle extras = getIntent().getExtras();
		db = new DBAdapter(this);
		db.open();
		if (extras != null) {
			searchTerm = extras.getString("searchTerm");
		}
		Cursor postCursor = db.getAllPosts();
		
		Log.d(TAG, "POST COUNT: " + postCursor.getCount());
		if (postCursor.getCount() <= 0) {
			db.close();
			pat.execute(new String[] { location, category, searchTerm });
		} else
			populateList();	
		db.close();
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	

	public void populateList() {
		Log.d(TAG, "In Populate List");
		db.open();
		Cursor postCursor = db.getAllPosts();
		String[] columns = new String[] { "postKey", "heading",
				"timestamp" };
		int[] to = new int[] { R.id.postkey, R.id.posttitle, R.id.postday };

		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.post_list_item,
				postCursor, columns, to);

		Log.d(TAG, "CLOSING THE DB");
		getListView().setAdapter(mAdapter);
		getListView().setOnItemClickListener(postClick2);
		db.close();
	}
	
	private OnItemClickListener postClick2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long arg3) {
			Cursor c1 = (Cursor) parent.getItemAtPosition(pos);
			int PostId = c1.getInt(c1.getColumnIndex("_id"));
			Log.d(TAG, "PostID: " + PostId);
			Intent i = new Intent(getApplicationContext(), PostActivity.class);
			i.putExtra("postID", PostId);
			startActivity(i);
		}
		
	};
	
}
