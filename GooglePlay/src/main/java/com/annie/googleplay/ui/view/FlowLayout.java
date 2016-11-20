package com.annie.googleplay.ui.view;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
	
	private int verticalSpacing = 15; //view之间的垂直间距
	private int horizontalSpacing = 15;//view之间的水平间距
	//新建一个集合,用来存放所有的line对象
	ArrayList<Line> lineList = new ArrayList<FlowLayout.Line>();

	
	public FlowLayout(Context context) {
		//super(context);
		this(context, null);
	}
	
	public FlowLayout(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
	}
	
	/**
	 * 提供设置水平间距的方法
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}
	
	/**
	 * 提供设置垂直间距的方法
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}
	
	/**
	 * MeasureSpec:测量规则 ，包含size和mode2个因素，封装的就是布局文件中的那些宽高参数
	 * size:表示具体的大小
	 * mode：表示测量的模式
	 * 		MeasureSpec.EXACTLY : 对应的是match_parent,具体的dp值
	 *      MeasureSpec.AT_MOST : 对应的是wrap_content,
	 * 		MeasureSpec.UNSPECIFIED : 初始值，一般不用
	 * 测量子控件和自己的宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//lineList.clear();
		//int spec = MeasureSpec.makeMeasureSpec(size, mode);
		
		//获取当前控件的布局的总宽度,是包含左右2边padding的宽度,不一定是屏幕的总宽度
		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
		//获取用于比较的宽度,不包含两边的padding
		int noPaddingWidth = totalWidth - getPaddingLeft() - getPaddingRight();
		
		Line line = new Line();
		//遍历所有子view,进行分行计算,因为之前已经将view对象添加进了flowlayout中,
		//所以 getChildCount 方法可以获取该布局中所有子view的数量
		for(int i = 0; i<getChildCount(); i++) {
			//取出子view
			View childView = getChildAt(i);
			//事先测量child,为了能够获取到宽度
			childView.measure(0, 0);
			
			// 1.判断当前line中是否有view对象,如果没有.则直接将该view对象添加进
			//集合,因为要确保每行至少有一个view对象
			if (line.viewList.size() == 0) {
				line.addLineView(childView);
				
			 // 2.如果当前child的宽+当前行的宽+水平间距看大于   比较宽度, 
			 //那么child需要放入下一行,
			}else if(childView.getMeasuredWidth() + line.getLineWidth() 
					+ horizontalSpacing > noPaddingWidth) {
				//在将当前子view放到下一行之前,需要先将之前的行对象,保存到一个专门的集合中
				lineList.add(line);
				
				line = new Line(); //接着创建新的行对象
				//将当前view对象添加到新的行对象中
				line.addLineView(childView);
			}else {
				// 3.如果小于最大宽度的话,则放入当前行,无需放入下一行
				line.addLineView(childView);
			}
			
			// 手动将最后一行的对象添加到lineList中
			if (i == getChildCount() - 1) {
				//如果当前是最后一个子View，那么则将最后一个line对象放入集合中
				lineList.add(line);
			}
		}
		
		// for循环结束后，得到很多的line对象，而每个line对象又记录了自己的view对象
		// 为了能够摆放所有行的View，需要计算FlowLayout自身所需的高度
		// 1.先计算所有行的高,每行的高度一致,所以任取一行的高度
		int totalHeight = 0;
		for(int i = 0;i<lineList.size(); i++) {
			totalHeight += lineList.get(i).getLineHeight();
		}
		// 2.再加上所有行之间的垂直间距
		totalHeight += verticalSpacing * (lineList.size() - 1);
		// 3.最后再加上上下的padding值
		totalHeight += getPaddingTop() + getPaddingBottom();
		
		//将计算出来的宽高设置给当前的FlowLayout
		setMeasuredDimension(totalWidth, totalHeight);
	}
	
	/**
	 * 设置view的摆放位置
	 * 将所有line的所有view对象放到对应的位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingTop = getPaddingTop();
		//获取所有的行对象
		for(int i= 0;i<lineList.size();i++) {
			Line line = lineList.get(i);
			
			// 设置每行所处的位置
			if (i > 0) {
				paddingTop += lineList.get(i-1).getLineHeight() + verticalSpacing;
			}
			//获取每一行中所有对象的集合
			ArrayList<View> viewList = line.getViewList();
			
			// a.获取留白区间
			int remainSpacing = getRemainSpacing(line);
			// b.计算该行的每个子view应分得的留白区间的大小
			int perSpacing = remainSpacing / viewList.size();
			
			for (int j = 0; j < viewList.size(); j++) {
				//获取当前行的第 j 个子view
				View view = viewList.get(j);
				// c.将perSpacing添加到每个子view的宽度上
				//重新设置新的测量规则,当模式设置为MeasureSpec.EXACTLY精确的时,测量时会使用你设置的宽高值
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec(view.getMeasuredWidth() + perSpacing, MeasureSpec.EXACTLY);
				//重新测量一下view,widthMeasureSpec:使用上面设置的测量规则;0:表示默认的测量方式
				view.measure(widthMeasureSpec, 0);
				
				
				//摆放view
				if (j == 0) {
					//如果摆放的是第一个子view
					view.layout(getPaddingLeft(), paddingTop, getPaddingLeft() + view.getMeasuredWidth(),
							paddingTop + view.getMeasuredHeight());
				}else {
					//如果摆放的不是第一个子view,摆放后面的view对象，需要参考前一个view对象
					View preView = viewList.get(j - 1);
					int left = preView.getRight() + horizontalSpacing;
					view.layout(left, preView.getTop(), left + view.getMeasuredWidth(), preView.getBottom());
					
				}
			}
		}
		
	}

	/**
	 * 获取留白区间的方法
	 * 因为onLayout方法在onMeasure方法之后执行,所以可以直接使用
	 * this.getMeasuredWidth方法来获取当前控件的宽度
	 */
	public int getRemainSpacing(Line line) {
		return getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getLineWidth();
	}
	
	/**
	 * @Description: 定义一个类用来封装一行的数据,包括该行的所有字view,行宽和行高
	 * @author Free
	 * @date 2016-7-2 下午9:16:52
	 */
	class Line {
		private ArrayList<View> viewList; //用来存放该行的所有view对象
		private int width; //表示所有view对象的宽 + 水平间距
		private int height; //行的高度,在摆放的时候使用
		
		public Line() {
			viewList = new ArrayList<View>();
		}
		
		/**
		 * 添加view对象的方法
		 */
		public void addLineView(View view) {
			if (!viewList.contains(view)) {
				viewList.add(view);
				
				//更新当前行的宽
				if (viewList.size() == 1) {
					//说明当前的view是第一个view对象，
					width = view.getMeasuredWidth();
				}else {
					//应该在当前width的基础上 + 水平间距 + view的宽
					width += horizontalSpacing + view.getMeasuredWidth();
				}
				
				//更新高度,行的高度就是View的高度
				height = view.getMeasuredHeight();
				
			}
		}
		
		/**
		 * 获取行的宽
		 * @return
		 */
		public int getLineWidth(){
			return width;
		}
		/**
		 * 获取行的高
		 * @return
		 */
		public int getLineHeight(){
			return height;
		}
		
		/**
		 * 获取行的view集合
		 * @return
		 */
		public ArrayList<View> getViewList(){
			return viewList;
		}
		
	}

}
