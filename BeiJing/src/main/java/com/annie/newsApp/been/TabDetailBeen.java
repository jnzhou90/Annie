package com.annie.newsApp.been;



import java.util.List;

public class TabDetailBeen {

	public int retcode;
	public Data data;

	public class Data {

		public String more;
		public String countcommenturl;
		public String title;
		public List<News> news;

		public class News {

			public String commentlist;
			public String commenturl;
			public Boolean comment;
			public int id;
			public String title;
			public String type;
			public String listimage;
			public String url;
			public String pubdate;
		}

		public List<Topnews> topnews;

		public class Topnews {

			public String commentlist;
			public String topimage;
			public String commenturl;
			public Boolean comment;
			public int id;
			public String title;
			public String type;
			public String url;
			public String pubdate;
		}

		public List<Topic> topic;

		public class Topic {

			public String description;
			public int id;
			public int sort;
			public String title;
			public String listimage;
			public String url;
		}

	}
}