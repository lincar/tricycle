package com.tricycle.news;

import java.util.ArrayList;
import java.util.List;

import com.tricycle.news.R;
import com.tricycle.news.favorite.FavoriteFragment;
import com.tricycle.news.newslist.ParentFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener {

	public static final String TAG_PARENTPAGER = "parentPager";
	public static final String TAG_CHILDPAGER = "childPager";
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private ListView mLeftMenuListView;
	private DrawerLayout mDrawerLayout;
	private ParentFragment mParentFragment = new ParentFragment();
	private FavoriteFragment mFavoriteFragment = new FavoriteFragment();
	private int mParentPager = 0;
	private int mChildPager = 0;
	TextView mTvHome;
	TextView mTvFavorite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		mParentPager = intent.getIntExtra(TAG_PARENTPAGER, 0);
		mChildPager = intent.getIntExtra(TAG_CHILDPAGER, 0);
		initMenu();
		initFragment();
		initPagers();
		initLeftMenu();
	}
	private void initMenu() {
		mTvHome = (TextView) findViewById(R.id.menu_textview_home);
		TextView tvVideo = (TextView) findViewById(R.id.menu_textview_video);
		tvVideo.setVisibility(View.GONE);
		mTvFavorite = (TextView) findViewById(R.id.menu_textview_favorite);
		mTvHome.setOnClickListener(this);
		tvVideo.setOnClickListener(this);
		mTvFavorite.setOnClickListener(this);
	}
	private void initLeftMenu() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	}
	public void showLeftDrawer() {
		mDrawerLayout.openDrawer(Gravity.START);
	}
	public void closeLeftDrawer() {
		mDrawerLayout.closeDrawer(Gravity.START);
	}
	private void initFragment() {
		mFragments.add(mParentFragment);
		mFragments.add(mFavoriteFragment);
	}
	
	private void initPagers() {
		mViewPager = (ViewPager) findViewById(R.id.pager_content);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mFragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		if(mParentPager != 0 || mChildPager != 0) {
			selectPager(mParentPager, mChildPager);
		}
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.menu_textview_home:
			mViewPager.setCurrentItem(0);
			setMenuSelect(0);
			break;
		case R.id.menu_textview_video:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.menu_textview_favorite:
			mViewPager.setCurrentItem(2);
			setMenuSelect(2);
			break;
		default:
			break;
		}
	}
	private void setMenuSelect(int position) {
		Drawable topDrawable;
		switch (position) {
		case 0:
			topDrawable = getResources().getDrawable(R.drawable.b_newhome_tabbar_press);  
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight()); 
			mTvHome.setCompoundDrawables(null, topDrawable, null, null);
			mTvHome.setTextColor(Color.rgb(255, 0, 0));
			topDrawable = getResources().getDrawable(R.drawable.b_newcare_tabbar);  
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight()); 
			mTvFavorite.setCompoundDrawables(null, topDrawable, null, null);
			mTvFavorite.setTextColor(Color.rgb(0, 0, 0));
			break;
		case 2:
			topDrawable = getResources().getDrawable(R.drawable.b_newhome_tabbar);  
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight()); 
			mTvHome.setCompoundDrawables(null, topDrawable, null, null);
			mTvHome.setTextColor(Color.rgb(0, 0, 0));
			topDrawable = getResources().getDrawable(R.drawable.b_newcare_tabbar_press);  
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight()); 
			mTvFavorite.setCompoundDrawables(null, topDrawable, null, null);
			mTvFavorite.setTextColor(Color.rgb(255, 0, 0));
			break;
		default:
			break;
		}

	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private boolean isExit = false;
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    public void selectPager(int parentPager, int childPager) {
    	mViewPager.setCurrentItem(parentPager);
    	if(parentPager == 0) {
    		mParentFragment.sellectPager(this, childPager);
    	}
    }
}