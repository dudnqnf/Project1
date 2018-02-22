package com.takebox.wedding.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogBuilder {
	//팝업OK
		public static AlertDialog.Builder pop_ok(Context mContext, String txt){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);     // 여기서 this는 Activity의 this

			// 여기서 부터는 알림창의 속성 설정
			builder.setTitle("")        // 제목 설정
			.setMessage(txt)        // 메세지 설정
			.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
			.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
				// 확인 버튼 클릭시 설정
				public void onClick(DialogInterface dialog, int whichButton){
					dialog.dismiss();   

				}
			});
			
			return builder;
		}
}
