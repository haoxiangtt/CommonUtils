package cn.richinfo.utils.secret;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/7/18 0018
 * @modifyDate : 2017/7/18 0018
 * @version    : 1.0
 * @desc       : 加密解密相关的工具类（UMC SDK Project）
 * </pre>
 */
public class Encrypt {
	private static final String HMAC_SHA256 = "HmacSHA256";
	public static final String HASH_TYPE_MD5 = "1";
	public static final String HASH_TYPE_SHA1 = "2";

	public static String md5(String text) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用sha1加密字符串
	 * 
	 * @param text
	 * @return
	 * @author licq 2014-5-7
	 */
	public static final String sha1(String text) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(text.getBytes());
			byte[] b = md.digest();
			return bytesToHexString(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 使用HmacSHA256消息摘要算法计算消息摘要
	 * 
	 * @param textBytes
	 *            做消息摘要的数据
	 * @param keyBytes
	 *            密钥
	 * @return 消息摘要（长度为16的字节数组）
	 */
	public static byte[] hmacSHA256(byte[] keyBytes, byte[] textBytes) {
		Mac mac = null;
		try {
			mac = Mac.getInstance(HMAC_SHA256);
			SecretKeySpec sec = new SecretKeySpec(keyBytes, HMAC_SHA256);
			mac.init(sec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		}
		return mac.doFinal(textBytes);
	}

	/**
	 * des解密
	 * 
	 * @param key
	 *            密钥
	 * @param text
	 *            加密字符串
	 * @return 解密后字符串
	 */
	public static String desDecrypt(String key, String text) {
		try {
			DesEncrypt desEncry = new DesEncrypt(key);
			return desEncry.decrypt(text);
		} catch (Exception e) {
		}
		return null;
	}

	public static String desEncrypt(String key, String text) {
		try {
			DesEncrypt desEncry = new DesEncrypt(key);
			return desEncry.encrypt(text);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 *            要加密的内容
	 * @param keyBytes
	 *            密钥的byte数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] rsaEncryptByPublicKey(byte[] data, byte[] keyBytes)
			throws Exception {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * byte转化成16进制
	 * 
	 * @param src
	 * @return
	 * @author licq 2014-5-8
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
