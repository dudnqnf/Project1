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
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */


public class Version {
	
	/**
	 * 정보 세팅
	 * @param key
	 * @param value
	 * @param mContext
	 */
	public static void setVersionInfo(String key, String value, Context mContext)
	{
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		SharedPreferences.Editor editor = app_preferences.edit();
		
		editor.putString(key, value);
	    
        editor.commit();
    }
	
	/**
	 * SharedPreferences 버전정보 불러오기
	 * 
	 * @param key
	 * @param mContext
	 */
	public static String getVersionInfo(String key, Context mContext)
	{
		 SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		 return CommonUtil.nvl(app_preferences.getString(key, ""));
	}
}
