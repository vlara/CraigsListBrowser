package com.vlara.craigslist.net;

import java.io.IOException;
import java.util.List;

import com.threetaps.model.Posting;
import com.vlara.craigslist.SearchClientTaps;
import com.vlara.craigslist.listActivity;
import com.vlara.craigslist.db.DBAdapter;

import android.os.AsyncTask;
import android.util.Log;

public class PostAsyncTask extends AsyncTask<String, Void, String> {
	public final static String TAG = "CraigsApp";
	public static DBAdapter db;

	@Override
	protected String doInBackground(String... params) {
		Log.d(TAG, "doInBackground Post");
		List<Posting> posts = null;
		SearchClientTaps sct = new SearchClientTaps();
		db = new DBAdapter(listActivity.ctx);
		db.open();
		try {
			posts = sct.search(params[2], params[0], params[1]);
			Log.d(TAG, "Posts size: " + posts.size());
			db.beginTransaction();
			for (int i = 0; i < posts.size(); i++) {
				Posting post = posts.get(i);
				if (post.getHeading() != null) {
					db.insert(post);
				}
			}
			db.setTransactionSuccessful();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			db.close();
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		db.close();
		return "";
	}

	@Override
	protected void onPostExecute(String posts) {
		Log.d(TAG, "OnPostExecute");
//		db.open();
//		listActivity.postCursor = db.getAllPosts();
//		String[] columns = new String[] { "postKey", "heading", "timestamp" };
//		int[] to = new int[] { R.id.postkey, R.id.posttitle, R.id.postday };
//		listActivity.mAdapter = new SimpleCursorAdapter(listActivity.ctx,
//				R.layout.post_list_item, listActivity.postCursor, columns, to);
		//listActivity.mAdapter.notifyDataSetChanged();
		((listActivity) listActivity.ctx).populateList();
		//db.close();
	}

}
