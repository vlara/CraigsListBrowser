package com.vlara.craigslist;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.vlara.craigslist.db.DBAdapter;

public class PostActivity extends SherlockActivity {
	public final static String TAG = "CraigsApp";
	public TextView heading;
	public TextView body;
	public TextView account;
	public int postID;
	public static DBAdapter db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post);
		Bundle extras = getIntent().getExtras();
		db = new DBAdapter(this);
		heading = (TextView) findViewById(R.id.postHeading);
		body = (TextView) findViewById(R.id.postBody);
		account = (TextView) findViewById(R.id.postAccount);
		
		if (extras != null) {
			postID = extras.getInt("postID");
			Log.d(TAG, "POSTID: " + postID);
		}
		db.open();
		Cursor c = db.getPost(postID);
		if (c.moveToFirst()) {
			Log.d(TAG, "Cursor Count: " + c.getCount());
			heading.setText(c.getString(c.getColumnIndexOrThrow("heading")));
			body.setText(c.getString(c.getColumnIndexOrThrow("body")));
			account.setText(c.getString(c.getColumnIndexOrThrow("accountName")));
		}
		db.close();
		// populateFields();
	}

	public void populateFields() {

	}
}
