package cn.richinfo.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩解压缩工具类
 * @author ouyangjinfu
 * @data 2015年10月26日
 */
public class GZIPCompressUtil {
	
	private static final String TAG = "GZIPCompressUtil";

	private GZIPCompressUtil() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}
	
	public static String strCompress(String str){
		return strCompress(str,null,null);
	}
	
	/**
	 * 压缩
	 * 
	 * @param str 压缩字符串
	 * @param charsetIn 输入字符串编码，如果为空，则采用平台默认编码
	 * @param charsetOut 输出字符串的编码，如果为空，则采用平台默认编码
	 * @return
	 */
	public static String strCompress(String str, String charsetIn, String charsetOut) {
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


	/*********************************扩展****************************************/

	private static final int BUFFER_LEN = 8192;

	/**
	 * 压缩文件
	 *
	 * @param resFilePath 待压缩文件路径
	 * @param zipFilePath 压缩文件路径
	 * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
	 * @throws IOException IO错误时抛出
	 */
	public static boolean zipFile(final String resFilePath,
								  final String zipFilePath)
			throws IOException {
		return zipFile(resFilePath, zipFilePath, null);
	}

	/**
	 * 压缩文件
	 *
	 * @param resFilePath 待压缩文件路径
	 * @param zipFilePath 压缩文件路径
	 * @param comment     压缩文件的注释
	 * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
	 * @throws IOException IO错误时抛出
	 */
	public static boolean zipFile(final String resFilePath,
								  final String zipFilePath,
								  final String comment)
			throws IOException {
		return zipFile(getFileByPath(resFilePath), getFileByPath(zipFilePath), comment);
	}

	/**
	 * 压缩文件
	 *
	 * @param resFile 待压缩文件
	 * @param zipFile 压缩文件
	 * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
	 * @throws IOException IO错误时抛出
	 */
	public static boolean zipFile(final File resFile,
								  final File zipFile)
			throws IOException {
		return zipFile(resFile, zipFile, null);
	}

	/**
	 * 压缩文件
	 *
	 * @param resFile 待压缩文件
	 * @param zipFile 压缩文件
	 * @param comment 压缩文件的注释
	 * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
	 * @throws IOException IO错误时抛出
	 */
	public static boolean zipFile(final File resFile,
								  final File zipFile,
								  final String comment)
			throws IOException {
		if (resFile == null || zipFile == null) return false;
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			return zipFile(resFile, "", zos, comment);
		} finally {
			if (zos != null) {
				closeIO(zos);
			}
		}
	}

	/**
	 * 关闭IO
	 *
	 * @param closeables closeables
	 */
	public static void closeIO(final Closeable... closeables) {
		if (closeables == null) return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 压缩文件
	 *
	 * @param resFile  待压缩文件
	 * @param rootPath 相对于压缩文件的路径
	 * @param zos      压缩文件输出流
	 * @param comment  压缩文件的注释
	 * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
	 * @throws IOException IO错误时抛出
	 */
	private static boolean zipFile(final File resFile,
								   String rootPath,
								   final ZipOutputStream zos,
								   final String comment)
			throws IOException {
		rootPath = rootPath + (isSpace(rootPath) ? "" : File.separator) + resFile.getName();
		if (resFile.isDirectory()) {
			File[] fileList = resFile.listFiles();
			// 如果是空文件夹那么创建它，我把'/'换为File.separator测试就不成功，eggPain
			if (fileList == null || fileList.length <= 0) {
				ZipEntry entry = new ZipEntry(rootPath + '/');
				if (!isSpace(comment)) entry.setComment(comment);
				zos.putNextEntry(entry);
				zos.closeEntry();
			} else {
				for (File file : fileList) {
					// 如果递归返回false则返回false
					if (!zipFile(file, rootPath, zos, comment)) return false;
				}
			}
		} else {
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(resFile));
				ZipEntry entry = new ZipEntry(rootPath);
				if (!isSpace(comment)) entry.setComment(comment);
				zos.putNextEntry(entry);
				byte buffer[] = new byte[BUFFER_LEN];
				int len;
				while ((len = is.read(buffer, 0, BUFFER_LEN)) != -1) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
			} finally {
				closeIO(is);
			}
		}
		return true;
	}

	/**
	 * 解压文件
	 *
	 * @param zipFilePath 待解压文件路径
	 * @param destDirPath 目标目录路径
	 * @return 文件链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<File> unzipFile(final String zipFilePath,
									   final String destDirPath)
			throws IOException {
		return unzipFileByKeyword(zipFilePath, destDirPath, null);
	}

	/**
	 * 解压文件
	 *
	 * @param zipFile 待解压文件
	 * @param destDir 目标目录
	 * @return 文件链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<File> unzipFile(final File zipFile,
									   final File destDir)
			throws IOException {
		return unzipFileByKeyword(zipFile, destDir, null);
	}

	/**
	 * 解压带有关键字的文件
	 *
	 * @param zipFilePath 待解压文件路径
	 * @param destDirPath 目标目录路径
	 * @param keyword     关键字
	 * @return 返回带有关键字的文件链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<File> unzipFileByKeyword(final String zipFilePath,
												final String destDirPath,
												final String keyword)
			throws IOException {
		return unzipFileByKeyword(getFileByPath(zipFilePath), getFileByPath(destDirPath), keyword);
	}

	/**
	 * 解压带有关键字的文件
	 *
	 * @param zipFile 待解压文件
	 * @param destDir 目标目录
	 * @param keyword 关键字
	 * @return 返回带有关键字的文件链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<File> unzipFileByKeyword(final File zipFile,
												final File destDir,
												final String keyword)
			throws IOException {
		if (zipFile == null || destDir == null) return null;
		List<File> files = new ArrayList<>();
		ZipFile zf = new ZipFile(zipFile);
		Enumeration<?> entries = zf.entries();
		if (isSpace(keyword)) {
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (!unzipChildFile(destDir, files, zf, entry, entryName)) return files;
			}
		} else {
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (entryName.contains(keyword)) {
					if (!unzipChildFile(destDir, files, zf, entry, entryName)) return files;
				}
			}
		}
		return files;
	}

	private static boolean unzipChildFile(File destDir, List<File> files, ZipFile zf, ZipEntry entry, String entryName) throws IOException {
		String filePath = destDir + File.separator + entryName;
		File file = new File(filePath);
		files.add(file);
		if (entry.isDirectory()) {
			if (!createOrExistsDir(file)) return false;
		} else {
			if (!createOrExistsFile(file)) return false;
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(zf.getInputStream(entry));
				out = new BufferedOutputStream(new FileOutputStream(file));
				byte buffer[] = new byte[BUFFER_LEN];
				int len;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			} finally {
				closeIO(in, out);
			}
		}
		return true;
	}

	/**
	 * 获取压缩文件中的文件路径链表
	 *
	 * @param zipFilePath 压缩文件路径
	 * @return 压缩文件中的文件路径链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<String> getFilesPath(final String zipFilePath)
			throws IOException {
		return getFilesPath(getFileByPath(zipFilePath));
	}

	/**
	 * 获取压缩文件中的文件路径链表
	 *
	 * @param zipFile 压缩文件
	 * @return 压缩文件中的文件路径链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<String> getFilesPath(final File zipFile)
			throws IOException {
		if (zipFile == null) return null;
		List<String> paths = new ArrayList<>();
		Enumeration<?> entries = new ZipFile(zipFile).entries();
		while (entries.hasMoreElements()) {
			paths.add(((ZipEntry) entries.nextElement()).getName());
		}
		return paths;
	}

	/**
	 * 获取压缩文件中的注释链表
	 *
	 * @param zipFilePath 压缩文件路径
	 * @return 压缩文件中的注释链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<String> getComments(final String zipFilePath)
			throws IOException {
		return getComments(getFileByPath(zipFilePath));
	}

	/**
	 * 获取压缩文件中的注释链表
	 *
	 * @param zipFile 压缩文件
	 * @return 压缩文件中的注释链表
	 * @throws IOException IO错误时抛出
	 */
	public static List<String> getComments(final File zipFile)
			throws IOException {
		if (zipFile == null) return null;
		List<String> comments = new ArrayList<>();
		Enumeration<?> entries = new ZipFile(zipFile).entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			comments.add(entry.getComment());
		}
		return comments;
	}

	private static boolean createOrExistsDir(final File file) {
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	private static boolean createOrExistsFile(final File file) {
		if (file == null) return false;
		if (file.exists()) return file.isFile();
		if (!createOrExistsDir(file.getParentFile())) return false;
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static File getFileByPath(final String filePath) {
		return isSpace(filePath) ? null : new File(filePath);
	}

	private static boolean isSpace(final String s) {
		if (s == null) return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}


}
