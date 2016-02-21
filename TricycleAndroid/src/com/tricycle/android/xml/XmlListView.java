package com.tricycle.android.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tricycle.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class XmlListView extends Activity {

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mListView = new ListView(this);
		Log.i("LCW", "oncreate");
		// ListAdapter adapter = new ArrayAdapter<String>(this,
		// R.layout.textview_list, getData());
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.img_title_info_layout, new String[] { "img", "title",
						"info" }, new int[] { R.id.img, R.id.title, R.id.info });
		mListView.setAdapter(adapter);
		setContentView(mListView);

	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.ic_launcher);
		map.put("title", "title1");
		map.put("info", "info11111111111\njkfjsd\njkjjjjjj");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.ic_launcher);
		map.put("title", "title2");
		map.put("info", "info22222222");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.ic_launcher);
		map.put("title", "title3");
		map.put("info", "info33333333");
		list.add(map);
		return list;
	}

	// private List<String> getData() {
	// List<String> data = new ArrayList<String>();
	// data.add("111111\njjj");
	// data.add("222222");
	// data.add("333333");
	// return data;
	// }

}
