package cn.bfy.utils;

import android.annotation.SuppressLint;
import android.os.StrictMode;

/**
 * 严格模式管理器
 */
public class StrictModeUtil {

	private static boolean isDebugMode = false;

	/**
	 * 严格模式开关，默认关闭
	 * 
	 * @param isDebugM
	 */
	public static void setDebugMode(boolean isDebugM) {
		isDebugMode = isDebugM;
		if(isDebugMode){
			startThreadPolicy();
			startVmPolicy();
		}
			
	}

	@SuppressLint("NewApi")
	private static void startThreadPolicy() {
		if (isDebugMode) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
		}
	}

	@SuppressLint("NewApi")
	private static void startVmPolicy() {
		if (isDebugMode) {
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedClosableObjects().detectLeakedSqlLiteObjects()
					.penaltyLog().penaltyDeath().build());
		}
	}
}
