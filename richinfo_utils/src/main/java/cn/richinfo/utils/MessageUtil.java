package cn.richinfo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author ouyangjinfu
 * @email ouyjf@giiso.com
 * @data 2015年10月26日
 * @version 1.0
 * @description 消息提示工具类
 */
public class MessageUtil {
	
	/**
	 * 打印消息
	 * @param context
	 * @param resid
	 */
	public static void showSimpleMessage(Context context,int resid){
		Toast.makeText(context, resid, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 打印消息
	 * @param context
	 * @param msg
	 */
	public static void showSimpleMessage(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

}
