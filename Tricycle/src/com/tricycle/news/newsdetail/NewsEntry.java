package com.tricycle.news.newsdetail;

public class NewsEntry {

	public static final int TYPE_TITLE = 0;
	public static final int TYPE_CONTENT = 1;
	public static final int TYPE_IMAGE = 2;
	public static final int NAX_TYPE = 3;
	public int type;
	public String title;
	public String content;
	public String imageUrl;
	
	public NewsEntry(int type, String arg) {
		this.type = type;
		switch(type) {
		case TYPE_TITLE:
			title = arg;
			break;
		case TYPE_CONTENT:
			content = "        " + arg;
			break;
		case TYPE_IMAGE:
			imageUrl = arg;
			break;
		default:
			break;
		}
	}
	
}
