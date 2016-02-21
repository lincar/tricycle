package com.tricycle.android.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.tricycle.android.R;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
;

public class WeatherWidgetService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	 

	@Override
	public void onCreate() {
		registTimeTickReceiver();
		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		onUpdateWidget(this);
		return START_NOT_STICKY;
		/**
			START_NOT_STICKY
			如果系统在onStartCommand()方法返回之后杀死这个服务，那么直到接受到新的Intent对象，这个服务才会被重新创建。这是最安全的选项，用来避免在不需要的时候运行你的服务。
			 
			START_STICKY
			如果系统在onStartCommand()返回后杀死了这个服务，系统就会重新创建这个服务并且调用onStartCommand()方法，但是它不会重新传递最后的Intent对象，系统会用一个null的Intent对象来调用onStartCommand()方法，在这个情况下，除非有一些被发送的Intent对象在等待启动服务。这适用于不执行命令的媒体播放器（或类似的服务），它只是无限期的运行着并等待工作的到来。
			 
			START_REDELIVER_INTENT
			如果系统在onStartCommand()方法返回后，系统就会重新创建了这个服务，并且用发送给这个服务的最后的Intent对象调用了onStartCommand()方法。任意等待中的Intent对象会依次被发送。这适用于那些应该立即恢复正在执行的工作的服务，如下载文件。
		*/
	}
	
	private void onUpdateWidget(Context context) {
		Log.i("LCW", "onUpdateWidget");
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		String time = format.format(c1.getTime());
		Log.i("LCW", "time:" + time);
		remoteViews.setTextViewText(R.id.tvCurrTime, "time: " + time);
		
		AppWidgetManager awg = AppWidgetManager.getInstance(context);
		awg.updateAppWidget(new ComponentName(context, WeatherWidgetProvider.class),
				remoteViews);
	}
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("LCW", "time kick");
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_TIME_TICK)) {
				onUpdateWidget(context);
			}
		}
    };
	
	private void registTimeTickReceiver() {
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(receiver,filter);
	}

	
	

}
