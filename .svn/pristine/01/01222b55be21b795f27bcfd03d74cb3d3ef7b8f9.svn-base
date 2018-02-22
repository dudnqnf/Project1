package com.takebox.wedding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.util.Time;

/**
 * @author sujong
 * 미가입 상태에서 
 * 
 * 초대받은 ID(웨딩룸 id)입력 하는 화면
 * 
 */
public class InviteFriendActivity extends HttpExceptionActivity {

	public String profile_img;
	public String wed_date;
	public String wed_time;
	public String place;
	public String room_id;
	public String room_no;
	public String description;
	private Thread mThread = null;
	private String TAG = "TRACKER";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_invite);
		
		Intent i =getIntent();
		
		profile_img = i.getStringExtra("profile_img");
		wed_date = i.getStringExtra("wed_date");
		wed_time = i.getStringExtra("wed_time");
		place = i.getStringExtra("place");
		room_id = i.getStringExtra("room_id");
		room_no = i.getStringExtra("room_no");
		description = i.getStringExtra("description");
		
		TextView tv = (TextView)findViewById(R.id.facebook_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.kakao_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.sms_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.email_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		
		
		Button btn_email = (Button)findViewById(R.id.pop_email_btn);
		btn_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				click_email();
			}
		});
		
		Button btn_sms = (Button)findViewById(R.id.pop_sms_btn);
		btn_sms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					click_sms();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Button btn_facebook = (Button)findViewById(R.id.authButton);
		btn_facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					click_facebook();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Button btn_kakaotalk = (Button)findViewById(R.id.pop_kakaotalk_btn);
		btn_kakaotalk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					click_kakaotalk();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		goToInitialAppInfo(InitialAppInfoActivity.kindOfActivity[4]);
	}
	
	private void click_kakaotalk() throws JSONException{
		processParsing(mThread, tracker);
		
		Intent intent = new Intent(InviteFriendActivity.this, KakaoLinkActivity.class);
		intent.putExtra("img_name", profile_img);
		intent.putExtra("date", wed_date);
		intent.putExtra("time", wed_time);
		intent.putExtra("place", place);
		intent.putExtra("room_id", room_id);
		intent.putExtra("room_no", room_no);
		intent.putExtra("description", description);
		InviteFriendActivity.this.startActivity(intent);
		
	}


	private void click_facebook() throws JSONException {
		processParsing(mThread, tracker);
		// TODO Auto-generated method stub
		Intent intent = new Intent(InviteFriendActivity.this, FacebookActivity.class);
		intent.putExtra("img_url", Info.MASTER_FILE_URL + "/image/"+ profile_img);
		intent.putExtra("room_id", room_id);
		intent.putExtra("room_no", room_no);
		InviteFriendActivity.this.startActivity(intent);
//		Toast.makeText(InviteFriendActivity.this, "업데이트 중입니다.", Toast.LENGTH_LONG).show();
//		facebookLogin();
		
	}

	private void click_sms() throws JSONException {
		// TODO Auto-generated method stub
		processParsing(mThread, tracker);

		Intent it = new Intent(Intent.ACTION_VIEW);   
		Uri dataUri = Uri.parse(profile_img);
		it.putExtra(Intent.EXTRA_STREAM, dataUri);
		it.putExtra("sms_body", description+ "\n\n" +
				"웨딩박스ID : " + room_id + "\n" +
				"날짜 : " + Time.calculateDate(wed_date) + "\n" +
				"시간 : " + Time.calculateTime(wed_time) + "\n" +
				"장소 : " + place + "\n\n" +
				"웨딩박스 앱에서 ID를 입력해주시면 더 많은 웨딩사진을 보실 수 있어요.\n" +
				"꼭 참석해 주세요.\n" +
				"모바일청첩장 : http://m.takebox.co.kr/invite/" + room_id);   
		it.setType("vnd.android-dir/mms-sms");   
		InviteFriendActivity.this.startActivity(it); 
	}



	private void click_email() {
		processParsing(mThread, tracker);
		
		Intent it = new Intent(Intent.ACTION_SEND);   
//		it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");   
		it.putExtra(Intent.EXTRA_TEXT, description+ "\n\n" +
				"웨딩박스ID : " + room_id + "\n" +
				"날짜 : " + Time.calculateDate(wed_date) + "\n" +
				"시간 : " + Time.calculateTime(wed_time) + "\n" +
				"장소 : " + place + "\n\n" +
				"웨딩박스 앱에서 ID를 입력해주시면 더 많은 웨딩사진을 보실 수 있어요.\n" +
				"꼭 참석해 주세요." +
				"모바일청첩장 : http://m.takebox.co.kr/invite/" + room_id);   
		it.setType("vnd.android-dir/mms-sms");   
		it.setType("text/plain");   
		InviteFriendActivity.this.startActivity(Intent.createChooser(it, "Choose Email Client")); 
	}
	
	private final Runnable tracker = new Runnable() {
		@Override
		public void run() {
			tracker();
		}
	};
	
	// 웨딩룸 정보 가져오기
	protected void tracker() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String, String>();

		// String room_id = User.getUserInfo("ROOM_ID", MainActivity.this);

		data.put("wed_seq", room_no);
		
		JSONObject obj = WeddingModel.tracker(data);
		if (!isHttpWorthCheck(obj)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {
			try {
				if (obj.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		
		String info;
		try {
			info = obj.getString("info");
			if (info.equals("ok")) {
				handler.sendEmptyMessage(1);

			} else {
				handler.sendEmptyMessage(2);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch (msg.what) {
			case 1:
				if (pd != null)
					pd.cancel();
				
				Log.i(TAG, "등록성공");

				break;
			case 2:
				if (pd != null)
					pd.cancel();
				// 등록 실패

				Log.i(TAG, "등록실패");
				break;

			}
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
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
