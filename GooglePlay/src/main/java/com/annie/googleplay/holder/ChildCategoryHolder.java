package com.annie.googleplay.holder;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.ChildCategory;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChildCategoryHolder extends BaseHolder {
	
	private TextView mTitle;
	private TextView mName1;
	private TextView mName2;
	private TextView mName3;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private LinearLayout mLl1;
	private LinearLayout mLl2;
	private LinearLayout mLl3;

	@Override
	public View initView() {
		//初始化子分类view
		View view = View.inflate(GooglePlayApplication.context, R.layout.child_category,null);
		mName1 = (TextView) view.findViewById(R.id.name1_category);
		mName2 = (TextView) view.findViewById(R.id.name2_category);
		mName3 = (TextView) view.findViewById(R.id.name3_category);
		mImageView1 = (ImageView) view.findViewById(R.id.icon1_category);
		mImageView2 = (ImageView) view.findViewById(R.id.icon2_category);
		mImageView3 = (ImageView) view.findViewById(R.id.icon3_category);
		mLl1 = (LinearLayout) view.findViewById(R.id.ll1_category);
		mLl2 = (LinearLayout) view.findViewById(R.id.ll2_category);
		mLl3 = (LinearLayout) view.findViewById(R.id.ll3_category);
		return view;
	}

	@Override
	public void setBindData(Object data) {
		//将数据强转为子分类类型
		ChildCategory childCategory = (ChildCategory) data;
		//赋值操作
		mName1.setText(childCategory.getName1());
		ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + childCategory.getUrl1(), mImageView1, ImageloaderOptions.FadeIn_options);
		
		//显示第二个或第三个数据时,它们有可能为空,所以需要判断
		if (!TextUtils.isEmpty(childCategory.getName2())) {
			//由于条目时复用的,所以为了防止复用了某个被隐藏的条目,所以当需要设置数据的时候,需要置为可见
			mLl2.setVisibility(View.VISIBLE);
			
			//不为空才显示
			mName2.setText(childCategory.getName2());
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + childCategory.getUrl2(), mImageView2, ImageloaderOptions.FadeIn_options);
		}else {
			//如果为空,则隐藏
			mLl2.setVisibility(View.INVISIBLE);
		}
		
		if (!TextUtils.isEmpty(childCategory.getName3())) {
			//由于条目时复用的,所以为了防止复用了某个被隐藏的条目,所以当需要设置数据的时候,需要置为可见
			mLl3.setVisibility(View.VISIBLE);
			
			//不为空才显示
			mName3.setText(childCategory.getName3());
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + childCategory.getUrl3(), mImageView3, ImageloaderOptions.FadeIn_options);
		}else {
			//如果为空,则隐藏
			mLl3.setVisibility(View.INVISIBLE);
		}
		
		
	}

}
