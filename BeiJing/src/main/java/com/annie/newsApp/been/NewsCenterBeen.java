package com.annie.newsApp.been;

import java.util.List;

import android.R.integer;

public class NewsCenterBeen {

	public int retcode;
	public List<Integer> extend;
	public List<leftMenu> data;

	public class leftMenu {
		public int id;
		public int type;
		public String title;
		public String url;
		public String url1;
		public String excurl;
		public String dayurl;
		public String weekurl;
		public List<newsTab> children;
	}

	public class newsTab {
		public int id;
		public String title;
		public int type;
		public String url;
	}

}
