package com.takebox.wedding;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewSetting extends WebViewClient {    
	 @Override    
	 public boolean shouldOverrideUrlLoading(WebView view, String url) {        
	  view.loadUrl(url);        
	  return true;    
	 }
}

