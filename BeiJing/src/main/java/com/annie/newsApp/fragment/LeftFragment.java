package com.annie.newsApp.fragment;

import java.util.List;

import com.annie.newsApp.MainUIActivity;
import com.annie.newsApp.R;
import com.annie.newsApp.base.BaseFragment;
import com.annie.newsApp.been.NewsCenterBeen;
import com.annie.newsApp.been.NewsCenterBeen.leftMenu;

import android.R.anim;
import android.R.integer;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftFragment extends BaseFragment implements OnItemClickListener {

	private List<leftMenu> data;
	private ListView listView;
	private int currentIndex;
	private MyAdapter maAdapter;

	@Override
	protected View initView() {
		listView = new ListView(mActivity);
		// 设置listview的背景
		listView.setBackgroundColor(Color.BLACK);
		// 去掉listview的点中效果
		listView.setSelector(android.R.color.transparent);
		// 去掉listview的间隔线
		listView.setDividerHeight(0);
		// 将listview的内容向下移动
		listView.setPadding(0, 40, 0, 0);

		// 设置listview的点击事件
		listView.setOnItemClickListener(this);
		return listView;
	}

	/**
	 * @Description:获取从新闻中心传来的数据
	 * @param:
	 */
	public void getNewsTag(List<leftMenu> data) {
		this.data = data;

		maAdapter = new MyAdapter();
		listView.setAdapter(maAdapter);
	}

	// 为listview设置适配器!!!
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) View.inflate(getActivity(),
					R.layout.left_menu_items, null);
			view.setText(data.get(position).title);

			// 判断当前条目是否被点击,被点击-->红色 否则-->白色
			if (currentIndex == position) {
				// 被点击了
				view.setEnabled(true);
			} else {
				// 当前条目未被点击
				view.setEnabled(false);
			}

			// view.setEnabled(position==currentIndex);
			return view;
		}

	}

	// 设置listview的点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 记下当前点击的条目位置
		currentIndex = position;
		// 刷新listview
		maAdapter.notifyDataSetChanged();

		// 点击条目后自动关闭左侧菜单
		MainUIActivity mainUIActivity = (MainUIActivity) mActivity;
		mainUIActivity.getSlidingMenu().toggle();

		// 关闭左侧菜单的同时需要将点击的条目信息传给新闻中心界面!!!!!!!!!
		mainUIActivity.getContentFragment().getNewsCenterPager()
				.switchPager(position);
	}

}
