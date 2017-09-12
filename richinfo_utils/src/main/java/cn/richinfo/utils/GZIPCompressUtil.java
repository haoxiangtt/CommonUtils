package cn.richinfo.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * zip压缩解压缩工具类
 * @author ouyangjinfu
 * @data 2015年10月26日
 */
public class GZIPCompressUtil {
	
	private static final String TAG = "GZIPCompressUtil";

	
	public static String compress(String str){
		return compress(str,null,null);
	}
	
	/**
	 * 压缩
	 * 
	 * @param str 压缩字符串
	 * @param charsetIn 输入字符串编码，如果为空，则采用平台默认编码
	 * @param charsetOut 输出字符串的编码，如果为空，则采用平台默认编码
	 * @return
	 */
	public static String compress(String str,String charsetIn,String charsetOut) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		String cmpStr = null;
		try {
			gzip = new GZIPOutputStream(out);
			if(TextUtils.isEmpty(charsetIn)){
				gzip.write(str.getBytes());
			}else{
				gzip.write(str.getBytes(charsetIn));
			}
			gzip.flush();
			if(TextUtils.isEmpty(charsetOut)){ 
				cmpStr = out.toString();
			}else{
				cmpStr = out.toString(charsetOut);
			}
			// return out.toString("ISO-8859-1");
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			return null;
		}finally{
			try {
				if(out != null){ out.close(); }
				if(gzip != null){ gzip.close(); }
			} catch (IOException e) {}
		}
		return cmpStr;
	}
	
	
	public static String strUnCompress(String str){
//		return strUnCompress(str,"ISO-8859-1");
		return strUnCompress(str,null,null);
	}
	
	/**
	 * 解压缩字符串
	 * 输出字符串编码采用平台默认编码
	 * @param str 
	 * @param charsetIn 输入字符串字符编码，如果为空，则采用平台默认编码
	 * @param charsetOut 输出字符串编码，如果为空，则采用平台默认编码
	 * @return
	 */
	public static String strUnCompress(String str,String charsetIn,String charsetOut) {
		if (str == null || str.length() == 0) {
			return str;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream gunzip = null;
		String cmpStr = null;
		try {
			out = new ByteArrayOutputStream();
			if(TextUtils.isEmpty(charsetIn)){
				in = new ByteArrayInputStream(
					str.getBytes());
			}else{
				in = new ByteArrayInputStream(
						str.getBytes(charsetIn));
			}
			gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			out.flush();
			if(TextUtils.isEmpty(charsetOut)){
				cmpStr = out.toString();
			}else{
				cmpStr = out.toString(charsetOut);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return null;
		}finally{
			try {
			if(in != null){ in.close(); }
			if(out != null){ out.close(); }
			if(gunzip != null){ gunzip.close(); }
			} catch (IOException e) {}
		}
		// toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
		return cmpStr;
	}
	
	/**
	 * 解压缩
	 * @param strByte
	 * @return
	 */
	public static String strByteUnCompress(byte[] strByte){
//		return strByteUnCompress(strByte, "ISO-8859-1");
		return strByteUnCompress(strByte, "UTF-8");
	}

	/**
	 * 解压缩
	 * 
	 * @param strByte
	 * @param charsetOut 输出字符串的编码
	 * @return 
	 */
	public static String strByteUnCompress(byte[] strByte,String charsetOut) {
		if (strByte == null || strByte.length == 0) {
			return "";
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String str = null;
		ByteArrayInputStream in = null;
		GZIPInputStream gunzip = null;
		try {
			in = new ByteArrayInputStream(strByte);
			gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			out.flush();
			// toString()使用平台默认编码，也可以显式的指定如toString(UTF-8)
			str = out.toString(charsetOut);
			
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.toString());
			return null;
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			return null;
		}finally{
			try {
				if(in != null){ in.close(); }
				if(out != null){ out.close(); }
				if(gunzip != null){ gunzip.close(); }
			} catch (IOException e) {}
		}
		return str;
	}

}
