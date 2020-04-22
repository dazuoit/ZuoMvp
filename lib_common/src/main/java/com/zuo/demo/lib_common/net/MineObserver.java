package com.zuo.demo.lib_common.net;

import com.zuo.demo.lib_common.base.view.BaseView;
import com.zuo.demo.lib_common.utils.EmptyUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author zuo
 * @filename: MineObserver
 * @date: 2020/4/20
 * @description: 自定义回调
 * @version: 1.0
 */
public abstract class MineObserver<T> implements Observer<RespData<T>> {
	BaseView mView;
	public MineObserver(BaseView view) {
		this.mView = view;
	}

	@Override
	public void onSubscribe(Disposable d) {
		if (mView != null){
			mView.showLoadView(null);
			mView.hideErrorDataView();
		}

	}

	@Override
	public void onNext(RespData<T> t) {
		if (mView != null) {
			mView.onComplete(true);
			if (EmptyUtils.isEmpty(t)) {
				mView.showErrorDataView();
			}
		}
	}

	@Override
	public void onError(Throwable e) {
		if (mView != null){
			mView.onComplete(false);
			mView.showErrorDataView();
			mView.hideLoadView();
		}
	}

	@Override
	public void onComplete() {
		if (mView != null){
			mView.hideLoadView();
		}
	}
}
