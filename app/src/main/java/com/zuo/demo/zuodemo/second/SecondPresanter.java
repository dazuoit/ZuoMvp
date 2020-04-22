package com.zuo.demo.zuodemo.second;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.zuo.demo.lib_common.base.present.BaseRecycleViewPresent;
import com.zuo.demo.lib_common.net.MineObserver;

import net.NetService;
import net.NewsDetail;
import com.zuo.demo.lib_common.net.RespData;

/**
 * @author zuo
 * @filename: ThirdPresanter
 * @date: 2020/4/16
 * @description: 描述
 * @version: 版本号
 */
public class SecondPresanter extends BaseRecycleViewPresent<SecondView> {
	public SecondPresanter(LifecycleProvider provider) {
		super(provider);
	}

	public void getNews(int page) {
		forNet(NetService.getInstance().getCommonService().login(getNetJson().set_token().set_object("status","0").set_object("pageIndex", page).builder()), new MineObserver<NewsDetail>(mMvpView) {
			@Override
			public void onNext(RespData<NewsDetail> t) {
				super.onNext(t);
				mMvpView.onNewsReturn(t.data);
			}
		});
	}
}
