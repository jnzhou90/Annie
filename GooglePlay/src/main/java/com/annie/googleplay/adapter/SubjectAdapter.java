package com.annie.googleplay.adapter;

import java.util.List;

import com.annie.googleplay.bean.SubjectBean;
import com.annie.googleplay.holder.BaseHolder;
import com.annie.googleplay.holder.SubjectHolder;

public class SubjectAdapter extends BasicAdapter<SubjectBean> {

	public SubjectAdapter(List list) {
		super(list);
	}

	@Override
	protected BaseHolder getHolder(int position) {
		return new SubjectHolder();
	}

}
