package com.takebox.wedding.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;
import android.widget.TextView;

public class Time {
	public static void CalculateOvertime(TextView tv, String regdate){
		//지난 시간체크
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(calendar.getTime());
		int secondterm = Integer.parseInt(time.substring(17, 19))-Integer.parseInt(regdate.substring(17, 19));
		int minuteterm = Integer.parseInt(time.substring(14, 16))-Integer.parseInt(regdate.substring(14, 16));
		int hourterm = Integer.parseInt(time.substring(11, 13))-Integer.parseInt(regdate.substring(11, 13));
		int dayterm = Integer.parseInt(time.substring(8, 10))-Integer.parseInt(regdate.substring(8, 10));
		int monthterm = Integer.parseInt(time.substring(5, 7))-Integer.parseInt(regdate.substring(5, 7));
		int yearterm = Integer.parseInt(time.substring(0, 4))-Integer.parseInt(regdate.substring(0, 4));
		//		row_profile_overtime.setText(""+yearterm+monthterm);
		if(yearterm>0){
			if(monthterm<0 && yearterm==1){
				tv.setText(12+monthterm+"달 전");
			} else {
				tv.setText(yearterm+"년 전");
			}
		}else if(monthterm>=1){
			if(dayterm<0 && monthterm==1){
				if(-dayterm>=7){
					tv.setText(-dayterm/7+"주 전");
				} else {
					tv.setText(-dayterm+"일 전");
				}
			} else {
				tv.setText(monthterm+"달 전");
			}
		}else if(dayterm>0){
			if(hourterm<0 && dayterm==1){
				tv.setText(24+hourterm+"시간 전");
			} else {
				if(dayterm>=7){
					tv.setText(dayterm/7+"주 전");
				} else {
					tv.setText(dayterm+"일 전");
				}
			}
		}else if(hourterm>0){
			if(minuteterm<0 && hourterm==1){
				tv.setText(60+minuteterm+"분 전");
			} else {
				if(minuteterm<0)
					tv.setText(hourterm-1+"시간 전");
				else
					tv.setText(hourterm+"시간 전");
			}
		}else if(minuteterm>0){
			if(secondterm<0 && minuteterm==1){
//				tv.setText(60-secondterm+"초 전");
				tv.setText("방금");
			} else {
				if(secondterm<0)
					tv.setText(minuteterm-1+"분 전");
				else 
					tv.setText(minuteterm+"분 전");
			} 
		}else if(secondterm>0){
//			tv.setText(secondterm+"초 전");
			tv.setText("방금");
		}else{
//			tv.setText("시간오류");
			tv.setText("방금");
		}
	}
	
	public static void regdate(TextView tv, String regdate, int format){
		//지난 시간체크
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String time = dateFormat.format(calendar.getTime());
		int secondterm = Integer.parseInt(time.substring(17, 19))-Integer.parseInt(regdate.substring(17, 19));
		int minuteterm = Integer.parseInt(time.substring(14, 16))-Integer.parseInt(regdate.substring(14, 16));
		int hourterm = Integer.parseInt(time.substring(11, 13))-Integer.parseInt(regdate.substring(11, 13));
		int dayterm = Integer.parseInt(time.substring(8, 10))-Integer.parseInt(regdate.substring(8, 10));
		int monthterm = Integer.parseInt(time.substring(5, 7))-Integer.parseInt(regdate.substring(5, 7));
		int yearterm = Integer.parseInt(time.substring(0, 4))-Integer.parseInt(regdate.substring(0, 4));
		
		//게시글 등록날짜
		if(!regdate.equalsIgnoreCase("null")){
			switch (format) {
			case 1:
				if(yearterm>0){
					tv.setText(regdate.substring(0, 4)+"."+regdate.substring(5, 7)+"."+regdate.substring(8, 10));
				} else {
					tv.setText(regdate.substring(5, 7)+"."+regdate.substring(8, 10));
				}
				break;
				
			case 2:
				tv.setText(regdate.substring(0, 4)+"."+regdate.substring(5, 7)+"."+regdate.substring(8, 10));
				break;

			default:
				tv.setText("");
				break;
			}
			
		} else {
			tv.setText("");
		}
	}
	
	
	public static String calculateDate(String regdate){
		String date = regdate.substring(0,4)+"."+regdate.substring(5,7)+"."+regdate.substring(8,10);
		return date;
	}
	
	public static String calculateTime(String regdate){
		int hour = Integer.parseInt(regdate.substring(0,2));
		String time;
		if(hour<12){
			time = "am "+regdate.substring(0,2)+":"+regdate.substring(2,4)+"";
		} else if(hour==12){
			time = "pm "+regdate.substring(0,2)+":"+regdate.substring(2,4)+"";
		} else {
			time = "pm "+(Integer.parseInt(regdate.substring(0,2))-12)+":"+regdate.substring(2,4)+"";
		}
		return time;
	}
}
