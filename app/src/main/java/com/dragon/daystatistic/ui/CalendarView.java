package com.dragon.daystatistic.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dragon.daystatistic.DSApplication;
import com.dragon.daystatistic.R;
import com.dragon.daystatistic.db.SelectedDay;
import com.dragon.daystatistic.db.SelectedDaysDataSource;
import com.dragon.daystatistic.utils.FloatRect;
import com.dragon.daystatistic.utils.Utils;
import com.dragon.daystatistic.utils.YearMonthDay;
import com.dragon.daystatistic.widget.CalendarAppWidgetProvider;

import java.util.Vector;

@SuppressLint("DefaultLocale")
public class CalendarView extends View implements View.OnTouchListener
{
	public final String	TAG	         = getClass().getName().toUpperCase();
	private Paint	        paint;
	private String[]	    weekDay;
	private Vector<Square>	m_days;
	private int m_dayNum;
	private float	        m_x;
	private float	        m_y;
	private boolean	        m_invalidat	 = false;
	private int	            m_year;
	private int	            padding;
	private int	            m_month;
	public static final int	LEFT_ALIGN	 = 1;
	public static final int	CENTER_ALIGN	= 2;
	public static final int	RIGHT_ALIGN	 = 3;
	private Square	        preBtn;
	private Square	        nextBtn;
	private SelectedDaysDataSource dbDateSource;
    private Context mContext;
    private DSApplication mApp;

	public CalendarView(Context context)
	{
		super(context);
		init(context);
	}
	
	public CalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context)
	{
		if (isInEditMode())
		{
			return;
		}
		mContext = context;
		mApp = (DSApplication)mContext.getApplicationContext();

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		weekDay = context.getResources().getStringArray(R.array.week_day);
		m_year = YearMonthDay.getCurrentYear();
		m_month = YearMonthDay.getCurrentMonth();
		m_days = new Vector<Square>(YearMonthDay.MAX_DAY_NUM_OF_MONTH);
		padding = 5;
		for (int i = 0; i < YearMonthDay.MAX_DAY_NUM_OF_MONTH; i++)
		{
			Square day = new Square(String.valueOf(i + 1), Color.RED, mApp);
			m_days.add(day);
		}
		preBtn = new Square("<", Color.RED, mApp);
		nextBtn = new Square(">", Color.RED, mApp);
		preBtn.setNeedSelect(true);
		nextBtn.setNeedSelect(true);
		dbDateSource = new SelectedDaysDataSource(context);
		dbDateSource.open();

		setOnTouchListener(this);
	}
	
	private void drawGrid(Canvas canvas, int lines, float c_width, float c_height, float x_width,
	        float y_width)
	{
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		for (int i = 0; i <= 7; i++)
		{
			canvas.drawLine(padding + i * x_width, padding + y_width, padding + i * x_width,
			        c_height - padding, paint);
		}
		for (int i = 1; i <= lines; i++)
		{
			canvas.drawLine(padding, padding + i * y_width, c_width - padding, padding + i
			        * y_width, paint);
		}
	}
	
	private void drawButton(Canvas canvas, Square button, int x_index, int y_index, float x_width,
	        float y_width)
	{
		FloatRect rect = new FloatRect();
		rect.left = padding + x_index * x_width;
		rect.top = padding + y_index * y_width;
		rect.bottom = rect.top + y_width;
		rect.right = rect.left + x_width;
		button.setRect(rect);
		button.draw(canvas);
	}
	
	private void drawDays(Canvas canvas, int year, int month, float c_width,
	        float x_width, float y_width)
	{
		float x, y;
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Rect rect = new Rect();
		float textSize;
		
		paint.setColor(Color.RED);
		textSize = x_width > y_width ? y_width / 2 : x_width / 2;
		paint.setTextSize(textSize);
		
		for (int i = 0; i < 7; i++)
		{
			paint.getTextBounds(weekDay[i], 0, weekDay[i].length(), rect);
			x = x_width / 2 - rect.exactCenterX();
			y = y_width / 2 - rect.exactCenterY();
			canvas.drawText(weekDay[i], padding + i * x_width + x, padding + y_width + y, paint);
		}
		
		String title = year + "年" + month + "月";
		drawText(canvas, paint, title, CENTER_ALIGN, padding, padding, c_width, y_width);
		
		initDays(canvas, x_width, y_width);
	}
	
	private void drawText(Canvas canvas, Paint paint, String text, int type, float startX,
	        float startY, float x_width, float y_width)
	{
		float x, y;
		float centerX, centerY;
		Rect rect = new Rect();
		
		paint.getTextBounds(text, 0, text.length(), rect);
		centerX = rect.exactCenterX();
		centerY = rect.exactCenterY();
		
		switch (type)
		{
		case LEFT_ALIGN:
			x = 0;
			y = y_width / 2 - centerY;
			break;
		case RIGHT_ALIGN:
			x = x_width - centerX * 2;
			y = y_width / 2 - centerY;
			break;
		case CENTER_ALIGN:
		default:
			x = x_width / 2 - centerX;
			y = y_width / 2 - centerY;
			break;
		}
		
		canvas.drawText(text, startX + x, startY + y, paint);
	}
	
	private int[] getSelectedDays(int year, int month)
	{
		Cursor cursor = dbDateSource.getCursor(year, month);
		int num = cursor.getCount();
		int[] days = null;
		int i = 0;
		
		if (num > 0)
        {
	        days = new int[num];
        }
		while (cursor.moveToNext())
        {
	        SelectedDay day = dbDateSource.cursorToSelectedDay(cursor);
	        days[i++] = day.getDay();
        }
		
		return days;
	}
	
	public void initDays(Canvas canvas, float x_width, float y_width)
	{
		FloatRect rect;
		int day = 1;
		int start_day = YearMonthDay.getWeekDay(m_year, m_month, 1);
		boolean b_begin = false;
		int lines = YearMonthDay.getLines(m_year, m_month) + 2;
		int[] days = getSelectedDays(m_year, m_month);
		
		if (days != null)
        {
			for (int i : days)
	        {
		        m_days.get(i - 1).setNeedSelect(true);
	        }
        }
		
		for (int i = 2; i < lines; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				if (start_day == j)
				{
					b_begin = true;
				}
				if (!b_begin)
				{
					continue;
				}
				if (day <= m_dayNum)
				{
					Square tempDay = m_days.get(day - 1);
					rect = new FloatRect();
					rect.left = padding + j * x_width;
					rect.top = padding + i * y_width;
					rect.bottom = rect.top + y_width;
					rect.right = rect.left + x_width;
					tempDay.setRect(rect);
					tempDay.draw(canvas);
					day++;
				}
				else
				{
					break;
				}
			}
		}
	}
	
	private Square getDay(float x, float y)
	{
		for (int i = 0; i<m_dayNum; i++)
		{
			Square day = m_days.get(i);
			if (day.getRect().isInRect(x, y))
			{
				return day;
			}
		}
		return new Square(mApp);
	}
	
	public Square dateOnClick(Canvas canvas, float x, float y)
	{
		Square sday = getDay(x, y);
        int day;
        if (Utils.isNumeric(sday.getText())){
            day = Integer.valueOf(sday.getText());
            mApp.dayOnClick(day);
        }

        sday.invalidate().draw(canvas);
        return sday;
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //setMeasuredDimension(200,200);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (isInEditMode())
		{
			return;
		}
		float c_height = canvas.getHeight();
		float c_width = canvas.getWidth();
		
		int lines = YearMonthDay.getLines(m_year, m_month) + 2;
		float x_width = (c_width - padding * 2) / 7;
		float y_width = (c_height - padding * 2) / lines;
		
		m_dayNum = YearMonthDay.getDaysOfMonth(m_year, m_month);
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		paint.setColor(Color.RED);
		if (m_invalidat)
		{
            mApp.setYear(m_year);
            mApp.setMonth(m_month);
			Square square = dateOnClick(canvas, m_x, m_y);
            Intent intent = new Intent(CalendarAppWidgetProvider.APP_DAY_CLICK_ACTION);
            if (square != null)
            {
                intent.putExtra("day", square.getText());
            }
            else
            {
                intent.putExtra("day", "");
            }
            mContext.sendBroadcast(intent);
			m_invalidat = false;
		}
		drawButton(canvas, preBtn, 5, 0, x_width, y_width);
		drawButton(canvas, nextBtn, 6, 0, x_width, y_width);
		drawGrid(canvas, lines, c_width, c_height, x_width, y_width);
		drawDays(canvas, m_year, m_month, c_width, x_width, y_width);
	}
	
	@Override
	public boolean performClick()
	{
		return super.performClick();
	}
	
	private void clearDaysSelected()
    {
	    for (int i = 0; i < YearMonthDay.MAX_DAY_NUM_OF_MONTH; i++)
        {
	        m_days.get(i).setNeedSelect(false);
        }
	    
    }
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		m_x = event.getX();
		m_y = event.getY();
		
		if (preBtn.getRect().isInRect(m_x, m_y))
        {
	        m_month --;
	        if (m_month <= 0)
            {
	            m_month = 12;
	            m_year --;
            }
			mApp.setMonth(m_month);
			mApp.setYear(m_year);
	        clearDaysSelected();
        }
		else if (nextBtn.getRect().isInRect(m_x, m_y))
        {
	        m_month ++;
	        if (m_month > 12)
            {
	            m_month = 1;
	            m_year ++;
            }
			mApp.setMonth(m_month);
			mApp.setYear(m_year);
	        clearDaysSelected();
        }
		else
		{
			m_invalidat = true;
		}
		invalidate();
		v.performClick();
		return false;
	}
}
