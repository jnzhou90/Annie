package com.annie.googleplay.api;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

/**
 * 数据加载的引擎：
 * 1.负责缓存数据和网络数据的调度
 * 2.负责解析数据成java bean
 * @author Free
 *
 */
public class DataEngine {

	//加载数据一个对象就够了,所以这里使用单例模式
	private static DataEngine mDataEngine = new DataEngine(); //创建对象
	//私有构造涵数
	private DataEngine() {
		
	}
	//对外提供公共的方法
	public static DataEngine getInstance() {
		return mDataEngine;
	}
	
	/**
	 * 加载bean数据
	 * @param url
	 * @return
	 */
	public <T> T loadBeanData(String url,Class<T> clazz) {
		// 1.首先访问网络加载数据
		String result = HttpEngine.getInstance().get(url);
		// 2.如果结果不为空,将结果更新到本地缓存中
		if (result != null) {
			CacheEngine.getInstance().saveCache(url, result);
		}else {
			// 3.如果结果为空,那么从本地缓存中获取数据
			result = CacheEngine.getInstance().getCache(url);
		}
		// 4.解析数据,将json数据解析成javabean
		if (result != null) {
			//当结果不为空的时候解析数据
			return com.annie.googleplay.util.GsonUtil.parseJsonToBean(result, clazz);
			//return new Gson().fromJson(result, clazz);
		}else {
			return null;
		} 
	}
	
	/**
	 * 加载list数据
	 * @param url
	 * @return
	 */
	public List<?> loadListData(String url,Type type) {
		// 1.首先从网络获取数据
		String result = HttpEngine.getInstance().get(url);
		// 2.如果返回的结果不为空,将结果保存到本地缓存
		if (result != null) {
			CacheEngine.getInstance().saveCache(url, result);
		}else {
			// 3.如果返回的结果为空,那么读取本地缓存的数据
			result = CacheEngine.getInstance().getCache(url);
		}
		
		// 4.当结果不为空的时候,解析数据
		if (result != null) {
			return com.annie.googleplay.util.GsonUtil.parseJsonToList(result, type);
			//return new Gson().fromJson(result, type);
		}else {
			return null;
		}
	}
}
