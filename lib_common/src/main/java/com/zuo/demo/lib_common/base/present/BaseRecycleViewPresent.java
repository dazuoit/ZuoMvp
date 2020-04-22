package com.zuo.demo.lib_common.base.present;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.zuo.demo.lib_common.base.view.BaseRecycleView;

/**
 * @author zuo
 * @filename: BaseRecycleViewPresent
 * @date: 2020/4/20
 * @description: 基类列表present
 * @version: 1.0
 */
public class BaseRecycleViewPresent<V extends BaseRecycleView> extends BasePresenter<V> {

	public BaseRecycleViewPresent(LifecycleProvider<ActivityEvent> provider) {
		super(provider);
	}
}
