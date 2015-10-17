package com.dragon.daystatistic.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dragon.daystatistic.DSApplication;
import com.dragon.daystatistic.MainActivity;
import com.dragon.daystatistic.R;
import com.dragon.daystatistic.utils.Utils;
import com.dragon.daystatistic.utils.YearMonthDay;

import java.util.ArrayList;

/**
 * Created by Phoenix on 2014/12/13.
 */
public class CalendarAppWidgetProvider extends AppWidgetProvider {

    public static final String DAY_CLICK_ACTION = "com.dragon.daystatistic.DAY_CLICK_ACTION";
    public static final String TODAY_ACTION = "com.dragon.daystatistic.TODAY_ACTION";
    public static final String APP_DAY_CLICK_ACTION = "com.dragon.daystatistic.APP_DAY_CLICK_ACTION";
    public static final String PRE_MONTH_CLICK_ACTION = "com.dragon.daystatistic.PRE_MONTH_CLICK_ACTION";
    public static final String NEXT_MONTH_CLICK_ACTION = "com.dragon.daystatistic.NEXT_MONTH_CLICK_ACTION";
    private DSApplication mApp = null;
    private AlarmManager mAlarmManager = null;
    private PendingIntent mAlarmPendingIntent = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int id : appWidgetIds){
            RemoteViews remoteViews;

            remoteViews = initRemoteView(context, id, mApp.getYear(), mApp.getMonth(), true);

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews initRemoteView(Context context, int id, int year, int month, boolean flag) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.calendar_widget_layout);
        Intent intent = new Intent(context, CalendarRemoteViewsService.class);
        ArrayList<CharSequence> data = new ArrayList<CharSequence>();
        int days = YearMonthDay.getDaysOfMonth(year, month);
        int start_day = YearMonthDay.getWeekDay(year, month, 1);

        String[] week = context.getResources().getStringArray(R.array.week_day);
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
        intent.putCharSequenceArrayListExtra("data", data);
        intent.putExtra("id", id);
        remoteViews.setRemoteAdapter(id, R.id.gridview, intent);
        String ym = year + "年" + month + "月";
        remoteViews.setTextViewText(R.id.yearMonthTextView, ym);

        if (flag){
            Intent preIntent = new Intent().setAction(PRE_MONTH_CLICK_ACTION);
            PendingIntent prePendingIntent = PendingIntent.getBroadcast(context, 0, preIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.preMonth, prePendingIntent);

            Intent nextIntent = new Intent().setAction(NEXT_MONTH_CLICK_ACTION);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.nextMonth, nextPendingIntent);

            Intent gridIntent = new Intent();
            gridIntent.setAction(DAY_CLICK_ACTION);
            gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.gridview, pendingIntent);
        }
        String total = "总共" + mApp.getDays()+"天";
        remoteViews.setTextViewText(R.id.total, total);
        Intent startActivityIntent=new Intent(context , MainActivity.class);
        PendingIntent startActivityPendingIntent= PendingIntent.getActivity(context, 0, startActivityIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.total, startActivityPendingIntent);
        return remoteViews;
    }

    private void updateRemoteView(AppWidgetManager manager, Context context, int id)
    {
        RemoteViews remoteViews;
        remoteViews = initRemoteView(context, id, mApp.getYear(), mApp.getMonth(), false);
        manager.updateAppWidget(id, remoteViews);
        manager.notifyAppWidgetViewDataChanged(id, R.id.gridview);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mApp == null){
            mApp = (DSApplication)context.getApplicationContext();
            mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(TODAY_ACTION);
            mAlarmPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            //mAlarmManager.setRepeating(AlarmManager.RTC, time.toMillis(true), 100 * 1000, mAlarmPendingIntent);
        }
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        String action = intent.getAction();
        int year = mApp.getYear();
        int month = mApp.getMonth();
        int day;
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, CalendarAppWidgetProvider.class));

        //Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        if (action.equals(DAY_CLICK_ACTION))
        {
            CharSequence temp = intent.getCharSequenceExtra("day");
            if (Utils.isNumeric(temp+"")) {
                day = Integer.valueOf(temp+"");
                mApp.dayOnClick(day);
            }
            for (int id : ids){
                updateRemoteView(manager, context, id);
            }
        }
        else if (action.equals(APP_DAY_CLICK_ACTION))
        {
            for (int id : ids){
                updateRemoteView(manager, context, id);
            }
        }
        else if (action.equals(TODAY_ACTION))
        {
            //Toast.makeText(context, "action", Toast.LENGTH_SHORT).show();
            mApp.setYear(YearMonthDay.getCurrentYear());
            mApp.setMonth(YearMonthDay.getCurrentMonth());
            for (int id : ids){
                updateRemoteView(manager, context, id);
            }
        }
        else if (action.equals(PRE_MONTH_CLICK_ACTION))
        {
            onPreNextAction(context, manager, year, month, ids, true);
        }
        else if (action.equals(NEXT_MONTH_CLICK_ACTION))
        {
            onPreNextAction(context, manager, year, month, ids, false);
        }

        mAlarmManager.cancel(mAlarmPendingIntent);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 10 * 1000, mAlarmPendingIntent);

        super.onReceive(context, intent);
    }

    private void onPreNextAction(Context context, AppWidgetManager manager, int year, int month, int[] ids, boolean is_pre) {
        int[] date;
        if (!is_pre) {
            date = YearMonthDay.getNextMonth(year, month);
        }
        else{
            date = YearMonthDay.getPreMonth(year, month);
        }
        mApp.setYear(date[0]);
        mApp.setMonth(date[1]);
        for (int id : ids){
            updateRemoteView(manager, context, id);
        }
    }
}
