package com.annie.googleplay.holder;

import java.net.URL;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.SubjectBean;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectHolder extends BaseHolder<SubjectBean> {

	ImageView mIcon;
	TextView mText;
	@Override
	public View initView() {
		View view = View.inflate(GooglePlayApplication.context, R.layout.subjectpage_layout, null);
		mIcon = (ImageView) view.findViewById(R.id.subject_icon);
		mText = (TextView) view.findViewById(R.id.subject_text);
		return view;
	}

	@Override
	public void setBindData(SubjectBean subject) {
		mText.setText(subject.getDes());
		
		//展示图片
		ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + subject.getUrl(), mIcon, ImageloaderOptions.options);
	} 
}
