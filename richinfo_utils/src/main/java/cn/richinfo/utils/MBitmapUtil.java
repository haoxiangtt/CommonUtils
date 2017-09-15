package cn.richinfo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/5/11 0011
 * @modifyDate : 2017/5/11 0011
 * @version    : 1.0
 * @desc       : 图片处理帮助类
 * </pre>
 */
public class MBitmapUtil {
	
	public static final String TAG = "MBitmapUtil";
	
	public static final boolean DEBUG = false;

	/**
	 * 按比例进行图片裁切
	 * 
	 * @param context
	 *            android上下文环境
	 * @param sourceBitmap
	 *            原始图片
	 * @param cropRatio
	 *            目标图片的宽高比
	 * @param horizontalRatio
	 *            宽图片裁切位置占比
	 * @param verticalRatio
	 *            高图片裁切位置占比
	 * @return
	 */
	public static Bitmap crop(Context context, Bitmap sourceBitmap,
			float cropRatio, float horizontalRatio, float verticalRatio) {

		Bitmap cropResult = null;

		int screenWidth = ScreenUtil.getScreenWidth(context);

		int sourceWidth = sourceBitmap.getWidth();
		int sourceHeight = sourceBitmap.getHeight();
		
		if(sourceWidth < 10 || sourceHeight < 10){ return null; }
		
//		if(DEBUG){ Logger.e(TAG, ">>>>>>>source height"+sourceHeight+"<<<<<<<<<<<<<<"); }

		float sourceRatio = (float) sourceWidth / sourceHeight;

		int height;
		int width;
		int startX;
		int startY;

		/**
		 * 宽图片
		 */
		if (sourceRatio > cropRatio) {
//			if(DEBUG){ Logger.e(TAG, ">>>>>>>width bitmap<<<<<<<<<<<<<<"); }
			height = sourceHeight;
			width = (int) (height * cropRatio);
			startX = (int) ((sourceWidth - width) * horizontalRatio);
			startY = 0;
		} else
		/**
		 * 长图片
		 */
		if (sourceRatio < cropRatio) {
//			if(DEBUG){ Logger.e(TAG, ">>>>>>>long bitmap<<<<<<<<<<<<<<"); }
			width = sourceWidth;
			height = (int) (width / cropRatio);
			startX = 0;
			startY = (int) ((sourceHeight - height) * verticalRatio);
		} else
		/**
		 * 宽高比刚好相等
		 */
		{
//			if(DEBUG){ Logger.e(TAG, ">>>>>>>width equals height<<<<<<<<<<<<<<"); }
			width = sourceWidth;
			height = sourceHeight;
			startX = 0;
			startY = 0;
		}

		float scale = (float) screenWidth / width;
		scale = scale > 1 ? 1 : scale;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

//		if(DEBUG){ Logger.e(TAG, ">>>>>>>final height"+height+"<<<<<<<<<<<<<<"); }
		
		if(width < 1 || height < 1){ return null; }
		
		cropResult = Bitmap.createBitmap(sourceBitmap, startX, startY, width,
				height, matrix, true);

		return cropResult;
	}
	
	/**
	 * 放缩图片至指定宽高
	 * @param bitmap 原始图片
	 * @param width 指定宽度
	 * @param height 指定高度
	 * @return 放缩后的图片
	 */
	public static Bitmap zoombitmap(Bitmap bitmap,int width,int height){
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float)width / w);
		float scaleHeight = ((float)height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h,matrix,true);
		return newBitmap;
	}
	
	/**
	 * 制作圆角边框
	 * @param bitmap 原始图片资源
	 * @param roundPx 圆角半径
	 * @return 处理后的图片资源
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0,0,w,h);
		final RectF rectf = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectf, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect,rect, paint);
		return output;
	}
	
	
	
	/**
	 * bitmap转换成二进制数组
	 * @param bm
	 * @return
	 */
	public static byte[] bitmap2bytes(Bitmap bm){
		if(bm == null){ return null; }
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	
	/**
	 * 二进制数组转换成bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap bytes2Bitmap(byte[] b){
		if(b != null && b.length > 0){
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static boolean gifCheck(String name){
		Pattern patternPhone = Pattern.compile(".gif$");
		Matcher matPhone = patternPhone.matcher(name);
		return matPhone.find();
	}
	
	
	/**
	 * 保存图片到本地
	 * @param path 图片保存路径
	 * @param bmp 图片资源
	 * @return
	 */
	public static String saveBitmap(String path,Bitmap bmp) {
		
		if(bmp == null){ return ""; }
		
//		File path=FileUtils.getImageCachePath();
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out != null){ out.close(); }
			} catch (IOException e) {}
		}
		
		return f.getAbsolutePath();

	}
	
	
	/**
	 * 获取视图界面的截图 
	 * @param views
	 * @return
	 */
	public static Bitmap getBitmapByView(ViewGroup views) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取scrollview实际高度
		for (int i = 0; i < views.getChildCount(); i++) {
			h += views.getChildAt(i).getHeight();
		}

		int scrollViewHeight = views.getHeight();
		if (scrollViewHeight > h) {
			h = scrollViewHeight;
		}
		// 创建bitmap，其大小为原试图界面大小的1/4，确保不会内存溢出
		bitmap = Bitmap.createBitmap(views.getWidth() / 2, h / 2,
				Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Paint p = new Paint();
		p.setAntiAlias(true);
		
		
		Canvas childCanvas = new Canvas();
		for (int i = 0; i < views.getChildCount(); i++) {
			View child = views.getChildAt(i);
			Bitmap bmp = Bitmap.createBitmap(child.getWidth(),child.getHeight(),
					Config.RGB_565);
			childCanvas.setBitmap(bmp);
			child.draw(childCanvas);
			Rect src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
			RectF dst = new RectF(child.getX() / 2,child.getY() / 2,
					bmp.getWidth() / 2 , bmp.getHeight() / 2);
			canvas.drawBitmap(bmp, src, dst, p);
			childCanvas.setBitmap(null);
			if(!bmp.isRecycled()){ bmp.recycle();bmp = null; }
		}
		
//		final Canvas canvas = new Canvas(bitmap);
//		views.draw(canvas);
		return bitmap;
	}
	
	/**
	* 以最省内存的方式读取本地资源的图片
	* @param context
	* @param resId
	* @return
	*/  
	 public static Bitmap readBitMap(Context context,int resId){  
		 BitmapFactory.Options opt = new BitmapFactory.Options();  
		 opt.inPreferredConfig = Config.ARGB_8888;
		 opt.inPurgeable = true;  
		 opt.inInputShareable = true; 
		 opt.inSampleSize = 2;
		 
		 //获取资源图片  
		 InputStream is = context.getResources().openRawResource(resId);  
		 return BitmapFactory.decodeStream(is, null, opt);
	 }
	 
	 
	 
	 
	 /**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int angle = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					angle = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					angle = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					angle = 270;
					break;
			}
		} catch (IOException e) {
			LogUtil.e(TAG, " read degree error: "+e.toString());
		}
		return angle;
	}
	
	
	
	
	/**
	 * 获取图片，放在临时存储中，避免内存溢出
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getBmpToCache(String path) {
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false; // 使图片不抖动
		bfOptions.inPurgeable = true; // 使得内存可以被回收
		bfOptions.inTempStorage = new byte[16 * 1024]; // 临时存储
		//bfOptions.inJustDecodeBounds = true;
		File file = new File(path);
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG, " file not found error: "+e.toString());
		} catch (Exception e) {
			LogUtil.e(TAG, " new file error: "+e.toString());
		}
		Bitmap bmp = null;
		if (fs != null){
			try {
				bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
			} catch (IOException e) {
				LogUtil.e(TAG, " decodeFileDescriptor io error: "+e.toString());
			} catch (OutOfMemoryError e) {
				LogUtil.e(TAG, " decodeFileDescriptor out of memory error: "+ e.toString());
			}catch (Exception e) {
				LogUtil.e(TAG, " decodeFileDescriptor error: "+e.toString());
			}finally {
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						LogUtil.e(TAG, " close stream error: "+e.toString());
					}
				}
			}
		}
		return bmp;
	}

	/**
	 * Drawable转化为Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if(drawable == null){ return null; }
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}
	/**
	 * Bitmap to Drawable
	 * @param bitmap
	 * @param context
	 * @return
	 */
	public static Drawable bitmapToDrawble(Context context,Bitmap bitmap){
		if(context == null || bitmap == null){ return null; }
		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
		return drawable;
	}





	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotateImage(int angle, Bitmap bitmap) {
		if(angle == 0){
			return bitmap;
		}
		Bitmap resizedBitmap = bitmap;
		if(null != bitmap){
			// 旋转图片 动作
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			LogUtil.d(TAG, " rotate angle2=" + angle);
			// 创建新的图片
			try {
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			} catch (OutOfMemoryError e) {
				LogUtil.e(TAG, " rotate img error, out of memory! ");
			} catch (Exception e) {
				LogUtil.e(TAG, " rotate img error ");
			}
		}
		
		
		
		return resizedBitmap;
	}


	/**
	 * bitmap转uri
	 * @param context 上下文
	 * @param bitmap 图片资源
	 * @return
	 */
	public static Uri bitmapToUri (Context context, Bitmap bitmap) {
		String url = MediaStore.Images.Media.insertImage(
			context.getContentResolver(), bitmap, null,null);
		return Uri.parse(url);
	}
	 

	
}
