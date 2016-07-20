package com.tricycle.news.newslist;

import java.util.List;

import com.tricycle.news.database.NewsBean;


public interface INewsListView {

	void showNewsList(List<NewsBean> newsInfoList);
	void loadMoreNewsList(List<NewsBean> newsInfoList);
}
