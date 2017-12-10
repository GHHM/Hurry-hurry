package org.androidtown.hurryhurry_client.utils;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import org.androidtown.hurryhurry_client.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Util {
	final static String TAG = "Util";

	public static ProgressDialog showLodingDialog(Context context){
		return ProgressDialog.show(context, "", "");
	}

	public static Dialog showProgressDialog(Context context){
		Dialog mDialog = new Dialog(context, R.style.custom_progress_dialog);

		mDialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		return mDialog;
	}

	public static void keybordHide(Context context, View v){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showKeyboard(Context context, View v){
		InputMethodManager imm = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
		imm.showSoftInput(v, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static boolean isRunningService(Context context, Class<?> cls) {
		boolean isRunning = false;

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);

		if (info != null) {
			for(ActivityManager.RunningServiceInfo serviceInfo : info) {
				ComponentName compName = serviceInfo.service;
				String className = compName.getClassName();

				if(className.equals(cls.getName())) {
					isRunning = true;
					break;
				}
			}
		}
		return isRunning;
	}

	public static boolean isRunningProcess(Context context, String packageName) {

		boolean isRunning = false;

		ActivityManager actMng = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();

		for(ActivityManager.RunningAppProcessInfo rap : list)
		{
			Log.i("VisitNurse", "running app name  : " + rap.processName);
			if(rap.processName.equals(packageName))
			{
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}


	// 2017.3.21  255:255:255 to 255,255,255
	public static String colorParser(String rgb_color, String rgb_type){
		StringTokenizer tokens = new StringTokenizer(rgb_color,":");
		String r = null, g = null, b = null;
		for (int i = 0; tokens.hasMoreElements(); i++){
			switch (i){
				case 0 :
					r = tokens.nextToken();
					break;
				case 1 :
					g = tokens.nextToken();
					break;
				case 2 :
					b = tokens.nextToken();
					break;
			}
		}
		if(rgb_type.equals("r")){
			return r;
		}else if(rgb_type.equals("g")){
			return g;
		}else{
			return b;
		}
	}

	public static boolean isEmail(String email) {
		if (email==null){
			return false;
		}
        boolean b = Pattern.matches(
            "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+" ,
            email.trim());
        return b;
    }

	public static String calcAgoTime(Date chageDate){
		Date today = new Date();

		long diff = today.getTime() - chageDate.getTime();

		//day
		long diffTime = diff / (24 * 60 * 60 * 1000);

		if(diffTime>0){
			return diffTime + " days ago";
		}

		//hours
		diffTime = diff / (60 * 60 * 1000);
		if(diffTime>0){
			return diffTime + " hours ago";
		}

		//minutes
		diffTime = diff / (60 * 1000);
		if(diffTime>0){
			return diffTime + " minutes ago";
		}
		else if(diffTime==0){
			return "1 minutes ago";
		}

		return "";
	}

	public static Bitmap getImageBitmap(String url) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
//			Log.e(TAG, "Error getting bitmap", e);
		}

		int height=bm.getHeight();
		int width=bm.getWidth();

		bm = Bitmap.createScaledBitmap(bm, 160, height/(width/160), true);

		return bm;
	}

//	public static void getImageBitmap(final Context context, String url) {
//		 AQuery mAquery = new AQuery(context);
//
//		 mAquery.id(R.id.item_widget_iv_profile).image(url, true, true);
//
////		 mAquery.image(url, false, true, 0, 0, new BitmapAjaxCallback(){
////			 @Override
////			 public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
////				 ComponentName componentName = new ComponentName(context, ZamongWidget.class);
////					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
////					appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widget_listview);
////
//////				 Intent intent = new Intent(ZamongWidgetUtil.ACTION_IMAGE_DOWNLOAD_DOWN);
//////				 context.sendBroadcast(intent);
////			 }
////		 });
//	}





	// strFilePath 는 저장할 경로
	public static File createThumbnail(Bitmap bitmap) {

		String strFilePath =  getFilePath();


//         File file = new File(strFilePath);

		File fileCacheItem = new File(strFilePath + "HealthUp_Profile.png");
		OutputStream out = null;

		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

//             int height=bitmap.getHeight();
//             int width=bitmap.getWidth();

			//160 부분을 자신이 원하는 크기로 변경할 수 있습니다.
//             bitmap = Bitmap.createScaledBitmap(bitmap, 160, height/(width/160), true);
//             bitmap.compress(CompressFormat.PNG, 100, out);


			//비율로 줄이기
			int iWidth = bitmap.getWidth();      //비트맵이미지의 넓이
			int iHeight = bitmap.getHeight();     //비트맵이미지의 높이
			int newWidth = iWidth ;
			int newHeight = iHeight ;
//			int maxResolution = 360;
            int maxResolution = 500;  //15.10.12 해상도 조절 작업
//			int maxResolution = 600; //해상도 조절
			float rate = 0.0f;

			//이미지의 가로 세로 비율에 맞게 조절
			if(iWidth > iHeight ){
				if(maxResolution < iWidth ){
					rate = maxResolution / (float) iWidth ;
					newHeight = (int) (iHeight * rate);
					newWidth = maxResolution;
				}
			}else{
				if(maxResolution < iHeight ){
					rate = maxResolution / (float) iHeight ;
					newWidth = (int) (iWidth * rate);
					newHeight = maxResolution;
				}
			}

			bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
			bitmap.compress(CompressFormat.PNG, 100, out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return fileCacheItem;
	}

	public static synchronized String getFilePath()
	{
		String sdcard = Environment.getExternalStorageState();
		File file = null;

		if ( !sdcard.equals(Environment.MEDIA_MOUNTED))
		{
			file = Environment.getRootDirectory();
		}
		else
		{
			// SD카드가 마운트되어있음
			file = Environment.getExternalStorageDirectory();
		}

		String dir = file.getAbsolutePath() + String.format("/healthup/profile/");
//	     String path = file.getAbsolutePath() + String.format("/mytestdata/file_%02d/myfile%04d.mp4", fileType, fileId);

		file = new File(dir);
		if ( !file.exists() )
		{
			file.mkdirs();
		}

		return dir;
	}

	public static synchronized Bitmap getRotatedImage(Bitmap bitmap, int degrees){

		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();

			m.setRotate(degrees, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2 );
			try {
				Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != b2) {
					bitmap.recycle();
					bitmap = b2;
				}
			}
			catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
//				Log.d(TAG, "getRotatedImage OutOfMemoryError ~!!!!!!!!");
			}
		}
		return bitmap;
	}

	public synchronized static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		}
		catch (IOException e) {
//			Log.e("TAG", "cannot read exif");
			e.printStackTrace();
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch(orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}

		return degree;
	}

	public static File createRotatedFile(Bitmap bitmap) {

		String strFilePath =  getFilePath();


//         File file = new File(strFilePath);

		File fileCacheItem = new File(strFilePath + "profile_rotated.png");
		OutputStream out = null;

		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(CompressFormat.PNG, 100, out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return fileCacheItem;
	}


	/*
	 * Bitmap
	 */

	public static Bitmap loadSelectImageBitmap(Context context, String imgFilePath) throws Exception, OutOfMemoryError{
		//파일 존재 유무 체크 해서 exception

		//폰의 화면 사이즈를 구한다
		Display dislplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int displayWidget = dislplay.getWidth();
		int displayHeight = dislplay.getHeight();

		//읽어 들일 이미지의 사이즈를 구한다.
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgFilePath, options);

		//화면 사이즈에 가장 근접한느 이미지의 스케일 팩터를 구한다.
		// 스케일 팩터는 이미지 손실을 최소화하기 위해 짝수로 한다.
		float widthScale = options.outWidth / displayWidget;
		float heightScale = options.outHeight / displayHeight;
		float scale = widthScale > heightScale ? widthScale : heightScale;

		if(scale >= 8){
			options.inSampleSize = 8;
		}
		else if(scale >= 6){
			options.inSampleSize = 6;
		}
		else if(scale >= 4){
			options.inSampleSize = 4;
		}
		else if(scale >= 2){
			options.inSampleSize = 2;
		}
		else{
			options.inSampleSize = 1;
		}

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(imgFilePath, options);
	}

	// 2016.12.05 image radius

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius*2, radius*2, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		int size = (bitmap.getWidth()/2);
		int size_2 = (bitmap.getHeight()/2);

		float mDrawableRadius = Math.min(bitmap.getHeight() / 2.1f, bitmap.getWidth() / 2.2f);

		canvas.drawCircle(size, size_2, mDrawableRadius, paint);

//		 canvas.drawCircle(bitmap.getWidth() / 2+0.7f, bitmap.getHeight() / 2+0.7f,
//				 bitmap.getWidth() / 2+0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// 2016.12.09
	public static String CheckDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	//2016.12.09
	public static String setDateFormat(String currentdate) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String format_date = null;
		int year = 0;
		int mounth = 0;
		int day = 0;
		try {
			Date date = (Date) format.parse(currentdate);
			year = date.getYear() + 1900;
			mounth = date.getMonth() + 1;
			day = date.getDate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		format_date = String.valueOf(year) + "." + Util.CheckDigit(mounth) + "." + Util.CheckDigit(day);

		return format_date;
	}

	//2017.07.26
	public static String setDateFormat_hyphen(String date){

		Date format_date = null;
		try {
			format_date = new SimpleDateFormat("yyyyMMdd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(format_date);

		return String.format("%d-%s-%s", calendar.get(Calendar.YEAR), Util.CheckDigit(calendar.get(Calendar.MONTH) + 1), Util.CheckDigit(calendar.get(Calendar.DATE)));
	}

	public static boolean isBlankOrSpacing(String str){
		if(str.equals("") || str.matches("\\s+")){
			return true;
		}
		return false;
	}
}
