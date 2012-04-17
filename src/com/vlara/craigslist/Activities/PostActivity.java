package com.vlara.craigslist.Activities;

import java.util.Date;

import com.actionbarsherlock.app.SherlockFragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.db.DBAdapter;

public class PostActivity extends SherlockFragment {
	public final static String TAG = "CraigsApp";
	public TextView heading;
	public TextView body;
	public TextView account;
	public TextView externalURL;
	public Posting post;
	public int postID, postType;
	public static DBAdapter db;
	public String[] images;

	static PostActivity newInstance(int _postId, int _type) {
		Log.d("ASASASASAS", "CONSTRUCTOR");
		PostActivity a = new PostActivity();
		Bundle args = new Bundle();
		args.putInt("postID", _postId);
		args.putInt("type", _type);
		a.setArguments(args);
		return a;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		postID = getArguments() != null ? getArguments().getInt("postID") : 1;
		postType = getArguments() != null ? getArguments().getInt("type") : 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflator.inflate(R.layout.post, container, false);
		images = null;
		db = new DBAdapter(v.getContext());
		heading = (TextView) v.findViewById(R.id.postHeading);
		body = (TextView) v.findViewById(R.id.postBody);
		account = (TextView) v.findViewById(R.id.postAccount);
		externalURL = (TextView) v.findViewById(R.id.externalURL);

		db.open();
		Cursor c = null;
		if (postType == 1){
			c = db.getFav(postID);
		}else{
			c = db.getPost(postID);
		}

		if (c.moveToFirst()) {
			Log.d(TAG, "POST Cursor Count: " + c.getCount());
			post = cursorToPost(c);
			Log.d(TAG, "POST: " +post.toString());

			heading.setText(post.getHeading());
			body.setText(post.getBody());
			externalURL.setText(post.getExternalURL());
			if (post.getAccountName().length() > 1)
				account.setText(post.getAccountName());
			else
				account.setVisibility(View.GONE);
		}
		db.close();
		return v;

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
		p.setTimestamp(new Date(c.getString(c
				.getColumnIndexOrThrow("timestamp"))));
		p.setIndexed(new Date(c.getString(c.getColumnIndexOrThrow("indexed"))));
		images = c.getString(c.getColumnIndexOrThrow("images")).split(",");
		return p;
	}
}