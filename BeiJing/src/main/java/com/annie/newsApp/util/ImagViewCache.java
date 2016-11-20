package com.annie.newsApp.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImagViewCache {

	public void display(ImageView imageView, String url) {
		/**
		 * 	1、从内存缓存取图片，取到就展示，取不到往下走

			2、从文件缓存取图片，取到就展示，取不到往下走
		
			3、访问网络下载图片，通过Handler展示
		 */
		Bitmap bitmap = null;
		//bitmap = 从内存缓存取图片;
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		
		//bitmap = 从文件缓存取图片
		if (bitmap != null ) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		
		//访问网络下载图片，通过Handler展示
		
	}
}
