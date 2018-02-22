package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.model.AlbumModel;

public class TemplateGalleryGridAdapter extends ArrayAdapter<TemplateGalleryGridItem>{
	
	private ArrayList<TemplateGalleryGridItem> items;
	private Context mContext;
	private Activity mActivity;
	private static final int PICK_FROM_ALBUM = 1;
	private Thread mThread = null;
	static ProgressDialog pd = null;
	private String info;
	private String con_seq;

	public TemplateGalleryGridAdapter(Activity activity, int textViewResourceId, ArrayList<TemplateGalleryGridItem> items) {
		super(activity.getApplicationContext(), textViewResourceId, items);
		mContext = activity.getApplicationContext();
		mActivity = activity;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.template_gallery_grid, null);
		}

		ImageView iv_img1 = (ImageView)v.findViewById(R.id.template_gallery_l);
		ImageView iv_img2 = (ImageView)v.findViewById(R.id.template_gallery_r);
		ImageView cancelImg01 = (ImageView)v.findViewById(R.id.cancelImg01);
		ImageView cancelImg02 = (ImageView)v.findViewById(R.id.cancelImg02);
		String img_url_l = items.get(position).img_url_l;
		String img_url_r = items.get(position).img_url_r;
		final String con_seq_l = items.get(position).con_seq_l;
		final String con_seq_r = items.get(position).con_seq_r;
		AQuery aq = new AQuery(mContext);
		if(img_url_l != null){
			aq.id(iv_img1).image(items.get(position).img_url_l, true, true);
			if(img_url_l != "#")
				cancelImg01.setVisibility(View.VISIBLE);
		}
		if(img_url_r != null){
			aq.id(iv_img2).image(items.get(position).img_url_r, true, true);
			if(img_url_r != "#")
				cancelImg02.setVisibility(View.VISIBLE);
		}
		
		if(img_url_l == null){		//+버튼일때
			iv_img1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("doTakeAlbumAction");
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_PICK);
					intent.setType("image/*");
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mActivity.startActivityForResult(intent, PICK_FROM_ALBUM);
				}
			});
		}else if(img_url_l != "#"){													//사진일때 삭제하기
			iv_img1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					con_seq = con_seq_l;
					deleteGallery();
				}
			});
		}
		
		if(img_url_r != "#"){													//사진일때 삭제하기
			iv_img2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					con_seq = con_seq_r;
					deleteGallery();
				}
			});
		}
		
		
			
		return v;
	}
	
	public void deleteGallery(){
		//게시글 지우기
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(mActivity);
	    alt_bld.setMessage("삭제하시겠습니까?").setCancelable(
	        false).setNegativeButton("아니요",
	    	        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            return;
		        }
	        }).setPositiveButton("네",
	    	        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		        	processParsing(mThread, content_delete);
		        	MainActivity.firstExcutePlug = false;
		        }
	        });
	    AlertDialog alert = alt_bld.create();
	    // Title for AlertDialog
	    alert.show();
	}
	
	public void processParsing(Thread thread, Runnable runnable) {
		pd = new ProgressDialog(mActivity);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(null);
		pd.setMessage("로드중...");
		pd.show();
		thread = new Thread(runnable);
		thread.start();
	}
	
	//게시글 삭제
	private final Runnable content_delete = new Runnable() {
		@Override
		public void run() {
			content_delete();
		}
	};
	
	/**
	 * 컨텐츠 삭제하기
	 */
	protected void content_delete() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		data.put("con_seq", con_seq);

		JSONObject obj = AlbumModel.procDelContent(data);

		try {
			info = obj.getString("info").toString();

			//성공
			if(info.equals("ok")){
				handler.sendEmptyMessage(1);
			} else {
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
				
				TemplateGalleryActivity.parsGalleryList(mActivity, mContext);

				break;
			case 2:
				if(pd!=null) pd.cancel();

				AlertDialog dialog = AlertDialogBuilder.pop_ok(mContext, "데이터 전송에 실패하였습니다.").create();    // 알림창 객체 생성
				dialog.show();
				
				break;
			}
		}

	};
	
}
