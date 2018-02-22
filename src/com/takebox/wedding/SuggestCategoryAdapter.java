package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.util.CustomUtil;
import com.takebox.wedding.R;

class SuggestCategoryAdapter extends BaseAdapter{
	Context mContext;
	LayoutInflater Inflater;
	static ArrayList<CategoryEditData> arrlist = new ArrayList<CategoryEditData>();
	private ProgressDialog pd = null;
	private Thread mThread = null;
	int mLayout;
	
	private String cata_seq;
	private int mPosition;
	private String room_no;
	private String category_name;
	
	
	public SuggestCategoryAdapter(Context context, int layout, ArrayList<CategoryEditData> list, String room_no){
		mContext = context;
		Inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		arrlist = list;
		mLayout = layout;
		this.room_no = room_no;
	}
	
	@Override
	public int getCount() {
		return arrlist.size();
	}

	@Override
	public CategoryEditData getItem(int position) {		
		return arrlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if(convertView == null) {
			convertView = Inflater.inflate(mLayout, parent, false);
		}
		
		final String category_no = arrlist.get(position).cata_seq;
		
		final TextView category = (TextView)convertView.findViewById(R.id.category);
		String name = arrlist.get(position).category+"";
		category.setText(name);
		category.setTypeface(CustomTextFont.NanumGothic);
		
		
		ImageView icon = (ImageView)convertView.findViewById(R.id.category_icon);
		String cate = category.getText().toString();
		icon.setImageResource(CustomUtil.getCategory(cate));
		
		Button btn = (Button)convertView.findViewById(R.id.categoryAddDel);
		btn.setBackgroundResource(R.drawable.btn_pop_add);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPosition = pos;
				cata_seq = category_no;
				category_name = category.getText().toString();
				processParsing(mThread, save);
			}
		});
		
		return convertView;
	}
	
	public void processParsing(Thread thread, Runnable runnable) {
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(null);
		pd.setMessage("로드중...");
		pd.show();

		thread = new Thread(runnable);
		thread.start();
	}
	
	private final Runnable save = new Runnable() {
		@Override
		public void run() {
			save();
		}
	};
	
	//앨범 카테고리 등록
	protected void save() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();		//추천 앨범 -> 현재앨범
		
		
		data.put("name", category_name);
		data.put("room_no", room_no);
		data.put("cata_seq", cata_seq);

		JSONObject obj = WeddingModel.procSaveCreateAlbum(data);		
		if(obj==null){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}
		
		try {
			String info = obj.getString("info");

			//에러코드 확인
			if(info.equals("ok")){
				//성공
				handler.sendEmptyMessage(1);
			}else{
				//실패
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

				if(pd!=null) pd.cancel(); //등록 성공
				
				add_category();
				notifyDataSetChanged();
				
				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(mContext, "데이터 전송에 실패하였습니다.").create();    // 알림창 객체 생성
				dialog.show();
				
				break;
			}
	
		}

	};
	
	public void add_category(){
		CategoryEditActivity.loadData2(mContext);
//		Toast.makeText(mContext, "통신완료", Toast.LENGTH_SHORT).show();
	}
	
}