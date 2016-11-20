package com.annie.googleplay.util;

public interface UrlUtil {
	//服务器主机地址
	String SERVER_HOST = "http://127.0.0.1:8090/";
	
	//首页的URL
	String home = SERVER_HOST + "home?index=";
	
	//图片的URL前缀: http://127.0.0.1:8090/image?name=
	String IMAGE_HEAD = "http://127.0.0.1:8090/image?name=";
	
	//应用界面资源的URL
	String app = SERVER_HOST + "app?index=";
	
	//游戏界面的url
	String game = SERVER_HOST + "game?index=";
	
	//专题界面的URL
	String subject = SERVER_HOST + "subject?index=";
	
	//推荐界面的URL
	String recommend = SERVER_HOST + "recommend?index=0";
	
	//分类界面的url 
	String category = SERVER_HOST + "category?index=0";
	
	//热门界面的url   
	String hot = SERVER_HOST + "hot?index=0";
	
	//应用详情界面的url
	String  detail = SERVER_HOST + "detail?packageName=";
	
	//apk 下载的url
	String download = SERVER_HOST + "download?name=";
	
	//apk 断点下载的url
	String break_download = SERVER_HOST  + "download?name=%s&range=%s";
}
