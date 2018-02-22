package com.takebox.wedding.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.takebox.wedding.MainActivity;
import com.takebox.wedding.dialog.PhotoAlbumDialog;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.AlbumModel;

public class ImageUpload {
	
	public static JSONObject imgUpload(String url, Bitmap bm, String path){
		
		Map<String,String> data = new HashMap<String,String>();
		String _file_name = path;
		data.put("FILE_NAME", _file_name);	
		
		HttpMultiPart hmp = new HttpMultiPart();
		String res = "";
		try {
			res = hmp.transfer(url, data, bm);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

}
