package com.takebox.wedding;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.R;
import com.takebox.wedding.util.CustomTextFont;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends HttpExceptionActivity {

	/** Called when the activity is first created. */
	Button btn_join;
	Button btn_enter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_start);

		//웨딩 만들기 버튼
		btn_join = (Button)findViewById(R.id.start_join_btn);
		btn_join.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(StartActivity.this, AboutUseActivityStart.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				startActivity(intent);
				Intent intent = new Intent(StartActivity.this, JoinSetupBasicActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);	
			}
		});

		//웨딩들어가기 버튼
		btn_enter = (Button)findViewById(R.id.start_enter_btn);
		btn_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//사용설명 페이지로 이동
//				Intent intent = new Intent(StartActivity.this, AboutUseActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				startActivity(intent);
				Intent intent = new Intent(StartActivity.this, InviteIDActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
		
		finishBeforeActivity();
		IntroActivity.activity.add(this);
		goToInitialAppInfo(InitialAppInfoActivity.kindOfActivity[1]);
	}
	
	public void finishBeforeActivity() {
		if (IntroActivity.activity != null) {
			if (IntroActivity.activity.size() == 0)
				return;
			ArrayList<Activity> actList = IntroActivity.activity;
			for (int i = 0; i < IntroActivity.activity.size(); i++) {
				actList.get(i).finish();
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
