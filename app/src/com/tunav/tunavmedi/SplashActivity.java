package com.tunav.tunavmedi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.tunav.tunavmedi.gui.LoginActivity;

public class SplashActivity extends Activity {

    private Thread splashThread;
    private final int WAIT_TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        final SplashActivity splashActivity = this;

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(WAIT_TIME);
                    }
                } catch (InterruptedException ie) {

                }

                finish();

                Intent intent = new Intent();
                intent.setClass(splashActivity, LoginActivity.class);
                startActivity(intent);
                stop();
            }
        };

        splashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashThread) {
                splashThread.notifyAll();
            }
        }
        return true;
    }
}
