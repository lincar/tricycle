package com.tricycle.news.newslist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tricycle.news.database.NewsBean;
import com.tricycle.news.database.NewsDao;
import com.tricycle.news.utils.NetWorkUtils;

public class TianxingNewsListModel implements INewsListModel {
	//private static final String httpUrl = "http://apis.baidu.com/txapi/social/social";
	private static final String httpArg = "num=50&page=1";
	
	private static final String SOCIAL_URL = "http://apis.baidu.com/txapi/social/social";
	private static final String KEJI_URL = "http://apis.baidu.com/txapi/keji/keji";
	private static final String TIYU_URL = "http://apis.baidu.com/txapi/tiyu/tiyu";
	public static final int TYPE_SOCIAL = 0;
	public static final int TYPE_KEJI = 1;
	public static final int TYPE_TIYU = 2;
	String httpUrl = SOCIAL_URL;
	private Context mContext;
	private RequestQueue mQueue;
	private NewsListListener mListener;
	private int mNewsType;
	private NewsDao mNewsDao;

	public TianxingNewsListModel(Context context, int newsType, NewsListListener listener) {
		mContext = context;
		mQueue = Volley.newRequestQueue(context);
		mListener = listener;
		mNewsType = newsType;
		mNewsDao = new NewsDao(context);
		setUrlFromType(newsType);
	}
	@Override
	public void getNewsList() {
		if(NetWorkUtils.isNetworkAvailable(mContext)) {
			request();
		}
	}
	
	public void setUrlFromType(int type) {
		switch (type) {
		case TYPE_SOCIAL:
			httpUrl = SOCIAL_URL;
			break;
		case TYPE_KEJI:
			httpUrl = KEJI_URL;
			break;
		case TYPE_TIYU:
			httpUrl = TIYU_URL;
			break;
		default:
			break;
		}
	}
	public static String getTitle(int type) {
		switch (type) {
		case TYPE_SOCIAL:
			return "社会";
		case TYPE_KEJI:
			return "科技";
		case TYPE_TIYU:
			return "体育";
		default:
			return "新闻";
		}
	}
	void request() {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(httpUrl + "?" + httpArg, null,
				new Response.Listener<JSONObject>() {  
			@Override  
			public void onResponse(JSONObject response) {  
				Log.i("lcw", response.toString());
				getNewsList(response);
				mListener.onNewsListGet();
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				Log.e("TAG", error.getMessage(), error);  
			}  
		}) {
			@Override
			public Map<String, String> getHeaders()
					throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("apikey",  "9b53b7bc47078ee2e8a414108d3042c2");
				return headers;
			}
		};
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 2, 1.0f));
		mQueue.add(jsonObjectRequest);
	}
	
	void getNewsList(JSONObject jsonObject) {
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("newslist");
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject newsObject = (JSONObject)jsonArray.get(i);
				NewsBean news = new NewsBean(newsObject, mNewsType);
				mNewsDao.insert(news);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
