package com.annie.googleplay.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

import com.annie.googleplay.R.string;

public class MD5Util {

	/**
	 * 使用md5方式加密
	 * @param data
	 * @return
	 */
	 public static String digest(String data) {
		 StringBuilder builder = new StringBuilder();
		 try {
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			//对字符串的字节进行加密
			byte[] digest = messageDigest.digest(data.getBytes());
			
			for (int i = 0; i < digest.length; i++) {
				//将字节转成十六进制
				builder.append(Integer.toHexString(digest[i] & 0xff));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return builder.toString();
		 
	 }
}
