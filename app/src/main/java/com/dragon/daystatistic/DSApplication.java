package com.dragon.daystatistic;

import android.app.Application;
import android.database.Cursor;
import android.widget.Toast;

import com.dragon.daystatistic.db.SelectedDay;
import com.dragon.daystatistic.db.SelectedDaysDataSource;
import com.dragon.daystatistic.utils.YearMonthDay;

/**
 * Created by Phoenix on 2014/12/17.
 */
public class DSApplication  extends Application{
    private int mYear = YearMonthDay.getCurrentYear();
    private int mMonth = YearMonthDay.getCurrentMonth();
    private SelectedDaysDataSource dbDateSource = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initSeletedDaysDataSource();
    }

    private void initSeletedDaysDataSource()
    {
        if (dbDateSource == null){
            dbDateSource = new SelectedDaysDataSource(getApplicationContext());
            dbDateSource.open();
        }
    }

    public int getDays()
    {
        return getDays(mYear, mMonth);
    }

    public int getDays(int year, int month){
        int days;
        Cursor cursor = dbDateSource.getCursor(year, month);
        days = cursor.getCount();
        return days;
    }

    public boolean is_today(int day){
        int cur_month = YearMonthDay.getCurrentMonth();
        int cur_year = YearMonthDay.getCurrentYear();
        int cur_day = YearMonthDay.getCurrentMonthDay();

        if (cur_month == mMonth && cur_year == mYear && day == cur_day){
            return true;
        }

        return false;
    }

    public boolean isCheck(int day)
    {
        Cursor cursor = dbDateSource.getCursor(mYear, mMonth, day);
        cursor.moveToNext();
        SelectedDay selectedDay = dbDateSource.cursorToSelectedDay(cursor);
        if (selectedDay != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void dayOnClick(int day)
    {
        Cursor cursor = dbDateSource.getCursor(mYear, mMonth, day);
        cursor.moveToNext();
        SelectedDay selectedDay = dbDateSource.cursorToSelectedDay(cursor);
        if (selectedDay != null)
        {
            dbDateSource.deleteSelectedDay(selectedDay);
            //Toast.makeText(DSApplication.this, mYear+" "+mMonth+" "+day+" delete", Toast.LENGTH_LONG).show();
        }
        else
        {
            dbDateSource.createSelectedDay(mYear, mMonth, day, 1);
            //Toast.makeText(DSApplication.this, mYear+" "+mMonth+" "+day+" add", Toast.LENGTH_LONG).show();
        }
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }
}
