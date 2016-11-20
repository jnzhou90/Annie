package com.annie.googleplay.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.annie.googleplay.bean.CategoryBean;
import com.annie.googleplay.bean.ChildCategory;
import com.annie.googleplay.holder.BaseHolder;
import com.annie.googleplay.holder.CategoryHolder;
import com.annie.googleplay.holder.ChildCategoryHolder;
import com.annie.googleplay.holder.TitleCategoryHolder;

public class CategoryAdapter extends BasicAdapter {

	
	public CategoryAdapter(List list) {
		super(list);
	}

	//定义条目类型
	public final int ITEM_TITLE = 0;  //标题类型的条目
	public final int ITEM_CHILD = 1; //子分类类型的条目
	
	// 获取返回的条目类型的数量
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	// 获取指定位置的条目是什么类型的
	@Override
	public int getItemViewType(int position) {
		//获取集合中指定位置的数据
		Object data = list.get(position);
		//如果的data的数据类型是String的话,说明它是大标题类型的条目
		if (data instanceof String) {
			return ITEM_TITLE;
		}else {
			//因为只有两种数据类型,所以else就是子分类类型
			return ITEM_CHILD;
		}
		//return super.getItemViewType(position);
	}
	
	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 1.获取条目类型
		int itemViewType = getItemViewType(position);
		// 2.根据不同的条目类型,去加载不同的view
		switch (itemViewType) {
		case ITEM_TITLE:
			// 加载大标题条目类型的view
			if (convertView == null) {
				//convertView = View.inflate(context, resource, root);
			}
			//设置title的数据
			
			break;
		case ITEM_CHILD:
			//加载子分类条目类型的view
			if (convertView == null) {
				//convertView = View.inflate(context, resource, root);
			}
			//设置子分类的数据
			
			break;
		}
		return convertView;
	}*/
	
	@Override
	protected BaseHolder getHolder(int position) {
		BaseHolder<Object> holder = null;
		// 1.获取条目类型
		int itemViewType = getItemViewType(position);
		// 2.根据不同的条目类型,去加载不同的view
		switch (itemViewType) {
		case ITEM_TITLE:
			// 根据条目类型返回相对应的holder,让它帮我们完成布局的加载和数据的绑定
			holder = new TitleCategoryHolder();

			break;
		case ITEM_CHILD:
			// 根据条目类型返回相对应的holder,让它帮我们完成布局的加载和数据的绑定
			holder = new ChildCategoryHolder();

			break;
		}
		//return holder;
		return holder;
	}

}
