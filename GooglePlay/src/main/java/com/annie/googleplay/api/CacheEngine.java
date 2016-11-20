package com.annie.googleplay.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;

import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.util.MD5Util;

import android.os.Environment;
import android.text.GetChars;

/**
 * 数据缓存引擎：
 * 负责缓存数据的加载与保存，
 * 1.定义数据缓存目录
 * 2.负责将网络数据保存到本地缓存
 * 3.负责将本地缓存数据取出来
 * @author Free
 *
 */
public class CacheEngine {

	//创建缓存目录,/mnt/sdcard/com.annie.googleplay/cache
	public static String CACHE_DIR = Environment.getExternalStorageDirectory() + File.separator
									 + GooglePlayApplication.context.getPackageName() + File.separator
									 + "cache";
	
	//缓存数据只需要一个对象就够了,所以这里也使用单例模式
	private static CacheEngine mCacheEngine = new CacheEngine();
	private CacheEngine() {
		//初始化缓存文件目录
		File dir = new File(CACHE_DIR);
		if (!dir.exists()) {
			//创建目录 
			dir.mkdirs(); 
		}
	} 
	public static CacheEngine getInstance() {
		return mCacheEngine;
	}
	
	/**
	 * 将数据存储到本地缓存中
	 */
	public void saveCache(String url,String result) {
		// 1.构建要写入的缓存文件
		File file = buildCacheFile(url);
		// 2.将resutl字符串写入缓存文件
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(result);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据url取出缓存数据
	 * @param url
	 * @return
	 */
	public String getCache(String url) {
		System.out.println("读取的缓存文件的URL:" + url);
		//构建将要读取的缓存文件
		File file = buildCacheFile(url);
		StringBuilder builder = new StringBuilder();
		try {
			//关联要读取的缓存文件
			BufferedReader br = new BufferedReader(new FileReader(file));
			//读取缓存文件,将其保存到StringBuilder中
			String line = null;
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}
			//关流
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("读取缓存文件:" + builder.toString());
		return builder.toString();
	}
	
	/**
	 * 根据url构建缓存文件名,生成唯一的缓存文件名称
	 * @param url
	 * @return
	 */
	private File buildCacheFile(String url) {
		
		return new File(CACHE_DIR, MD5Util.digest(url));
	}
}
