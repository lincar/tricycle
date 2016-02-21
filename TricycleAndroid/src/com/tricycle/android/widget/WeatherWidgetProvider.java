package com.tricycle.android.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

public class WeatherWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i("LCW", "onUpdate");
		Time time = new Time();
	    time.setToNow();
	    //ʹ��Service����ʱ��
	    Intent intent = new Intent(context, WeatherWidgetService.class);
	    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
	    //ʹ��Alarm��ʱ���½�������
	    AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    alarm.setRepeating(AlarmManager.RTC, time.toMillis(true),1000, pendingIntent);
	}

}
