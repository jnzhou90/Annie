package com.annie.googleplay.bean;

import java.util.List;

public class CategoryBean {
	
	private String title;
	private List<ChildCategory> infos;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<ChildCategory> getInfos() {
		return infos;
	}
	public void setInfos(List<ChildCategory> infos) {
		this.infos = infos;
	}
	
	

}
