package com.annie.newsApp.util;

/**
 * @Description: 保存信息的公共接口
 * @author Free
 * @date 2016-6-13 下午8:22:40
 */
public interface Constant {

	/**
	 * 是否是第一次打开应用
	 */
	String IS_FIRST_OPEN_APP = "is_first_open_app";
	
	/**
	 * 保存的新闻中心界面返回的数据的key
	 */
	String NEWSCENTERPAGER_JSON = "newscenterpager_json";
	
	/**
	 * 保存已读新闻的id
	 */
	String CACHE_READ_NEWID = "cache_read_newid";

	/**
	 * 服务器的地址
	 */
	public static final String SERVER_URL = "http://10.0.2.2:8080/zhbj";

	/**
	 * 新闻中心的地址
	 */
	public static final String NEWSCENTER_URL = SERVER_URL + "/categories.json";
	
	/**
	 * 组图的URL
	 */
	String PHOTODETAIL_URL = SERVER_URL+"/photos/photos_1.json";

}
