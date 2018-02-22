package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.R;

/**
 * @author sujong
 * 
 * 웨딩룸아이디로 찾은 
 * 신랑신부 이름 확인 후 메인으로 이동 
 * 
 */
public class RoomConfirmActivity extends Activity {

	
	
	String room_id;
	String room_seq;
	private ListView lv_list;
	private ArrayList<String> list_item;
	private Map<String,String> list;
	private WeddingListAdapter adapter;
	private ArrayList<WeddingListItem> wedding_list_item;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_room_confirm);

		lv_list = (ListView)findViewById(R.id.room_confirm_name);
		
		list_item = new ArrayList<String>();	
		wedding_list_item = new ArrayList<WeddingListItem>();
		
		JSONArray array_item = Info.INVITE_ID_INFO;
		
		list = new HashMap<String, String>();
		int list_cnt = array_item.length();
		
		for(int i=0; i<list_cnt; i++){

			try {
				JSONObject obj = array_item.getJSONObject(i);
	
				list_item.add(obj.getString("room_id"));
				list.put(obj.getString("room_id"), obj.getString("wed_seq"));
				
				wedding_list_item.add(new WeddingListItem(obj.getString("room_id"), obj.getString("profile_img")));
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		adapter = new WeddingListAdapter(RoomConfirmActivity.this, R.layout.wedding_list, wedding_list_item);
		lv_list.setAdapter(adapter);		
		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				String room_id = list_item.get(position);
				String room_seq = list.get(list_item.get(position));
				
				System.out.println("room_id = " + room_id);
				System.out.println("room_seq = " + room_seq);
				
				
				//메인으로 이동
				Intent intent = new Intent(RoomConfirmActivity.this, MainActivity.class);
				intent.putExtra("ROOM_ID", room_id);
				intent.putExtra("ROOM_SEQ", room_seq);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

				startActivity(intent);
				
				finish();
			}
		});
		
		
		
		/*
		 * 
		 
		//방정보 받기
		room_id = getIntent().getStringExtra("ROOM_ID");
		room_seq = getIntent().getStringExtra("ROOM_SEQ");
		
		//신랑신부 이름 받기
		String male = getIntent().getStringExtra("MALE_NAME");
		String female = getIntent().getStringExtra("FEMALE_NAME");
		*/



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
