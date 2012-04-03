package com.vlara.craigslist.net;

import java.io.IOException;
import java.util.List;

import com.threetaps.model.Category;
import com.vlara.craigslist.CraigsListBrowserActivity;
import com.vlara.craigslist.ReferenceClientTaps;
import com.vlara.craigslist.db.DBAdapter;

import android.os.AsyncTask;
import android.util.Log;

public class CategoryAsyncTask extends AsyncTask<String,Void,String>{
	public final static String TAG = "CraigsApp";
	public static DBAdapter db;
	@Override
	protected String doInBackground(String... params) {
		Log.d(TAG,"Getting Categories");
		ReferenceClientTaps cct = new ReferenceClientTaps();
		db = new DBAdapter(CraigsListBrowserActivity.ctx);
		db.open();
		try{
			List<Category> cats = cct.getCategories();
			Log.d(TAG, "Categories size: " + cats.size());
			db.beginTransaction();
			for (int i=0; i<cats.size();i ++){
				Category cat = cats.get(i);
				if (cat.getGroup() != null){
					Log.d(TAG, "Category: " + cat.getCategory() + " Group: " + cat.getGroup());
					db.insertCategory(cat.getGroup(), cat.getCategory(), cat.getCode());
				}
			}
			db.setTransactionSuccessful();
		}catch (IOException e) {
			// TODO Auto-generated catch block
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
		Log.d(TAG, "Finished adding Categories to db");
		LocationAsyncTask lat = new LocationAsyncTask();
		lat.execute("");
		//((Activity) SplashActivity.ctx).setResult(1);
		//((Activity) SplashActivity.ctx).finish();
	}

}
