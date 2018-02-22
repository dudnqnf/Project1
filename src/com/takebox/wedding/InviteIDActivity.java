package com.takebox.wedding;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.info.User;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.R;

/**
 * @author sujong
 * 미가입 상태에서 
 * 
 * 초대받은 ID(웨딩룸 id)입력 하는 화면
 * 
 */
public class InviteIDActivity extends HttpExceptionActivity {

	private Thread mThread = null;


	EditText et_id ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub

		setContentView(R.layout.activity_invite_id);


		et_id = (EditText)findViewById(R.id.invite_id_input_edit);


		Button btn_ok = (Button)findViewById(R.id.invite_id_ok_btn);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				if(et_id.getText().toString().isEmpty()){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(InviteIDActivity.this, "ID를 입력해 주세요.").create();    // 알림창 객체 생성
					dialog.show();  
					return;
				}
				if(et_id.getText().toString().length()<2){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(InviteIDActivity.this, "ID를 2글자 이상 입력해 주세요.").create();    // 알림창 객체 생성
					dialog.show();  
					return;
				}


				processParsing(mThread, find_room_id);				
			}
		});


	}


	private final Runnable find_room_id = new Runnable() {
		@Override
		public void run() {
			find_room_id();
		}
	};


	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd!=null) pd.cancel();
				//웨딩룸 아이디 확인 성공


				String login_yn = User.getUserInfo("LOGIN_YN", InviteIDActivity.this);
				String session_id = User.getUserInfo("JSESSIONID", InviteIDActivity.this);

				if(login_yn.equals("Y") && !session_id.isEmpty()){
					//로그인 상태 

					go_invite_room_confirm();

				}else{

					//비로그인 상태
					go_Join();

				}


				break;
				
			case 2:
				if(pd!=null) pd.cancel();
				Toast.makeText(InviteIDActivity.this, "존재하지 않는 웨딩아이디 입니다.", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	protected void find_room_id() {
		// TODO Auto-generated method stub
		String id = et_id.getText().toString();


		Map<String, String> data = new HashMap<String,String>();
		data.put("room_id", id);


		JSONObject obj = WeddingModel.procFindRoomID(data);

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
			if(!obj.getString("name").equals("null") && obj.getString("name").length()>3){
				Log.i("Tag", "호출됨2");
				String name = obj.getString("name");

				Info.INVITE_ID_INFO = new JSONArray(name);

				//성공
				handler.sendEmptyMessage(1);
				return;
				
			} else {
				Log.i("Tag", "호출됨2");
				handler.sendEmptyMessage(2);
				return;
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 초대받은 아이디로 웨딩룸(신랑, 신부) 확인 하기 
	 */
	protected void go_invite_room_confirm() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(InviteIDActivity.this, RoomConfirmActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);
	}

	protected void go_Join() {
		// TODO Auto-generated method stub

		Intent intent = new Intent(InviteIDActivity.this, JoinActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("MODE", "INVITE");//초대받은사람 가입 

		startActivity(intent);
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
