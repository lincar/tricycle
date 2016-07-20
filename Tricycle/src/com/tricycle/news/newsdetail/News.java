package com.tricycle.news.newsdetail;

import java.util.ArrayList;
import java.util.List;

public class News {

	private List<NewsEntry> mNews = new ArrayList<NewsEntry>();
	
	public News addEntry(NewsEntry newsEntry) {
		mNews.add(newsEntry);
		return this;
	}
	
	public List<NewsEntry> getNews() {
		return mNews;
	}
	public News clear() {
		mNews.clear();
		return this;
	}
}
