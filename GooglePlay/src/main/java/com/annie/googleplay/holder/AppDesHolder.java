package com.annie.googleplay.holder;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class AppDesHolder extends BaseHolder<AppInfo> implements OnClickListener {
	TextView tv_des, tv_author;
	ImageView iv_des_arrow;
	private ScrollView scrollView;

	/**
	 * 对外提供方法,获取ScrollView对象
	 */
	public void setScrollView(ScrollView scrollView){
		this.scrollView = scrollView;
	}
	@Override
	public View initView() {
		View view = View.inflate(GooglePlayApplication.context, R.layout.layout_detail_des, null);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
		tv_author = (TextView) view.findViewById(R.id.tv_author);
		iv_des_arrow = (ImageView) view.findViewById(R.id.iv_des_arrow);
		
		//设置点击事件,以实现view的代开关闭效果
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void setBindData(AppInfo appInfo) {
		String des = appInfo.getDes();
		String author = appInfo.getAuthor();
		tv_des.setText(des);
		tv_author.setText(author);
		
		//显示5行文本,计算view显示的最小高度
		tv_des.setMaxLines(5);
		//通过设置全局布局监听器来获取高度
		tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			/**
			 * 该方法在ll_safe_anim进行完布局之后执行
			 * onLayout方法之后才能使用getHeight获取高度
			 */
			@Override
			public void onGlobalLayout() {
				//一般会立即移除监听器
				tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				//计算显示的最小高度,也就是5行文本的高度
				minHeight = tv_des.getHeight();
				
				
				//再次计算显示的最大高度,也就是全部文本的高度
				getMaxHeight();
				
			}

		});
		
	}

	/**
	 * @Description: 计算显示的最大高度
	 */
	private void getMaxHeight() {
		//显示全部文本,这里因为布局高度发生了改变,所以会立即重新Layout,
		//但此时Layout还没有布局完,所以不能直接获取高度
		tv_des.setMaxLines(Integer.MAX_VALUE); 
		//所以再次添加全局布局监听器
		tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//一般会立即移除监听器
				tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				//获取最大高度
				maxHeight = tv_des.getHeight();
				
				//测量完最大高度后,须重新让view显示为最小的5行文本
				//tv_des.setMaxLines(5);//该方式在执行动画的时候会影响显示效果
				//(tv_des是新创建的所以new LayoutParams,否则getLayoutParams)
				android.view.ViewGroup.LayoutParams params = tv_des.getLayoutParams();
				params.height = minHeight;
				tv_des.setLayoutParams(params);
			}
		});
	}

	
	private int maxHeight;//计算显示的最大高度
	private int minHeight;//计算显示的最小高度,也就是5行文本的高度
	private boolean isOpen = false; //用于标识动画是否被打开
	private boolean isAnim = false; //用于判断动画是否正在执行
	//设置点击事件,以实现view的代开关闭效果
	@Override
	public void onClick(View v) {
		ValueAnimator valueAnimator = null;
		if (isOpen) {
			//创建值动画,值从参1-->参2,关闭view
			valueAnimator = ValueAnimator.ofInt(maxHeight,minHeight); 
		}else {
			//创建值动画,值从参1-->参2,打开view
			valueAnimator = ValueAnimator.ofInt(minHeight,maxHeight); 
		}
		 
		//我们要监听动画值改变的进度，实现自己的动画逻辑
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//不断获取动画的值
				int  animatedValue = (Integer) animation.getAnimatedValue();
				
				//将不断获取的值赋给view
				android.view.ViewGroup.LayoutParams params = tv_des.getLayoutParams();
				params.height = animatedValue;
				tv_des.setLayoutParams(params);
				
				//在TextView高度增高的时候，让scrollView向上滚动,
				//注:向上滚动值为正
				scrollView.scrollBy(0, 12);//由于该滚动会一直执行，所以没有必要传递非常大的值
				
			}
		});
		valueAnimator.setDuration(520);
		valueAnimator.start();
		
		//旋转箭头
		ViewPropertyAnimator.animate(iv_des_arrow)
							.rotationBy(180).setDuration(520).start();
		
		
		// 最后将isOpen取反
		isOpen = !isOpen;
	}

}
