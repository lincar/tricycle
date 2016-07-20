package com.tricycle.news.newslist;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.tricycle.news.R;
import com.tricycle.news.database.NewsBean;
import com.tricycle.news.newsdetail.NewsActivity;
import com.tricycle.news.utils.BitmapCache;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewsFragment extends Fragment implements OnItemClickListener,
		OnRefreshListener, OnScrollListener, INewsListView {
	private static final int MSG_SHOW_NEWSLIST = 0;
	private static final int MSG_LOAD_MORE_NEWSLIST = 1;

	private ListView mListView;
	private View mFooterView;
	private SimpleAdapter mAdapter;
	private List<Map<String, Object>> mNewsMapList = new ArrayList<Map<String, Object>>();
	static final String[] KEYS = {"time", "title", "description"};
	static final int[] VIEWS = {R.id.news_listview_time, R.id.news_listview_title, R.id.news_listview_description};
	
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	NewsListPresent mNewsListPresent;
	private int mNewsType = 0;
	private boolean mIsRefresh = false;
	SwipeRefreshLayout mSwipeRefreshLayout;
	public NewsFragment(int type) {
		mNewsType = type;
	}
	public NewsFragment() {

	}
	public String getTitle() {
		return TianxingNewsListModel.getTitle(mNewsType);
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_fragment, null);
		mQueue = Volley.newRequestQueue(getContext()); 
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mNewsListPresent = new NewsListPresent(this, getContext(), mNewsType);
		initListView(view);
		mNewsListPresent.getInitNewsList();
		mNewsListPresent.loadNewsList();
		return view;
	}
	private void initListView(View view) {

		mAdapter = new SimpleAdapter(getContext(), mNewsMapList, R.layout.news_listview, KEYS, VIEWS) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.i("lcw", "getView, position:" + position);
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_listview, null);
				ImageView imageView  = (ImageView) convertView.findViewById(R.id.news_listview_image);
				ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.tabbar_icon_picture_pressed, R.drawable.tabbar_icon_picture_pressed);
				Map<String, Object> map = (Map<String, Object>)getItem(position);
				String picUrl = (String)map.get("picUrl");
				if(picUrl != null) {
					mImageLoader.get((String)map.get("picUrl"), listener);
				}
				return super.getView(position, convertView, parent);
			}

		};
		mListView = (ListView) view.findViewById(R.id.news_listview);
		mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.footer_item, null);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
		mListView.addFooterView(mFooterView);
		mFooterView.setVisibility(View.GONE);
		mListView.setAdapter(mAdapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent.getId() == R.id.news_listview) {
			Log.i("lcw", "onItemClick position: " + position);
			String url = (String)mNewsMapList.get(position).get("url");
			Intent intent = new Intent();
			intent.setClass(getContext().getApplicationContext(), NewsActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
		}
	}

	@Override
	public void showNewsList(final List<NewsBean> newsInfoList) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				handleShowNewsList(newsInfoList);
			}
		});
	}
	@Override
	public void loadMoreNewsList(final List<NewsBean> newsInfoList) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				handleLoadMoreNewsList(newsInfoList);
			}
		});
	}
	@Override
	public void onRefresh() {
		mIsRefresh = false;
		mNewsListPresent.loadNewsList();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(mIsRefresh == false) {
					Toast.makeText(getContext(), "No network", Toast.LENGTH_LONG).show();
				}
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}, 10000);
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	private boolean IsLoading = false;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(IsLoading) {
			return;
		}
		if(firstVisibleItem + visibleItemCount == totalItemCount) {
			IsLoading = true;
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					IsLoading = false;
				}
			}, 2000);
			mFooterView.setVisibility(View.VISIBLE);
			mNewsListPresent.loadMoreNewsList(totalItemCount, 20);
		}
	}


	private void handleShowNewsList(List<NewsBean> newsInfoList) {
		if(newsInfoList == null) {
			return;
		}
		mNewsMapList.clear();
		for(NewsBean newsInfo : newsInfoList) {
			mNewsMapList.add(newsInfo.getMap());
			Log.i("lcw",newsInfo.getMap().toString() );
		}
		mIsRefresh = true;
		mSwipeRefreshLayout.setRefreshing(false);
		mAdapter.notifyDataSetChanged();
	}
	private void handleLoadMoreNewsList(List<NewsBean> newsInfoList) {
		if(newsInfoList == null) {
			return;
		}
		for(NewsBean newsInfo : newsInfoList) {
			mNewsMapList.add(newsInfo.getMap());
		}
		mFooterView.setVisibility(View.GONE);
		mAdapter.notifyDataSetChanged();
	}
}
