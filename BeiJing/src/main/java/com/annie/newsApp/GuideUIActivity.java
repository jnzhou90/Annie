package com.annie.newsApp;

import java.util.ArrayList;
import java.util.List;

import com.annie.newsApp.util.Constant;
import com.annie.newsApp.util.SharedPreferencesUtil;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GuideUIActivity extends Activity implements OnClickListener {

	private ViewPager mViewPager;
	private List<ImageView> list;
	private LinearLayout mLayout;
	private ImageView mRedPoint;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 代码的方式请求去除标题,必须在setContentView之前执行!!!!!!!!!!!!!!!!!
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_guide_ui);

		// 初始化控件
		initView();

	}

	/**
	 * @Description: 初始化控件
	 * @param:
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.guideui_vp_bg);
		mLayout = (LinearLayout) findViewById(R.id.guideui_ll_pointnormal);
		mRedPoint = (ImageView) findViewById(R.id.guideui_iv_redPoint);
		mButton = (Button) findViewById(R.id.guideui_bt_start);

		// 加载数据
		fillData();
	}

	/**
	 * @Description: 加载数据
	 * @param:
	 */
	private void fillData() {
		// 1.和listview一样,在获得控件后,为了展示,都需要先 准备数据
		list = new ArrayList<ImageView>();
		int imageId[] = new int[] { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 }; // 将引导界面的图片id保存到的胡祖中
		// 遍历数组,将引导界面的图片添加到集合中,以备后续使用
		for (int i = 0; i < imageId.length; i++) {
			ImageView imageView = new ImageView(this);

			// 通过设置缩放类型,来让图片填充满屏幕
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(imageId[i]); // 这种方式保存的图片无法填充满整个屏幕

			list.add(imageView); // 将imageview对象添加到集合中

			// 有几张图片就创建几个点
			View view = new View(this);
			// 设置点的默认样式
			view.setBackgroundResource(R.drawable.guide_point_normal);
			// 设置点宽高属性
			LinearLayout.LayoutParams layoutParams = new LayoutParams(
					dp2px(10), dp2px(10));
			// 设置点之间的间距
			if (i != 2) {
				layoutParams.rightMargin = dp2px(10);
			}
			// 将设置好的属性给点
			view.setLayoutParams(layoutParams);
			// 将点添加到实现定义好的容器中
			mLayout.addView(view);

		}

		// 2.设置适配器Adapter
		mViewPager.setAdapter(new MyAdapter());

		/**
		 * 红点覆盖黑点的原理: 在布局文件中定义一个imageview,其背景设为红色圆点,放在黑点的上方-->通过监听界面中手指的移动距离
		 * 来获取红点应该移动的距离-->将红点移动的相应的位置,实现黑点的覆盖 // 红点移动的距离/灰点的间距 = 手指移动的距离/屏幕的宽度
		 * // 红点移动的距离 = 手指移动的距离/屏幕的宽度 *灰点的间距 // // 红点移动的距离 = 手指移动比例 * 灰点的间距
		 */

		// 设置viewpager的界面切换监听,获取手指移动的距离
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		// Button 按钮的点击事件
		mButton.setOnClickListener(this);
	}

	// 设置viewpager的界面切换监听,获取手指移动的距离
	private class MyOnPageChangeListener implements OnPageChangeListener {

		// 当界面切换的时候调用,positionOffset:移动比例; positionOffsetPixels:移动的像素
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// 移动红点 :通过不停的设置红点的左边距来实现红点移动的步骤:
			// 1.计算红点的移动距离,还要加上之前移动的距离!!!
			int redPixels = (int) (positionOffset * dp2px(20) + position
					* dp2px(20));
			// 2.拿到红点当前的的属性
			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mRedPoint
					.getLayoutParams();
			// 3.重新设置(修改)该属性值
			params.leftMargin = redPixels;
			// 4.将修改后的属性值重新设置给红点,来实现红点的移动
			mRedPoint.setLayoutParams(params);

		}

		// 界面切换完成时调用
		@Override
		public void onPageSelected(int position) {
			// 当滑动到最后一个界面时候,显示开始体验按钮
			if (position == list.size() - 1) {
				mButton.setVisibility(View.VISIBLE);
			} else {
				mButton.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}

	}

	// 为viewpager设置适配器
	private class MyAdapter extends PagerAdapter {

		// 设置条目的个数
		@Override
		public int getCount() {
			return list.size();
		}

		// 判断viewpager的界面的对象是否和instantiateItem条目的对象一致，一致表示可以执行，不一致：没有条目
		// View : viewpager的界面的对象; object:instantiateItem返回的object对象
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// 添加条目, container : viewpager; position : 条目的位置
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = list.get(position); // 根据条目位置湖区imageview对象
			// 将imageview添加到ViewPager中展示
			container.addView(imageView);
			return imageView;
		}

		// 删除viewpager条目的操作,container : ViewPager; position : 条目的位置; Object :
		// instantiateItem返回的添加的对象
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

	}

	// Button按钮的点击事件
	@Override
	public void onClick(View v) {
		// 当点击开始体验的时候,将IS_FIRST_OPEN_APP的值保存为false,表示不再是第一次进入 了
		SharedPreferencesUtil.saveBoolean(GuideUIActivity.this,
				Constant.IS_FIRST_OPEN_APP, false);
		Intent intent = new Intent(GuideUIActivity.this, MainUIActivity.class);
		startActivity(intent);
		finish();
	}

	public int dp2px(int dp) {
		// px = dp * 密度比
		// 获取收据对应的密度比
		float density = getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5);
	}

}
