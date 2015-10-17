package com.dragon.daystatistic.utils;

public class FloatRect
{
	public float top;
	public float left;
	public float right;
	public float bottom;
	
	public FloatRect()
	{
		
	}
	
	public FloatRect(float left, float top, float right, float bottom)
	{
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public float exactCenterX()
	{
		return (right + left)/2;
	}
	
	public float exactCenterY()
	{
		return (bottom + top)/2;
	}
	
	public float height()
	{
		return bottom - top;
	}
	
	public float width()
	{
		return right - left;
	}
	
	public boolean isInRect(float x, float y)
	{
		if ((left <= x) && (right > x) && (top <= y) && (bottom > y))
        {
	        return true;
        }
		return false;
	}
}
