package com.tricycle.news.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tricycle.news.R;

@DatabaseTable(tableName = "tb_news")
public class NewsBean {
	public static int TYPE_SOCIAL = 0;
	public static int TYPE_SCIENCE = 1;
	public static int TYPE_SPORTS = 2;
	
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(columnName = "title")
	public String title;
	@DatabaseField(columnName = "description")
	public String description;
	@DatabaseField(columnName = "time")
	public String time;
	@DatabaseField(columnName = "picUrl")
	public String picUrl;
	@DatabaseField(columnName = "url")
	public String url;
	@DatabaseField(columnName = "commentJson")
	public String commentJson = "";
	@DatabaseField(columnName = "type")
	public int type = TYPE_SOCIAL;
	@DatabaseField(columnName = "hasRead")
	public boolean hasRead = false;
	@DatabaseField(columnName = "isFavorite")
	public boolean isFavorite = false;
	private JSONArray comments = new JSONArray();
	public NewsBean(String title, String description, String time, String picUrl, String url, int type) {
		this.title = title;
		this.description = description;
		this.time = time;
		this.picUrl = picUrl;
		this.url = url;
		this.type = type;
	}
	public NewsBean(JSONObject news, int type) {
		try {
			time = news.getString("ctime");
			title = news.getString("title");
			description = news.getString("description");
			picUrl = news.getString("picUrl");
			url = news.getString("url");
			this.type = type;
	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public NewsBean(){
		
	}

	public void addComment(String comment) {
		if(commentJson == null || commentJson.equals("")) {
			comments = new JSONArray();
		} else {
			try {
				comments = new JSONArray(commentJson);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		comments.put(comment);
		commentJson = comments.toString();
	}
	public List<String> getComments() {
		List<String> commentList = new ArrayList<String>();
		if(commentJson == null) {
			return commentList;
		}
		try {
			comments = new JSONArray(commentJson);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for(int i=0;i<comments.length();i++) {
			try {
				commentList.add((String)comments.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return commentList;
	}
	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", time);
		map.put("title", title);
		map.put("description", description);
		map.put("picUrl", picUrl);
		map.put("url", url);
		map.put("pic", R.drawable.b_newhome_tabbar_press);
		return map;
	}
}
