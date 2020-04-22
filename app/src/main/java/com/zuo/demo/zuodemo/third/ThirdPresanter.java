package com.zuo.demo.zuodemo.third;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.zuo.demo.lib_common.base.present.BasePresenter;
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
public class ThirdPresanter extends BasePresenter<ThirdView> {
	public ThirdPresanter(LifecycleProvider provider) {
		super(provider);
	}

	public void getThis(){
		forNet(NetService.getInstance().getCommonService().login(getNetJson().set_token().builder()), new MineObserver<NewsDetail>(mMvpView) {
			@Override
			public void onNext(RespData<NewsDetail> t) {

			}
		});
	}
}
