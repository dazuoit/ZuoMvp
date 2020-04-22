package com.zuo.demo.lib_common.base.view;
import android.support.annotation.StringRes;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.kongzue.dialog.v3.TipDialog;

import java.util.List;


/**
 * @author zuo
 * @filename: BaseView
 * @date: 2020/4/13
 * @description: 基类BaseView
 * @version: 1.0
 */
public interface BaseView extends MvpView {
	//显示加载的View
	TipDialog showLoadView(String tips);
	//隐藏加载的View
	void hideLoadView();

	void showMsg(String msg);

	void showMsg(@StringRes int resId);

	boolean checkNetWork();

	boolean checkToken();

	//显示无数据View
	void showNoDataView();
	//隐藏无数据View
	void hideNoDataView();

	void onComplete(boolean success);

	//显示错误数据数据View
	void showErrorDataView();

	void hideErrorDataView();

	void onScanSuccess(String result);

	void onScanFailed();

	void onMatisseSelectResult(List<String> urls);

}
