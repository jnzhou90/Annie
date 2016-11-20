package com.annie.googleplay.util;

import android.util.Log;

/**
 * 用来管理log
 */
public class LogUtil {
	
	//是否是开发调试模式,应用上线前要置为false
	public static boolean isDebug = true; 
	
	/**
	 * 打印d级别
	 * @param tag
	 * @param msg
	 */
	public static  void e(Object obj,String msg) {
		if (isDebug) {
			Log.e(obj.getClass().getSimpleName(),msg);
		}
	}
	
	/**
	 * 打印e级别
	 * @param tag
	 * @param msg
	 */
	public static void d(Object obj,String msg) {
		if (isDebug) {
			Log.d(obj.getClass().getSimpleName(), msg);
		}
	}
	
}
