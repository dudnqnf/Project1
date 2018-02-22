package com.takebox.wedding.info;

import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.takebox.wedding.util.CommonUtil;

/*
 * 사용 하는 키값
 * ID - 로그인 이메일
 * PW - 
 * LOGIN_YN - 로그인 상태 
 * 
 * ROOM_ID - 웨딩룸 아이디
 * ROOM_SEQ - 웨딩룸 번호
 * 
 * JSESSIONID - 쿠키
 * 
 * 
 */


public class User {
	
	
	/**
	 * 회원정보 세팅 
	 * 
	 * @param itemObj
	 * @param mContext
	 */
	public static void setUserInfo(Map<String, String> itemObj, Context mContext)
	{
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		SharedPreferences.Editor editor = app_preferences.edit();
		
		for(Entry<String, String> entry : itemObj.entrySet()) 
		{
			String key = entry.getKey();
		    String value = entry.getValue();
		    editor.putString(key, value);
		}
	    
        editor.commit();
    }
	
	/**
	 * 정보 세팅 1건 
	 * @param key
	 * @param value
	 * @param mContext
	 */
	public static void setUserInfo(String key, String value, Context mContext)
	{
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		SharedPreferences.Editor editor = app_preferences.edit();
		
		editor.putString(key, value);
	    
        editor.commit();
    }
	
	/**
	 * SharedPreferences 함수를 이용한 회원 데이타 가져오기 
	 * 
	 * @param key
	 * @param mContext
	 */
	public static String getUserInfo(String key, Context mContext)
	{
		 SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		 return CommonUtil.nvl(app_preferences.getString(key, ""));
		 
	}
}
