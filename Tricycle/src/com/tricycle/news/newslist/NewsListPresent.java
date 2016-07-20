package com.tricycle.news.newslist;

import java.util.List;

import com.tricycle.news.BackgroundHandlerThread;
import com.tricycle.news.database.NewsBean;
import com.tricycle.news.database.NewsDao;

import android.content.Context;
import android.os.Handler;


public class NewsListPresent implements NewsListListener {
	
	public static final int MSG_GET_INIT_NEWSLIST = 0;
	public static final int MSG_LOAD_NEWSLIST = 1;
	public static final int MSG_LOAD_MORE_NEWSLIST = 2;
	

	INewsListView mNewsListView;
	INewsListModel mNewsListModel;
	List<NewsBean> mNewsInfoList;
	private Handler mHandler = BackgroundHandlerThread.getInstance().getHandler();
	Context mContext;
	NewsDao mNewsDao;
	int mNewsType;
	
	public NewsListPresent(INewsListView newsListView, Context context, int newsType) {
		mNewsListView = newsListView;
		mContext = context;
		mNewsDao = new NewsDao(mContext);
		mNewsType = newsType;
		mNewsListModel = new TianxingNewsListModel(mContext, newsType, this);

	}
	public void getInitNewsList() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mNewsListView.showNewsList(getNewsFromDatabase(0, 20));
			}
		});
	}
	public void loadNewsList() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mNewsListModel.getNewsList();
			}
		});
	}
	public void loadMoreNewsList(final int start, final int num) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mNewsListView.loadMoreNewsList(getNewsFromDatabase(start, num));
			}
		});
	}
	private List<NewsBean> getNewsFromDatabase(int start, int num) {
		return mNewsDao.getNewsList(mNewsType, start, num);
	}
	@Override
	public void onNewsListGet() {
		mNewsListView.showNewsList(getNewsFromDatabase(0, 20));
	}
}
