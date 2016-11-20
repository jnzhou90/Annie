package com.annie.googleplay.bean;

import java.util.ArrayList;

public class AppInfo {

	private String des; //应用描述
	private String downloadUrl; //App 的下载地址
	private String iconUrl; //图片地址
	private int id;
	private String name;
	private String packageName;
	private long size; //App 所占的空间的大小
	private float stars; //App 的星级
	
	private String author;//作者
	private String date;//上传日期
	private String downloadNum;//
	private String version;//
	private ArrayList<String> screen;//截图的url后缀
	private ArrayList<SafeInfo> safe;//安全信息
	
	
	
	public String getAuthor() {
		return author;
	}



	public void setAuthor(String author) {
		this.author = author;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getDownloadNum() {
		return downloadNum;
	}



	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}



	public String getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = version;
	}



	public ArrayList<String> getScreen() {
		return screen;
	}



	public void setScreen(ArrayList<String> screen) {
		this.screen = screen;
	}



	public ArrayList<SafeInfo> getSafe() {
		return safe;
	}



	public void setSafe(ArrayList<SafeInfo> safe) {
		this.safe = safe;
	}



	public String getDes() {
		return des;
	}



	public void setDes(String des) {
		this.des = des;
	}



	public String getDownloadUrl() {
		return downloadUrl;
	}



	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}



	public String getIconUrl() {
		return iconUrl;
	}



	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPackageName() {
		return packageName;
	}



	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}



	public long getSize() {
		return size;
	}



	public void setSize(long size) {
		this.size = size;
	}



	public float getStars() {
		return stars;
	}



	public void setStars(float stars) {
		this.stars = stars;
	}



	@Override
	public String toString() {
		return "AppInfo [des=" + des + ", downloadUrl=" + downloadUrl
				+ ", iconUrl=" + iconUrl + ", id=" + id + ", name=" + name
				+ ", packageName=" + packageName + ", size=" + size
				+ ", stars=" + stars + "]";
	}
	
	
}
