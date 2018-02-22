package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.takebox.wedding.billing.IabHelper;
import com.takebox.wedding.billing.IabResult;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.AlbumModel;
import com.takebox.wedding.util.CustomTextFont;

public class TemplateEditActivity extends HttpExceptionActivity {
	
	public static final int RESULTCODE = 1;
	private Thread mThread = null;
	private JSONArray item;
	private JSONArray item2;
	public static ViewPager mPager;
	private static ArrayList<TemplateItem> items;
	private static TemplateEditAdapter mAdapter;
	static int post_position = -1;
	private String post_card_type;
	private static WebView mWebView;
	private static String room_id;
	private static boolean cash = false;
	private static String selected_no_str;
	private String wed_seq;
	static String cash_yn = "N";
	private static int leng;
	private ArrayList<String> buy_list;
	
	//인앱결제를 위한 정보들
	String base64EncodedPublicKey = Info.base64EncodedPublicKey;
	IInAppBillingService mService;
	IabHelper mHelper;
	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_template_edit);
	    
	    bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"), mServiceConn, Context.BIND_AUTO_CREATE);

        mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// 구매오류처리 ( 토스트하나 띄우고 결제팝업 종료시키면 되겠습니다 )
				}
					AlreadyPurchaseItems();    // AlreadyPurchaseItems(); 메서드는 구매목록을 초기화하는 메서드입니다. v3으로 넘어오면서 구매기록이 모두 남게 되는데 재구매 가능한 상품( 게임에서는 코인같은아이템은 ) 구매후 삭제해주어야 합니다.  이 메서드는 상품 구매전 혹은 후에 반드시 호출해야합니다. ( 재구매가 불가능한 1회성 아이템의경우 호출하면 안됩니다 )
			}
		});
	    
	    Intent intent = getIntent();
		post_card_type = intent.getStringExtra("card_type");
		room_id = intent.getStringExtra("room_id");
		wed_seq = intent.getStringExtra("wed_seq");
		
		setWebView();
		
		mPager = (ViewPager)findViewById(R.id.pager);
	
	    FrameLayout btn = (FrameLayout)findViewById(R.id.btn_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		FrameLayout arrow_left = (FrameLayout)findViewById(R.id.arrow_left);
		arrow_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPager.getCurrentItem()-1>=0)
					mPager.setCurrentItem(mPager.getCurrentItem()-1);
			}
		});
		FrameLayout arrow_right = (FrameLayout)findViewById(R.id.arrow_right);
		arrow_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPager.getCurrentItem()<mPager.getChildCount())
					mPager.setCurrentItem(mPager.getCurrentItem()+1);
			}
		});
		FrameLayout edit_btn = (FrameLayout)findViewById(R.id.edit_button);
		TextView inner_btn = (TextView)findViewById(R.id.inner_edit_button);
		inner_btn.setTypeface(CustomTextFont.hunsomsatangR);
		edit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//물어보고 구입하기
				if(cash && !buy_list.contains("type"+selected_no_str))
					confirmDialog();
				else
					processParsing(mThread, SaveData);
			}
		});
		
		processParsing(mThread, SetData);
	}

	private final Runnable SetData = new Runnable() {
		@Override
		public void run() {
			SetData();
		}
	};
	
	protected void SetData() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		
		JSONObject obj = AlbumModel.procGetCardType(data);	//카드불러오기
		JSONObject obj2 = AlbumModel.procGetBuyItems(data);	//구입한 것들 불러오기
		
		if(!isHttpWorthCheck(obj)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}else{
			try {
				if(obj.getString("info").equals("session-out")){
					reLogin();
					return;
				}
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			String info = obj.getString("info");
			item = new JSONArray(obj.getString("item"));
			item2 = new JSONArray(obj2.getString("item"));

			//에러코드 확인
			if(info.equals("ok")){
				//성공
				handler.sendEmptyMessage(1);
			}else{
				//실패
				handler.sendEmptyMessage(2);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	
	private final Runnable SaveData = new Runnable() {
		@Override
		public void run() {
			SaveData();
		}
	};
	
	protected void SaveData() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		data.put("card_type", "type"+selected_no_str);
		data.put("wed_seq", wed_seq);
		
		JSONObject obj = AlbumModel.procWedRoomProfileUpdate(data);	//사진 외 정보들 저장
		
		if(!isHttpWorthCheck(obj)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}else{
			try {
				if(obj.getString("info").equals("session-out")){
					reLogin();
					return;
				}
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			String info = obj.getString("info");

			//에러코드 확인
			if(info.equals("ok")){
				//성공
				handler.sendEmptyMessage(3);
			}else{
				//실패
				handler.sendEmptyMessage(4);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	
	private final Runnable BuyTemplate = new Runnable() {
		@Override
		public void run() {
			BuyTemplate();
		}
	};
	
	protected void BuyTemplate() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		data.put("type", "type"+selected_no_str);
		
		JSONObject obj = AlbumModel.procInsertBuyItem(data);	//사진 외 정보들 저장
		
		if(!isHttpWorthCheck(obj)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}else{
			try {
				if(obj.getString("info").equals("session-out")){
					reLogin();
					return;
				}
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			String info = obj.getString("info");

			//에러코드 확인
			if(info.equals("ok")){
				//성공
				handler.sendEmptyMessage(5);
			}else{
				//실패
				handler.sendEmptyMessage(6);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd!=null) pd.cancel(); //등록 성공
				
				get_buy_list();
				show_image();
				
				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				break;
			case 3:
				if(pd!=null) pd.cancel();
				
				go_profile_edit();
				
				break;
				
			case 4:
				if(pd!=null) pd.cancel();
				
				break;
				
			case 5:
				if(pd!=null) pd.cancel();
				
				processParsing(mThread, SaveData);
				
				break;
				
			case 6:
				if(pd!=null) pd.cancel();
				
				Toast.makeText(TemplateEditActivity.this, "결제도중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
				
				break;
			}
		}
	};
	
	protected void show_image() {
		// TODO Auto-generated method stub
		items = new ArrayList<TemplateItem>();
		leng = item.length();
		for(int i=0; i< leng; i+=3){
			try {
				if(i<=item.length()-3){
					JSONObject	obj1 = item.getJSONObject(i);
					JSONObject	obj2 = item.getJSONObject(i+1);
					JSONObject	obj3 = item.getJSONObject(i+2);
					items.add(new TemplateItem(obj1, obj2, obj3));
				}
				if(i==item.length()-2){
					JSONObject	obj1 = item.getJSONObject(i);
					JSONObject	obj2 = item.getJSONObject(i+1);
					JSONObject	obj3 = null;
					items.add(new TemplateItem(obj1, obj2, obj3));
				}
				if(i==item.length()-1){
					JSONObject	obj1 = item.getJSONObject(i);
					JSONObject	obj2 = null;
					JSONObject	obj3 = null;
					items.add(new TemplateItem(obj1, obj2, obj3));
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		mAdapter = new TemplateEditAdapter(getApplicationContext(), items, buy_list, post_card_type);
		mPager.setAdapter(mAdapter);
	}
	
	public void get_buy_list(){
		buy_list = new ArrayList<String>();
		for(int i=0; i< item2.length(); i++){
			try {
				String list = item2.getJSONObject(i).getString("type");
				buy_list.add(list);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i("buy_list", buy_list+"");
	}
	
	public static void refresh(Context mContext, int position, int no){
		int selected_no = (position*3)+no;
		if(leng<selected_no){
			Toast.makeText(mContext, "준비중인 템플릿입니다.", Toast.LENGTH_SHORT).show();
			return;
		}
		if(post_position >=0 ){
			items.set(post_position, new TemplateItem(items.get(post_position).obj1, items.get(post_position).obj2, items.get(post_position).obj3));
			items.set(position, new TemplateItem(items.get(position).obj1, items.get(position).obj2, items.get(position).obj3, R.drawable.btn_radio_on, no));
			mAdapter.notifyDataSetChanged();
		}else{
			items.set(position, new TemplateItem(items.get(position).obj1, items.get(position).obj2, items.get(position).obj3, R.drawable.btn_radio_on, no));
			mAdapter.notifyDataSetChanged();
		}
		post_position = position;
		
		String url;
		if(selected_no<10){
			selected_no_str = "0"+selected_no;
		}else{
			selected_no_str = ""+selected_no;
		}
		
		url = "http://m.takebox.co.kr/invite/tpl/php/index.php?room_id="+ room_id +"&preview=type"+ selected_no_str;
		mWebView.loadUrl(url);
		
		try {
			switch(no){
				case 1:
					cash_yn = items.get(position).obj1.getString("cash_yn");
					break;
					
				case 2:
					cash_yn = items.get(position).obj2.getString("cash_yn");
					break;
					
				case 3:
					cash_yn = items.get(position).obj3.getString("cash_yn");
					break;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(cash_yn.equals("Y")){
			cash = true;
		}else if (cash_yn.equals("N")){
			cash = false;
		}
	}
	
	public void setWebView(){
		String url = "http://m.takebox.co.kr/invite/"+ room_id;
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebviewSetting());
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {

				WebView newWebView = new WebView(TemplateEditActivity.this); 
				WebView.WebViewTransport transport = (WebView.WebViewTransport)resultMsg.obj;
				transport.setWebView(newWebView);
				resultMsg.sendToTarget();

		 		return true;
			}
		});
		WebSettings webset = mWebView.getSettings();
		webset.setJavaScriptEnabled(true);
		webset.setJavaScriptCanOpenWindowsAutomatically(true);
		webset.setBuiltInZoomControls(false);
		webset.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webset.setSupportMultipleWindows(true);
		webset.setSupportZoom(false);
		webset.setBlockNetworkImage(false);
		webset.setLoadsImagesAutomatically(true);
		webset.setUseWideViewPort(true);
		webset.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webset.setLoadWithOverviewMode(true);
		
		mWebView.loadUrl(url);
	}
	
	@Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if (mServiceConn != null) {
			unbindService(mServiceConn);
		}
    }
	
	 public void AlreadyPurchaseItems() {
			try {
				Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
				int response = ownedItems.getInt("RESPONSE_CODE");
				if (response == 0) {
					ArrayList purchaseDataList = ownedItems
							.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
					String[] tokens = new String[purchaseDataList.size()];
					for (int i = 0; i < purchaseDataList.size(); ++i) {
						String purchaseData = (String) purchaseDataList.get(i);
						JSONObject jo = new JSONObject(purchaseData);
						tokens[i] = jo.getString("purchaseToken");
						// 여기서 tokens를 모두 컨슘 해주기
						mService.consumePurchase(3, getPackageName(), tokens[i]);
					}
				}

				// 토큰을 모두 컨슘했으니 구매 메서드 처리
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
	    public void Buy(String id_item) {
			// Var.ind_item = index;
			try {
				Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),	id_item, "inapp", "test");
				PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

				if (pendingIntent != null) {
					startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
//					mHelper.launchPurchaseFlow(this, getPackageName(), 1001,  mPurchaseFinishedListener, "test");
					// 위에 두줄 결제호출이 2가지가 있는데 위에것을 사용하면 결과가 onActivityResult 메서드로 가고, 밑에것을 사용하면 OnIabPurchaseFinishedListener 메서드로 갑니다.  (참고하세요!)
				} else {
					// 결제가 막혔다면
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 	
	 	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			System.out.println("requestCode : " + requestCode);
			System.out.println("resultCode : " + resultCode);
			if(requestCode == 1001)
				if (resultCode == RESULT_OK) {
					if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
						super.onActivityResult(requestCode, resultCode, data);
						
						int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
						String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
						String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
						
						Log.i("responseCode", responseCode+"");
						Log.i("purchaseData", purchaseData+"");
						Log.i("dataSignature", dataSignature+"");
						
						processParsing(mThread, BuyTemplate);
						// 여기서 아이템 추가 해주시면 됩니다.
						// 만약 서버로 영수증 체크후에 아이템 추가한다면, 서버로 purchaseData , dataSignature 2개 보내시면 됩니다.
					} else {
						// 구매취소 처리
						CancleToast();
					}
				}else{
					// 구매취소 처리
					CancleToast();
				}
			else{
				// 구매취소 처리
				CancleToast();
			}
		}
	 	
	 	public void CancleToast(){
	 		Toast.makeText(TemplateEditActivity.this, "구매가 최소되었습니다.", Toast.LENGTH_SHORT).show();
	 	}
	 	
	 	public void confirmDialog(){
			AlertDialog.Builder builder = new AlertDialog.Builder(TemplateEditActivity.this);
			builder.setTitle("")        // 제목 설정
			.setMessage("유료템플릿을 구입하시겠습니까?")
			.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
			.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.setPositiveButton("구입", new DialogInterface.OnClickListener(){       
				public void onClick(DialogInterface dialog, int whichButton){
					Buy("type"+selected_no_str);
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	 	
	 	/**
		 * PROFILE EDIT 으로 이동
		 */
		protected void go_profile_edit() {
			// TODO Auto-generated method stub
			Intent resultIntent = new Intent();
			resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			resultIntent.putExtra("card_type", "type"+selected_no_str);
			setResult(RESULT_OK, resultIntent);
			finish();
			overridePendingTransition(0, 0);
		}
	
}
