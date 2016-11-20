package com.annie.newsApp.pagerMenu;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.annie.newsApp.R;
import com.annie.newsApp.base.MenuBasePager;
import com.annie.newsApp.been.PhotoBean;
import com.annie.newsApp.been.NewsCenterBeen.newsTab;
import com.annie.newsApp.been.PhotoBean.Data.News;
import com.annie.newsApp.util.BitmapUtilsHelper;
import com.annie.newsApp.util.Constant;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PhotoDetailPager extends MenuBasePager {

	private View view;
	private ListView mListView;
	private GridView mGridView;
	private List<News> newsData;
	private BitmapUtils bitmapUtils;

	public PhotoDetailPager(Context context) {
		super(context);
		
		bitmapUtils = BitmapUtilsHelper.getBitmapUtils(mContext);
	}

	@Override
	public View initView() {
		view = View.inflate(mContext, R.layout.photo_detail_pager, null);
		mListView = (ListView) view.findViewById(R.id.photo_lv_list);
		mGridView = (GridView) view.findViewById(R.id.photo_gv_grid);
		return view;
	}

	@Override
	public void initData() {
		//请求网络获取数据
		getDataFromServer();
	}

	//请求网络获取数据
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET,Constant.PHOTODETAIL_URL, new RequestCallBack<String >() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//解析服务器返回的数据
				parseJson(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
			}
		});
	}

	//解析服务器返回的数据
	protected void parseJson(String result) {
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
		System.out.println("ha"+photoBean.data.news.get(0).title);
		newsData = photoBean.data.news;
		
		//设置设配器展示数据
		mListView.setAdapter(new MyAdapter());
	}
	
	//为listview设置适配器展示数据
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return newsData.size();
		}

		@Override
		public Object getItem(int position) {
			return newsData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.photo_item, null);
			}
			ImageView mImageView = (ImageView) convertView.findViewById(R.id.photoitem_iv_photo);
			TextView mTextView = (TextView) convertView.findViewById(R.id.photoitem_tv_title);
			mTextView.setText(newsData.get(position).title);
			
			//设置图片的缩放类型
			mImageView.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(mImageView, newsData.get(position).listimage);
			
			return convertView;
		}
	}

	//切换界面的显示类型.是listview还是gradview
	public boolean isListview = true;
	public void switchType(ImageButton mType) {
		//如果当前是以listview展示的,那么点击后应变为以gradvieww展示
		if (isListview) {
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			//切换按钮的图片
			mType.setBackgroundResource(R.drawable.icon_pic_list_type);
			//设置适配器展示数据
			mGridView.setAdapter(new MyAdapter());
		}else {
			mListView.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
			//切换按钮的图片
			mType.setBackgroundResource(R.drawable.icon_pic_grid_type);
			//设置适配器展示数据
			mListView.setAdapter(new MyAdapter());
		}
		
		//重新设置标识,
		isListview = !isListview;
	}
}
