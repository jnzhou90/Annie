package com.annie.newsApp.util;

import com.lidroid.xutils.BitmapUtils;

import android.content.Context;

/**
 * @Description: 为BitmapUtils设置单例模式,保证内存中只有一个该对象
 * @author Free
 * @date 2016-6-18 下午7:12:00
 */
public class BitmapUtilsHelper {
	
private static BitmapUtils bitmapUtils;
	
	private BitmapUtilsHelper(){
	}
	
	public static BitmapUtils getBitmapUtils(Context context){
		if(bitmapUtils==null){
			bitmapUtils = new BitmapUtils(context);
		}
		return bitmapUtils;
	}
 
}
