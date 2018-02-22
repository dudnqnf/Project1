package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.R;

public class JoinSetupIDActivity extends HttpExceptionActivity {
	
	private EditText setupidtext;
	private ListView setupidlist;
	private Thread mThread = null;
	private JSONArray arr;
	private Adapter mAdapter;
	private ArrayList<String> arrlist;
	public static final String SETUP_ID = "ID";
	public String str;
	public String room_id;
	public String pre_room_id;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_join_setup_id);
	    Intent i = getIntent();
	    str = i.getStringExtra(SETUP_ID);
	    System.out.println(str); 
	    setupidtext = (EditText)findViewById(R.id.setupid);
	    setupidtext.setText(str);
	    
	    processParsing(mThread, join);
	    
	    Button btn = (Button)findViewById(R.id.setupidbtn);
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(setupidtext.getText().toString().length()<2){
					baseDialog("아이디를 두 글자 이상 입력해주세요");
				}else{
					processParsing(mThread, check_id);
				}
				
				
			}
		});
	}
	
	private final Runnable join = new Runnable() {
		@Override
		public void run() {
			join();
		}
	};
	
	private final Runnable check_id = new Runnable() {
		@Override
		public void run() {
			check_id();
		}
	};
	
	protected void join() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		String id = setupidtext.getText().toString();

		data.put("room_id",id);

		JSONObject obj = MemberModel.recom(data);
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
			arr = obj.getJSONArray("recommendId");
			System.out.println("info1 : "+arr);
			//에러코드 확인
			if(arr.getString(0) != null){
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
	
	private void check_id() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		room_id = setupidtext.getText().toString();
		pre_room_id = str;
		data.put("room_id",room_id);
		data.put("pre_room_id", pre_room_id);
		
		if(room_id.equals(pre_room_id)){		//같으면 검사 안함
			finish();
			return;
		}
		
		JSONObject obj = MemberModel.updateId(data);
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
		
		System.out.println("room_id : "+room_id);
		System.out.println("pre_room_id : "+pre_room_id);

		try {
			String info = obj.getString("info");
			System.out.println("info : "+info);
			//에러코드 확인
			if(info.equals("ok") || setupidtext.getText().equals(str)){
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
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd!=null) pd.cancel();
				//등록 성공
				
				try {
					set_recommand_id();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(JoinSetupIDActivity.this, "네트워크통신 실패").create();    // 알림창 객체 생성
				dialog.show();   
				
				break;
			
			case 3:
				if(pd!=null) pd.cancel();
				//웨딩룸 이름(아이디) 변경 성공

				//셋업으로 이동 
				go_setup_profile();
				break;
			
			case 4:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog2 = AlertDialogBuilder.pop_ok(JoinSetupIDActivity.this, "이미 존재하는 아이디 입니다.").create();    // 알림창 객체 생성
				dialog2.show();   
				
				break;

			}
			
		}

		private void set_recommand_id() throws JSONException {
		    setupidlist = (ListView)findViewById(R.id.setupidlist);
			arrlist = new ArrayList<String>();
			for(int i = 0; i<arr.length(); i++){
				arrlist.add(arr.getString(i));
			}
		    mAdapter = new ArrayAdapter<String>(JoinSetupIDActivity.this, android.R.layout.simple_list_item_1, arrlist);
		    setupidlist.setAdapter((ListAdapter) mAdapter);
		    setupidlist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					setupidtext =  (EditText)findViewById(R.id.setupid);
					String text = (String) mAdapter.getItem(position);
					setupidtext.setText(text);
				}
		    	
			});
		}
	};
	
	
	
	/**
	 * 셋업 프로필로 이동
	 */
	protected void go_setup_profile() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("ROOM_ID",room_id);
		
		setResult(RESULT_OK, intent);
		finish();
		overridePendingTransition(0, 0);
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
