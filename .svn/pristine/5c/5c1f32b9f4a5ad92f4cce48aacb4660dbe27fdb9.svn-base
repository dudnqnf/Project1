package com.takebox.wedding.dialog;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.R;


/**
 * @author sujong
 * 앨범 사진 공유 팝업
 */
public class AlbumShareDialog extends Dialog implements android.view.View.OnClickListener {
	public Context mContext;
	public Activity mActivity;





	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";


	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
	



	public AlbumShareDialog(Context context, Activity activity) {
		super(context, android.R.style.Theme_Black_NoTitleBar);

		mActivity = activity;
		mContext = context;

		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.dialog_album_share);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




		//facebook_init(Info.savedInstanceState);


	}	    


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pop_share_facebook_btn:
			click_facebook();
			break;

		default:
			break;
		}

	}


	private void click_facebook() {
		// TODO Auto-generated method stub
		
	}
	
	
}