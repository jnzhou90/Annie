package com.annie.googleplay.adapter;

import java.util.ArrayList;
import java.util.List;

import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.holder.BaseHolder;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;

public abstract class BasicAdapter<T> extends BaseAdapter {
	
	List<T> list = new ArrayList<T>();
	//通过构造方法将所需要的集合传进来
	public BasicAdapter(List<T> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	//需要子类自己去重写
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		BaseHolder<T> holder;
		if (convertView == null) {
			// 1.创建holder对象
			holder = getHolder(position);  //需要一个不确定的holder
		} else {
			// 从convertview中取出holder
			holder = (BaseHolder<T>) convertView.getTag();
		}

		// 绑定数据
		T data = list.get(position);
		holder.setBindData(data);

		View holdView = holder.getHolderView();
		//缩放动画
		ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(400);
		//设置回弹效果的差值器,传入的参数表示是默认的几倍!!!
		sa.setInterpolator(new OvershootInterpolator());
		//设置来回执行的插补器
		//sa.setInterpolator(new CycleInterpolator(4));
		//像球落地一样的感觉的插补器
		//sa.setInterpolator(new BounceInterpolator());
		holdView.startAnimation(sa);
		
		/*//旋转动画
		ViewHelper.setRotationY(holdView, 60);
		ViewPropertyAnimator.animate(holdView)
				.rotationY(360)
				.setInterpolator(new BounceInterpolator())//像球落地一样的感觉
				.setDuration(1000)
				.start();*/
		
		return holdView;
	}

	/**
	 * @Description: 获取holder对象
	 * @param:
	 */
	protected abstract BaseHolder<T> getHolder(int position);

}
