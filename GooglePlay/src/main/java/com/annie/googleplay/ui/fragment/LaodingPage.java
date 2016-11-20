package com.annie.googleplay.ui.fragment;

import java.util.List;

import com.annie.googleplay.R;
import com.annie.googleplay.util.CommonUtil;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;



/**
 * 负责管理数据加载的状态和界面切换逻辑
 * @author Free
 *
 */
public abstract class LaodingPage extends FrameLayout{

	public LaodingPage(Context context) {
		//super(context);
		this(context, null);
	}

	public LaodingPage(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context, attrs, 0);
	}

	public LaodingPage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		
		//初始化加载界面
		initLoadingPage();
		
		
	}

	//定义界面切换的三种状态
	private final int STATE_LOADING = 1;
	private final int STATE_ERROR = 2;
	private final int STATE_SUCCESS = 3;
	 
	//定义当前界面的状态,这里设置当前界面状态时一定不能用static修饰!!!
	private int mState = STATE_LOADING;
	
	private View mLoadingPage;
	private View mErrorPage;
	private View mSuccessPage;
	
	//初始化加载界面,将三种状态的界面包含进来
	public void initLoadingPage() {
		//将加载中界面加载进来,有就直接添加进来,没有就填充一个view
		if (mLoadingPage == null) {
			mLoadingPage = View.inflate(getContext(), R.layout.page_loading, null);
			
		}
		addView(mLoadingPage);
		
		//将加载失败的界面加载进来,有就直接添加进来,没有就填充一个view 
		if (mErrorPage == null) {
			mErrorPage = View.inflate(getContext(), R.layout.page_error, null);
			
			//设置重新加载按钮的点击事件
			Button mReLoadButton = (Button) mErrorPage.findViewById(R.id.btn_reload);
			mReLoadButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//当点击重新加载按钮后,应该先将界面置为加载中的状态,然后在加载数据刷新界面
					mState = STATE_LOADING;
					showPage();
					
					//加载数据,刷新界面
					loadDataAndRefreshPage();
					
				}
			});
		}
		addView(mErrorPage);
		
		//当界面加载成功后,每个界面显示的内容都不相同,
		//因此具体的界面的实现应交给单独的每个界面去事项,即创建一个抽象方法,强制子类去实现
		if (mSuccessPage == null) {
			mSuccessPage = createSuccessPage();
		}
		if (mSuccessPage != null) {
			addView(mSuccessPage);
			
			//一定要重新加载数据,否则会出现第一次打开界面,数据加载失败的bug!!!
			//loadData();
		}else {
			//说明某个哥们没有实现createSuccessView()
			//则明确告诉它要干嘛
			throw new IllegalArgumentException("createSuccessPage()方法不能返回null");
		}
		
		//根据不同的状态,展示不同的界面,默认展示loadingpage界面
		showPage();
		
		//加载界面数据,根据加载回来的数据更新UI
		loadDataAndRefreshPage();
	}
	
	/**
	 * @Description: 加载界面数据,根据加载回来的数据更新UI
	 * @param: 加载数据肯定是要通过网络的,所以应该在子线程中去执行
	 */
	public void loadDataAndRefreshPage() {
		new Thread() {
			public void run() {
				//模拟服务器加载数据延时
				SystemClock.sleep(1500);
				
				//因为界面加载的数据时以json格式返回的,所以可能是javabean也可能是集合,因此用object来接收数据
				// 1.由于每个界面加载的数据是不同的,所以加载数据的操作应由具体的界面去实现,
				//所以这里创建一个抽象方法,强制子界面去实现
				Object data = loadData();
				// 2.根据加载回来的data，判断页面的状态
				mState = checkData(data);
				// 3. 根据状态,切换界面,刷新UI必须在主线程中去执行
				CommonUtil.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//刷新界面
						showPage();
					}
				});
				
			};
		}.start();
	}

	/**
	 * @Description: 根据加载回来的data，判断页面的状态
	 * @param:
	 */
	private int checkData(Object data) {
		if (data == null) {
			//如果为空,说明没有数据,返回加载失败
			return mState = STATE_ERROR;
		}else {
			//如果有数据,需要细分,是bean还是list
			if (data instanceof List) {
				List list = (List) data;
				//如果是集合,当集合的长度为0 时,也认为没有数据,加载失败
				if (list.size() == 0) {
					return mState = STATE_ERROR;
				}
			}
		}
		
		//其余情况说明确实加载到了数据,返回加载成功
		return mState = STATE_SUCCESS;
	}

	/**
	 * @Description: 根据状态判断该切换到哪个界面
	 * @param:
	 */
	public void showPage() {
		//开始时先将所有界面都隐藏掉,之后展示哪个显示哪个
		mLoadingPage.setVisibility(View.INVISIBLE);
		mErrorPage.setVisibility(View.INVISIBLE);
		mSuccessPage.setVisibility(View.INVISIBLE);
		switch (mState) {
		case STATE_LOADING:
			mLoadingPage.setVisibility(View.VISIBLE);
			break;
		case STATE_ERROR:
			mErrorPage.setVisibility(View.VISIBLE);
			break;
		case STATE_SUCCESS:
			mSuccessPage.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	//界面加载成功后强制要求子类去实现的方法
	protected abstract View createSuccessPage();
	
	/**
	 * 加载数据的方法，每个子类实现都不一样，所以让子类实现，
	 * 并且将子类返回的数据抽象为Object，其实要么是javabean，要么是list
	 * @return
	 */
	protected abstract Object loadData();
	
}
