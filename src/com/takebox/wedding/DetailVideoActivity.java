package com.takebox.wedding;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class DetailVideoActivity extends HttpExceptionActivity{
	
	MediaPlayer mediaPlayer;
	MediaController controller; 
	ProgressDialog dialog;
	
	boolean isMobile;
	boolean isWifi;
	int mSelect2 = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
//	    setRequestedOrientation(LinearLayout.HORIZONTAL);
	    setContentView(R.layout.activity_detail_video);
	    // TODO Auto-generated method stub

		ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		isMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		
		VideoView videoView = (VideoView)this.findViewById(R.id.video);
	    
	    Intent k = getIntent();
	    String videoUrl = k.getExtras().get("videoUrl").toString();
		
		if(isWifi || isMobile){
			if(isWifi && !isMobile){mPlay(videoUrl,videoView);}
			else if(!isWifi && isMobile){
				if(mSelect2 == 0){mPlay(videoUrl,videoView);}
				else if(mSelect2 == 1){
					String mse1 = "네트워크 연결에 실패 하였습니다.";
					Toast.makeText(this, mse1, Toast.LENGTH_SHORT).show();
					finish();
				}
			}else if(isWifi && isMobile){mPlay(videoUrl,videoView);}
		}else if(!isWifi && !isMobile){
			String mse1 = "네트워크 연결에 실패 하였습니다.";
			Toast.makeText(this, mse1, Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	public void mPlay(String videoUrl, VideoView videoView){
		
		dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요..", true);  
		dialog.show();
		
	    Uri video = Uri.parse(videoUrl);
	    
	    MediaController mc = new MediaController(this);         
        mc.setAnchorView(videoView);   
        
		videoView.setMediaController(mc);
		videoView.setVideoURI(video);
		videoView.requestFocus();
		videoView.start();
		
		videoView.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				finish();
			}
		});
			
		videoView.setOnPreparedListener(new OnPreparedListener(){
			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
				
				dialog.dismiss();
			}		
		});
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
