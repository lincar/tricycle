package com.tricycle.news.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class NewsDao {
	private DatabaseHelper helper;
	private Dao<NewsBean, Integer> newsDao;
	private Context mContext;
	public NewsDao(Context context) {
		mContext = context;
		helper = DatabaseHelper.getHelper(context);
		try {
			newsDao = helper.getNewsDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean insert(NewsBean news) {
		return insert(news.title, news.description, news.time, news.url, news.picUrl, news.type);
	}
	public boolean insert(String title, String description, String time, String url, String picUrl, int type) {
		NewsBean news = new NewsBean(title, description, time, picUrl, url, type);
		if(-1 == isExist(news)) {
			try {
				newsDao.create(news);
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
    public int isExist(NewsBean news) {
    	try {
    		List<NewsBean> newsList = newsDao.queryBuilder().where().eq("url", news.url).query();
    		if(newsList != null && newsList.size() > 0) {
    			return newsList.get(0).id;
    		}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return -1;
    }
	public void addComment(NewsBean news, String comment) {
    	if(news == null) {
    		return;
    	}
    	NewsBean newsBean = getNewsByUrl(news.url);
    	if(newsBean != null) {
    		newsBean.addComment(comment);
    		try {
				newsDao.update(newsBean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}

	}
    public boolean hasRead(NewsBean news) {
    	if(news == null) {
    		return false;
    	}
    	NewsBean newsBean = getNewsByUrl(news.url);
    	if(newsBean != null) {
    		return newsBean.hasRead;
    	}
    	return false;
    }
    public void setRead(NewsBean news, boolean hasRead) {
    	if(news == null) {
    		return;
    	}
    	NewsBean newsBean = getNewsByUrl(news.url);
    	if(newsBean != null) {
    		newsBean.hasRead = hasRead;
    		try {
				newsDao.update(newsBean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    public boolean isFavorite(NewsBean news) {
    	if(news == null) {
    		return false;
    	}
    	NewsBean newsBean = getNewsByUrl(news.url);
    	if(newsBean != null) {
    		return newsBean.isFavorite;
    	}
    	return false;
    }
    public void setFavorite(NewsBean news, boolean isFavorite) {
    	if(news == null) {
    		return;
    	}
    	NewsBean newsBean = getNewsByUrl(news.url);
    	if(newsBean != null) {
    		newsBean.isFavorite = isFavorite;
    		try {
				newsDao.update(newsBean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    public NewsBean getNewsByUrl(String url) {
    	try {
    		List<NewsBean> newsList = newsDao.queryBuilder().where().eq("url", url).query();
    		if(newsList != null && newsList.size() > 0) {
    			for(NewsBean news : newsList) {
    				Log.i("lcw", ": " + news.title + " ||| " + news.url);
    			}
    				
    			return newsList.get(0);
    		}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public List<NewsBean> getFavoriteNewsList(int start, int num) {
    	int end = start + num;
    	List<NewsBean> newsList = new ArrayList<NewsBean>();
    	try {
			//newsList = newsDao.queryBuilder().orderBy("id", false).where().eq("isFavorite", true).query();
			Where<NewsBean, Integer> where = newsDao.queryBuilder().orderBy("id", false).where();
			newsList = where.or(where.eq("isFavorite", true), where.not(where.eq("commentJson", ""))).query();
			Log.i("lcw", newsList.toString());
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	if(start >= newsList.size()) {
    		return null;
    	}
    	if(end >= newsList.size()) {
    		end = newsList.size();
    	}
    	return newsList.subList(start, end);
    }

    public List<NewsBean> getNewsList(int type, int start, int num) {
    	int end = start + num;
    	List<NewsBean> newsList = getNewsList(type);
    	if(start >= newsList.size()) {
    		return null;
    	}
    	if(end >= newsList.size()) {
    		end = newsList.size() - 1;
    	}
    	return newsList.subList(start, end);
    }
    public List<NewsBean> getNewsList(int type) {
    	List<NewsBean> newsList = new ArrayList<NewsBean>();
    	try {
			newsList = newsDao.queryBuilder().orderBy("id", false).where().eq("type", type).query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return newsList;
    }
    public NewsBean getNewsById(int id) {
    	try {
			return newsDao.queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public String getTitle(int id) {
    	try {
			NewsBean news = newsDao.queryForId(id);
			return news.title;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "null";
    	
    }
    
    
}
