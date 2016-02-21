package com.tricycle.android.webview;

import com.tricycle.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class WebActivity extends Activity {

	private WebView webview;
	private EditText editText;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		webview = (WebView)findViewById(R.id.webview);
		webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                     
                    return false;
                     
            }
		});
		//֧��javascript
		webview.getSettings().setJavaScriptEnabled(true); 
		// ���ÿ���֧������ 
		webview.getSettings().setSupportZoom(true); 
		// ���ó������Ź��� 
		webview.getSettings().setBuiltInZoomControls(true);
		//�������������
		webview.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setLoadWithOverviewMode(true);
		
		editText = (EditText)findViewById(R.id.editText1);
		button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = editText.getText().toString();
				if(url == null || url.equals("")) {
				    url = "http://www.baidu.com/";
				} else if(!url.startsWith("http://") && !url.startsWith("https://")) {
				    url = "http://" + url;
				}
				webview.loadUrl(url);
			}
		});
	}	
}
