package com.tricycle.news.utils;

import com.tricycle.news.MainActivity;
import com.tricycle.news.R;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

public class MyDrawerLayout extends RelativeLayout implements OnItemClickListener {

	private static final String[] MENU_TITLE = {"科技新闻", "体育新闻", "社会新闻", "我的收藏"};
	private static final int MENU_POSITION_KEJI = 0;
	private static final int MENU_POSITION_TIYU = 1;
	private static final int MENU_POSITION_SOCIAL = 2;
	private static final int MENU_POSITION_FAVORITE = 3;
	
	private ListView mMenuList;
	private ArrayAdapter<String> mAdapter;
	private Context mContext;
 	public MyDrawerLayout(Context context) {
		super(context);
		mContext = context;
		initDrawer(context);
	}

	public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initDrawer(context);
	}

	public MyDrawerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initDrawer(context);
	}

	private void initDrawer(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.mydrawer, null);
		addView(view);
		mMenuList = (ListView) findViewById(R.id.left_menu);
		mMenuList.setOnItemClickListener(this);
		mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
		for(String menuTitle : MENU_TITLE) {
			mAdapter.add(menuTitle);
		}
		mMenuList.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case MENU_POSITION_KEJI:
		case MENU_POSITION_TIYU:
		case MENU_POSITION_SOCIAL:
			if(MainActivity.class.getSimpleName().equals(getRunningActivityName())) {
				((MainActivity)mContext).selectPager(0, position);
			} else {
				Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
				intent.putExtra(MainActivity.TAG_PARENTPAGER, 0);
				intent.putExtra(MainActivity.TAG_CHILDPAGER, position);
				mContext.startActivity(intent);
			}
			break;
		case MENU_POSITION_FAVORITE:
			if(MainActivity.class.getSimpleName().equals(getRunningActivityName())) {
				((MainActivity)mContext).selectPager(1, position);
			} else {
				Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
				intent.putExtra(MainActivity.TAG_PARENTPAGER, 1);
				intent.putExtra(MainActivity.TAG_CHILDPAGER, position);
				mContext.startActivity(intent);
			}
			break;
		default:
			break;
		}
		((DrawerLayout)getParent()).closeDrawer(Gravity.START);
	}

	private String getRunningActivityName() {  
        String contextString = mContext.toString();  
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));  
}
}
