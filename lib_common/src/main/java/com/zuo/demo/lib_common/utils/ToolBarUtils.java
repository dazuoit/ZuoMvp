package com.zuo.demo.lib_common.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zuo.demo.lib_common.R;
import com.zuo.demo.lib_common.glide.MyGlide;

/**
 * @author zuo
 * @filename: ToolBarUtils
 * @date: 2020/4/20
 * @description: title设置
 * @version: 1.0
 */
public class ToolBarUtils {
	public static void initTitle(RelativeLayout mToolbar, String title) {
		if (EmptyUtils.isEmpty(mToolbar) || EmptyUtils.isEmpty(title)) {
			return;
		}
		TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(title);
	}

	public static void initTitle(RelativeLayout mToolbar, String title, View.OnClickListener onClickListener) {
		if (EmptyUtils.isEmpty(mToolbar) || EmptyUtils.isEmpty(title)) {
			return;
		}
		RelativeLayout mRlback = mToolbar.findViewById(R.id.rl_back);
		TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(title);
		if (onClickListener != null) {
			mRlback.setVisibility(View.VISIBLE);
			mRlback.setOnClickListener(onClickListener);
		} else {
			mRlback.setVisibility(View.GONE);
		}
	}

	public static void initTitleRightTv(RelativeLayout mToolbar,String subTitle,View.OnClickListener onClickListener){
		if (EmptyUtils.isEmpty(mToolbar) || EmptyUtils.isEmpty(subTitle)) {
			return;
		}
		ImageView ivRight = mToolbar.findViewById(R.id.iv_right);
		ivRight.setVisibility(View.GONE);
		TextView tvRight = mToolbar.findViewById(R.id.tv_right);
		tvRight.setText(subTitle);
		if (onClickListener != null){
			tvRight.setOnClickListener(onClickListener);
		}
	}

	public static void initTitleRightIv(RelativeLayout mToolbar,Object image,View.OnClickListener onClickListener){
		if (EmptyUtils.isEmpty(mToolbar) || EmptyUtils.isEmpty(image)) {
			return;
		}
		TextView tvRight = mToolbar.findViewById(R.id.tv_right);
		tvRight.setVisibility(View.GONE);
		ImageView ivRight = mToolbar.findViewById(R.id.iv_right);
		MyGlide.disPlay(mToolbar.getContext(),image,ivRight);
		if (onClickListener != null){
			ivRight.setOnClickListener(onClickListener);
		}
	}
}
