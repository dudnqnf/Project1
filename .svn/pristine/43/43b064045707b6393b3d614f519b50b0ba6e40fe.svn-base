package com.takebox.wedding.custom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.takebox.wedding.JoinSetupProfileActivity;
import com.takebox.wedding.R;
import com.takebox.wedding.info.Info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomCropImage extends ImageView {
	private static final String TAG = "Crop_Image";
	float sx, ex, sy, ey;
	static int DEP = 30;

	CustomCropImageActivity cnxt;

	Bitmap bitmap = null;
	float mWidth;
	float mHeight;
	Paint pnt;

	Bitmap hBmp;
	Bitmap wBmp;

	public CustomCropImage(Context context, AttributeSet attrs) {
		super(context, attrs);

		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		mWidth = display.getWidth();
//		mHeight = display.getHeight();
//
//		sx = mWidth / 5;
//		ex = mWidth * 4 / 5;
//		sy = mHeight / 5;
//		ey = mHeight * 4 / 5;


		cnxt = (CustomCropImageActivity) context;

		BitmapFactory.Options resizeOpts = new Options();
		resizeOpts.inSampleSize = 1;
		try {
			bitmap = BitmapFactory.decodeStream(
					new FileInputStream(JoinSetupProfileActivity.outFilePath), null, resizeOpts);
			
//			float imgWidth = bitmap.getWidth();
//			float imgHeight = bitmap.getHeight();
//			float temp = display.getWidth() - imgWidth;
//			imgWidth = imgWidth + temp;
//			imgHeight = imgHeight + temp;
			
			mWidth = bitmap.getWidth();
			mHeight = bitmap.getHeight();
			
			float temp = display.getWidth() - mWidth;
			float r = mHeight / mWidth;
			
			
			
			mWidth = mWidth + temp;
			mHeight = mHeight + (temp*r);
			
			sx = mWidth / 5;
			ex = mWidth * 4 / 5;
			sy = mHeight / 5;
			ey = mHeight * 4 / 5;
			
			bitmap = Bitmap.createScaledBitmap(bitmap, (int) mWidth,
					(int) mHeight, false);
			Log.e(TAG, "" + bitmap.getHeight() * bitmap.getWidth());
			hBmp = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.camera_crop_height);
			wBmp = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.camera_crop_width);

			pnt = new Paint();
			pnt.setColor(Color.MAGENTA);
			pnt.setStrokeWidth(3);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDraw(Canvas canvas) {
		try {
			canvas.drawBitmap(bitmap, 0, 0, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		canvas.drawLine(sx, sy, ex, sy, pnt);
		canvas.drawLine(ex, sy, ex, ey, pnt);
		canvas.drawLine(sx, sy, sx, ey, pnt);
		canvas.drawLine(sx, ey, ex, ey, pnt);
		canvas.drawBitmap(hBmp, (ex + sx) / 2 - 19, sy - 19, null);
		canvas.drawBitmap(hBmp, (ex + sx) / 2 - 19, ey - 19, null);
		canvas.drawBitmap(wBmp, sx - 19, (ey + sy) / 2 - 19, null);
		canvas.drawBitmap(wBmp, ex - 19, (ey + sy) / 2 - 19, null);
	}

	float dx = 0, dy = 0;
	float oldx, oldy;
	boolean bsx, bsy, bex, bey;
	boolean bMove = false;
	
	int standard_x;
	
	public boolean onTouchEvent(MotionEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();

		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			oldx = x;
			oldy = y;
			if ((x > sx - DEP) && (x < sx + DEP))
				bsx = true;
			else if ((x > ex - DEP) && (x < ex + DEP))
				bex = true;

			if ((y > sy - DEP) && (y < sy + DEP))
				bsy = true;
			else if ((y > ey - DEP) && (y < ey + DEP))
				bey = true;

			if ((bsx || bex || bsy || bey))
				bMove = false;
			else if (((x > sx + DEP) && (x < ex - DEP))
					&& ((y > sy + DEP) && (y < ey - DEP)))
				bMove = true;

			return true;
		}

		if (e.getAction() == MotionEvent.ACTION_MOVE) {  // 이부분이 영역을 넘어간다
			if (bsx)
				sx = x;
			if (bex)
				ex = x;
			Log.i("tag","ex:"+ex);
			if (bsy)
				sy = y;
			Log.i("tag","sy:"+sy);
			if (bey)
				ey = y;
			Log.i("tag","ey:"+ey);

			if (ex <= sx + DEP) {
				ex = sx + DEP;
				ey = sx + DEP;
				return true;
			}
			if (ey <= sy + DEP) {
				ey = sy + DEP;
				ex = sy + DEP;
				return true;
			}

			if (bMove) {
					dx = oldx - x;
					dy = oldy - y;
	
					sx -= dx;
					ex -= dx;
					sy -= dy;
					ey -= dy;
					if (sx <= 0)
						sx = 0;
					if (ex >= mWidth)
						ex = mWidth - 1;

			}
			
			if (sy <= 0)
				sy = 0;
			if (ey >= mHeight)
				ey = mHeight - 1;

			invalidate();
			oldx = x;
			oldy = y;
			return true;
		}

		if (e.getAction() == MotionEvent.ACTION_UP) {
			bsx = bex = bsy = bey = bMove = false;
			return true;
		}
		return false;
	}

	public void save() {
		Bitmap tmp = Bitmap.createBitmap(bitmap, (int) sx, (int) sy,
				(int) (ex - sx), (int) (ey - sy));
		byte[] byteArray = bitmapToByteArray(tmp);
		File file = new File(JoinSetupProfileActivity.outFilePath);
		Log.e("nicehee", file.getAbsolutePath());
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(byteArray);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Toast.makeText(cnxt, "파일 저장 중 에러 발생 : " + e.getMessage(), 0).show();
			return;
		}
	}
	
	
//	public void delete(){
//		File file = new File(outFilePath);
//		if(file.exists()){
//			file.delete();
//		}
//	}
	

	public byte[] bitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
}