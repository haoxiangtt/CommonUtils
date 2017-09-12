package cn.richinfo.utils.secret;

import android.text.TextUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author fengxueyun
 * @version 1.0
 * @Title:
 * @Description:
 * @Copyright: Copyright (c) 2011
 * @Company:深圳彩讯科技有限公司
 * @time 2015年4月3日 上午11:33:34
 */
public class AESEncrypt {

    private static byte[] Encrypt(byte[] text, byte[] key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    private static byte[] Decrypt(byte[] text, byte[] key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    public static String EncodeAES(String text, String key) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        byte[] keybBytes = MD5STo16Byte.encrypt2MD5toByte16(key);
        byte[] textBytes = text.getBytes();
        byte[] aesBytyes;
        try {
            aesBytyes = Encrypt(textBytes, keybBytes);
            return new String(Base64.encode(aesBytyes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String DeCodeAES(String text, String key) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        byte[] keybBytes = MD5STo16Byte.encrypt2MD5toByte16(key);
        byte[] debase64Bytes = Base64.decode(text);
        try {
            return new String(Decrypt(debase64Bytes, keybBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
