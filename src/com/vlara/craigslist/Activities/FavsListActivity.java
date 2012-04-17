package com.vlara.craigslist.Activities;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListActivity;
import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.db.DBAdapter;

public class FavsListActivity extends SherlockListActivity {
	public final static String TAG = "CraigsApp";
	public static List<Posting> posts;
	public static DBAdapter db;
	public static SimpleCursorAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Favorite List");
		
		setContentView(R.layout.list);
		db = new DBAdapter(this);
		populateList();	
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
			Intent i = new Intent(getApplicationContext(), postTabs.class);
			i.putExtra("postID", PostId);
			i.putExtra("type", 1);
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
