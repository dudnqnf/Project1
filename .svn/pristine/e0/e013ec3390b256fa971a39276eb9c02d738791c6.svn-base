package com.takebox.wedding.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.takebox.wedding.FacebookActivity;
import com.takebox.wedding.KakaoLinkActivity;
import com.takebox.wedding.R;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.util.Time;


/**
 * @author sujong
 * 하객 초대 하기 팝업
 */
public class InviteDialog extends Dialog implements android.view.View.OnClickListener {
	public Context mContext;

	Activity mActivity;
	public JSONObject mRoomInfo;
	private UiLifecycleHelper uiHelper;

	public InviteDialog(Context context, Activity activity, JSONObject room_info) {
		super(context, android.R.style.Theme_Black_NoTitleBar);

		mActivity = activity;
		mContext = context;
		mRoomInfo = room_info;
		
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.dialog_invite);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		TextView tv = (TextView)findViewById(R.id.facebook_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.kakao_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.sms_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.email_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		
		
		Button btn_email = (Button)findViewById(R.id.pop_email_btn);
		Button btn_sms = (Button)findViewById(R.id.pop_sms_btn);
		Button btn_facebook = (Button)findViewById(R.id.authButton);
		Button btn_kakaotalk = (Button)findViewById(R.id.pop_kakaotalk_btn);
		
		
		btn_email.setOnClickListener(this);
		btn_sms.setOnClickListener(this);
		btn_facebook.setOnClickListener(this);
		btn_kakaotalk.setOnClickListener(this);
		
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.pop_email_btn:
				click_email();
			break;
		case R.id.pop_sms_btn:
				click_sms();
			break;
		case R.id.authButton:
				click_facebook();
			break;
		case R.id.pop_kakaotalk_btn:
				click_kakaotalk();
				break;	
			
		default:
			break;
		}

	}


	private void click_kakaotalk(){
		
		Intent intent = new Intent(mContext, KakaoLinkActivity.class);
		try {
			intent.putExtra("img_name", mRoomInfo.getString("profile_img"));
			intent.putExtra("date", mRoomInfo.getString("wed_date"));
			intent.putExtra("time", mRoomInfo.getString("wed_time"));
			intent.putExtra("place", mRoomInfo.getString("place"));
			intent.putExtra("room_id", mRoomInfo.getString("room_id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mActivity.startActivity(intent);
		
		
	}

	private void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		
	}



	private void click_facebook() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, FacebookActivity.class);
		try {
			intent.putExtra("img_url", Info.MASTER_FILE_URL + "/image/"+ mRoomInfo.getString("profile_img"));
			intent.putExtra("room_id", mRoomInfo.getString("room_id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mActivity.startActivity(intent);
//		Toast.makeText(mContext, "업데이트 중입니다.", Toast.LENGTH_LONG).show();
	}



	private void click_sms() {
		// TODO Auto-generated method stub
		String profile_img = "";
		String room_id = "";
		String date = "";
		String time = "";
		String place = "";
		
		try {
			profile_img = Info.MASTER_FILE_URL + "/image/"+ mRoomInfo.getString("profile_img");
			room_id = mRoomInfo.getString("room_id");
			date = mRoomInfo.getString("wed_date");
			time = mRoomInfo.getString("wed_time");
			place = mRoomInfo.getString("place");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent it = new Intent(Intent.ACTION_VIEW);   
		Uri dataUri = Uri.parse(profile_img);
		it.putExtra(Intent.EXTRA_STREAM, dataUri);
		it.putExtra("sms_body", "사랑이란 이름으로 두 사람이 하나 되는 아름다운 날입니다.\n" +
				"부디 참석하셔서 자리를 빛내주시고, 사진도 많이 찍어주세요.\n\n" +
				"웨딩박스ID : " + room_id + "\n" +
				"날짜 : " + Time.calculateDate(date) + "\n" +
				"시간 : " + Time.calculateTime(time) + "\n" +
				"장소 : " + place + "\n\n" +
				"웨딩박스 앱에서 ID를 입력해주시면 더 많은 웨딩사진을 보실 수 있어요.\n" +
				"꼭 참석해 주세요.");   
		it.setType("vnd.android-dir/mms-sms");   
		mActivity.startActivity(it); 
	}



	private void click_email() {
		// TODO Auto-generated method stub
		String profile_img = "";
		String room_id = "";
		String date = "";
		String time = "";
		String place = "";
		
		try {
			profile_img = Info.MASTER_FILE_URL + "/image/"+ mRoomInfo.getString("profile_img");
			room_id = mRoomInfo.getString("room_id");
			date = mRoomInfo.getString("wed_date");
			time = mRoomInfo.getString("wed_time");
			place = mRoomInfo.getString("place");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent it = new Intent(Intent.ACTION_SEND);   
//		it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");   
		it.putExtra(Intent.EXTRA_TEXT, "사랑이란 이름으로 두 사람이 하나 되는 아름다운 날입니다.\n" +
				"부디 참석하셔서 자리를 빛내주시고, 사진도 많이 찍어주세요.\n\n" +
				"웨딩박스ID : " + room_id + "\n" +
				"날짜 : " + Time.calculateDate(date) + "\n" +
				"시간 : " + Time.calculateTime(time) + "\n" +
				"장소 : " + place + "\n\n" +
				"웨딩박스 앱에서 ID를 입력해주시면 더 많은 웨딩사진을 보실 수 있어요.\n" +
				"꼭 참석해 주세요.");   
		it.setType("vnd.android-dir/mms-sms");   
		it.setType("text/plain");   
		mActivity.startActivity(Intent.createChooser(it, "Choose Email Client")); 
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i("TAG", "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i("TAG", "Logged out...");
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};



}