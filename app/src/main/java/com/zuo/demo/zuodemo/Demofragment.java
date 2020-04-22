package com.zuo.demo.zuodemo;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.zuo.demo.lib_common.arouter.ArouterConstans;
import com.zuo.demo.lib_common.arouter.ArouterKeyConstans;
import com.zuo.demo.lib_common.base.fragment.BaseRecycleViewFragment;
import com.zuo.demo.lib_common.model.ZuoGobal;
import com.zuo.demo.lib_common.utils.EmptyUtils;
import com.zuo.demo.lib_common.utils.ToolBarUtils;

import net.NewsDetail;

import com.zuo.demo.zuodemo.second.MineAdpter;
import com.zuo.demo.zuodemo.second.SecondActivity;
import com.zuo.demo.zuodemo.second.SecondPresanter;
import com.zuo.demo.zuodemo.second.SecondView;

/**
 * @author zuo
 * @filename: Demofragment
 * @date: 2020/4/21
 * @description: 描述
 * @version: 版本号
 */
public class Demofragment extends BaseRecycleViewFragment<MineAdpter, SecondView, SecondPresanter> implements SecondView, BaseQuickAdapter.OnItemClickListener {
	public static Demofragment newInstance(String title) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		Demofragment fragment = new Demofragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@NonNull
	@Override
	public SecondPresanter createPresenter() {
		return new SecondPresanter(this);
	}

	@Override
	protected MineAdpter createAdapter() {
		return new MineAdpter();
	}

	@Override
	public void initTitle() {
		Bundle arguments = getArguments();
		if (arguments != null) {
			String mTitle = arguments.getString("title");
			ToolBarUtils.initTitle(mToolbar, mTitle, null);
			if ("微信".equals(mTitle)) {
				ToolBarUtils.initTitleRightTv(mToolbar, "扫一扫", v ->
						rxPermissions(ZuoGobal.XPERMINSSION_TIP_CAMERA_AND_WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE));
			}
		}
	}

	@Override
	public void loadData(boolean isLoadMore) {
		presenter.getNews(mPage);
	}


	@Override
	public void onNewsReturn(NewsDetail newsDetailRespData) {
		if (EmptyUtils.isNotEmpty(newsDetailRespData)) {
			if (mPage == 1) {
				mAdapter.setNewData(newsDetailRespData.getList());
			} else {
				mAdapter.addData(newsDetailRespData.getList());
			}
			if ("1".equals(newsDetailRespData.getIs_last_page())) {
				noMoreDataShowNoDataView();
			}
			mPage++;
		} else {
			noMoreDataShowNoDataView();
		}
	}

	@Override
	public void onScanSuccess(String result) {
		ToastUtils.showShort(result);
	}


	@Override
	protected void rxPermissionsSuccess(String tip, String... permissions) {
		if (ZuoGobal.XPERMINSSION_TIP_CAMERA_AND_WRITE_EXTERNAL_STORAGE.equals(tip)) {
			startScan();
		}
	}

	@Override
	public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
		ARouter.getInstance().build(ArouterConstans.SecondActivity_MODEL)
				.withLong(ArouterKeyConstans.NUMBER, 666L)
				.withSerializable(ArouterKeyConstans.NAME, mAdapter.getData().get(i))
				.navigation();
	}
}
