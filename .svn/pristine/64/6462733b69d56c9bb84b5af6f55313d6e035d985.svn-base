package com.takebox.wedding;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;
import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendAppActivity extends HttpExceptionActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_recommend_app);
	    // TODO Auto-generated method stub
	    
	  //페이스북 세션 설정
	  	facebook_init(savedInstanceState);
	    
	    TextView tv = (TextView)findViewById(R.id.recom_facebook_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.recom_kakao_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.recom_sms_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		tv = (TextView)findViewById(R.id.recom_email_recom);
		tv.setTypeface(CustomTextFont.hunsomsatangR);
		
		//백버튼
		FrameLayout back_btn = (FrameLayout)findViewById(R.id.btn_back);
		back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	    
	    //페이스북 버튼
	    Button facebook = (Button)findViewById(R.id.recom_pop_facebook_btn);
	    facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecommendAppActivity.this, FacebookActivity.class);
				RecommendAppActivity.this.startActivity(intent);
//				Toast.makeText(RecommendAppActivity.this, "업데이트 중입니다.", Toast.LENGTH_SHORT).show();
			}
		});
	    
	    //카카오톡 버튼
	    Button kakao = (Button)findViewById(R.id.recom_pop_kakaotalk_btn);
	    kakao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecommendAppActivity.this, KakaoLinkAppRecomActivity.class);
				RecommendAppActivity.this.startActivity(intent);
			}
		});
	    
	    //문자 버튼
	    Button sms = (Button)findViewById(R.id.recom_pop_sms_btn);
	    sms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Intent.ACTION_VIEW);   
				it.putExtra("sms_body", "[웨딩박스]"+
				"\n둘만의 결혼식이 아닌, 모두가 함께 만들어가는 소셜 웨딩 앨범 웨딩박스" +
                		"\n\n지금 시작해보세요!");   
				it.setType("vnd.android-dir/mms-sms");   
				RecommendAppActivity.this.startActivity(it); 
			}
		});
	    
	    //이메일 버튼
	    Button email = (Button)findViewById(R.id.recom_pop_email_btn);
	    email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Intent.ACTION_SEND);   
//				it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");   
				it.putExtra(Intent.EXTRA_TEXT, "[웨딩박스]"+
				"\n둘만의 결혼식이 아닌, 모두가 함께 만들어가는 소셜 웨딩 앨범 웨딩박스" +
                		"\n\n지금 시작해보세요!");   
				it.setType("text/plain");
				RecommendAppActivity.this.startActivity(Intent.createChooser(it, "Choose Email Client")); 
			}
		});
	    
//	    goToInitialAppInfo(InitialAppInfoActivity.kindOfActivity[3]);
	}
	
	private void facebook_init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, new StatusCallback() {

					@Override
					public void call(Session session, SessionState state, Exception exception) {
						// TODO Auto-generated method stub

					}
				}, savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this).setCallback(new StatusCallback() {

					@Override
					public void call(Session session, SessionState state, Exception exception) {
						// TODO Auto-generated method stub

					}
				}));
			}
		}
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
