package com.annie.googleplay.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.annie.googleplay.adapter.CategoryAdapter;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.bean.CategoryBean;
import com.annie.googleplay.bean.ChildCategory;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;
import com.google.gson.reflect.TypeToken;

import android.R.anim;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Description: 这里使用多种条目类型的listview来展示
 * @author Free
 * @date 2016-7-1 下午11:05:20
 */
public class CategoryFragment extends BaseFragment {

	private List<CategoryBean> categoryInfos;
	//创建一个集合,将大分类标题和具体的小分类添加到一个集合中,方便展示
	private List<Object> list = new ArrayList<Object>();
	private ListView listView;
	
	//定义一个变量用于标识requestData方法第几次被调用,背面重复调用requestData
	private int times;

	@Override
	public View getSuccessPage() {
		//定义一个变量用于标识requestData方法第几次被调用,背面重复调用requestData
		times = 0;
		
		listView = new ListView(getActivity());
		//去除listview默认的状态选择器的效果
		listView.setSelector(android.R.color.transparent);
		//去掉listview默认的分割线
		listView.setDividerHeight(0);
		
		return listView;
	}

	@Override
	public Object requestData() {
		//标识自增一
		times++;
		if (times >= 2) {
			//清空集合,避免数据被重复加载,产生重复数据
			list.clear();
		}
			//从网络获取数据
			Type type = new TypeToken<List<CategoryBean>>(){}.getType();
			categoryInfos = (List<CategoryBean>) DataEngine.getInstance().loadListData(UrlUtil.category, type);
			//System.out.println(categoryInfos.toString());
			//如果数据不为空,更新UI
			if (categoryInfos != null) {
				//更新UI,遍历获取的集合数据,去除大分类和相应的小分类,添加到另一个集合中
				for ( CategoryBean category : categoryInfos) {
					String title = category.getTitle();
					list.add(title); //将大分类添加到集合
					List<ChildCategory> infos = category.getInfos();
					list.addAll(infos); //将相应的小分类添加到集合
				}
				
					//System.out.println(list.toString());
				//设置adapter
				CommonUtil.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						listView.setAdapter(new CategoryAdapter(list));
					}
				});
			}
		
		return categoryInfos;
	}
}

/**
 * 集合中数据的形态
 * title
 * ChildCategory
 * ChildCategory
 * ChildCategory
 * title
 * ChildCategory
 * ChildCategory
 * ChildCategory
 * .....
 */

