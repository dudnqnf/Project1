package com.takebox.wedding.dialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.luminous.pick.Action;
import com.takebox.wedding.MainActivity;
import com.takebox.wedding.R;
import com.takebox.wedding.info.Info;


public class PhotoAlbumDialog extends Dialog implements android.view.View.OnClickListener {
	public Context mContext;

	public Button btn_photo;
	public Button btn_album;
	private static final int PICK_FROM_MULTI_ALBUM = 2;

	//카메라 액티비티 리턴
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_CAPTURE_VIDEO = 3;
	private static final int PICK_FROM_SELECT_VIDEO = 4;

	public static Uri mImageCaptureUri;
	ImageView mPhotoImageView;

	Activity mActivity;
	public String url;



	public PhotoAlbumDialog(Context context, Activity activity, String delete_YN) {
		super(context, android.R.style.Theme_Black_NoTitleBar);

		mActivity = activity;

		mContext = context;
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.dialog_photo_album);

		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		btn_photo = (Button)findViewById(R.id.pop_photo_btn);
		btn_album = (Button)findViewById(R.id.pop_album_btn);

		btn_photo.setOnClickListener(this);
		btn_album.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(mContext, "현재 동영상은 최대 100MB까지 전송 가능합니다.(약 15초)", 500);
		toast.show();
		if(v == btn_photo){
			Log.i("tag", "photo");
			camera_dialog();
			dismiss();
		}else if(v == btn_album){
			Log.i("tag", "album");
			album_dialog();
			dismiss();
		}else{
			dismiss();
		}
	}

	/**
	 *  to get Image from album
	 */
	private void doTakeAlbumAction()
	{
//		System.out.println("doTakeAlbumAction");
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(Action.ACTION_MULTIPLE_PICK);
//		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//		mActivity.startActivityForResult(intent, PICK_FROM_MULTI_ALBUM);
//		mActivity.overridePendingTransition(0,0);
		Intent intent = new Intent(
				Action.ACTION_MULTIPLE_PICK);
		mActivity.startActivityForResult(intent,
				PICK_FROM_MULTI_ALBUM);
		mActivity.overridePendingTransition(0,0);
	}

	private void doTakePhotoAction()
	{
		File file = new File(Info.filePath);
		if(!file.exists()){
			file.mkdirs(); 
		}
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
      
        MainActivity.imageUri = Uri.fromFile(new File(Info.filePath, url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, MainActivity.imageUri);
        mActivity.startActivityForResult(intent, PICK_FROM_CAMERA);
        mActivity.overridePendingTransition(0,0);

	}
	
	private void doCaptureVideo()
	{
	    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    mActivity.startActivityForResult(intent, PICK_FROM_CAPTURE_VIDEO);
	}
	
	private void doSelectVideo()
	{
//	    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//	    i.setType("video/*");

	    Intent i = new Intent(Intent.ACTION_PICK);
	    i.setType("video/*");
	//    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    try
	    {
	        mActivity.startActivityForResult(i, PICK_FROM_SELECT_VIDEO);
	    } catch (android.content.ActivityNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    
	    
	    
	    
	    
	}

	private void camera_dialog(){
		new AlertDialog.Builder(mActivity)
		.setTitle("선택하세요")
		.setItems(R.array.items_video_or_image01, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0){
					doTakePhotoAction();
				}else if(which == 1){
					doCaptureVideo();
				}
			}
		})
		.show();
	}
	
	private void album_dialog(){
		new AlertDialog.Builder(mActivity)
		.setTitle("선택하세요")
		.setItems(R.array.items_video_or_image02, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("tag", "dialog");
				if(which == 0){
					Log.i("tag", "dialog0");
					doTakeAlbumAction();
				}else if(which == 1){
					Log.i("tag", "dialog1");
					doSelectVideo();
				}
			}
		})
		.show();
	}
	
}





