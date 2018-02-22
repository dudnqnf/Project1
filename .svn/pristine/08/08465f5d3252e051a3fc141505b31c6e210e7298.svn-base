package com.takebox.wedding;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class InitialAppInfoActivity extends Activity{
	
	View view;
	Intent intent;
	RelativeLayout layout;
	SharedPreferences saveData;
	SharedPreferences.Editor saveEdit;
	public static final String[] kindOfActivity = {"main", "start", "join_basic", "recommend", "invite", "invited_list"};
	final int[] backgroundArray = {R.drawable.initial_appinfo_main
							,R.drawable.initial_appinfo_start
							,R.drawable.initial_appinfo_join_basic
							,R.drawable.initial_appinfo_recommend
							,R.drawable.initial_appinfo_invite
							,R.drawable.initial_appinfo_invited_list};
	String kind;
	boolean initialCheck = false;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		intent = getIntent();
		kind = intent.getStringExtra("kindOfActivity");
		booleanCircurate(kind);
		if(initialCheck)
				finish();
		for(int i=0; i<kindOfActivity.length; i++){
			if(kind.compareTo(kindOfActivity[i])==0)
				createView(i);
		}
		setContentView(view);
		setLayoutEvent();
	}
	
	public void booleanCircurate(String kind){
		saveData = getSharedPreferences("appInfoData", MODE_PRIVATE);
		initialCheck = saveData.getBoolean(kind, false);
	}
	
	public void createView(int index){
		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.appinfo_main, null);
		layout = (RelativeLayout)view.findViewById(R.id.background);
		layout.setBackgroundResource(backgroundArray[index]);
	}
	
	public void setLayoutEvent(){
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveEdit = saveData.edit();
				saveEdit.putBoolean(kind, true);
				saveEdit.commit();
				finish();
				overridePendingTransition(0, 0);
			}
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		saveEdit = saveData.edit();
			saveEdit.putBoolean(kind, true);
			saveEdit.commit();
			overridePendingTransition(0, 0);
			finish();
		}
    	return super.onKeyDown(keyCode, event);
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
