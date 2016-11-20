package com.annie.googleplay.global;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * @Description: 这里存放的是一些全局的变量或类...
 * @author Free
 * @date 2016-6-25 下午7:23:44
 */
public class GooglePlayApplication extends Application{
	
	public static Context context; //公共的上下文变量
	public static Handler mainHandler; //全局的主线程的handler
	
	/**
	 * 当点击app突变app启动的时候首先会被执行的方法，它在任何acvitity的onCreate之前执行，
	 * 界面还为出现,这里就已经执行了,它也是android app的入口函数
	 * 但这个类默认不会执行,只有在清单文件的application中配置一下才会执行
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化向下文
		context = this;
		//初始化mainHandler
		mainHandler = new Handler();
		
		//初始化ImageLoader
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		
		/*//子线程创建handler的写法
		new Thread(){
			public void run() {
				//在子线程创建handler需要注意：需要手动的创建Looper并开启轮询器
				Looper.prepare();//创建轮询器
				Looper.loop();//开启轮询器
				mainHandler = new Handler();
			};
		}.start();*/
	}
}
