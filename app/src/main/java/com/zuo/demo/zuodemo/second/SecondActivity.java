package com.zuo.demo.zuodemo.second;

import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zuo.demo.lib_common.arouter.ArouterConstans;
import com.zuo.demo.lib_common.arouter.ArouterKeyConstans;
import com.zuo.demo.lib_common.base.activity.BaseRecycleViewActivity;
import com.zuo.demo.lib_common.utils.EmptyUtils;
import com.zuo.demo.lib_common.utils.ToolBarUtils;
import com.zuo.demo.zuodemo.third.ThirdActivity;

import net.NewsDetail;

/**
 * @author zuo
 * @filename: SecondActivity
 * @date: 2020/4/20
 * @description: 数据
 * @version: 1.0
 */
@Route(path = ArouterConstans.SecondActivity_MODEL)
public class SecondActivity extends BaseRecycleViewActivity<MineAdpter, SecondView, SecondPresanter> implements SecondView {
	@Autowired(name = ArouterKeyConstans.NUMBER)
	public long my_num;

	@Autowired(name = ArouterKeyConstans.NAME)
	public NewsDetail.ListBean news;

	@NonNull
	@Override
	public SecondPresanter createPresenter() {
		return new SecondPresanter(this);
	}

	@Override
	public void initTitle() {
		// 调用 inject 方法，如果传递过来的参数含有，这样使用 @Autowired 的会自动解析
		ToolBarUtils.initTitle(mToolbar, "数据列表", v -> onBackPressed());
	}

	@Override
	protected MineAdpter createAdapter() {
		return new MineAdpter();
	}

	@Override
	public void loadData(boolean isLoadMore) {
		presenter.getNews(mPage);
	}

	@Override
	public void onNewsReturn(NewsDetail newsDetailRespData) {
		if (EmptyUtils.isNotEmpty(newsDetailRespData)) {
			if (mPage == 1) {
				LogUtils.w("ARouter2",news);
				mAdapter.setNewData(newsDetailRespData.getList());
				mAdapter.addData(news);
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
	public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
		super.onItemClick(baseQuickAdapter, view, i);
		ActivityUtils.startActivity(ThirdActivity.class);
	}
}
