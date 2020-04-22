package com.zuo.demo.lib_common.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.RxLifecycle;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.RxLifecycleAndroid;
import com.zhihu.matisse.MimeType;
import com.zuo.demo.lib_common.R;
import com.zuo.demo.lib_common.activity.MineCaptureActivity;
import com.zuo.demo.lib_common.base.present.BasePresenter;
import com.zuo.demo.lib_common.base.view.BaseView;
import com.zuo.demo.lib_common.model.Constans;
import com.zuo.demo.lib_common.model.ZuoGobal;
import com.zuo.demo.lib_common.utils.ActivityManagerUtils;
import com.zuo.demo.lib_common.utils.EmptyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author zuo
 * @filename: BaseActivity
 * @date: 2020/4/13
 * @description: 基类
 * @version: 1.0
 */
public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends MvpActivity<V, P> implements BaseView, LifecycleProvider<ActivityEvent>, CustomAdapt {

	private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
	protected Context mContext;// 上下文
	protected BaseActivity eventTag; // eventbus tag
	final RxPermissions rxPermissions = new RxPermissions(this);
	public View mRootView;
	ViewStub viewStubContent;
	ViewStub viewStubNodata;
	ViewStub viewNetStubError;
	ViewStub viewDataStubError;
	private TextView tvErrorNetData;
	private TextView tvErrorDataData;
	private TextView mNoDataView;
	protected RefreshLayout mRefreshLayout;
	protected RelativeLayout mToolbar;

	@NonNull
	@Override
	public P createPresenter() {
		return (P) new BasePresenter<>(this);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 调用 inject 方法，如果传递过来的参数含有，这样使用 @Autowired 的会自动解析
		ARouter.getInstance().inject(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		lifecycleSubject.onNext(ActivityEvent.CREATE);
		mContext = this;
		setContentView(R.layout.activity_root);
		initRootView();
		mRootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
		if (EmptyUtils.isNotEmpty(mRootView)) {
			mRootView.setFitsSystemWindows(true);
		}
		ButterKnife.bind(this);
		initView();
		initData();
		initListener();
		ActivityManagerUtils.getInstance().addActivity(this);
	}

	protected void initRootView() {
		mToolbar = findViewById(R.id.toolbar_root);
		mRefreshLayout = findViewById(R.id.srl_layout);
		mRefreshLayout.setEnableRefresh(setEnableRefresh());
		mRefreshLayout.setEnableLoadMore(setEnableLoadMore());
		viewStubContent = findViewById(R.id.view_stub_content);
		viewStubNodata = findViewById(R.id.view_stub_nodata);
		viewNetStubError = findViewById(R.id.view_stub_error_net);
		viewDataStubError = findViewById(R.id.view_stub_error_data);
		viewStubContent.setLayoutResource(getLayoutId());
		viewStubContent.inflate();
	}

	public boolean setEnableRefresh() {
		return false;
	}

	public boolean setEnableLoadMore() {
		return false;
	}

	/**
	 * 布局
	 *
	 * @return 布局id
	 */
	public abstract int getLayoutId();

	protected abstract void initView();

	protected void initData() {
	}


	protected void initListener() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		lifecycleSubject.onNext(ActivityEvent.RESUME);
	}

	@Override
	protected void onStart() {
		super.onStart();
		lifecycleSubject.onNext(ActivityEvent.START);
	}

	@Override
	protected void onPause() {
		lifecycleSubject.onNext(ActivityEvent.PAUSE);
		super.onPause();
		KeyboardUtils.hideSoftInput(this);
	}

	@Override
	protected void onStop() {
		lifecycleSubject.onNext(ActivityEvent.STOP);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		lifecycleSubject.onNext(ActivityEvent.DESTROY);
		if (EmptyUtils.isNotEmpty(eventTag) && isEventBusRegistered(eventTag)) {
			unregisterEventBus(eventTag);
		}
		hideLoadView();
		super.onDestroy();
		ActivityManagerUtils.getInstance().finishActivity(this);
	}


	//显示加载的View
	@Override
	public TipDialog showLoadView(String tips) {
		return WaitDialog.show(this, EmptyUtils.isEmpty(tips) ? "载入中..." : tips).setCancelable(true);
	}

	//隐藏加载的View
	@Override
	public void hideLoadView() {
		WaitDialog.dismiss();
	}

	@Override
	public void showErrorDataView() {
		showDataErrView(true);
	}

	@Override
	public void hideErrorDataView() {
		showDataErrView(false);
	}

	@Override
	public void showMsg(String msg) {
		ToastUtils.showShort(msg);
	}

	@Override
	public void showMsg(int resId) {
		ToastUtils.showShort(resId);
	}

	@Override
	public boolean checkNetWork() {
		if (!NetworkUtils.isConnected()) {
			onComplete(false);
		}
		showNetWorkErrView(!NetworkUtils.isConnected());
		return NetworkUtils.isConnected();
	}

	@Override
	public boolean checkToken() {
		return false;
	}

	@Override
	public void showNoDataView() {
		showNoDataView(true);
	}

	@Override
	public void hideNoDataView() {
		showNoDataView(false);
	}

	@Override
	public Observable<ActivityEvent> lifecycle() {
		return lifecycleSubject.hide();
	}

	@Override
	public void onComplete(boolean success) {
		if (mRefreshLayout.getState() == RefreshState.Refreshing) {
			mRefreshLayout.finishRefresh(success);
		} else if (mRefreshLayout.getState() == RefreshState.Loading) {
			mRefreshLayout.finishLoadMore(success);
		}
	}

	@Override
	@NonNull
	@CheckResult
	public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
		return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
	}

	@Override
	@NonNull
	@CheckResult
	public final <T> LifecycleTransformer<T> bindToLifecycle() {
		return RxLifecycleAndroid.bindActivity(lifecycleSubject);
	}

	@Override
	public boolean isBaseOnWidth() {
		return false;
	}

	@Override
	public float getSizeInDp() {
		return ZuoGobal.AUTO_SIZE_IN_DP;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/**
		 * 处理二维码扫描结果
		 */
		presenter.onActivityResult(requestCode, resultCode, data, this);

	}

	/**
	 * 扫描失败
	 */
	@Override
	public void onScanFailed() {

	}

	/**
	 * 扫描成功
	 *
	 * @param result
	 */
	@Override
	public void onScanSuccess(String result) {

	}

	/**
	 * 选择图片的结果
	 *
	 * @param urls
	 */
	@Override
	public void onMatisseSelectResult(List<String> urls) {
	}

	/**
	 * 设置eventbus
	 *
	 * @param eventTag
	 */
	protected void setEventTag(BaseActivity eventTag) {
		if (EmptyUtils.isNotEmpty(eventTag)) {
			this.eventTag = eventTag;
			registerEventBus(eventTag);
		}
	}

	/**
	 * 订阅EventBus
	 *
	 * @param subscribe 订阅
	 */
	protected void registerEventBus(Object subscribe) {
		if (!isEventBusRegistered(subscribe)) {
			EventBus.getDefault().register(subscribe);
		}
	}

	/**
	 * 是否订阅EventBus
	 *
	 * @param subscribe 订阅
	 * @return boolean
	 */
	protected boolean isEventBusRegistered(Object subscribe) {
		return EventBus.getDefault().isRegistered(subscribe);
	}

	/**
	 * 取消订阅EventBus
	 *
	 * @param subscribe
	 */
	protected void unregisterEventBus(Object subscribe) {
		if (isEventBusRegistered(subscribe)) {
			EventBus.getDefault().unregister(subscribe);
		}
	}

	/**
	 * 权限申请
	 *
	 * @param tip
	 * @param permissions
	 */
	public void rxPermissions(String tip, String... permissions) {
		if (EmptyUtils.isEmpty(permissions)) {
			LogUtils.w("rxPermissions", "Permissions is empty!!");
			return;
		}
		rxPermissions.request(permissions)
				.subscribe(granted -> {
					if (granted) {
						rxPermissionsSuccess(tip, permissions);
					} else {
						if (EmptyUtils.isNotEmpty(tip)) {
							ToastUtils.showShort("需要" + tip + "权限");
						}
						PermissionUtils.launchAppDetailsSettings();
					}
				});
	}

	protected void rxPermissionsSuccess(String tip, String... permissions) {
	}


	/**
	 * 开始扫码
	 */
	public void startScan() {
		Intent intent = new Intent(this, MineCaptureActivity.class);
		startActivityForResult(intent, Constans.REQUEST_SCAN);
	}

	/**
	 * 设置沉浸式状态栏
	 */
	public void transparentStatusBar() {
		View decView = findViewById(android.R.id.content);
		if (EmptyUtils.isNotEmpty(decView)) {
			if (decView instanceof ViewGroup) {
				((ViewGroup) decView).getChildAt(0).setFitsSystemWindows(false);
			}
		}
		BarUtils.transparentStatusBar(this);
	}


	/**
	 * 选择图片扫码
	 */
	protected void selectImageScan() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, Constans.REQUEST_IMAGE);
	}

	/**
	 * 选择图片
	 *
	 * @param mimeTypes MimeType.ofImage() ofVideo
	 * @param maxSelect
	 */
	protected void matisseSelect(Set<MimeType> mimeTypes, int maxSelect) {
		presenter.matisseSelect(mimeTypes, maxSelect, this);
	}

	protected void showNetWorkErrView(boolean show) {
		if (tvErrorNetData == null) {
			View view = viewNetStubError.inflate();
			tvErrorNetData = view.findViewById(R.id.tv_error_data);
			tvErrorNetData.setOnClickListener(v -> {
				if (checkNetWork()) {
					initData();
				}
			});
		}
		viewNetStubError.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected void showDataErrView(boolean show) {
		if (tvErrorDataData == null) {
			View view = viewDataStubError.inflate();
			tvErrorDataData = view.findViewById(R.id.tv_error_data);
			tvErrorDataData.setOnClickListener(v -> {
				viewDataStubError.setVisibility(View.GONE);
				initData();
			});
		}
		viewDataStubError.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected void showNoDataView(boolean show) {
		if (mNoDataView == null) {
			View view = viewStubNodata.inflate();
			mNoDataView = view.findViewById(R.id.tv_empty_data);
		}
		viewStubNodata.setVisibility(show ? View.VISIBLE : View.GONE);
	}



}
