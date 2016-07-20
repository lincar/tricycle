package com.tricycle.news.newsdetail;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.tricycle.news.R;
import com.tricycle.news.utils.BitmapCache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	ImageLoader mImageLoader;
	RequestQueue mQueue;
	private LayoutInflater mInflater;
	private News mNews;
	public NewsAdapter(Context context) {
		mInflater = LayoutInflater.from(context); 
		mQueue = Volley.newRequestQueue(context); 
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
	}

	public void setData(News news) {
		mNews = news;
	}
	@Override
	public int getCount() {
		if(mNews == null) {
			return 0;
		}
		return mNews.getNews().size();
	}

	@Override
	public Object getItem(int position) {
		if(mNews == null) {
			return null;
		}
		return mNews.getNews().get(position);
	}

	@Override
	public int getItemViewType(int position) {
		NewsEntry newsEntry = (NewsEntry)getItem(position);
		if(newsEntry == null) {
			return -1;
		}
		return newsEntry.type;
	}

	@Override
	public int getViewTypeCount() {
		return NewsEntry.NAX_TYPE;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsEntry newsEntry = (NewsEntry)getItem(position);
		if(newsEntry == null) {
			return null;
		}
		ViewHolder viewHolder;
		switch(getItemViewType(position)) {
		case NewsEntry.TYPE_TITLE:
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.news_title_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.title = (TextView) convertView.findViewById(R.id.news_title_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.title.setText(newsEntry.title);
			break;
		case NewsEntry.TYPE_CONTENT:
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.news_content_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.content = (TextView) convertView.findViewById(R.id.news_content_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.content.setText(newsEntry.content);
			break;
		case NewsEntry.TYPE_IMAGE:
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.news_image_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView.findViewById(R.id.news_image_image);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ImageListener listener = ImageLoader.getImageListener(viewHolder.image, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
			mImageLoader.get(newsEntry.imageUrl, listener);
			break;
		default:
			break;
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView title;
		public TextView content;
		public ImageView image;
	}

}
