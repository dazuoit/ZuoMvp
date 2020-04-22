package com.zuo.demo.lib_common.base.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


/**
 * @author zuo
 * @filename: BaseRecycleAdaper
 * @date: 2020/4/21
 * @description: 适配器
 * @version: 1.0
 */
public abstract class BaseRecycleAdaper<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

	public BaseRecycleAdaper(@LayoutRes int  layoutRes) {
		super(layoutRes);
	}
	@Override
	protected  abstract void convert(K helper, T item) ;

}
