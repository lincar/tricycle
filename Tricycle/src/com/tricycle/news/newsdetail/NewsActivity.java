package com.tricycle.news.newsdetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tricycle.news.MainActivity;
import com.tricycle.news.R;
import com.tricycle.news.database.NewsBean;
import com.tricycle.news.database.NewsDao;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsActivity extends Activity implements OnClickListener, OnCommentSetListener {

	private static final int NEWS_SERVER_TENCENT = 0;
	private static final int NEWS_SERVER_IFENG = 1;
	private ListView mListView;
	private News mNews = new News();
	private NewsAdapter mAdapter;
	private ListView mCommentListView;
	private ArrayAdapter<String> mCommentAdapter;
	RequestQueue mQueue;
	private NewsDao mNewsDao;
	private String mStrUrl;
	private NewsBean mNewsBean;
	private DrawerLayout mDrawerLayout;
	private ImageButton mIbTopUser;
	TextView commentTextView;
	ImageView favoriteIamge;
	ImageView disfavoriteIamge;
	ImageView shareImage;
	ImageView commentGotoImage;
	TextView commentGotoTextView;
	CommentPopupWindow mCommentPopupWindow;
	ScrollView mScrollView;
	TextView mCommentTitleTextView;
	TextView mTitleTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_activity);
		mListView = (ListView) findViewById(R.id.news_activity_listview);
		mScrollView = (ScrollView) findViewById(R.id.news_activity_scroll);

		mQueue = Volley.newRequestQueue(this);
		mNewsDao = new NewsDao(this);
		mStrUrl = getIntent().getStringExtra("url");
		Log.i("lcw", mStrUrl);
		mNewsBean = mNewsDao.getNewsByUrl(mStrUrl);
		initTopTitle();
		setNewsRead();
		setRequest();
		mAdapter = new NewsAdapter(this);
		mAdapter.setData(mNews);
		mListView.setAdapter(mAdapter);
		initBottom();
		initDrawer();
		initComment();
		mCommentPopupWindow = new CommentPopupWindow(this, this);
		ShareSDK.initSDK(this, "130a49604e4f0"); 
	}
	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.news_drawer_layout);
		mIbTopUser = (ImageButton) findViewById(R.id.ib_top_user);
		mIbTopUser.setOnClickListener(this);
	}
	private void initBottom() {
		commentTextView = (TextView) findViewById(R.id.detail_bottom_comment);
		commentTextView.setOnClickListener(this);
		favoriteIamge = (ImageView) findViewById(R.id.detail_bottom_favorite);
		favoriteIamge.setOnClickListener(this);
		disfavoriteIamge = (ImageView) findViewById(R.id.detail_bottom_disfavorite);
		disfavoriteIamge.setOnClickListener(this);
		shareImage = (ImageView) findViewById(R.id.detail_bottom_share);
		shareImage.setOnClickListener(this);
		commentGotoImage = (ImageView)findViewById(R.id.detail_bottom_goto_comment);
		commentGotoImage.setOnClickListener(this);
		commentGotoTextView = (TextView)findViewById(R.id.detail_bottom_goto_comment_text);
		int commentCount = mNewsBean.getComments().size();
		if(commentCount > 0) {
			commentGotoTextView.setText("" + mNewsBean.getComments().size());
			commentGotoTextView.setVisibility(View.VISIBLE);
		}
		if(mNewsBean.isFavorite) {
			favoriteIamge.setVisibility(View.GONE);
			disfavoriteIamge.setVisibility(View.VISIBLE);
		} else {
			disfavoriteIamge.setVisibility(View.GONE);
			favoriteIamge.setVisibility(View.VISIBLE);
		}
	}
	private void initComment() {
		mCommentListView = (ListView) findViewById(R.id.news_activity_comment_listview);
		mCommentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		mCommentAdapter.addAll(mNewsBean.getComments());
		mCommentListView.setAdapter(mCommentAdapter);
		mCommentTitleTextView = (TextView)findViewById(R.id.news_activity_comment_title);
	}
	private void initTopTitle() {
		String typeTitle = "新闻";
		switch (mNewsBean.type) {
		case 0:
			typeTitle = "社会新闻";
			break;
		case 1:
			typeTitle = "科技新闻";
			break;
		case 2:
			typeTitle = "体育新闻";
			break;

		default:
			break;
		}
		mTitleTextView = (TextView)findViewById(R.id.ib_top_title);
		mTitleTextView.setText(typeTitle);
	}
	private void setNewsRead() {
		mNewsDao.setRead(mNewsBean, true);
	}
	void setRequest() {
		StringRequest stringReques = new StringRequest(mStrUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				parseNews(response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("lcw", error.toString());
			}
		});
		mQueue.add(stringReques);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail_bottom_comment:
			mCommentPopupWindow.showPopupWindow(v);
			break;
		case R.id.detail_bottom_goto_comment:
			int[] location = new int[2];
			mCommentTitleTextView.getLocationOnScreen(location);
			int[] scrollViewLocation = new int[2];
			mScrollView.getLocationOnScreen(scrollViewLocation);
			int offset = location[1] - scrollViewLocation[1];
			mScrollView.scrollBy(0, offset);
			break;
		case R.id.detail_bottom_favorite:
			mNewsDao.setFavorite(mNewsBean, true);
			favoriteIamge.setVisibility(View.GONE);
			disfavoriteIamge.setVisibility(View.VISIBLE);
			break;
		case R.id.detail_bottom_disfavorite:
			mNewsDao.setFavorite(mNewsBean, false);
			disfavoriteIamge.setVisibility(View.GONE);
			favoriteIamge.setVisibility(View.VISIBLE);
			break;
		case R.id.detail_bottom_share:
			showShare();
			break;
		case R.id.ib_top_user:
			mDrawerLayout.openDrawer(Gravity.START);
			break;
		default:
			break;
		}
	}
//	void setNewsFavorite() {
//		new NewsDao(this).isExist(new NewsBean(null, null, null, null, mStrUrl, 0));
//	}
	void parseNews(String response) {
		mNews.clear(); 
		switch (getNewsServer()) {
		case NEWS_SERVER_TENCENT:
			parseTencentNews(response);
			break;
		case NEWS_SERVER_IFENG:
			parseIfengNews(response);
			break;
		default:
			parseTencentNews(response);
			break;
		}
		mAdapter.notifyDataSetChanged();

	}
	private int getNewsServer() {
		if(mStrUrl == null) {
			return -1;
		}
		if(mStrUrl.contains(".qq.com")) {
			return NEWS_SERVER_TENCENT;
		} else if(mStrUrl.contains(".ifeng.com")) {
			return NEWS_SERVER_IFENG;
		}
		return NEWS_SERVER_TENCENT;
	}
	void parseTencentNews(String response) {
		mNews.clear(); 
		Document doc = Jsoup.parse(response);
		Elements titleEles = doc.select("h1");
		if(titleEles != null && titleEles.size() > 0) {
			Element titleEle = titleEles.get(0);
			Log.i("lcw", "title:" + titleEle.text());
			mNews.addEntry(new NewsEntry(NewsEntry.TYPE_TITLE, titleEle.text()));
		}
		Elements contents = doc.select("[class=bd][accesskey=3]");
		Elements elements;
		if(contents.size() > 0) {
			Element content = contents.get(0);
			elements = content.select("p");
		} else {
			elements = doc.select("p");
		}		
		for(Element element : elements) {
			Elements imgEles = element.getElementsByTag("img");
			for(Element img : imgEles) {
				if(img.attr("src") != null) {
					mNews.addEntry(new NewsEntry(NewsEntry.TYPE_IMAGE, img.attr("src")));
				}
			}
			mNews.addEntry(new NewsEntry(NewsEntry.TYPE_CONTENT, element.text()));
		}
		mAdapter.notifyDataSetChanged();

	}
	void parseIfengNews(String response) {
		mNews.clear(); 
		Document doc = Jsoup.parse(response);
		Elements titleEles = doc.select("h1");
		if(titleEles != null && titleEles.size() > 0) {
			Element titleEle = titleEles.get(0);
			Log.i("lcw", "title:" + titleEle.text());
			mNews.addEntry(new NewsEntry(NewsEntry.TYPE_TITLE, titleEle.text()));
		}
		Elements contents = doc.select("[id=main_content]");
		Elements elements;
		if(contents.size() > 0) {
			Element content = contents.get(0);
			elements = content.select("p");
		} else {
			elements = doc.select("p");
		}		
		for(Element element : elements) {
			Elements imgEles = element.getElementsByTag("img");
			for(Element img : imgEles) {
				if(img.attr("src") != null && !img.attr("src").endsWith("icon_logo.gif")) {
					mNews.addEntry(new NewsEntry(NewsEntry.TYPE_IMAGE, img.attr("src")));
				}
			}
			mNews.addEntry(new NewsEntry(NewsEntry.TYPE_CONTENT, element.text()));
		}
		mAdapter.notifyDataSetChanged();

	}

	 private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 
		 // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(mNewsBean.title);
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(mNewsBean.url);
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText(getString(R.string.app_name) + "分享");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath(mNewsBean.picUrl);//确保SDcard下面存在此张图片
		 oks.setImageUrl(mNewsBean.picUrl);
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl(mNewsBean.url);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 //oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 //oks.setSiteUrl("http://sharesdk.cn");
		 
		 // 启动分享GUI
		 oks.show(this);
	 }
	@Override
	public void onCommentSet(String comment) {
		if(comment == null || comment.length() == 0) {
			return;
		}
		Toast.makeText(this, "已保存评论", Toast.LENGTH_LONG).show();
		mNewsBean.addComment(comment);
		mNewsDao.addComment(mNewsBean, comment);
		mCommentAdapter.add(comment);
		mCommentAdapter.notifyDataSetChanged();
		commentGotoTextView.setText("" + mNewsBean.getComments().size());
		commentGotoTextView.setVisibility(View.VISIBLE);
	}

}
