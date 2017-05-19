package com.live_test.www;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class TestView extends View {
    private Context context;
    private int i = 0;

    Paint paint;
    Rect src;
    public static final String TAG = "TestView";

    public TestView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        this.context = context;

    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Log.e(TAG, "i=" + i);
        Rect src = new Rect(0, 0, i, i);
        Rect dst = new Rect(400, 400, 800, 800);
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    public void start_an() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while ((i += 20) < 500) {
                        postInvalidate();
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
