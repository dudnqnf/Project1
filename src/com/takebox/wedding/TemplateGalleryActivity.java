package com.takebox.wedding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.util.AndroidUtil;
import com.takebox.wedding.util.ImageReScale;

public class TemplateGalleryActivity extends HttpExceptionActivity {
	
	public static Thread thread= null;
	public static ListView template_gallery_list;
	public static TemplateGalleryGridAdapter GalleryGridAdapter;
	public static ArrayList<TemplateGalleryGridItem> GalleryGridItem;
	public static String room_id;
	public String wed_seq;
	private static JSONArray contents;
	
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_ALBUM = 2;
	private Uri uri;
	String path=Environment.getExternalStorageDirectory()+"";
	String img_file_name="temp";
	Uri selPhotoUri;
	private AndroidUtil andUtil = new AndroidUtil();
	public static Bitmap selPhoto = null;
	private String selPhotoPath;
	private String file_name = "";
	
	public static Activity acti;
	public static Context cont;
	public static TextView gallery_cnt;

	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_template_gallery);
	    
	    Intent intent = getIntent();
	    room_id = intent.getStringExtra("room_id");
	    wed_seq = intent.getStringExtra("wed_seq");
	    
	    template_gallery_list = (ListView)findViewById(R.id.template_gallery_list);
	    gallery_cnt = (TextView)findViewById(R.id.gallery_cnt);
	    
	    processParsing(thread, galleryList);
	    
	    FrameLayout back_btn = (FrameLayout)findViewById(R.id.btn_back);
	    back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	    
	}
	
	private final Runnable galleryList = new Runnable() {
		@Override
		public void run() {
			MainActivity.firstExcutePlug = false;
			galleryList();
		}
	};
	
	public void galleryList(){
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String, String>();

		data.put("room_id", room_id);
		data.put("cata_seq", "0");

		JSONObject res = MemberModel.loadRoomInfo(data);
		
		if (!isHttpWorthCheck(res)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {

			try {
				if (res.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		
		try {

			contents = new JSONArray(res.getString("contents"));
			handler.sendEmptyMessage(1);

			return;
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
				
				setGalleryList();
				
				break;
			case 2:
				if (pd != null)
					pd.cancel();

				break;
			
			case 3:
				if (pd != null)
					pd.cancel();
				
				processParsing(thread, galleryList);
				
				break;
			case 4:
				if (pd != null)
					pd.cancel();

				break;
				
			}
		}
	};
	
	private void setGalleryList() {
		// TODO Auto-generated method stub
		int cnt = contents.length();
		int card_img_yn_cnt = 0;
		GalleryGridItem = new ArrayList<TemplateGalleryGridItem>();
		boolean Flag = false;
		TemplateGalleryGridItem item = new TemplateGalleryGridItem();
		for (int i = 0; i < cnt; i++) {
			try {
				if(contents.getJSONObject(i).getString("card_img_yn").equals("Y")){
					card_img_yn_cnt++;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(card_img_yn_cnt!=8){
			item = new TemplateGalleryGridItem();
			item.img_url_l = null;
			if(card_img_yn_cnt==0)
				GalleryGridItem.add(item);
			Flag = true;
		}
		gallery_cnt.setText("( " + card_img_yn_cnt + " / " + 8 + " )");
		for (int i = 0; i < cnt; i++) {
			JSONObject object;
			try {
				object = contents.getJSONObject(i);
				String card_img_yn = object.getString("card_img_yn");
				if(card_img_yn.equals("Y")){
					if(Flag){
						item.img_url_r = Info.MASTER_FILE_URL + "/image/" + object.getString("file_name");
						item.con_seq_r = object.getString("con_seq");
						GalleryGridItem.add(item);
						Flag = false;
					}else{
						item = new TemplateGalleryGridItem();
						item.img_url_l = Info.MASTER_FILE_URL + "/image/" + object.getString("file_name");
						item.con_seq_l = object.getString("con_seq");
						if(i == card_img_yn_cnt-1)
							GalleryGridItem.add(item);
						Flag = true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 쇼핑리스트
		GalleryGridAdapter = new TemplateGalleryGridAdapter(TemplateGalleryActivity.this, R.layout.template_gallery_grid, GalleryGridItem);
		template_gallery_list.setAdapter(GalleryGridAdapter);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode != RESULT_OK){
			return;
		}
		
		switch(requestCode){
		case PICK_FROM_ALBUM:
			
			selPhotoUri = data.getData();
			selPhotoPath = andUtil.getRealPathFromURI(TemplateGalleryActivity.this, selPhotoUri);
			path = selPhotoPath.substring(0, selPhotoPath.lastIndexOf("/"));
			img_file_name = System.currentTimeMillis()+".jpg";
			Log.i("path", path);
			
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(data.getData(), "image/*");
			intent.putExtra("outputX", 644); // crop한 이미지의 x축 크기
			intent.putExtra("outputY", 416); // crop한 이미지의 y축 크기
			intent.putExtra("aspectX", 644); // crop 박스의 x축 비율 
			intent.putExtra("aspectY", 415); // crop 박스의 y축 비율
			intent.putExtra("scale", true);
			intent.putExtra("output", selPhotoUri);
			
			File f = new File(path, "/"+img_file_name);
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());  
			}
			
			uri = Uri.fromFile(f);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, CROP_FROM_ALBUM);
			
			break;
			            
		case CROP_FROM_ALBUM:
			try {
				
				String filePath = path + "/" + img_file_name;
				Log.i("filePath", filePath);
				selPhotoUri = Uri.parse(filePath);

				selPhotoPath = andUtil.getRealPathFromURI(TemplateGalleryActivity.this, selPhotoUri);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;

				if(selPhoto != null){
					selPhoto.recycle();
					selPhoto = null;
				}
				
				//이미지 사이즈 화면 사이즈에 맞게 리스케일
				ImageReScale imgReScale2 = new ImageReScale();
				selPhoto = imgReScale2.loadBackgroundBitmap(getApplicationContext(), selPhotoPath);	
				setImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setImage()
	{
		processParsing(thread, uploadImage);
	}
	
	private final Runnable uploadImage = new Runnable() {
		@Override
		public void run() {
			uploadImage();
		}
	};
	
	/**
	 * 이미지 등록
	 */
	protected void uploadImage() {
		// TODO Auto-generated method stub
		file_name = "croped_img_" + System.currentTimeMillis();
		Map<String, String> data = new HashMap<String, String>();
		data.put("wed_seq", wed_seq);
		data.put("content", "");
		data.put("cata_seq", "0");
		data.put("FILE_NAME", file_name);
		data.put("card_img_yn", "Y");

		JSONObject obj = WeddingModel.procImgUpload(data, selPhoto);
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
				handler.sendEmptyMessage(3);
			} else {
				handler.sendEmptyMessage(4);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parsGalleryList(Activity mActivity, Context mContext){
		acti = mActivity;
		cont = mContext;
		processParsing2(thread, galleryList2);
	}
	
	public static void processParsing2(Thread thread, Runnable runnable) {
		pd = new ProgressDialog(acti);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(null);
		pd.setMessage("로드중...");
		pd.show();

		thread = new Thread(runnable);
		thread.start();
	}
	
	private final static Runnable galleryList2 = new Runnable() {
		@Override
		public void run() {
			galleryList2();
		}
	};
	
	public static void galleryList2(){
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String, String>();

		data.put("room_id", room_id);
		data.put("cata_seq", "0");

		JSONObject res = MemberModel.loadRoomInfo(data);
		
		try {

			contents = new JSONArray(res.getString("contents"));
			handler2.sendEmptyMessage(1);

			return;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final static Handler handler2 = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch (msg.what) {
			case 1:
				if (pd != null)
					pd.cancel();
				
				setGalleryList2();
				
				break;
			case 2:
				if (pd != null)
					pd.cancel();

				break;
				
			}
		}
	};
	
	private static void setGalleryList2() {
		// TODO Auto-generated method stub
		int cnt = contents.length();
		int card_img_yn_cnt = 0;
		GalleryGridItem = new ArrayList<TemplateGalleryGridItem>();
		boolean Flag = false;
		TemplateGalleryGridItem item = new TemplateGalleryGridItem();
		for (int i = 0; i < cnt; i++) {
			try {
				if(contents.getJSONObject(i).getString("card_img_yn").equals("Y")){
					card_img_yn_cnt++;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(card_img_yn_cnt!=8){
			item = new TemplateGalleryGridItem();
			item.img_url_l = null;
			if(card_img_yn_cnt==0)
				GalleryGridItem.add(item);
			Flag = true;
		}
		gallery_cnt.setText("( " + card_img_yn_cnt + " / " + 8 + " )");
		for (int i = 0; i < cnt; i++) {
			JSONObject object;
			try {
				object = contents.getJSONObject(i);
				String card_img_yn = object.getString("card_img_yn");
				if(card_img_yn.equals("Y")){
					if(Flag){
						item.img_url_r = Info.MASTER_FILE_URL + "/image/" + object.getString("file_name");
						item.con_seq_r = object.getString("con_seq");
						GalleryGridItem.add(item);
						Flag = false;
					}else{
						item = new TemplateGalleryGridItem();
						item.img_url_l = Info.MASTER_FILE_URL + "/image/" + object.getString("file_name");
						item.con_seq_l = object.getString("con_seq");
						if(i == card_img_yn_cnt-1)
							GalleryGridItem.add(item);
						Flag = true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 쇼핑리스트
		GalleryGridAdapter = new TemplateGalleryGridAdapter(acti, R.layout.template_gallery_grid, GalleryGridItem);
		template_gallery_list.setAdapter(GalleryGridAdapter);
	}
	
}




class TemplateGalleryGridItem {
	String img_url_l = "#";
	String img_url_r = "#";
	String con_seq_l = null;
	String con_seq_r = null;
	
	TemplateGalleryGridItem(){
	}
}