package com.dragon.daystatistic.utils;


import java.util.Calendar;

public class YearMonthDay {
    public static final int[] DAYSOFMONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int MAX_DAY_NUM_OF_MONTH = 31;

    public static int getWeekDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR);
    }

    public static String getYearMonth() {
        return getCurrentYear() + "年" + getCurrentMonth() + "月";
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int[] getPreMonth(int year, int month)
    {
        int[] date = new int[2];
        if (month > 1)
        {
            date[0] = year;
            date[1] = month - 1;
        }
        else
        {
            date[0] = year - 1;
            if (date[0] < 0)
            {
                date[0] = 0;
            }
            date[1] = 12;
        }
        return date;
    }

    public static int[] getNextMonth(int year, int month)
    {
        int[] date = new int[2];
        if (month < 12)
        {
            date[0] = year;
            date[1] = month + 1;
        }
        else
        {
            date[0] = year + 1;
            date[1] = 1;
        }
        return date;
    }

	public static int getDaysOfMonth(int year, int month)
	{
		if (month > 12 || month <0)
        {
	        return 0;
        }
		int days = DAYSOFMONTH[month - 1];
		if (month == 2 && isLeapYear(year))
        {
	        days += 1;
        }
		
		return days;
	}
	
	public static boolean isLeapYear(int year)
	{
		if (year % 400 == 0)
        {
	        return true;
        }
		else if ((year % 4 == 0) && (year % 100 != 0))
        {
	        return true;
        }
		return false;
	}
	
	public static int getLines(int year, int month)
	{
		if (year <= 0 || month <= 0 || month > 12)
        {
	        return -1;
        }
		
		int lines = 0;
		int weekDay = getWeekDay(year, month, 1);
		int days = getDaysOfMonth(year, month);
		if (weekDay == 0)
        {
	        lines = days / 7;
	        if (days % 7 != 0)
            {
	            lines ++;
            }
        }
		else 
		{
			days = days - 7 + weekDay;
			lines = days / 7 + 1;
			if (days % 7 != 0)
            {
	            lines ++;
            }
		}
		return lines;
	}
}
