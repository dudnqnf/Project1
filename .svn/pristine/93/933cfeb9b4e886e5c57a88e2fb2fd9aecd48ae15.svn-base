package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.model.WeddingModel;

/**
 * @author sujong
 * 초대된 사람 리스트
 * 
 */
public class InviteListActivity extends HttpExceptionActivity {

	private static ProgressDialog pd_refresh = null;
	private static Thread mThread = null;
	public static Activity mActivity;
	public static Context mContext;

	private static String wed_seq;
	private static String user_auth;
	private static String user_id;
	static JSONArray invite_array;
	
	static ListView lv_list;
	static ListView lv_list_admin;
	static ArrayList<InviteListItem> items;
	static ArrayList<InviteListItem> itemsAdmin;
	static ArrayAdapter<InviteListItem> adapter;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_invite_list);

		wed_seq = getIntent().getStringExtra("WED_SEQ");
		user_auth = getIntent().getStringExtra("auth");
		user_id = getIntent().getStringExtra("u_id");

		
		lv_list = (ListView)findViewById(R.id.invite_list);
		lv_list_admin = (ListView)findViewById(R.id.invite_list_admin);
		lv_list.setDivider(new ColorDrawable(0xffe3e3e3));
		lv_list.setDividerHeight(1);
		lv_list_admin.setDivider(new ColorDrawable(0xffe3e3e3));
		lv_list_admin.setDividerHeight(1);
		
		FrameLayout btn = (FrameLayout)findViewById(R.id.invite_list_back_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		processParsing(mThread, get_invite_user);
		
		
		goToInitialAppInfo(InitialAppInfoActivity.kindOfActivity[5]);
	}
	
	public static void DataRefresh(Activity acti, Context cont){
		mActivity = acti;
		mContext = cont;
		processParsingRefresh(mThread, get_invite_user_refresh);
	}
	
	public static void processParsingRefresh(Thread thread, Runnable runnable) {
		Log.i("실행진행도", "4");
		pd_refresh = new ProgressDialog(mActivity);
		pd_refresh.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd_refresh.setTitle(null);
		pd_refresh.setMessage("로드중...");
		pd_refresh.show();

		thread = new Thread(runnable);
		thread.start();
	}


	//초대된 사람들 가져오기
	private final Runnable get_invite_user = new Runnable() {
		@Override
		public void run() {
			get_invite_user();
		}
	};
	
	//초대된 사람들 가져오기
	private final static Runnable get_invite_user_refresh = new Runnable() {
		@Override
		public void run() {
			Log.i("실행진행도", "1");
			get_invite_user_refresh();
		}
	};



	protected void get_invite_user() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		data.put("wed_seq",wed_seq);

		JSONObject obj = WeddingModel.procGetInviteUser(data);

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
			invite_array = new JSONArray(obj.getString("inviteUserList"));

			//성공
			handler.sendEmptyMessage(1);
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void get_invite_user_refresh() {
		// TODO Auto-generated method stub
		Log.i("실행진행도", "2");
		Map<String, String> data = new HashMap<String,String>();
		data.put("wed_seq",wed_seq);
		Log.i("실행진행도", "3");

		JSONObject obj = WeddingModel.procGetInviteUser(data);

		try {
			invite_array = new JSONArray(obj.getString("inviteUserList"));

			//성공
			handler_refresh.sendEmptyMessage(1);
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

				if(pd!=null) pd.cancel();
				//회원 등록 성공
				 show_invite_list();
				break;

			}
		}
	};
	
	private final static Handler handler_refresh = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd_refresh!=null) pd_refresh.cancel();
				//회원 등록 성공
				 show_invite_list_refresh();
				break;

			}
		}
	};
	
	protected void show_invite_list() {
		// TODO Auto-generated method stub
		
		items = new ArrayList<InviteListItem>();
		itemsAdmin = new ArrayList<InviteListItem>();
		
		int list_cnt = invite_array.length();
		for(int i = 0 ; i< list_cnt; i++){
			try {
				JSONObject obj = invite_array.getJSONObject(i);
				String u_seq = obj.getString("u_seq");
				String name = obj.getString("u_name");
				String nicname = obj.getString("u_nick");
				String img = obj.getString("u_picture");
				String social_img = obj.getString("u_sns_picture");
				String auth = obj.getString("auth");
				
				if(!auth.equals("admin")){
					items.add(new InviteListItem(u_seq, name, nicname, img, social_img, auth));
				} else {
					itemsAdmin.add(new InviteListItem(u_seq, name, nicname, img, social_img, auth));
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		adapter = new InviteListAdapter(InviteListActivity.this, R.layout.invite_list, items, user_auth, wed_seq, user_id);
		lv_list.setAdapter(adapter);
		
		adapter = new InviteListAdapter(InviteListActivity.this, R.layout.invite_list, itemsAdmin, user_auth, wed_seq, user_id);
		lv_list_admin.setAdapter(adapter);
	}
	
	protected static void show_invite_list_refresh() {
		// TODO Auto-generated method stub
		
		items = new ArrayList<InviteListItem>();
		itemsAdmin = new ArrayList<InviteListItem>();
		
		int list_cnt = invite_array.length();
		for(int i = 0 ; i< list_cnt; i++){
			try {
				JSONObject obj = invite_array.getJSONObject(i);
				String u_seq = obj.getString("u_seq");
				String name = obj.getString("u_name");
				String nicname = obj.getString("u_nick");
				String img = obj.getString("u_picture");
				String social_img = obj.getString("u_sns_picture");
				String auth = obj.getString("auth");
				
				if(!auth.equals("admin")){
					items.add(new InviteListItem(u_seq, name, nicname, img, social_img, auth));
				} else {
					itemsAdmin.add(new InviteListItem(u_seq, name, nicname, img, social_img, auth));
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		adapter = new InviteListAdapter(mActivity, R.layout.invite_list, items, user_auth, wed_seq, user_id);
		lv_list.setAdapter(adapter);
		
		adapter = new InviteListAdapter(mActivity, R.layout.invite_list, itemsAdmin, user_auth, wed_seq, user_id);
		lv_list_admin.setAdapter(adapter);
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