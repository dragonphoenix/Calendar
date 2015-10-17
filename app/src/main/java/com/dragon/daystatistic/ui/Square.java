package com.dragon.daystatistic.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.dragon.daystatistic.DSApplication;
import com.dragon.daystatistic.utils.FloatRect;
import com.dragon.daystatistic.utils.Utils;

public class Square {
    private FloatRect m_rect;
    private int m_color;
    private String m_text;
    private boolean m_needSelect;
    private DSApplication mApp;

    public Square(DSApplication app) {
        m_rect = new FloatRect();
        m_text = "";
        m_color = Color.BLACK;
        m_needSelect = false;
        mApp = app;
    }

    public Square(String text, int color, DSApplication app) {
        m_rect = new FloatRect();
        m_text = text;
        m_color = color;
        m_needSelect = false;
        mApp = app;
    }

    public FloatRect getRect() {
        return m_rect;
    }

    public void setRect(FloatRect rect) {
        m_rect = rect;
    }

    public int getColor() {
        return m_color;
    }

    public void setColor(int color) {
        m_color = color;
    }

    public String getText() {
        return m_text;
    }

    public void setNeedSelect(boolean needSelect) {
        m_needSelect = needSelect;
    }

    public Square invalidate() {
        m_needSelect = !m_needSelect;
        return this;
    }

    public boolean getNeedSelect() {
        return m_needSelect;
    }

    public void draw(Canvas canvas) {
        if (m_needSelect) {
            drawDaySelect(canvas);
        } else {
            drawDay(canvas);
        }
    }

    public void drawDay(Canvas canvas) {
        if (m_text.length() < 1) {
            return;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect rect = new Rect();
        float textSize;
        float semiwidth = m_rect.width() / 2;
        float semiheight = m_rect.height() / 2;

        textSize = semiwidth > semiheight ? semiheight : semiwidth;
        if (Utils.isNumeric(m_text) && mApp.is_today(Integer.valueOf(m_text))) {
            paint.setColor(Color.rgb(0, 0, 0));
        } else {
            paint.setColor(m_color);
        }

        paint.setTextSize(textSize);
        paint.getTextBounds(m_text, 0, m_text.length(), rect);
        float x = m_rect.left + semiwidth - rect.exactCenterX();
        float y = m_rect.top + semiheight - rect.exactCenterY();
        canvas.drawText(m_text, x, y, paint);
    }

    public void drawDaySelect(Canvas canvas) {
        if (m_text.length() < 1) {
            return;
        }
        float radius;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float semiwidth = m_rect.width() / 2;
        float semiheight = m_rect.height() / 2;

        radius = semiwidth > semiheight ? semiheight : semiwidth;
        paint.setColor(Color.CYAN);
        canvas.drawCircle(m_rect.left + semiwidth, m_rect.top + semiheight, radius, paint);
        drawDay(canvas);
    }
}
