package com.vlara.craigslist.db;

import com.threetaps.model.Posting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	private static final String DATABASE_NAME = "Craigslist_3taps";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE_NAME = "Locations";
	private static final String DATABASE_TABLE_POSTS_NAME = "Posts";
	private static final String DATABASE_TABLE_FAVS_NAME = "Favs";
	public static final String ID = "_id";
	public static final String CODE = "code";
	public static final String COUNTRYRANK = "countryRank";
	public static final String COUNTRY = "country";
	public static final String CITYRANK = "cityRank";
	public static final String CITY = "city";
	public static final String STATECODE = "stateCode";
	public static final String STATENAME = "stateName";
	public static final String HIDDEN = "hidden";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	private static final String DATABASE_TABLE_CATEGORY_NAME = "category";
	public static final String CATGROUP = "catgroup";
	public static final String CATCODE = "code";
	public static final String CATCATEGORY = "category";
	public final static String TAG = "CraigsApp";

	public static final String postKey = "postKey";
	public static final String postLocation = "location";
	public static final String postCategory = "category";
	public static final String postSource = "source";
	public static final String postHeading = "heading";
	public static final String postBody = "body";
	public static final String postLat = "latitude";
	public static final String postLong = "longitude";
	public static final String postPrice = "price";
	public static final String postCurrency = "currency";
	// replace with another db table
	public static final String postStatus = "status";
	public static final String postExternalID = "externalID";
	public static final String postExternalURL = "externalURL";
	public static final String postAccountName = "accountName";
	public static final String postAccountID = "accountID";
	public static final String postTimestamp = "timestamp";
	public static final String postIndexed = "indexed";

	private static final String DATABASE_TABLE_CREATE_CATEGORIES = "CREATE TABLE "
			+ DATABASE_TABLE_CATEGORY_NAME
			+ " ("
			+ ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CATCODE
			+ " TEXT, "
			+ ""
			+ CATGROUP + " TEXT, " + CATCATEGORY + " TEXT);";

	private static final String DATABASE_TABLE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE_NAME + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CODE + " TEXT, " + ""
			+ COUNTRY + " TEXT, " + COUNTRYRANK + " TEXT, " + CITY + " TEXT, "
			+ CITYRANK + " TEXT, " + STATENAME + " TEXT, " + STATECODE
			+ " TEXT, " + HIDDEN + " TEXT, " + LATITUDE + " TEXT, " + LONGITUDE
			+ " TEXT );";

	private static final String DATABASE_TABLE_CREATE_POSTS = "CREATE TABLE "
			+ DATABASE_TABLE_POSTS_NAME + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + postKey + " TEXT, " + ""
			+ postLocation + " TEXT, " + postCategory + " TEXT, " + postSource
			+ " TEXT, " + postHeading + " TEXT, " + postBody + " TEXT, "
			+ postLat + " TEXT, " + postLong + " TEXT, " + postPrice
			+ " TEXT, " + postCurrency + " TEXT, " + postStatus + " TEXT, "
			+ postExternalID + " TEXT, " + postExternalURL + " TEXT, "
			+ postAccountName + " TEXT, " + postAccountID + " TEXT, "
			+ postTimestamp + " TEXT, " + postIndexed + " TEXT );";

	private static final String DATABASE_TABLE_CREATE_FAVS = "CREATE TABLE "
			+ DATABASE_TABLE_FAVS_NAME + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + postKey + " TEXT, " + ""
			+ postLocation + " TEXT, " + postCategory + " TEXT, " + postSource
			+ " TEXT, " + postHeading + " TEXT, " + postBody + " TEXT, "
			+ postLat + " TEXT, " + postLong + " TEXT, " + postPrice
			+ " TEXT, " + postCurrency + " TEXT, " + postStatus + " TEXT, "
			+ postExternalID + " TEXT, " + postExternalURL + " TEXT, "
			+ postAccountName + " TEXT, " + postAccountID + " TEXT, "
			+ postTimestamp + " TEXT, " + postIndexed + " TEXT );";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Creating Database");
			db.execSQL(DATABASE_TABLE_CREATE_CATEGORIES);
			db.execSQL(DATABASE_TABLE_CREATE_POSTS);
			db.execSQL(DATABASE_TABLE_CREATE_FAVS);
			db.execSQL(DATABASE_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS Receipts");
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert(Posting post) {
		ContentValues iv = new ContentValues();
		iv.put(postKey, post.getPostKey());
		iv.put(postLocation, post.getLocation());
		iv.put(postCategory, post.getCategory());
		iv.put(postSource, post.getSource());
		iv.put(postHeading, post.getHeading().trim());
		iv.put(postBody, post.getBody());
		iv.put(postLat, post.getLatitude());
		iv.put(postLong, post.getLongitude());
		iv.put(postPrice, post.getPrice());
		iv.put(postCurrency, post.getCurrency());
		iv.put(postStatus, post.getStatus());
		iv.put(postExternalID, post.getExternalID());
		iv.put(postExternalURL, post.getExternalURL());
		iv.put(postAccountName, post.getAccountName());
		iv.put(postAccountID, post.getAccountID());
		iv.put(postTimestamp,
				post.getTimestamp()
						.toGMTString()
						.substring(
								0,
								post.getTimestamp().toGMTString()
										.indexOf(" ", 9)));
		iv.put(postIndexed, post.getIndexed().toGMTString());
		return db.insert(DATABASE_TABLE_POSTS_NAME, null, iv);
	}

	public long insertFav(Posting post) {
		
		ContentValues iv = new ContentValues();
		iv.put(postKey, post.getPostKey());
		iv.put(postLocation, post.getLocation());
		iv.put(postCategory, post.getCategory());
		iv.put(postSource, post.getSource());
		iv.put(postHeading, post.getHeading().trim());
		iv.put(postBody, post.getBody());
		iv.put(postLat, post.getLatitude());
		iv.put(postLong, post.getLongitude());
		iv.put(postPrice, post.getPrice());
		iv.put(postCurrency, post.getCurrency());
		iv.put(postStatus, post.getStatus());
		iv.put(postExternalID, post.getExternalID());
		iv.put(postExternalURL, post.getExternalURL());
		iv.put(postAccountName, post.getAccountName());
		iv.put(postAccountID, post.getAccountID());
		iv.put(postTimestamp,
				post.getTimestamp()
						.toGMTString()
						.substring(
								0,
								post.getTimestamp().toGMTString()
										.indexOf(" ", 9)));
		iv.put(postIndexed, post.getIndexed().toGMTString());
		return db.insert(DATABASE_TABLE_FAVS_NAME, null, iv);

	}

	public long insert(String Code, String City, int i, String Country, int j,
			String StateCode, String StateName, boolean b, float f, float g) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(CODE, Code);
		initialValues.put(CITY, City);
		initialValues.put(CITYRANK, i);
		initialValues.put(COUNTRY, Country);
		initialValues.put(COUNTRYRANK, j);
		initialValues.put(STATECODE, StateCode);
		initialValues.put(STATENAME, StateName);
		initialValues.put(HIDDEN, b);
		initialValues.put(LATITUDE, f);
		initialValues.put(LONGITUDE, g);
		return db.insert(DATABASE_TABLE_NAME, null, initialValues);
	}

	public long insertCategory(String group, String category, String code) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(CATCODE, code);
		initialValues.put(CATCATEGORY, category);
		initialValues.put(CATGROUP, group);
		return db.insert(DATABASE_TABLE_CATEGORY_NAME, null, initialValues);
	}

	public Cursor getAllPosts() {
		return db.query(DATABASE_TABLE_POSTS_NAME, new String[] { ID, postKey,
				postLocation, postCategory, postSource, postHeading, postBody,
				postLat, postLong, postPrice, postCurrency, postStatus,
				postExternalID, postExternalURL, postAccountName,
				postAccountID, postTimestamp, postIndexed }, null, null, null,
				null, null);
	}

	public Cursor getAllFavs() {
		return db.query(DATABASE_TABLE_FAVS_NAME, new String[] { ID, postKey,
				postLocation, postCategory, postSource, postHeading, postBody,
				postLat, postLong, postPrice, postCurrency, postStatus,
				postExternalID, postExternalURL, postAccountName,
				postAccountID, postTimestamp, postIndexed }, null, null, null,
				null, null);
	}
	
	public Cursor getAllLocationss() {
		return db.query(DATABASE_TABLE_NAME, new String[] { ID, CODE, CITY,
				CITYRANK, COUNTRY, COUNTRYRANK, STATECODE, STATENAME, HIDDEN,
				LATITUDE, LONGITUDE }, null, null, null, null, null);
	}

	public Cursor getAllCities(String stateName) {
		return db.query(DATABASE_TABLE_NAME, new String[] { ID, CITY, CITYRANK,
				CODE }, "stateName = '" + stateName + "'", null, null, null,
				CITY);
	}

	public Cursor getAllStates() {
		return db.query(DATABASE_TABLE_NAME, new String[] { ID, STATECODE,
				STATENAME }, null, null, STATENAME, null, STATENAME);
	}

	public Cursor getAllGroups() {
		return db.query(DATABASE_TABLE_CATEGORY_NAME, new String[] { ID,
				CATGROUP, CODE }, null, null, CATGROUP, null, CATGROUP);
	}

	public Cursor getAllCategorys(String group) {
		return db.query(DATABASE_TABLE_CATEGORY_NAME, new String[] { ID,
				CATCATEGORY, CATGROUP, CATCODE }, CATGROUP + " = '" + group
				+ "'", null, CATCATEGORY, null, CATCATEGORY);
	}

	public Cursor getPost(int postID) {
		return db.query(DATABASE_TABLE_POSTS_NAME, null, ID + " = '" + postID
				+ "'", null, null, null, null);
	}
	
	public Cursor getFav(int postID) {
		return db.query(DATABASE_TABLE_FAVS_NAME, null, ID + " = '" + postID
				+ "'", null, null, null, null);
	}

	public Cursor getLocation(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_NAME, new String[] { ID,
				CODE, CITY, CITYRANK, COUNTRY, COUNTRYRANK, STATECODE,
				STATENAME, HIDDEN, LATITUDE, LONGITUDE }, ID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public void clearPosts() throws SQLException {
		db.delete(DATABASE_TABLE_POSTS_NAME, null, null);
	}
	
	public void deleteFav(int postID) {
		db.delete(DATABASE_TABLE_FAVS_NAME, ID + "=?", new String[] {""+postID});
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	public void endTransaction() {
		db.endTransaction();
	}



}
