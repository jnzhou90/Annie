package com.annie.newsApp.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.annie.newsApp.R;
import com.annie.newsApp.been.NewsCenterBeen.newsTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ReflshListView extends ListView {

	private int downY = -1;
	private View view;
	private int headHeight;
	private ImageView headArrow;
	private ProgressBar headpProgressBar;
	private TextView headState;
	private TextView headTime;
	// 下拉刷新的状态标示
	private static final int PULL_DOWN = 1;
	// 松开刷新的状态标示
	private static final int RELEASE_REFRESH = 2;
	// 正在刷新的状态标示
	private static final int REFRESHING = 3;
	// 当前的状态
	private static int CURRENTSTATE = PULL_DOWN;
	private RotateAnimation up;
	private RotateAnimation down;
	private MyRefreshListener Mlistener;
	private View footerView;
	private int footerHeight;

	public ReflshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//在构造方法中初始化刷新头,使其一开始就被添加到listview的头部
		initHeader();
		//在尾部添加加载更多布局
		initFooter();
	}
	
	//在尾部添加加载更多布局
	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.refresh_footer, null);
		
		//隐藏尾部加载更多布局
		footerView.measure(0, 0);
		footerHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, 0, 0, -footerHeight);
		this.addFooterView(footerView);
		
		//设置refreshListview的滑动监听
		this.setOnScrollListener(new MyOnScrollListener());
	}
	
	//设置refreshListview的滑动监听
	private boolean isLoadMore = false; //是否正在加载更多
	private class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 当处于停止或惯性停止状态时，且Listview展示的条目最后一条数据，显示加载更多布局
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (getLastVisiblePosition() == getCount() - 1 && isLoadMore == false) {
					
					//重置标识.表示现在正在加载更多
					isLoadMore = true;
					//显示加载更多的布局
					footerView.setPadding(0, 0, 0, 0);
					//让加载更多脚布局自动显示在指定的位置!!!
					setSelection(getCount());
					
					//当加载更多的时候,调用外界传过来的监听器业务
					if (Mlistener != null) {
						Mlistener.onLoardMore();
					}
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
		
	}

	//添加刷新头
	public void initHeader() {
		view = View.inflate(getContext(), R.layout.refresh_header, null);
		headArrow = (ImageView) view.findViewById(R.id.reflshHeader_iv_arrow);
		headpProgressBar = (ProgressBar) view.findViewById(R.id.reflshHeader_pb_progress);
		headState = (TextView) view.findViewById(R.id.reflshHeader_tv_state);
		headTime = (TextView) view.findViewById(R.id.reflshHeader_tv_time);
		
		//因为现在控件还没有渲染出来,所以无法通过getWidth方法来获取宽高
		view.measure(0, 0); ////默认宽高
		headHeight = view.getMeasuredHeight();
		//隐藏刷新头
		view.setPadding(0, -headHeight, 0, 0);
		//给listview添加刷新头
		this.addHeaderView(view);
		
		// 设置动画
		initAnimation();
	}
	
	//设置动画
	private void initAnimation() {
		up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		up.setDuration(500);
		up.setFillAfter(true);
		
		down = new RotateAnimation(-180, -360,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		down.setDuration(500);
		down.setFillAfter(true);
	}

	//处理触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//在这里listview获得事件后,没有拦截,通过事件分发的方法,将事件分发给了孩子,
		//只有孩子不处理这个事件的时候才会返回给父控件ontouchevent方法,
		//但这里孩子将事件处理了,不再回传,所以父控件拿不到down事件,因此将移动的第一个坐标赋给按下的坐标
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY  = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			if (downY == -1) {
				downY = (int) ev.getY();
			}
			int moveY = (int) ev.getY();
			
			int distanceY = moveY - downY;
			//下拉显示刷新头,显示刷新头条件：1：必须listview向下拉；2：当前界面显示的第一个条目是listview的第一个条目
			if (distanceY > 0 && getFirstVisiblePosition() == 0) {
				//计算顶部空白区域的高度
				int emptyHeight = distanceY - headHeight;
				
				//设置下拉刷新头的状态,根据emptyHeight值是否大于0判断状态切换
				if (emptyHeight < 0 && CURRENTSTATE != PULL_DOWN) {
					 // 头布局没有完全显示，切换到下拉刷新状态
					CURRENTSTATE = PULL_DOWN;
					System.out.println("下拉刷新");
					switchState(CURRENTSTATE);
				}else if (emptyHeight > 0 && CURRENTSTATE !=RELEASE_REFRESH) {
					// 头布局已经完全显示，切换到松开刷新状态
					CURRENTSTATE = RELEASE_REFRESH;
					System.out.println("松开刷新");
					switchState(CURRENTSTATE);
				}
				
				//显示刷新头
				view.setPadding(0, emptyHeight, 0, 0);
				
				//如果有空白区域显示的话，不能使用系统的触摸事件,否则按住的条目的坐标会随着手指的移动而发生变化
				return true;  //当有空白区域显示，不使用系统的listview的触摸事件，使用上边自己计算的触摸操作
			}
			break;
		case MotionEvent.ACTION_UP:
			//为了不影响下一次为down赋值,在抬起的时候应将down重置为-1
			downY = -1;
			
			// 手指抬起时，根据当前状态判断是否切换到正在刷新状态
			if (CURRENTSTATE == PULL_DOWN) {
				// 当前处于下拉状态，不切到正在刷新，需要把头布局隐藏
				view.setPadding(0, -headHeight, 0, 0);
			}else if (CURRENTSTATE == RELEASE_REFRESH) {
				// 当前处于松开状态，需要切到正在刷新，把头布局设置成刚好完全展示
				view.setPadding(0, 0, 0, 0);
				CURRENTSTATE = REFRESHING;
				System.out.println("正在刷新");
				switchState(CURRENTSTATE);
				
				// 当处于下拉刷新状态时，调用外界传进来的监听器的真正的业务,进行刷新
				if (Mlistener !=null) {
					Mlistener.onRefreshing();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	//根据刷新状态更新刷新头
	public void switchState(int state) {
		switch (CURRENTSTATE) {
		case PULL_DOWN:
			headState.setText("下拉刷新");
			headArrow.setVisibility(View.VISIBLE);
			//这里不能用none,因为上面测量高度是请求系统测量的布局中的高度,none表示将其直接删除了
			headpProgressBar.setVisibility(View.INVISIBLE);
			headArrow.startAnimation(down);
			break;
		case RELEASE_REFRESH:
			headState.setText("松开刷新");
			headArrow.startAnimation(up);
			break;
		case REFRESHING:
			//因为之前动画都保留了最后的状态,所以需要先清除动画
			headArrow.clearAnimation();
			headState.setText("正在刷新");
			headArrow.setVisibility(View.INVISIBLE);
			headpProgressBar.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	
	//提供完成下拉刷新之后调用的方法
	public void refreshFinished(boolean success) {
		headState.setText("下拉刷新");
		headArrow.setVisibility(View.VISIBLE);
		//这里不能用none,因为上面测量高度是请求系统测量的布局中的高度,none表示将其直接删除了
		headpProgressBar.setVisibility(View.INVISIBLE);
		CURRENTSTATE = PULL_DOWN;
		view.setPadding(0, -headHeight, 0, 0);
		
		if (success) {
			//如果刷新完成.设置刷新时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			String time = format.format(new Date());
			headTime.setText("最后刷新时间为:" +time);
		}else {
			Toast.makeText(getContext(), "网络出问题了", 0).show();
		}
	}
	
	//加载更多完成之后调用 的方法
	public void getMoreDataFinished() {
		//重新隐藏加载更多布局
		footerView.setPadding(0, 0, 0, -footerHeight);
		isLoadMore = false;
		Toast.makeText(getContext(), "没有更多数据了", 0).show();
	}
	
	//对外暴露监听接口
	public interface MyRefreshListener {
		//下拉刷新的方法
		void onRefreshing();
		//加载更多的方法
		void onLoardMore();
	}
	
	//让外界传递监听器
	public void setMyRefreshListener(MyRefreshListener listeneer) {
		//保存外界传入的监听器
		this.Mlistener = listeneer;
	}

}
