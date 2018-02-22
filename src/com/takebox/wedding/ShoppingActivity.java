package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.WeddingModel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ShoppingActivity extends HttpExceptionActivity{
	
	private ListView listView;
	private WebView mWebView; 
	private RelativeLayout webviewLayout;
	private ArrayList<ShoppingGridItem> shoppingGridItem;
	private JSONArray responseDataItems;
	private Thread thread= null;
	private Handler handler;
	private ShoppingGridAdapter shoppingGridAdapter;
	
	private final int SHOPPING_DATA_PARSE_SUCCESS = 870219;
	private final int SHOPPING_DATA_PARSE_FAIL = 870200;
	private boolean webOnOffFlug = false;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);

		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.arg1 == SHOPPING_DATA_PARSE_SUCCESS){
					setShoppingList();
					pd.dismiss();
				}else if(msg.arg1 == SHOPPING_DATA_PARSE_FAIL){
					warningHTTPErrorMsgDialog();
				}
			}
		};
		createUI();
		processParsing(thread, shoppingList);
	}

	private final Runnable shoppingList = new Runnable() {
		@Override
		public void run() {
			parseShoppingDate();
		}
	};
	
	public void parseShoppingDate(){
		Map<String, String> data = new HashMap<String, String>();
		JSONObject res = WeddingModel.loadShoppingInfo(data);
		
		if (!isHttpWorthCheck(res)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {
			try {
				if (res.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
				Message msg = new Message();
				if(res.getString("info").equals("ok")){
					responseDataItems = new JSONArray(res.getString("shopList"));
					msg.arg1 = SHOPPING_DATA_PARSE_SUCCESS;	
				}else{
					msg.arg1 = SHOPPING_DATA_PARSE_FAIL;
				}
				handler.sendMessage(msg);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	

	public void onItemClick(String url){
		webOnOffFlug = true;
		webViewVisible(webOnOffFlug);
		mWebView.loadUrl(url);
//	    mWebView.scrollTo(0, 80);
	}
	
	public void webViewVisible(boolean onOff){
		if(onOff){
			webviewLayout.setVisibility(View.VISIBLE);
		}else{
			webviewLayout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (webOnOffFlug) {
			webOnOffFlug = false;
			webViewVisible(webOnOffFlug);
			mWebView.clearView();
		}else{
			finish();
		}
	}
	
	public void createUI(){
		listView = (ListView) findViewById(R.id.shopping_list);
		
		mWebView = (WebView) findViewById(R.id.webview);
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
		webviewLayout = (RelativeLayout)findViewById(R.id.webview_layout);
		webViewVisible(webOnOffFlug);
	}

	private void setShoppingList() {
		// TODO Auto-generated method stub
		shoppingGridItem = new ArrayList<ShoppingGridItem>();
		int list_cnt = responseDataItems.length();

		for (int i = 0; i < list_cnt; i++) {
			try {
				JSONObject obj = responseDataItems.getJSONObject(i);

				ShoppingGridItem item = new ShoppingGridItem();
				item.img_file_name = obj.getString("file_name");
				item.title = obj.getString("title");
				item.no = obj.getString("shop_no");
				item.url = obj.getString("src");
				item.img = Info.MASTER_FILE_URL + "/image/"
						+ item.img_file_name;
				shoppingGridItem.add(item);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 쇼핑리스트
		shoppingGridAdapter = new ShoppingGridAdapter(ShoppingActivity.this, R.layout.shopping_grid, shoppingGridItem);
		listView.setAdapter(shoppingGridAdapter);
	}
	
	public void btnBack(View view){
		if (webOnOffFlug) {
			webOnOffFlug = false;
			webViewVisible(webOnOffFlug);
		}else{
			finish();
		}
	}
	public void goForward(View view){
		mWebView.goForward();
	}
	public void goBack(View view){
		mWebView.goBack();
	}
	public void refreshWeb(View view){
		mWebView.reload();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}
	
	public class ShoppingGridItem {
		public String img_file_name;	//파일이름
		public String img; //사진주소
		public String title;	//상품명
		public String no;	//숍번호
		public String content;	//설명
		public String url;	//주소
	}

}
