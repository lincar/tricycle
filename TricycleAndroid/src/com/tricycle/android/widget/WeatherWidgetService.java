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
			���ϵͳ��onStartCommand()��������֮��ɱ�����������ôֱ�����ܵ��µ�Intent�����������Żᱻ���´����������ȫ��ѡ����������ڲ���Ҫ��ʱ��������ķ���
			 
			START_STICKY
			���ϵͳ��onStartCommand()���غ�ɱ�����������ϵͳ�ͻ����´�����������ҵ���onStartCommand()�������������������´�������Intent����ϵͳ����һ��null��Intent����������onStartCommand()���������������£�������һЩ�����͵�Intent�����ڵȴ����������������ڲ�ִ�������ý�岥�����������Ƶķ��񣩣���ֻ�������ڵ������Ų��ȴ������ĵ�����
			 
			START_REDELIVER_INTENT
			���ϵͳ��onStartCommand()�������غ�ϵͳ�ͻ����´�����������񣬲����÷��͸�������������Intent���������onStartCommand()����������ȴ��е�Intent��������α����͡�����������ЩӦ�������ָ�����ִ�еĹ����ķ����������ļ���
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
