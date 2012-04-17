package com.vlara.craigslist.Activities;

import java.util.Date;
import java.util.Map;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.net.ImageAsyncTask;

public class imageActivity extends SherlockFragment {
	public final static String TAG = "CraigsApp";
	public TextView heading;
	public TextView body;
	public TextView account;
	public TextView externalURL;
	public TextView tv;
	public Posting post;
	public int postID, postType;
	public String[] images;
	public Map<String, Bitmap> imageMap;
	public static Context ctx;
	public Gallery gallery;
	Dialog dialog;

	static imageActivity newInstance(String[] imgs) {
		Log.d("ASASASASAS", "CONSTRUCTOR");
		imageActivity a = new imageActivity();
		Bundle args = new Bundle();
		args.putStringArray("images", imgs);
		a.setArguments(args);
		return a;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		images = (getArguments() != null ? getArguments().getStringArray(
				"images") : null);
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflator.inflate(R.layout.images, container, false);
		gallery = ((Gallery) v.findViewById(R.id.gallery));
		tv = (TextView) v.findViewById(R.id.noImages);
		tv.setText("Loading Images");
		// images = null;
		if (images.length > 0) {
			Log.d(TAG, "IMAGES LENGTH: " + images.length);
			for (String s : images) {
				Log.d(TAG, "Image URL: " + s);
			}
		} else {
			TextView tv = (TextView) v.findViewById(R.id.noImages);
			tv.setText("No Images");
		}

		ctx = getActivity().getApplicationContext();
		ImageAsyncTask iat = new ImageAsyncTask(this);
		iat.execute(images);
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

	public class ImageAdapter extends BaseAdapter {
		/** The parent context */
		private Context myContext;

		/** Simple Constructor saving the 'parent' context. */
		public ImageAdapter(Context c) {
			this.myContext = c;
		}

		/** Returns the amount of images we have defined. */
		public int getCount() {
			return imageMap.size();
		}

		/* Use the array-Positions as unique IDs */
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * Returns a new ImageView to be displayed, depending on the position
		 * passed.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(this.myContext);
			/* Apply the Bitmap to the ImageView that will be returned. */
			// if (images.length > 1)
			// i.setImageBitmap(imageMap.get(images[position +
			// 1].replace("/thumb", "")));
			// else
			i.setImageBitmap(imageMap.get(images[position]));
			/* Image should be scaled as width/height are set. */
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			/* Set the Width/Height of the ImageView. */
			i.setLayoutParams(new Gallery.LayoutParams(300, 300));
			return i;
		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}

	public void displayImages() {
		// TODO Auto-generated method stub
		for (Map.Entry<String, Bitmap> entry : imageMap.entrySet()) {
			Log.d(TAG, "Value: " + entry.getKey());
		}
		gallery.setAdapter(new ImageAdapter(getActivity()
				.getApplicationContext()));
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Log.d(TAG, "POS: " + position + " id: " + id);
				loadPhoto(position);
			}
		});
	}

	public void loadPhoto(int pos) {
		dialog = new Dialog(this.getActivity());
		dialog.setContentView(R.layout.fullimage_dialog);
		dialog.setTitle("Post Image");
		dialog.setCancelable(true);

		ImageView iv = (ImageView) dialog.findViewById(R.id.fullimage);
		iv.setImageBitmap(imageMap.get(images[pos]));
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void showText() {
		tv.setText("No Images");
	}

	public void clearText() {
		tv.setText("");
	}

}