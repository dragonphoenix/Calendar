package com.dragon.daystatistic.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dragon.daystatistic.DSApplication;
import com.dragon.daystatistic.R;
import com.dragon.daystatistic.utils.Utils;
import com.dragon.daystatistic.utils.YearMonthDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phoenix on 2014/12/13.
 */
public class CalendarRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private DSApplication mApp;
    private List<CharSequence> mData;

    public CalendarRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mApp = (DSApplication)mContext.getApplicationContext();
        mData = intent.getCharSequenceArrayListExtra("data");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int year = mApp.getYear();
        int month = mApp.getMonth();
        ArrayList<CharSequence> data = new ArrayList<CharSequence>();
        int days = YearMonthDay.getDaysOfMonth(year, month);
        int start_day = YearMonthDay.getWeekDay(year, month, 1);

        String[] week = mContext.getResources().getStringArray(R.array.week_day);
        for (int i = 0; i < 7; i++)
        {
            data.add(week[i]);
        }
        for (int i = 0; i < start_day + days; i++)
        {
            String temp = "";
            if (start_day <= i)
            {
                temp =  String.valueOf(i - start_day + 1);
            }
            data.add(temp);
        }
        mData = data;
    }

    @Override
    public void onDestroy() {
        mData.clear();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    private void setBackground(RemoteViews remoteviews, int textColorId, int bgColorId){
        remoteviews.setTextColor(R.id.dayTextView, mContext.getResources().getColor(textColorId));
        remoteviews.setInt(R.id.dayTextView, "setBackgroundColor", mContext.getResources().getColor(bgColorId));
    }


    @Override
    public RemoteViews getViewAt(int position) {
        CharSequence temp = "";
        int day;

        RemoteViews remoteviews = new RemoteViews(mContext.getPackageName(), R.layout.calendar_widget_item);
        if (mData.size() > position) {
            temp = mData.get(position);
        }

        remoteviews.setTextViewText(R.id.dayTextView, temp);

        if (Utils.isNumeric(temp + "")) {
            day = Integer.valueOf(temp+"");
            if (mApp.isCheck(day)){
                setBackground(remoteviews, R.color.selected, R.color.selected_bg);
                if (mApp.is_today(day)){
                    setBackground(remoteviews, R.color.activity_font_color, R.color.today_select_color);
                }
            }
            else {
                setBackground(remoteviews, R.color.textcolor, R.color.transparent);
                if (mApp.is_today(day)){
                    setBackground(remoteviews, R.color.today_color, R.color.transparent);
                }
            }
        }
        else {
            setBackground(remoteviews, R.color.textcolor, R.color.transparent);
        }

        Intent intent = new Intent();
        intent.putExtra("day", temp);
        remoteviews.setOnClickFillInIntent(R.id.dayLinearLayout, intent);
        return remoteviews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
