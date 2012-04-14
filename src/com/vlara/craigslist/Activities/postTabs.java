package com.vlara.craigslist.Activities;

import java.util.Date;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.threetaps.model.Posting;
import com.vlara.craigslist.R;
import com.vlara.craigslist.db.DBAdapter;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments. It uses a trick (see the code below) to allow the
 * tabs to switch between fragments instead of simple views.
 */
public class postTabs extends SherlockFragmentActivity {
	TabHost mTabHost;
	TabManager mTabManager;
	public Posting post;
	public int postID;
	public static DBAdapter db;
	public final static String TAG = "CraigsApp";
	public String[] images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);
		db = new DBAdapter(this);
		images = null;
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Post");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			postID = extras.getInt("postID");
		}

		db.open();
		Cursor c = db.getPost(postID);

		if (c.moveToFirst()) {
			Log.d(TAG, "Cursor Count: " + c.getCount());
			post = cursorToPost(c);
			Log.d(TAG, post.toString());
		}
		db.close();
		Bundle imgsExtra = new Bundle();
		imgsExtra.putStringArray("images", images);
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
		mTabManager.addTab(mTabHost.newTabSpec("Post").setIndicator("Post"),
				PostActivity.class, extras);
		mTabManager.addTab(mTabHost.newTabSpec("image").setIndicator("Images"),
				imageActivity.class, imgsExtra);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
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
		i.setData(Uri.parse(post.getExternalURL()));
		startActivity(i);
	}

	private void sharePost() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = post.getHeading() + "\n" + post.getAccountName()
				+ "\n" + post.getBody() + "\n" + post.getExternalURL();
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"CraigsList Post Sent Via CraigslistBrowser");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share Via"));
	}

	public void addFav() {
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
		p.setTimestamp(new Date(c.getString(c
				.getColumnIndexOrThrow("timestamp"))));
		p.setIndexed(new Date(c.getString(c.getColumnIndexOrThrow("indexed"))));
		images = c.getString(c.getColumnIndexOrThrow("images")).split(",");
		return p;

	}

	/**
	 * This is a helper class that implements a generic mechanism for
	 * associating fragments with the tabs in a tab host. It relies on a trick.
	 * Normally a tab host has a simple API for supplying a View or Intent that
	 * each tab will show. This is not sufficient for switching between
	 * fragments. So instead we make the content part of the tab host 0dp high
	 * (it is not shown) and the TabManager supplies its own dummy view to show
	 * as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct fragment shown in a separate content area whenever
	 * the selected tab changes.
	 */
	public static class TabManager implements TabHost.OnTabChangeListener {
		private final FragmentActivity mActivity;
		private final TabHost mTabHost;
		private final int mContainerId;
		private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
		TabInfo mLastTab;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(FragmentActivity activity, TabHost tabHost,
				int containerId) {
			mActivity = activity;
			mTabHost = tabHost;
			mContainerId = containerId;
			mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			info.fragment = mActivity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}

			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		@Override
		public void onTabChanged(String tabId) {
			TabInfo newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mActivity,
								newTab.clss.getName(), newTab.args);
						ft.add(mContainerId, newTab.fragment, newTab.tag);
					} else {
						ft.attach(newTab.fragment);
					}
				}

				mLastTab = newTab;
				ft.commit();
				mActivity.getSupportFragmentManager()
						.executePendingTransactions();
			}
		}
	}
}
