package com.zuo.demo.lib_common.base.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.zuo.demo.lib_common.base.activity.BaseActivity;
import com.zuo.demo.lib_common.activity.MineCaptureActivity;
import com.zuo.demo.lib_common.base.present.BasePresenter;
import com.zuo.demo.lib_common.base.view.BaseView;
import com.zuo.demo.lib_common.model.Constans;
import com.zuo.demo.lib_common.model.ZuoGobal;
import com.zuo.demo.lib_common.utils.EmptyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author zuo
 * @filename: BaseFragment
 * @date: 2020/4/13
 * @description: fragment基类
 * @version: 1.0
 */
public abstract class BaseFragment<V extends BaseView, P extends BasePresenter<V>> extends LazyLoadFragment<V, P> implements BaseView, LifecycleProvider<ActivityEvent>, CustomAdapt {

	private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
	protected BaseActivity mContext;// 上下文
	protected BaseFragment eventTag; // eventbus tag
	RxPermissions rxPermissions;
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
	private Unbinder unbinder;

	@NonNull
	@Override
	public P createPresenter() {
		return (P) new BasePresenter<>(this);
	}


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mContext = (BaseActivity) getActivity();
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.activity_root, container, false);
			unbinder = ButterKnife.bind(this, mRootView);
			initRootView();
			initView();
			initListener();
		} else {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (parent != null) {
				parent.removeView(mRootView);
			}
		}
		return mRootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (presenter == null) {
			presenter = createPresenter();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lifecycleSubject.onNext(ActivityEvent.CREATE);
	}

	protected void initRootView() {
		mToolbar = mRootView.findViewById(R.id.toolbar_root);
		mRefreshLayout = mRootView.findViewById(R.id.srl_layout);
		mRefreshLayout.setEnableRefresh(setEnableRefresh());
		mRefreshLayout.setEnableLoadMore(setEnableLoadMore());
		viewStubContent = mRootView.findViewById(R.id.view_stub_content);
		viewStubNodata = mRootView.findViewById(R.id.view_stub_nodata);
		viewNetStubError = mRootView.findViewById(R.id.view_stub_error_net);
		viewDataStubError = mRootView.findViewById(R.id.view_stub_error_data);
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

	@Override
	protected void loadData() {
		initData();
	}

	protected void initData() {
	}


	protected void initListener() {
	}

	@Override
	public void onResume() {
		super.onResume();
		lifecycleSubject.onNext(ActivityEvent.RESUME);
	}

	@Override
	public void onStart() {
		super.onStart();
		lifecycleSubject.onNext(ActivityEvent.START);
	}

	@Override
	public void onPause() {
		lifecycleSubject.onNext(ActivityEvent.PAUSE);
		super.onPause();
		KeyboardUtils.hideSoftInput(mContext);
	}

	@Override
	public void onStop() {
		lifecycleSubject.onNext(ActivityEvent.STOP);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		lifecycleSubject.onNext(ActivityEvent.DESTROY);
		if (EmptyUtils.isNotEmpty(eventTag) && isEventBusRegistered(eventTag)) {
			unregisterEventBus(eventTag);
		}
		hideLoadView();
		unbinder.unbind();
		mRootView = null;
		super.onDestroy();
	}


	//显示加载的View
	@Override
	public TipDialog showLoadView(String tips) {
		return WaitDialog.show(mContext, EmptyUtils.isEmpty(tips) ? "载入中..." : tips).setCancelable(true);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/**
		 * 处理二维码扫描结果
		 */
		presenter.onActivityResult(requestCode, resultCode, data, mContext);

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
	protected void setEventTag(BaseFragment eventTag) {
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
		if (rxPermissions == null) {
			rxPermissions = new RxPermissions(this);
		}
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
		Intent intent = new Intent(getActivity(), MineCaptureActivity.class);
		startActivityForResult(intent, Constans.REQUEST_SCAN);
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
		presenter.matisseSelect(mimeTypes, maxSelect, getActivity());
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
