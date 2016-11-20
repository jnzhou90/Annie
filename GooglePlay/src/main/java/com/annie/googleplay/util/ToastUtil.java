package com.annie.googleplay.util;

import com.annie.googleplay.global.GooglePlayApplication;

import android.widget.Toast;

/**
 * @Description: 创建一个单例模式的吐司,实现吐司的连续显示
 * @author Free
 * @date 2016-6-25 下午8:24:58
 */
public class ToastUtil {
	
	private static Toast toast;
	
	/**
	 * 创建单例的，强大的，能够连续弹的吐司
	 * @param text
	 */
	public static void toastShow(String text ) {
		//如果toast为空
		if (toast == null) {
			toast = Toast.makeText(GooglePlayApplication.context, text, 0);
		}
		//如果吐司不为空,则直接更改当前的吐司文本
		toast.setText(text);
		//展示
		toast.show();
	}
	
}
