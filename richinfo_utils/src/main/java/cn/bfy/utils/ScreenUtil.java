package cn.bfy.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　XXXXXXXXXXXXXXX
 * @company    : XXXXXXXXXXXXXXX
 * @author     : OuyangJinfu
 * @e-mail     : jinfu123.-@163.com
 * @createDate : 2017/5/11 0011
 * @modifyDate : 2017/5/11 0011
 * @version    : 1.0
 * @desc       : 获得屏幕相关的辅助类
 * </pre>
 */
public class ScreenUtil {
	private ScreenUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = 0;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 设置屏幕为全屏
	 * <p>需在 {@code setContentView} 之前调用</p>
	 *
	 * @param activity activity
	 */
	public static void setFullScreen(@NonNull final Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 设置屏幕为横屏
	 * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
	 * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
	 * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
	 * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
	 * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
	 *
	 * @param activity activity
	 */
	public static void setLandscape(@NonNull final Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 设置屏幕为竖屏
	 *
	 * @param activity activity
	 */
	public static void setPortrait(@NonNull final Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 判断是否横屏
	 *
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isLandscape(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	/**
	 * 判断是否竖屏
	 *
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * 判断是否锁屏
	 *
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isScreenLock(Context context) {
		KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		return km.inKeyguardRestrictedInputMode();
	}

	/**
	 * 设置进入休眠时长
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
	 *
	 * @param duration 时长
	 */
	public static void setSleepDuration(Context context, final int duration) {
		Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
	}

	/**
	 * 获取进入休眠时长
	 *
	 * @return 进入休眠时长，报错返回-123
	 */
	public static int getSleepDuration(Context context) {
		try {
			return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
			return -123;
		}
	}

	/**
	 * 判断是否是平板
	 *
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 获取屏幕旋转角度
	 *
	 * @param activity activity
	 * @return 屏幕旋转角度
	 */
	public static int getScreenRotation(@NonNull final Activity activity) {
		switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
			default:
			case Surface.ROTATION_0:
				return 0;
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
		}
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;

	}

	/*public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}*/

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param context
	 * @param dipValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	/*public static int dp2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}*/

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param pxValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	/*public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}*/

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	/*public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}*/

	/**
	 * 截屏
	 * @param activity
	 * @return
	 */
	/*public static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题栏
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}*/

	/**
	 * 截屏并保存
	 * @param a 上下文
	 * @param filePath 保存路径
	 */
	public static void shot(Activity a, File filePath) {
		if (filePath == null) {
			return;
		}
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		savePic(snapShotWithoutStatusBar(a), filePath);
	}

	/**
	 * 保存图片
	 * @param b 图片资源
	 * @param filePath 保存路径
	 */
	private static void savePic(Bitmap b, File filePath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static void backgroundAlpha(Activity context, float bgAlpha)
	{
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		context.getWindow().setAttributes(lp);
	}

}
