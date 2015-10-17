package com.dragon.daystatistic.db;

public class SelectedDay
{
	private long id;
	private int year;
	private int month;
	private int day;
	private int isSelected;
	
	public SelectedDay()
	{
		
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getMonth()
	{
		return month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

	public int getDay()
	{
		return day;
	}

	public void setDay(int day)
	{
		this.day = day;
	}

	public int isSelected()
	{
		return isSelected;
	}

	public void setSelected(int isSelected)
	{
		this.isSelected = isSelected;
	}
}
