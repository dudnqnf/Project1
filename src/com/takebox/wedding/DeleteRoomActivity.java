package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.info.User;
import com.takebox.wedding.model.WeddingModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeleteRoomActivity extends HttpExceptionActivity {
	
	private Thread mThread = null;
	private ListView lv_list;
	private DeleteRoomAdapter adapter;
	private ArrayList<WeddingListItem> wedding_list_item;
	private JSONArray array_item;
	private Map<String,String> list;
	private ArrayList<String> list_item;
	private String room_id;
	private String room_seq;
	private String post_room_seq;
	private int mPosition;
	boolean flag;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_delete_room);
	    
	    post_room_seq = getIntent().getStringExtra("room_seq");
	    
	    lv_list = (ListView)findViewById(R.id.delete_room_list);
	    list_item = new ArrayList<String>();
		wedding_list_item = new ArrayList<WeddingListItem>();
		
		processParsing(mThread, get_room_list);
		
		FrameLayout btn = (FrameLayout)findViewById(R.id.delete_room_back_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mPosition = position;
				deleteDialog();
			}
		});
	}
	
	public void deleteDialog(){
		room_seq = list.get(list_item.get(mPosition));
		String Message = "정말로 삭제하시겠습니까?\n(모든 사진 및 정보가 삭제됩니다)";
		flag = false;
		if(post_room_seq.equals(room_seq)){
			flag = true;
			Message = "현재 계신 방을 정말로 삭제하시겠습니까?\n(모든 사진 및 정보가 삭제됩니다)";
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(DeleteRoomActivity.this);
		builder.setTitle("")        // 제목 설정
		.setMessage(Message)        // 메세지 설정
		.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
			// 확인 버튼 클릭시 설정
			public void onClick(DialogInterface dialog, int whichButton){
				if(flag){
					MainActivity.isDeletedRoom = true;
					flag = false;
				}
				list_item.remove(mPosition);
				wedding_list_item.remove(mPosition);
				adapter.notifyDataSetChanged();
				
				processParsing(mThread, delete_room);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private final Runnable get_room_list = new Runnable() {
		@Override
		public void run() {
			get_room_list();
		}
	};
	
	private final Runnable delete_room = new Runnable() {
		@Override
		public void run() {
			delete_room();
		}
	};
	
	/**
	 * 웨딩룸 리스트 가져오기
	 */
	protected void get_room_list() {
		// TODO Auto-generated method stub

		JSONObject res = WeddingModel.procGetUserRoomList(null);
		if(!isHttpWorthCheck(res)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}else{
			try {
				if(res.getString("info").equals("session-out")){
					reLogin();
					return;
				}
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(res.getString("roomList") == null){
				handler.sendEmptyMessage(2);
				return;
			}

			String value = res.getString("roomList");

			if(value != null){
				array_item = new JSONArray(value);
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 웨딩룸 지우기
	 */
	protected void delete_room() {
		// TODO Auto-generated method stub
		
		Map<String, String> data = new HashMap<String,String>();
		data.put("wed_seq", room_seq);

		JSONObject obj = WeddingModel.procDeleteRoom(data);
		
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
			if(obj.getString("info").equals("ok")){
				handler.sendEmptyMessage(3);
			}else{
				handler.sendEmptyMessage(4);
			}
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
				//리스트 받아오기 성공
				show_room_list();
				
				break;
			case 2:
				if(pd!=null) pd.cancel();
				//리스트 받아오기 실패 
				
				//서버 세션 사라졌음.
				Map<String,String> data = new HashMap<String,String>();
				data.put("JSESSIONID", "");
				User.setUserInfo(data, DeleteRoomActivity.this);
				
				break;
				
			case 3:
				if(pd!=null) pd.cancel();
				//지우기 성공
				
				break;
				
			case 4:
				if(pd!=null) pd.cancel();
				//지우기 실패
				Toast.makeText(DeleteRoomActivity.this, "데이터통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
				
				break;
			}
		}
	};
	
	/**
	 * 웨딩룸 리스트 보여주기
	 */
	protected void show_room_list() {
	// TODO Auto-generated method stub

		list = new HashMap<String, String>();
	
		int list_cnt = array_item.length();
	
		for(int i=0; i<list_cnt; i++){
	
			try {
				JSONObject obj = array_item.getJSONObject(i);
				
				if(obj.getString("auth").equals("admin")){
					list_item.add(obj.getString("room_id"));
					list.put(obj.getString("room_id"), obj.getString("wed_seq"));
					
					wedding_list_item.add(new WeddingListItem(obj.getString("room_id"), obj.getString("profile_img")));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		adapter = new DeleteRoomAdapter(DeleteRoomActivity.this, R.layout.wedding_list, wedding_list_item);
		lv_list.setAdapter(adapter);
	}

}
