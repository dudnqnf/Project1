package com.takebox.wedding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MemberProvisionActivity extends HttpExceptionActivity{

	Intent intent;
	WebView mWebView;
	RelativeLayout title_bar;
	FrameLayout backBtn;
	String title;
	String url;
	
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_provision);
		
		intent = getIntent();
		title = intent.getStringExtra("title");
		url = intent.getStringExtra("url");
		
		createUI();
	}
	
	
	public void createUI(){
		
		title_bar = (RelativeLayout)findViewById(R.id.title_bar);
		if(title.equals("서비스 이용약관"))
			title_bar.setBackgroundResource(R.drawable.title_provision1);
		if(title.equals("개인정보 취급방침"))
			title_bar.setBackgroundResource(R.drawable.title_provision2);
		
		mWebView =(WebView)findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebviewSetting());
		WebSettings webset = mWebView.getSettings();
		webset.setJavaScriptCanOpenWindowsAutomatically(true);
		webset.setBuiltInZoomControls(true);
		webset.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webset.setSupportMultipleWindows(false);
		webset.setSupportZoom(false);
		webset.setBlockNetworkImage(false);
		webset.setUseWideViewPort(true);
		webset.setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.loadUrl(url);
		
		backBtn = (FrameLayout)findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
