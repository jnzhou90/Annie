package com.annie.newsApp.pagerMenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.annie.newsApp.NewsDetailActivity;
import com.annie.newsApp.R;
import com.annie.newsApp.base.MenuBasePager;
import com.annie.newsApp.been.NewsCenterBeen.newsTab;
import com.annie.newsApp.been.TabDetailBeen;
import com.annie.newsApp.been.TabDetailBeen.Data.News;
import com.annie.newsApp.util.BitmapUtilsHelper;
import com.annie.newsApp.util.Constant;
import com.annie.newsApp.util.SharedPreferencesUtil;
import com.annie.newsApp.view.ReflshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class TabDetailPaper extends MenuBasePager {

	private newsTab newTabs;
	private TextView textView;
	private TabDetailBeen tabDetailBeen;
	private ViewPager mViewPager;
	private TextView mIconInfo;
	private View view;
	private BitmapUtils bitmapUtils;
	private LinearLayout mLayout;
	private ImageView imageView;
	private int preRedPoint;
	private ReflshListView mListView;
	private View topnews;

	public TabDetailPaper(Context context, newsTab newsTabs) {
		super(context);
		this.newTabs = newsTabs;
		bitmapUtils = BitmapUtilsHelper.getBitmapUtils(mContext);
		//bitmapUtils = new BitmapUtils(mContext);
	}

	@Override
	public View initView() {
		//listview的布局
		view = View.inflate(mContext, R.layout.tabdetail, null);
		mListView = (ReflshListView) view.findViewById(R.id.tabdetail_lv_listview);
		
		//将之前的轮播图布局抽取成,listview的头布局
		topnews = View.inflate(mContext, R.layout.topnes, null);
		mViewPager = (ViewPager) topnews.findViewById(R.id.tabdetail_vp_viewpager);
		mIconInfo = (TextView) topnews .findViewById(R.id.tabdetail_tv_iconInfo);
		mLayout = (LinearLayout) topnews.findViewById(R.id.tabdetail_ll_point);
		
		//轮播图布局添加到listv的头布局
		mListView.addHeaderView(topnews);
		
		//为下拉刷新listview设置监听
		mListView.setMyRefreshListener(new MyRefreshListener());
		
		//设置listview的条目点击事件,以判断该条目是否被点击过
		mListView.setOnItemClickListener(new MyOnItemClickListener());
		
		return view;
	}

	//设置listview的条目点击事件,以判断该条目是否被点击过
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			System.out.println("哈哈哈哈哈哈哈哈哈哈哈哈哈");
			
			//当listview条目被点击的时候,获取该条目当前的位置,因为之前在listview中添加了两个头
			//所以条目真实的位置为当前位置 - 2
			int realPosition = position - 2;
			//根据获取的条目位置,获取新闻-->得到新闻的id
			String id2 = String.valueOf(tabDetailBeen.data.news.get(realPosition).id);
			//将新闻的id保存到sp中
			//先获取之前保存的缓存id
			String cacheId = SharedPreferencesUtil.getString(mContext, Constant.CACHE_READ_NEWID, "");
			String tempId = "";
			//如果当前被点击的新闻没有被缓存id,则将其id缓存到sp中
			if (!cacheId.contains(id2)) {
				tempId = cacheId + "," + id2;
				SharedPreferencesUtil.saveString(mContext, Constant.CACHE_READ_NEWID,tempId); 
				//通知更新数据
				myListviewAdapter.notifyDataSetChanged();
			}
			
			//当点击条目的时候,跳转到相应的界面
			Intent intent = new Intent(mContext,NewsDetailActivity.class);
			intent.putExtra("url", tabDetailBeen.data.news.get(realPosition).url);
			mContext.startActivity(intent);
		}
		
	}
	
	private boolean isRefreshing = false;  //当期 是否下拉刷新访问网络
	//为下拉刷新listview设置监听
	private class MyRefreshListener implements com.annie.newsApp.view.ReflshListView.MyRefreshListener{

		@Override
		public void onRefreshing() {
			isRefreshing = true; //表示当前正在执行下拉刷新操作
			//执行刷新业务
			getDataFromServer();
		}

		@Override
		public void onLoardMore() {
			if (TextUtils.isEmpty(tabDetailBeen.data.more)) {
				//如果没有更多的数据了,则调用加载更多完成的方法
				// 恢复加载更多界面状态
				mListView.getMoreDataFinished();
				Toast.makeText(mContext, "没有更多数据了，亲", 0).show();
			}else {
				//表示当前加载了更多数据
				isLoadMore = true;
				//如果有更多的数据,则请求网络获取更多数据
				getMoreDataFromServer();
			}
		}
		
	}
	
	@Override
	public void initData() {
		//先获取缓存数据
		String cacheJson = SharedPreferencesUtil.getString(mContext, newTabs.url, "");
		if (!TextUtils.isEmpty(cacheJson)) {
			//有缓存--》展示缓存数据
			parseGson(cacheJson);
		}
		
		//请求网络获取数据
		getDataFromServer();
		
		/**
		 * 适配器不能再这里设置,因为getDataFromServer中包含请求网络的操作,异步操作,导致设置适配器的代码和getDataFromServer
		 * 平级,所以适配器中需要的数据,getDataFromServer还没有返回,-->导致空指针异常!!!
		 */
		/*//展示轮播图(图片),为viewpager设置适配器
		mViewPager.setAdapter(new Myadapter());*/
	}

	/**
	 * @Description:请求网络获取更多数据
	 * @param:
	 */
	private  boolean isLoadMore = false; //判断是否加载了更多的数据
	private MyListviewAdapter myListviewAdapter;
	private MyHandler hd;
	private void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constant.SERVER_URL + tabDetailBeen.data.more,new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//加载更多数据成功,解析数据
						parseGson(responseInfo.result);
						//数据解析完成之后,将标识重置为false,表示当前没有再加载更多的数据
						isLoadMore = false;
						//调用加载更多完成后的方法
						mListView.getMoreDataFinished();
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						//没有获取更多数据,将标识重置为false,表示当前没有再加载更多的数据
						isLoadMore = false;
						//调用加载更多完成后的方法
						mListView.getMoreDataFinished();
					}
				});
	}
	
	/**
	 * @Description:请求网络获取数据
	 * @param:
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constant.SERVER_URL+ newTabs.url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				//缓存页签详情数据,需要与每一个页签对应起来,把每个页签的URL作为key!!!!
				SharedPreferencesUtil.saveString(mContext, newTabs.url, responseInfo.result);
				
				//使用Gson解析数据
				parseGson(responseInfo.result);
				
				//如果正在执行下拉刷洗操作
				if (isRefreshing) {
					//下拉刷新完后恢复刷新状态,并回传true,表示刷新成功,false表示刷新失败
					mListView.refreshFinished(true);
					//重置isRefreshing状态
					isRefreshing = false;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//如果正在执行下拉刷洗操作
				if (isRefreshing) {
					//下拉刷新完后恢复刷新状态,并回传true,表示刷新成功,false表示刷新失败
					mListView.refreshFinished(false);
					//重置isRefreshing状态
					isRefreshing = false;
				}
					}
				});
	}

	/**
	 * @Description: 使用Gson解析数据
	 * @param:
	 */
	protected void parseGson(String result) {
		//如果加载了更多的数据,则将加载的更多数据添加到当前集合中
		Gson gson = new Gson();
		tabDetailBeen = gson.fromJson(result, TabDetailBeen.class);
		if (!isLoadMore) {
			// 解析数据
			
			// 展示轮播图(图片),为viewpager设置适配器
			mViewPager.setAdapter(new Myadapter());
			
			//要想在展示轮播图的同时,在图片上方显示出轮播图片的描述,必须要对viewpager进行监听
			//当轮播图滑动时，根据滑动的位置更新图片描述
			mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
			
			// 初始化轮播图第一页的图片描述
			mIconInfo.setText(tabDetailBeen.data.topnews.get(0).title);
			
			//z设置点
			/**
			 * 因为viewpager有预加载的功能,且每次同时加载三个界面,所以当显示到第三个界面的时候,第一个界面就会从预加载中去除
			 * 这时如果返回第一个界面,就会重新调用initdata方法返回一个view,但由于之前的rootview中的点并没有清空,所以这次显示的
			 * 时候会加上之前的点-->为此在创建点之前需要先清空rootview中之前的点,并将保存的前一个红点的位置重新置为0 
			 */
			
			mLayout.removeAllViews();
			preRedPoint = 0;
			for (int i = 0; i < tabDetailBeen.data.topnews.size(); i++) {
				//创建点
				imageView = new ImageView(mContext);
				imageView.setBackgroundResource(R.drawable.point_selector);//设置点的背景
				imageView.setEnabled(false);//设置为白点
				LinearLayout.LayoutParams params = new LayoutParams(5, 5);//设置点点宽高
				params.rightMargin = 5;//设置点的右边距
				imageView.setLayoutParams(params);//将设置好的点的属性值赋给点
				mLayout.addView(imageView);//将点添加到定义好的容器中
			}
			//初始化点,将第一个点变成红色
			mLayout.getChildAt(0).setEnabled(true);
			
			//设置listview的adapter来显示新闻列表
			myListviewAdapter = new MyListviewAdapter();
			mListView.setAdapter(myListviewAdapter);
		}else { 
			//将加载的更多数据添加到当前的新闻集合中展示
			tabDetailBeen.data.news.addAll(tabDetailBeen.data.news);
			//更新数据
			myListviewAdapter.notifyDataSetChanged();
			
		}
		
		//发送消息,切换轮播图
		//因为当缓存或访问网络的时候都要发送消息,所以为了确保只发送一个消息,需要先清空之前未发送的消息
		if (hd == null) {
			hd = new MyHandler();
		}
		//清空之前的还为来得及发送的消息
		hd.removeCallbacksAndMessages(null); //null: 删除之前发送的所有消息,填入具体的消息则删除指点的消息
		//发送延时消息
		hd.sendMessageDelayed(Message.obtain(), 2000);
		
	}
	
	//设置listview的adapter来显示新闻列表
	private class MyListviewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return tabDetailBeen.data.news.size();
		}

		@Override
		public Object getItem(int position) {
			return tabDetailBeen.data.news.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsHolder holder = null;
			if(convertView==null){
				convertView = View.inflate(mContext, R.layout.tabdetail_newsitem, null);
				holder = new NewsHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.iv_newsitem_img);
				holder.title = (TextView) convertView.findViewById(R.id.tv_newsitem_title);
				holder.time = (TextView) convertView.findViewById(R.id.tv_newsitem_time);
				convertView.setTag(holder);
			}else{
				holder = (NewsHolder) convertView.getTag();
			}
			
			//通过XUtils,展示图片
			bitmapUtils.display(holder.img, tabDetailBeen.data.news.get(position).listimage);
			holder.time.setText(tabDetailBeen.data.news.get(position).pubdate);
			holder.title.setText(tabDetailBeen.data.news.get(position).title);
			
			//判断当前条目是否已经被点击过
			 int id = tabDetailBeen.data.news.get(position).id;
			String cacheId = SharedPreferencesUtil.getString(mContext, Constant.CACHE_READ_NEWID, "");
			if (cacheId.contains(String.valueOf(id))) {
				holder.title.setTextColor(Color.GRAY);
			}else {
				holder.title.setTextColor(Color.BLACK);
			}
			
			
			return convertView;
		}
		
	}
	
	class NewsHolder{
		public ImageView img;
		public TextView title;
		public TextView time;
	}

	//设置viewpager的界面滑动监听,显示轮播图片的描述信息
	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			//显示轮播图片的描述信息
			mIconInfo.setText(tabDetailBeen.data.topnews.get(position).title);
			
			//切换到该界面的时候先将之间的红点变成白点
			mLayout.getChildAt(preRedPoint).setEnabled(false);
			//切换到该界面后将该界面的点变红
			mLayout.getChildAt(position).setEnabled(true);
			//记录前一个红点的位置
			preRedPoint = position;
			
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}
		
	}
	// 为viewpager设置适配器
	private class Myadapter extends PagerAdapter {

		@Override
		public int getCount() {
			return tabDetailBeen.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 展示图片需要用imageview作为图片的载体
			ImageView imageView = new ImageView(mContext);
			
			// 展示图片需要根据图片的网址到服务器中下载图片,这个过程还需要开子线程,麻烦,所以使用XUtils中获取图片的模块
			// 但BitmapUtils采用了三级缓存,没new一个对象就会在在内存中开辟出一块内存空间,new多次后可能导致内存溢出,因此需要为它设置单例模式

			//设置图片的缩放类型,让图片内填充满控件
			//ScaleType.FIT_XY:不按图片的比例进行拉伸; CENTER_CROP: 按照图片的比例进行拉伸,但会对图片进行裁剪,只展示放大后图片的中间部分
			imageView.setScaleType(ScaleType.CENTER_CROP);
			// 参数1: 图片保存在哪 参数2: 图片的地址 它会根据这个地址去服务器下载图片保存到container中
			bitmapUtils.display(imageView, tabDetailBeen.data.topnews.get(position).topimage);
			
			// 将imageview添加到viewpager中展示
			container.addView(imageView);
			
			//设置图片的触摸监听,根据图片的触摸状态判断是否继续轮播图片
			imageView.setOnTouchListener(new MyOnTouchListener());
			
			return imageView;
		}

	}
	
	//设置图片的触摸监听,根据图片的触摸状态判断是否继续轮播图片
	private class MyOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//当按下图片的时候需要停止轮播,即删除已经发送的消息
				hd.removeCallbacksAndMessages(null); //删除发送的消息
				break;
			case MotionEvent.ACTION_UP:
				//当手指抬起的时候,继续轮播图片,即发送消息,继续切换下一张图片
				hd.sendMessageDelayed(Message.obtain(), 2000); //发送延时信息
				break;
			case MotionEvent.ACTION_CANCEL:
				//当事件取消的时候(按下控件后,不抬起,移出控件),继续轮播图片
				hd.sendMessageDelayed(Message.obtain(), 2000); //发送延时信息
				break;

			default:
				break;
			}
			// 返回false 孩子不处理事件 父容器就不传递其他的事件了,也就无法得到抬起或取消的事件,因此须返回true,表示孩子处理事件
			return true;
		} 
		
	}
	
	private class MyHandler extends Handler {
	@Override
	public void handleMessage(Message msg) {
		
		//解决内存泄漏,判断当前ViewPager是否存在界面上，如果不显示就不再发消息了
		if (mViewPager.getWindowVisibility() == View.GONE) {
			hd.removeCallbacksAndMessages(null);
			return;
		}
		//切换到最后一页时,需要重新切回第一页,所以对当前的mViewPager.getCurrentItem() + 1取模
		int newIndex = (mViewPager.getCurrentItem() + 1) % tabDetailBeen.data.topnews.size();
		
		//接收消息,切换轮播图,当即将切换到第一页的时候,不要切换动画
		if (newIndex == 0) {
			mViewPager.setCurrentItem(newIndex, false);//false: 不要切换动画
		}else {
			mViewPager.setCurrentItem(newIndex);
		}
		
		
		//再次发送消息,实现无限循环(即使不动也要发消息，使轮播图继续)
		hd.sendMessageDelayed(Message.obtain(), 2000);
	}
	}
}
