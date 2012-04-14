package com.vlara.craigslist.net;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.vlara.craigslist.Activities.imageActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageAsyncTask extends AsyncTask<String,Void,Map<String,Bitmap> >{
	public final static String TAG = "CraigsApp";
	public imageActivity activity;
	
	public ImageAsyncTask(imageActivity a){
		activity = a;
	}
	@Override
	protected Map<String,Bitmap> doInBackground(String... params) {
		Log.d(TAG,"Images");
		Map<String,Bitmap> imageMap = new HashMap<String,Bitmap>();
		for (String s: params){
			
			try {
				String url = s.replace("/thumb", "");
				URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				/* Buffered is always good for a performance plus. */
				BufferedInputStream bis = new BufferedInputStream(is);
				/* Decode url-data to a bitmap. */
				Bitmap bm = BitmapFactory.decodeStream(bis);
				imageMap.put(s, bm);
				bis.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/* Apply the Bitmap to the ImageView that will be returned. */
			//i.setImageBitmap(bm);
		}
		return imageMap;
	}
	
	@Override
	protected void onPostExecute(Map<String, Bitmap> m) {
		Log.d(TAG, "Map size: " + m.size());
		activity.imageMap = m;
		activity.displayImages();
	}

}
