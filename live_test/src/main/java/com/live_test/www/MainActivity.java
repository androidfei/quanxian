package com.live_test.www;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;

public class MainActivity extends AppCompatActivity {

    private TestView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (TestView) findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                test.start_an();
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
