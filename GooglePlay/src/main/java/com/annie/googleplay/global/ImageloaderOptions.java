package com.annie.googleplay.global;

import android.graphics.Bitmap;

import com.annie.googleplay.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @Description: 由于展示图片是一个公共的操作,所以抽取到全局类中
 * @author Free
 * @date 2016-6-29 下午4:35:06
 */
public interface ImageloaderOptions {

	DisplayImageOptions options = new DisplayImageOptions.Builder()
	//设置加载过程中显示的图片
	.showImageOnLoading(R.drawable.ic_launcher) 
	//设置URL为空显示的图片
	.showImageForEmptyUri(R.drawable.ic_launcher) 
	 //设置加载失败显示的图片
	.showImageOnFail(R.drawable.ic_launcher)
	.cacheInMemory(true) //是否在内存缓存
	.cacheOnDisk(true) // 是否在硬盘缓存
	// 配置后，会参考ImageVIew的宽高来缩放图片
	.imageScaleType(ImageScaleType.EXACTLY)
	//配置bitmap显示的渲染模式,内存占用减少一半,一个像素占两个字节
	.bitmapConfig(Bitmap.Config.RGB_565)
	//.considerExifParams(true) //是否识别图片的方向信息
	//图片圆角展示,可以设置为圆形,当设为大于宽的一半时为正圆
	//.displayer(new RoundedBitmapDisplayer(30)).build(); 
	.displayer(new FadeInBitmapDisplayer(1800)).build(); //设置图片渐渐显示的动画
	
	
	//因为有的地方需要用圆形图片显示,有的需要用渐变动画,所以另copy一份
	DisplayImageOptions FadeIn_options = new DisplayImageOptions.Builder()
	//设置加载过程中显示的图片
	.showImageOnLoading(R.drawable.ic_launcher) 
	//设置URL为空显示的图片
	.showImageForEmptyUri(R.drawable.ic_launcher) 
	 //设置加载失败显示的图片
	.showImageOnFail(R.drawable.ic_launcher)
	.cacheInMemory(true) //是否在内存缓存
	.cacheOnDisk(true) // 是否在硬盘缓存
	// 配置后，会参考ImageVIew的宽高来缩放图片
	.imageScaleType(ImageScaleType.EXACTLY)
	//配置bitmap显示的渲染模式,内存占用减少一半,一个像素占两个字节
	.bitmapConfig(Bitmap.Config.RGB_565)
	//.considerExifParams(true) //是否识别图片的方向信息
	//图片圆角展示,可以设置为圆形,当设为大于宽的一半时为正圆
	.displayer(new RoundedBitmapDisplayer(30)).build(); 
	//.displayer(new FadeInBitmapDisplayer(1800)).build(); //设置图片渐渐显示的动画
}
