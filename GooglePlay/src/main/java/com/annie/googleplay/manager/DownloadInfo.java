package com.annie.googleplay.manager;

import java.io.File;

import com.annie.googleplay.bean.AppInfo;

public class DownloadInfo {

	/**
	 *  专门用来封装下载任务相关的数据
	 */
	private int id;//下载任务的唯一标识
	private int state;//下载状态
	private long currentLength;//已经下载的长度
	private long size;//总大小
	private String downloadUrl;//下载地址
	private String path;//下载文件的保存路径
	
	
	/**
	 * 创建DownloadInfo实例对象，其中有些字段直接从appInfo中取
	 * 这里创建之后,以后用的时候就不用自己在挨个new了
	 * @param appInfo
	 */
	public static DownloadInfo create(AppInfo appInfo) {
		DownloadInfo downloadInfo = new DownloadInfo();
		
		downloadInfo.setId(appInfo.getId());
		downloadInfo.setDownloadUrl(appInfo.getDownloadUrl());
		downloadInfo.setSize(appInfo.getSize());
		
		downloadInfo.setState(DownloadManager.STATE_NONE);//初始化状态
		downloadInfo.setCurrentLength(0);
		//初始化下载路径: /mnt/sdcard/包名/download/有缘网.apk
		downloadInfo.setPath(buildPath(appInfo));
		
		return downloadInfo;
	}
	
	/**
	 * 构建下载文件路径
	 * @param appInfo
	 */
	public static String buildPath(AppInfo appInfo) {
		return DownloadManager.DOWNLOAD_DIR + File.separator + appInfo.getPackageName() + ".apk";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getCurrentLength() {
		return currentLength;
	}
	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
}
