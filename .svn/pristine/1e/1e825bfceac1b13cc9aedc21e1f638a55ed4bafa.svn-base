package com.takebox.wedding;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetupBioActivity extends Activity {
	
	ListView list;
	ArrayList<BioData> arr;
	BioAdapter mAdapter;
	EditText text;
	Button btn;
	TextView lengthText;
	private int maxLength = 200;
	int post_position = -1;
	public static int initial_bio_position = -1;
	String bio;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_setup_bio);
	    
	    text = (EditText)findViewById(R.id.setup_bio_edittext);
	    bio = getIntent().getStringExtra("bio");
	    text.setText(bio);
	    
	    
	    list = (ListView)findViewById(R.id.setup_bio_list);
	    arr = new ArrayList<BioData>();
	    arr = BioData.get_bio();
	    mAdapter = new BioAdapter(SetupBioActivity.this, R.layout.bio_box, arr, bio);
	    list.setAdapter(mAdapter);
	    list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(post_position >=0 ){
					arr.set(post_position, new BioData(arr.get(post_position).bio, R.drawable.btn_radio));
					arr.set(position, new BioData(arr.get(position).bio, R.drawable.btn_radio_on));
					mAdapter.notifyDataSetChanged();
				}
				if(initial_bio_position >= 0){
					arr.set(initial_bio_position, new BioData(arr.get(initial_bio_position).bio, R.drawable.btn_radio));
					arr.set(position, new BioData(arr.get(position).bio, R.drawable.btn_radio_on));
					mAdapter.notifyDataSetChanged();
				}
				ImageView iv = (ImageView)view.findViewById(R.id.bio_box_radio);
				iv.setImageResource(R.drawable.btn_radio_on);
				String bio = arr.get(position).bio;
				text.setText(bio);
				post_position = position;
			}
		});
	    
	    
	    btn = (Button)findViewById(R.id.setup_bio_confirm);
	    btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetupBioActivity.this, JoinSetupProfileActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("bio", text.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(0, 0);
			}
		});
	    
	    lengthText = (TextView)findViewById(R.id.lengthText);
		lengthText.setText(maxLength+"");
	    
	    text.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				try {

					lengthText.setText((maxLength - text.length())+"");
					
					if((maxLength-text.length())==0){
						 Toast.makeText(SetupBioActivity.this, maxLength+"자까지 가능합니다.", Toast.LENGTH_SHORT).show();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
	}

}

class BioAdapter extends BaseAdapter {
	
	Context mContext;
	LayoutInflater Inflater;
	ArrayList<BioData> arrlist;
	int mLayout;
	String post_text;
	boolean flag = true;
	
	public BioAdapter(Context context, int layout, ArrayList<BioData> list, String post_text){
		mContext = context;
		Inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		arrlist = list;
		mLayout = layout;
		this.post_text = post_text;
	}

	@Override
	public int getCount() {
		return arrlist.size();
	}

	@Override
	public Object getItem(int position) {
		return arrlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = Inflater.inflate(mLayout, parent, false);
		}
		
		String bio = arrlist.get(position).bio;
		TextView bio_box = (TextView)convertView.findViewById(R.id.bio_box);
		bio_box.setText(bio);
		int bio_radio = arrlist.get(position).radio;
		ImageView radio = (ImageView)convertView.findViewById(R.id.bio_box_radio);
		radio.setImageResource(bio_radio);
		if(post_text.equals(bio) && flag){
			flag = false;
			radio.setImageResource(R.drawable.btn_radio_on);
			SetupBioActivity.initial_bio_position = position;
		}
		
		return convertView;
	}
	
}
