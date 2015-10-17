package com.dragon.daystatistic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SelectedDaysDataSource
{
	
	// Database fields
	private SQLiteDatabase	       database;
	private SelectedDaysOpenHelper	dbHelper;
	private String[]	           allColumns	= { SelectedDaysOpenHelper.COLUMN_ID,
	        SelectedDaysOpenHelper.COLUMN_YEAR, SelectedDaysOpenHelper.COLUMN_MONTH,
	        SelectedDaysOpenHelper.COLUMN_DAY, SelectedDaysOpenHelper.COLUMN_ISSELECTED };
	
	public SelectedDaysDataSource(Context context)
	{
		dbHelper = new SelectedDaysOpenHelper(context);
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public SelectedDay createSelectedDay(int year, int month, int day, int isSelected)
	{
		ContentValues values = new ContentValues();
		values.put(SelectedDaysOpenHelper.COLUMN_YEAR, year);
		values.put(SelectedDaysOpenHelper.COLUMN_MONTH, month);
		values.put(SelectedDaysOpenHelper.COLUMN_DAY, day);
		values.put(SelectedDaysOpenHelper.COLUMN_ISSELECTED, isSelected);
		long insertId = database.insert(SelectedDaysOpenHelper.TABLE_SELECTEDDAYS, null, values);
		Cursor cursor = database.query(SelectedDaysOpenHelper.TABLE_SELECTEDDAYS, allColumns,
		        SelectedDaysOpenHelper.COLUMN_ID + " = " + insertId, null, null, null,
		        SelectedDaysOpenHelper.COLUMN_DAY + " DESC");
		cursor.moveToFirst();
		SelectedDay newDay = cursorToSelectedDay(cursor);
		cursor.close();
		return newDay;
	}
	
	public void deleteSelectedDay(SelectedDay day)
	{
		long id = day.getId();
		// System.out.println("Comment deleted with id: " + id);
		database.delete(SelectedDaysOpenHelper.TABLE_SELECTEDDAYS, SelectedDaysOpenHelper.COLUMN_ID
		        + " = " + id, null);
	}
	
	public SelectedDay cursorToSelectedDay(Cursor cursor)
	{
		if (cursor.isAfterLast())
		{
			return null;
		}
		SelectedDay day = new SelectedDay();
		day.setId(cursor.getLong(SelectedDaysOpenHelper.ID));
		day.setYear(cursor.getInt(SelectedDaysOpenHelper.YEAR));
		day.setMonth(cursor.getInt(SelectedDaysOpenHelper.MONTH));
		day.setDay(cursor.getInt(SelectedDaysOpenHelper.DAY));
		day.setSelected(cursor.getInt(SelectedDaysOpenHelper.ISSELECTED));
		return day;
	}
	
	public Cursor getCursor(int year, int month)
	{
		String selection = SelectedDaysOpenHelper.COLUMN_YEAR + "=" + year + " and "
		        + SelectedDaysOpenHelper.COLUMN_MONTH + "=" + month;
		return database.query(SelectedDaysOpenHelper.TABLE_SELECTEDDAYS, allColumns, selection,
		        null, null, null, SelectedDaysOpenHelper.COLUMN_DAY + " DESC");
	}
	
	public Cursor getCursor(int year, int month, int day)
	{
		String selection = SelectedDaysOpenHelper.COLUMN_YEAR + "=" + year + " and "
		        + SelectedDaysOpenHelper.COLUMN_MONTH + "=" + month + " and "
		        + SelectedDaysOpenHelper.COLUMN_DAY + "=" + day;
		return database.query(SelectedDaysOpenHelper.TABLE_SELECTEDDAYS, allColumns, selection,
		        null, null, null, SelectedDaysOpenHelper.COLUMN_DAY + " DESC");
	}
}
