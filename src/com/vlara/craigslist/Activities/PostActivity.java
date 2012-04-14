package com.vlara.craigslist.Activities;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.db.DBAdapter;

public class PostActivity extends SherlockActivity {
	public final static String TAG = "CraigsApp";
	public TextView heading;
	public TextView body;
	public TextView account;
	public TextView externalURL;
	public Posting post;
	public int postID;
	public static DBAdapter db;
	public String[] images;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Post");
		images = null;
		Bundle extras = getIntent().getExtras();
		db = new DBAdapter(this);
		heading = (TextView) findViewById(R.id.postHeading);
		//body = (TextView) findViewById(R.id.postBody);
		//account = (TextView) findViewById(R.id.postAccount);
		//externalURL = (TextView) findViewById(R.id.externalURL);

		if (extras != null) {
			postID = extras.getInt("postID");
			Log.d(TAG, "POSTID: " + postID);
		}
		db.open();
		Cursor c = db.getPost(postID);
		
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
			
			//((Gallery) findViewById(R.id.gallery)).setAdapter(new ImageAdapter(this));
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.favsmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId,
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.Favorite:
			Log.d(TAG, "Pressed Favorite Button");
			addFav();
			item.setIcon(R.drawable.not_important);
			return true;
		case R.id.ShareButton:
			sharePost();
			return true;
		case R.id.browsePost:
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

	public void addFav(){
		db.open();
		db.insertFav(post);
		db.close();
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
		images = c.getString(c.getColumnIndexOrThrow("images")).split(",");
		return p;

	}

    public class ImageAdapter extends BaseAdapter {
    	/** The parent context */
        private Context myContext;

		
        /** URL-Strings to some remote images. */
        private String[] myRemoteImages = {
        		"http://www.anddev.org/images/tiny_tutheaders/weather_forecast.png",
        		"http://www.anddev.org/images/tiny_tutheaders/cellidtogeo.png",
        		"http://www.anddev.org/images/tiny_tutheaders/droiddraw.png"
        };
        
        /** Simple Constructor saving the 'parent' context. */
        public ImageAdapter(Context c) { this.myContext = c; }

        /** Returns the amount of images we have defined. */
        public int getCount() { return ((PostActivity) myContext).images.length; }

        /* Use the array-Positions as unique IDs */
        public Object getItem(int position) { return position; }
        public long getItemId(int position) { return position; }

        /** Returns a new ImageView to 
         * be displayed, depending on 
         * the position passed. */
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(this.myContext);
            String[] imgs = ((PostActivity) myContext).images;
            try {
				/* Open a new URL and get the InputStream to load data from it. */
				URL aURL = new URL(imgs[position]);
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				/* Buffered is always good for a performance plus. */
				BufferedInputStream bis = new BufferedInputStream(is);
				/* Decode url-data to a bitmap. */
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
				/* Apply the Bitmap to the ImageView that will be returned. */
				i.setImageBitmap(bm);
			} catch (IOException e) {
				i.setImageResource(R.drawable.warning);
				Log.e("DEBUGTAG", "Remtoe Image Exception", e);
			}
            
            /* Image should be scaled as width/height are set. */
            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
            /* Set the Width/Height of the ImageView. */
            i.setLayoutParams(new Gallery.LayoutParams(150, 150));
            return i;
        }

        /** Returns the size (0.0f to 1.0f) of the views
         * depending on the 'offset' to the center. */
        public float getScale(boolean focused, int offset) {
        	/* Formula: 1 / (2 ^ offset) */
            return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
        }
    }
	
}
