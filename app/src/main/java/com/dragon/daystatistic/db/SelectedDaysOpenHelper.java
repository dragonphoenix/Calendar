
package com.dragon.daystatistic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SelectedDaysOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_SELECTEDDAYS = "selecteddays";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_ISSELECTED = "isselected";
	
	public static final int ID = 0;
	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	public static final int ISSELECTED = 4;

	private static final String DATABASE_NAME = "selecteddays.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
	      + TABLE_SELECTEDDAYS + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_YEAR
	      + " integer, " + COLUMN_MONTH
	      + " integer, " + COLUMN_DAY
	      + " integer, " + COLUMN_ISSELECTED
	      + " integer);";
	  
    public SelectedDaysOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SelectedDaysOpenHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTEDDAYS);
		onCreate(db);
	}

}
