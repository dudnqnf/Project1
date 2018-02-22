package com.takebox.wedding.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

import com.takebox.wedding.R;

public class CustomUtil {
	
	public static int getCategory(String name){
		ArrayList<String> arr;
		String[] category = new String[]{
				"전체",
				"웨딩사진",
				"드레스",
				"프로포즈",
				"브라이덜샤워",
				"신혼여행",
				"추억의연애시절",
				"메이크업",
				"스튜디오",
				"한복",
				"혼수",
				"부케고르기",
				"예물",
				"신혼집꾸미기",
				"청첩장"
		};
		
		int[] icons = new int[]{
				R.drawable.icon_pop_all,
				R.drawable.icon_pop_wedding,
				R.drawable.icon_pop_dress,
				R.drawable.icon_pop_propose,
				R.drawable.icon_pop_brider,
				R.drawable.icon_pop_honeymoon,
				R.drawable.icon_pop_memory,
				R.drawable.icon_pop_makeup,
				R.drawable.icon_pop_rehearsal,
				R.drawable.icon_pop_traditional,
				R.drawable.icon_pop_furniture,
				R.drawable.icon_pop_bouquet,
				R.drawable.icon_pop_ring,
				R.drawable.icon_pop_bed,
				R.drawable.icon_pop_card
		};
		
		int default_icon = R.drawable.icon_pop_new;
		
		arr = new ArrayList<String>();
		arr.addAll(Arrays.asList(category));
		
		if(arr.indexOf(name) > 0)
			return icons[arr.indexOf(name)];
		else
			return default_icon;
	}
}
