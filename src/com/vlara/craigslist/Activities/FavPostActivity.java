package com.vlara.craigslist.Activities;

import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.db.DBAdapter;

public class FavPostActivity extends SherlockActivity {
	public final static String TAG = "CraigsApp";
	public TextView heading;
	public TextView body;
	public TextView account;
	public TextView externalURL;

	public Posting post;
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
		externalURL = (TextView) findViewById(R.id.externalURL);

		if (extras != null) {
			postID = extras.getInt("postID");
			Log.d(TAG, "POSTID: " + postID);
		}
		db.open();
		Cursor c = db.getFav(postID);

		if (c.moveToFirst()) {
			Log.d(TAG, "Cursor Count: " + c.getCount());
			post = cursorToPost(c);
			Log.d(TAG, post.toString());

			heading.setText(post.getHeading());
			body.setText(post.getBody());
			externalURL.setText(post.getExternalURL());
			if (post.getAccountName().length() > 1)
				account.setText(post.getAccountName());
			else
				account.setVisibility(View.GONE);
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.favpostmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.deletePost:
			Log.d(TAG, "Pressed Delete Button");
			deleteFav();
			finish();
			return true;
		case R.id.FavShareButton:
			sharePost();
			return true;
		case R.id.browseFavPost:
			browsePost();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void browsePost() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(externalURL.getText().toString()));
		startActivity(i);
	}

	private void sharePost() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = post.getHeading() + "\n" + post.getAccountName()
				+ "\n" + post.getBody() + "\n" + post.getExternalURL();
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CraigsList Post Sent Via CraigslistBrowser");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share Via"));
	}

	public void deleteFav() {
		db.open();
		db.deleteFav(postID);
		db.close();
	}

	public Posting cursorToPost(Cursor c) {
		Posting p = new Posting();
		postID = Integer.parseInt(c.getString(c.getColumnIndexOrThrow("_id")));
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
		p.setTimestamp(new Date(c.getString(c
				.getColumnIndexOrThrow("timestamp"))));
		p.setIndexed(new Date(c.getString(c.getColumnIndexOrThrow("indexed"))));

		return p;

	}

}
