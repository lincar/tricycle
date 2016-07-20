//package com.tricycle.news.newslist;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.tricycle.news.database.NewsBean;
//
//public class ShowApiNewsListModel implements INewsListModel {
////	private static final String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
////	private static final String httpArg = "num=20&page=1";
//	String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
//	String httpArg = "channelId=5572a108b3cdc86cf39001cd&page=1&needContent=0&needHtml=0";
//	private RequestQueue mQueue;
//	private NewsListListener mListener;
//	private List<NewsBean> mNewsInfoList = new ArrayList<NewsBean>();
//
//	public ShowApiNewsListModel(Context context, NewsListListener listener) {
//		mQueue = Volley.newRequestQueue(context);
//		mListener = listener;
//	}
//	@Override
//	public void getNewsList() {
//		request();
//	}
//	
//	void request() {
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(httpUrl + "?" + httpArg, null,
//				new Response.Listener<JSONObject>() {  
//			@Override  
//			public void onResponse(JSONObject response) {  
//				Log.i("lcw", response.toString());
//				getNewsList(response);
//				mListener.onNewsListGet(mNewsInfoList);
//			}  
//		}, new Response.ErrorListener() {  
//			@Override  
//			public void onErrorResponse(VolleyError error) {  
//				Log.e("TAG", error.getMessage(), error);  
//			}  
//		}) {
//			@Override
//			public Map<String, String> getHeaders()
//					throws AuthFailureError {
//				Map<String, String> headers = new HashMap<String, String>();
//				headers.put("apikey",  "9b53b7bc47078ee2e8a414108d3042c2");
//				return headers;
//			}
//		};
//		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 2, 1.0f));
//		mQueue.add(jsonObjectRequest);
//	}
//	
//	void getNewsList(JSONObject jsonObject) {
//		try {
//			JSONObject body = jsonObject.getJSONObject("showapi_res_body");
//			Log.i("lcw", body.toString());
//			JSONObject pagebean = body.getJSONObject("pagebean");
//			Log.i("lcw", pagebean.toString());
////			JSONObject contentlist = pagebean.getJSONObject("contentlist");
////			Log.i("lcw", contentlist.toString());
//			JSONArray jsonArray = jsonObject.getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("contentlist");
//			for(int i=0; i<jsonArray.length(); i++) {
//				JSONObject newsObject = (JSONObject)jsonArray.get(i);
//				NewsBean news = parseNews(newsObject);
//				mNewsInfoList.add(news);
//				
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	private NewsBean parseNews(JSONObject news) {
//		String time = null;
//		String title = null;
//		String description = null;
//		String picUrl = null;
//		String url = null;
//		try {
//			time = news.getString("pubDate");
//			title = news.getString("title");
//			description = news.getString("source");
//			JSONArray picUrls = news.getJSONArray("imageurls");
//			if(picUrls.length() > 0) {
//				picUrl = ((JSONObject)picUrls.get(0)).getString("url");
//			}
//			url = news.getString("link");
//	
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new NewsBean(time, title, description, picUrl, url, 0);
//	}
//
//}
