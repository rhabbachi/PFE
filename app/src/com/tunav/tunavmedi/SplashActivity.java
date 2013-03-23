package com.tunav.tunavmedi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.tunav.tunavmedi.gui.LoginActivity;

public class SplashActivity extends Activity {

    private static final String TAG = "SPLASH_ACTIVITY";
    private Thread splashThread;
    private final int WAIT_TIME = 3000;
    final SplashActivity splashActivity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.splash);

	splashThread = new Thread(new LoadService());

	splashThread.start();
    }

    private class LoadService implements Runnable {

	@Override
	public void run() {
	    boolean runApp = false;
	    try {
		synchronized (this) {
		    // TODO start services
		    wait(WAIT_TIME);
		    runApp = true;
		}
	    } catch (InterruptedException ie) {
		Log.d(TAG, this.getClass().getName(), ie);
	    } finally {
		if (runApp) {
		    Intent intent = new Intent();
		    intent.setClass(splashActivity, LoginActivity.class);
		    startActivity(intent);
		} else {
		    // TODO Error message
		}
		finish();
	    }
	    return;
	}

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
