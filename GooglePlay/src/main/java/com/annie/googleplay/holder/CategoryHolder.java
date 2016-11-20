package com.annie.googleplay.holder;

import java.util.List;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.CategoryBean;
import com.annie.googleplay.bean.ChildCategory;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryHolder extends BaseHolder {
	
	private Object data;
	private int itemViewType;
	private TextView mTitle;
	private TextView mName1;
	private TextView mName2;
	private TextView mName3;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	
	public CategoryHolder(Object object) {
		super();
		this.data = object;
		if (object instanceof String) {
			itemViewType = 0;
		}else {
			itemViewType = 1;
		}
		
	}

	@Override
	public View initView() {
		System.out.println(itemViewType);
		View view = null;
		switch (itemViewType) {
		case 0:
			System.out.println("333333333333333333333333333333333333333333333333333333333333333333333");
			//加载大标题条目类型的view
			view = View.inflate(GooglePlayApplication.context, R.layout.title_category, null);
			mTitle = (TextView) view.findViewById(R.id.title_category);
			
			
			break;
		case 1:
			view = View.inflate(GooglePlayApplication.context, R.layout.child_category, null);
			System.err.println("hahahahhahahhahahah按键大卡的嘎嘎嘎开了房可怜噶课老师");
			mName1 = (TextView) view.findViewById(R.id.name1_category);
			mName2 = (TextView) view.findViewById(R.id.name2_category);
			mName3 = (TextView) view.findViewById(R.id.name3_category);
			mImageView1 = (ImageView) view.findViewById(R.id.icon1_category);
			mImageView2 = (ImageView) view.findViewById(R.id.icon2_category);
			mImageView3 = (ImageView) view.findViewById(R.id.icon3_category);
			
			
			break;
		}
		return view;
	}

	@Override
	public void setBindData(Object data) {
		/*	System.out.println(itemViewType);
		switch (itemViewType) {
		case 0:
			System.out.println("2222222222222222222222222222222222222222222222222222222");
			//能走到这里说明数据一定是字符串类型
			String title = (String) data;
			mTitle.setText(title);
			break;
		case 1:
			System.out.println("1111111111111111111111111111111111111111111111111111");
//			ChildCategory caildcCategory = (ChildCategory) data;
//			System.out.println(caildcCategory.name1 + caildcCategory.url1);
//			mName1.setText(caildcCategory.name1);
//			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + caildcCategory.url1, mImageView1, ImageloaderOptions.FadeIn_options);
//			
			ChildCategory childCategory = (ChildCategory) data;
			//mTitle.setText(childCategory.name1);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + childCategory.url1, mImageView1, ImageloaderOptions.options);
			
			break;
		}*/
	}

}
