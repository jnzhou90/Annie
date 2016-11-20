package com.annie.googleplay.adapter;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageScreenAdapter extends PagerAdapter {
	
	private ArrayList<String> list;
	//通过构造方法初始化所需的参数
	public ImageScreenAdapter(ArrayList<String> list) {
		this.list = list;
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//super.destroyItem(container, position, object);
		container.removeView((View) object);
	}
	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
			// 1.展示图片需要用imageview
			//ImageView imageView = new ImageView(GooglePlayApplication.context);
			// 使用第三方类库来实现图片的缩放,使用方式和imageview一模一样!!!
			PhotoView imageView = new PhotoView(GooglePlayApplication.context);
			// 2.展示图片
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + list.get(position),
					imageView, ImageloaderOptions.options);
			// 3.将imageview添加到viewpager中
			container.addView(imageView);
		return imageView;
	}

	/**
	 *  图片放大的具体核心代码,也可以实现图片的平移,扭曲能效果
	 *  图片像素点的封装
	 *  Matrix matrix = new Matrix(imageView.getImageMatrix());
	 *  matrix.postScale(sx, sy);  //设置缩放的值
	 *  imageView.setImageMatrix(matrix); //将设置好的值再设置给imageview
	 */

}
