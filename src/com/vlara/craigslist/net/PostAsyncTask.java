package com.vlara.craigslist.net;

import java.io.IOException;
import java.util.List;

import com.threetaps.model.Posting;
import com.vlara.craigslist.SearchClientTaps;
import com.vlara.craigslist.listActivity;

import android.os.AsyncTask;
import android.util.Log;

public class PostAsyncTask extends AsyncTask<String, Void, List<Posting>> {
	public final static String TAG = "CraigsApp";

	@Override
	protected List<Posting> doInBackground(String... params) {
		Log.d(TAG,"doInBackground Post");
		List<Posting> posts = null;
		SearchClientTaps sct = new SearchClientTaps();
		try {
			posts = sct.search(params[2], params[0], params[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return posts;
	}
	
	@Override
	protected void onPostExecute( List<Posting> posts){
		Log.d(TAG,"OnPostExecute size: " + posts.size());
		listActivity.posts = posts;
		listActivity.psa.notifyDataSetChanged();
	}


}
