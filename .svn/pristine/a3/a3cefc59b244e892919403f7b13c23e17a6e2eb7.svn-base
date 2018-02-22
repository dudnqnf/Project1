package com.takebox.wedding;

import org.apache.http.util.EncodingUtils;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * @author sujong
 * 공지사항 
 */
public class NoticeActivity extends Activity {

	
	final String url = "http://app.takebox.co.kr/notice.global";
	
	FrameLayout backBtn;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_notice);
	    createUI();
	  
			
	}
	
	public void createUI(){
		
		//웹뷰
	    WebView mWebView = (WebView)findViewById(R.id.qna_web);
	    mWebView.loadUrl(url);
	    mWebView.setWebViewClient(new WebviewSetting());
	    WebSettings webset = mWebView.getSettings();
	    webset.setJavaScriptEnabled(true);
	    webset.setJavaScriptCanOpenWindowsAutomatically(true);
	    webset.setBuiltInZoomControls(true);
	    webset.setPluginState(WebSettings.PluginState.ON_DEMAND);
	    webset.setSupportMultipleWindows(false);
	    webset.setSupportZoom(false);
	    webset.setBlockNetworkImage(false);
	    webset.setLoadsImagesAutomatically(true);
	    webset.setUseWideViewPort(true);
	    webset.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		backBtn = (FrameLayout)findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}
	
	@Override
	public void onStop() {
		super.onStop();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	
	}
	
	
}
	
