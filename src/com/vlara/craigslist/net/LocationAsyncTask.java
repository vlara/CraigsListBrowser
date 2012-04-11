package com.vlara.craigslist.net;

import java.io.IOException;
import java.util.List;

import com.threetaps.model.Location;
import com.vlara.craigslist.CraigsListBrowserActivity;
import com.vlara.craigslist.ReferenceClientTaps;
import com.vlara.craigslist.SplashActivity;
import com.vlara.craigslist.db.DBAdapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class LocationAsyncTask extends AsyncTask<String, Void, String> {
	public final static String TAG = "CraigsApp";
	public static DBAdapter db;

	@Override
	protected String doInBackground(String... params) {

		ReferenceClientTaps rct = new ReferenceClientTaps();
		db = new DBAdapter(CraigsListBrowserActivity.ctx);
		db.open();
		try {
			List<Location> locs = rct.getLocations();
			Log.d(TAG, "location size:" + locs.size());
			db.beginTransaction();
			for (int i = 0; i < locs.size(); i++) {
				Location loc = locs.get(i);
				
				if ((loc.getCountry() != null
						&& loc.getCountry().equalsIgnoreCase("United States")
						&& loc.getStateName() != null && loc.getCity() != null)) {

					db.insert(loc.getCode(), loc.getCity(), loc.getCityRank(),
							loc.getCountry(), loc.getCountryRank(),
							loc.getStateCode(), loc.getStateName(),
							loc.getHidden(), loc.getLatitude(),
							loc.getLongitude());
				}
			}
			db.setTransactionSuccessful();
		} catch (IOException e) {
			db.close();
			e.printStackTrace();
		} catch (NullPointerException e) {
			db.close();
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		db.close();
		return "";
	}

	@Override
	protected void onPostExecute(String s) {
		Log.d(TAG, "Finished adding everythign to db");
		((Activity) SplashActivity.ctx).setResult(1);
		((Activity) SplashActivity.ctx).finish();
	}

}
