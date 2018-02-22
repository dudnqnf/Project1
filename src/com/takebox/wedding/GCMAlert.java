package com.takebox.wedding;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GCMAlert extends Activity {

	Handler h;
	
	String mode = null;
    String seq = null;
    String lang = null;
	String msg = null;
	
	String nickname = null;
	String other_mm_idx = null;
	
	private LinearLayout btn_linear1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    requestWindowFeature(Window.FEATURE_NO_TITLE);

//	    setContentView(R.layout.gcmalert);
//	    
//	    mode = getIntent().getStringExtra("mode");
//	    seq = getIntent().getStringExtra("seq");
//	    lang = getIntent().getStringExtra("lang");
//	    msg = getIntent().getStringExtra("msg");
//	    other_mm_idx = getIntent().getStringExtra("other_mm_idx");
//	    nickname 	 = getIntent().getStringExtra("nickname");
//	    
//	    
//	    TextView tv_msg = (TextView)findViewById(R.id.text_msg);
//	    btn_linear1 = (LinearLayout)findViewById(R.id.btn_linear1);
//	    
//	    btn_linear1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) 
//			{
//				Intent intent = new Intent(GCMAlert.this, LoadingActivity.class); 
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				
//				intent.putExtra("GCM_MM_IDX", other_mm_idx);
//				intent.putExtra("GCM_NICKNAME", nickname);
//				intent.putExtra("GCM_ZM_IDX", seq);
//				intent.putExtra("GCM_AM_MODE", mode);
//				
//				startActivity(intent);
//				/*
//				if(mode.equals("TINGTALK_OPEN") || mode.equals("CHAT"))
//				{
//					Intent intent = new Intent(GCMAlert.this, ChatRoomListActivity.class); 
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(GCMAlert.this, LoadingActivity.class); 
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//					startActivity(intent);
//				}
//				*/
//				
//				finish();
//				overridePendingTransition(0,0);
//			}
//		});
//	    
//	    tv_msg.setText(msg);
//
//	    
//	    AnimationSet set = new AnimationSet(true);
//	    set.setInterpolator(new AccelerateInterpolator());
//	    
//	    Animation animation = new AlphaAnimation(0.0f, 1.0f);
//	    animation.setDuration(400);
//	    set.addAnimation(animation);
//	    
//	    tv_msg.setAnimation(set);
//	    
//	    
//	    
//	    Handler handler = new Handler();
//		handler.postDelayed(new Runnable(){
//		@Override
//		      public void run(){
//				finish();
//		   }
//		}, 5000);
	    
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
