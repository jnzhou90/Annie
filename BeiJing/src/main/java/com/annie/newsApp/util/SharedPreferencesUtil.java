package com.annie.newsApp.util;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Description: SharedPreferences保存数据
 * @author Free
 * @date 2016-6-13 下午8:10:26
 */
public class SharedPreferencesUtil {
	private static SharedPreferences sp;

	/**
	 * @Description:保存布尔类型的数据
	 * @param:
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			// name: 保存数据的文件名称 mode: 模式,一般为私有
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		// 获取编辑器,提交保存的数据
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			// name: 保存数据的文件名称 mode: 模式,一般为私有
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * @Description:保存字符串类型的数据
	 * @param:
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null) {
			// name: 保存数据的文件名称 mode: 模式,一般为私有
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		// 获取编辑器,提交保存的数据
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context, String key,String defValue) {
		if (sp == null) {
			// name: 保存数据的文件名称 mode: 模式,一般为私有
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		return sp.getString(key, defValue);
	}
}
