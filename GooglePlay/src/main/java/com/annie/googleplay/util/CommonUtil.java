package com.annie.googleplay.util;

import android.R.integer;
import android.graphics.drawable.Drawable;

import com.annie.googleplay.global.GooglePlayApplication;

/**
 * 封装零碎的方法。比如获取资源，dp转px
 * @author lxj
 *
 */
public class CommonUtil {
	/**
	 * 在主线程更新UI
	 * @param runnable
	 */
	public static void runOnUiThread(Runnable runnable) {
		//通过查看源码,发现只要获取主线程的handler,然后通过它调用post方法就可以了
		GooglePlayApplication.mainHandler.post(runnable);
	}
	
	/**
	 * 获取字符串资源
	 * @return 
	 * @return
	 */
	public static String getString (int resId) {
		return GooglePlayApplication.context.getResources().getString(resId);
	}
	
	/**
	 * 获取字符串数组资源
	 * @return 
	 * @return
	 */
	public static String[] getStrings(int resId) {
		return GooglePlayApplication.context.getResources().getStringArray(resId);
	}
	
	/**
	 * 获取颜色资源
	 * @return 
	 * @return
	 */
	public static int getColor (int resId) {
		return GooglePlayApplication.context.getResources().getColor(resId);
	}
	
	/**
	 * 获取图片资源
	 * @return 
	 * @return
	 */
	public static Drawable getDrawable (int resId) {
		return GooglePlayApplication.context.getResources().getDrawable(resId);
	}
	
	/**
	 * 获取dimens文件的资源，其实就是定义的dp值，会自动将dp值转为px值
	 * @param resId
	 * @return
	 */
	public static int getDimens(int resId) {
		return GooglePlayApplication.context.getResources().getDimensionPixelSize(resId);
	}
}
