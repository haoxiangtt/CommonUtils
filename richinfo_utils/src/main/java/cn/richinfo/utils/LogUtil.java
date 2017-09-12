package cn.richinfo.utils;

import android.util.Log;

/**
 * create by ouyangjinfu
 * 日志打印工具
 */
public class LogUtil {
    public static boolean isDebug = BuildConfig.DEBUG;
    public static final LogUtil instance = new LogUtil();

    public static LogUtil getInstance() {
        return instance;
    }

    public void setDebugMode(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
