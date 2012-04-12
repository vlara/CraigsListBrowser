package com.vlara.craigslist;

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
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListActivity;
import com.threetaps.model.Posting;
import com.vlara.craigslist.db.DBAdapter;

public class FavsListActivity extends SherlockListActivity {
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
		
		Bundle extras = getIntent().getExtras();
		db = new DBAdapter(this);
		db.open();
		if (extras != null) {
			//searchTerm = extras.getString("searchTerm");
		}
		populateList();	
		db.close();
	}
	
	public void populateList() {
		Log.d(TAG, "In Populate List");
		db.open();
		Cursor postCursor = db.getAllFavs();
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
			Intent i = new Intent(getApplicationContext(), FavPostActivity.class);
			i.putExtra("postID", PostId);
			startActivity(i);
		}
		
	};
	
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
	
	@Override
	public void onResume(){
		super.onResume();
		populateList();
	}
	
}
