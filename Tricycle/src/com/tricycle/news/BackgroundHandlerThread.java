package com.tricycle.news;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class BackgroundHandlerThread extends HandlerThread {

	private static final String TAG = "BackgroundHandlerThread";
	private static BackgroundHandlerThread sInstance = null;
	
    private Handler mHandler;
	private BackgroundHandlerThread() {
		super(TAG);
	}
	public synchronized static BackgroundHandlerThread getInstance() {
		if(sInstance == null) {
			sInstance = new BackgroundHandlerThread();
			sInstance.start();
			sInstance.mHandler = new Handler(sInstance.getLooper());
		}
		return sInstance;
	}
	public Handler getHandler() {
		return mHandler;
	}

}
