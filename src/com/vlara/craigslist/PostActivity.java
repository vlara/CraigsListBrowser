package com.vlara.craigslist;

import java.util.Date;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.threetaps.model.Posting;
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

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Post");

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
			Posting post = cursorToPost(c);
			Log.d(TAG, post.toString());
			
			heading.setText(post.getHeading());
			body.setText(post.getBody());
			account.setText(post.getAccountName());
		}
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

	public Posting cursorToPost(Cursor c) {
		Posting p = new Posting();
		p.setAccountID(c.getString(c.getColumnIndexOrThrow("accountID")));
		p.setAccountName(c.getString(c.getColumnIndexOrThrow("accountName")));
		p.setBody(c.getString(c.getColumnIndexOrThrow("body")));
		p.setCategory(c.getString(c.getColumnIndexOrThrow("category")));
		p.setCurrency(c.getString(c.getColumnIndexOrThrow("currency")));
		p.setPostKey(c.getString(c.getColumnIndexOrThrow("postKey")));
		p.setLocation(c.getString(c.getColumnIndexOrThrow("location")));
		p.setSource(c.getString(c.getColumnIndexOrThrow("source")));
		p.setHeading(c.getString(c.getColumnIndexOrThrow("heading")));
		p.setLatitude(Float.parseFloat(c.getString(c
				.getColumnIndexOrThrow("latitude"))));
		p.setLongitude(Float.parseFloat(c.getString(c
				.getColumnIndexOrThrow("longitude"))));
		p.setPrice(Float.parseFloat(c.getString(c
				.getColumnIndexOrThrow("price"))));
		p.setStatus(c.getString(c.getColumnIndexOrThrow("status")));
		p.setExternalID(c.getString(c.getColumnIndexOrThrow("externalID")));
		p.setExternalURL(c.getString(c.getColumnIndexOrThrow("externalURL")));
		p.setTimestamp(new Date(c.getString(c.getColumnIndexOrThrow("timestamp"))));
		p.setIndexed(new Date(c.getString(c.getColumnIndexOrThrow("indexed"))));

		return p;

	}

}
