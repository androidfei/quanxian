package com.live_test.www;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class SpeedView extends View {
    private Paint mTextPaint;
    private Paint mScalePaint;
    int hour, mum, sec;
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd---hh--mm--ss");
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
                hour = dateFormat.getCalendar().get(java.util.Calendar.HOUR);
                mum = dateFormat.getCalendar().get(java.util.Calendar.MINUTE);
                sec = dateFormat.getCalendar().get(java.util.Calendar.SECOND);
                invalidate();
            }
        }
    };

    public SpeedView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这是矩形范围
        RectF rectF = new RectF(10, 10, 510, 510);
        //绘制矩形背景
        canvas.drawRect(rectF, mTextPaint);
        mTextPaint.setColor(Color.RED);
        //绘制弧度
        canvas.drawArc(rectF, 0f, 360f, true, mTextPaint);
        //绘制刻度线
        mTextPaint.setColor(Color.BLACK);
        canvas.save();
        canvas.translate(260, 260);
        for (int i = 0; i < 12; i++) {
            canvas.drawLine(0, 250, 0, 210, mTextPaint);
            if (i % 3 == 0) {
                //安卓坐标系
                switch (i / 3) {
                    case 1:
                        mTextPaint.setTextSize(30f);
                        canvas.drawText(String.valueOf(9), -mTextPaint.getStrokeWidth()/2, 200, mTextPaint);
                        break;
                    case 2:
                        mTextPaint.setTextSize(30f);
                        canvas.drawText(String.valueOf(12), -mTextPaint.getStrokeWidth()/2, 200, mTextPaint);
                        break;
                    case 3:
                        mTextPaint.setTextSize(30f);
                        canvas.drawText(String.valueOf(3), -mTextPaint.getStrokeWidth()/2, 200, mTextPaint);
                        break;
                    case 0:
                        mTextPaint.setTextSize(30f);
                        canvas.drawText(String.valueOf(6), -mTextPaint.getStrokeWidth()/2, 200, mTextPaint);
                        break;
                }
            }
            canvas.rotate(30f);
        }
        //时针转一小时分针转一圈秒针60圈
        //时针一小时是30度
        //加270因为android的坐标系和数学坐标不同需要转动270
        float hour_x = (float) Math.cos(Math.toRadians((hour + 1) * 30 + 270)) * 250 * 0.5f;
        float hour_y = (float) Math.sin(Math.toRadians((hour + 1) * 30 + 270)) * 250 * 0.5f;
        float mum_x = (float) Math.cos(Math.toRadians((mum + 1) * 6 + 270)) * 250 * 0.7f;
        float mum_y = (float) Math.sin(Math.toRadians((mum + 1) * 6 + 270)) * 250 * 0.7f;
        float sec_x = (float) Math.cos(Math.toRadians((sec + 1) * 6 + 270)) * 250 * 0.9f;
        float sec_y = (float) Math.sin(Math.toRadians((sec + 1) * 6 + 270)) * 250 * 0.9f;
        canvas.drawLine(0, 0, hour_x, hour_y, mTextPaint);
        canvas.drawLine(0, 0, mum_x, mum_y, mTextPaint);
        canvas.drawLine(0, 0, sec_x, sec_y, mTextPaint);
        canvas.restore();
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int withPadingWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int withPaddingHeight = heightSize - getPaddingBottom() - getPaddingTop();
        if (widthMode != MeasureSpec.UNSPECIFIED && heightMode != MeasureSpec.UNSPECIFIED) {
            size = Math.max(withPadingWidth, withPaddingHeight);
        }
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingBottom() + getPaddingTop());
    }

}
