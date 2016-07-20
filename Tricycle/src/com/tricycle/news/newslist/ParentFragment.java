package com.tricycle.news.newslist;

import java.util.ArrayList;
import java.util.List;

import com.tricycle.news.MainActivity;
import com.tricycle.news.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

public class ParentFragment extends Fragment implements OnClickListener {
	

//	private String mName = "";
	private List<Fragment> mChildFragments = new ArrayList<Fragment>();
	
	private ViewPager mViewPager;
	private TabPageIndicator mTab;
	private FragmentPagerAdapter mAdapter;
	private ImageButton mIbTopUser;
	private Handler mHandler;
	public ParentFragment() {
		initChildFragments();
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.parent_fragment, null);
		mHandler = new Handler(getActivity().getMainLooper());
		mViewPager = (ViewPager) view.findViewById(R.id.main_content);
		mTab = (TabPageIndicator) view.findViewById(R.id.indicator);
		mIbTopUser = (ImageButton) view.findViewById(R.id.ib_top_user);
		mIbTopUser.setOnClickListener(this);
		mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mChildFragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mChildFragments.get(arg0);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return ((NewsFragment)getItem(position)).getTitle();
			}
		};
		mViewPager.setAdapter(mAdapter);
		mTab.setViewPager(mViewPager);
		return view;
	}

	private void initChildFragments() {
		mChildFragments.add(new NewsFragment(1));
		mChildFragments.add(new NewsFragment(2));
		mChildFragments.add(new NewsFragment(0));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_top_user:
			MainActivity activity = (MainActivity)getActivity();
			activity.showLeftDrawer();
			break;

		default:
			break;
		}
		
	}
	
	public void sellectPager(Context context, final int position) {
		if(mHandler == null) {
			mHandler = new Handler(context.getMainLooper());
		}
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				handleSellectPager(position);
			}
		});
	}
	
	private void handleSellectPager(int position) {
		if(mViewPager != null && mAdapter != null && mAdapter.getCount() > position) {
			mViewPager.setCurrentItem(position);
		}
	}

}
