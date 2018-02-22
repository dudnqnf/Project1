package com.takebox.wedding;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.info.Version;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.R;

/**
 * @author sujong
 * 메뉴 - 앱 정보
 */
public class AppInfoActivity extends HttpExceptionActivity {

	private Thread mThread = null;

	public String server_version;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_app_info);
	    processParsing(mThread, version_check);
	    
	    FrameLayout back_btn = (FrameLayout)findViewById(R.id.btn_back);
	    back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private final Runnable version_check = new Runnable() {
		@Override
		public void run() {
			version_check();
		}
	};
	
	private void version_check(){
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		
		
		String version = Version.getVersionInfo("VERSION", AppInfoActivity.this);
		data.put("ver", version);
		
		JSONObject obj;
		if(Info.JSESSIONID.equals("")){
			obj = WeddingModel.VersionCheck(data);	//사진 외 정보들 저장
			Info.JSESSIONID = "";
		} else {
			obj = WeddingModel.VersionCheck(data);	//사진 외 정보들 저장
		}
		if(!isHttpWorthCheck(obj)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}
		
		try {
			server_version = obj.getString("server_ver");
			String info = obj.getString("info");		
			System.out.println(info.toString());
			
			//에러코드 확인
			if(info.equals("ok") || info.equals("update")){
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
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
				
				complete_version_check();
				
				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패
				
				Toast.makeText(AppInfoActivity.this, "현재 앱버전이 서버버전보다 높습니다. 다시확인하여주세요.", Toast.LENGTH_LONG).show();
				
				break;

			}
		}
	};
	
	public void update_version(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Info.MARKET_URL));
        startActivity(intent);
        finish();
	}
	
	public void complete_version_check(){
		TextView cur_ver = (TextView)findViewById(R.id.cur_version);
		String cur_ver_text = Version.getVersionInfo("VERSION", AppInfoActivity.this);
		cur_ver.setText(Version.getVersionInfo("VERSION", AppInfoActivity.this));
		TextView new_ver = (TextView)findViewById(R.id.new_version);
		new_ver.setText(server_version);
		Button version_update = (Button)findViewById(R.id.version_update);
		version_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				update_version();
			}
		});
		if(!cur_ver_text.equals(server_version)){
			version_update.setVisibility(View.VISIBLE);
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
