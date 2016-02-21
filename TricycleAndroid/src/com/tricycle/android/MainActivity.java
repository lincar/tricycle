package com.tricycle.android;

import java.util.ArrayList;
import java.util.List;

import com.tricycle.android.rxjava.RxListView;
import com.tricycle.android.webview.WebActivity;
import com.tricycle.android.xml.XmlListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView mListView = null;

	private static List<Class<?>> sActivityList = new ArrayList<Class<?>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivityList();
		mListView = new ListView(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getActivityName());
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		setContentView(mListView);
	}

	private List<String> getActivityName() {
		List<String> list = new ArrayList<String>();
		for(Class<?> activity : getActivityList()) {
			list.add(activity.getName());
		}
		return list;
	}

	private void setActivityList() {
		sActivityList.add(RxListView.class);
		sActivityList.add(XmlListView.class);
		sActivityList.add(WebActivity.class);
	}

	private List<Class<?>> getActivityList() {
		return sActivityList;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Class<?> activity = sActivityList.get(arg2);
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), activity);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		sActivityList.clear();
		super.onDestroy();
	}
}
