package com.zuo.demo.lib_common.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.zuo.demo.lib_common.base.activity.BaseActivity;
import com.zuo.demo.lib_common.base.fragment.BaseFragment;

import java.util.ArrayList;


/**
 * @author zuo
 * @filename: BaseFragmentAdapter
 * @date: 2020/4/13
 * @description: BaseFragmentAdapter 基类
 * @version: 1.0
 */

public abstract class BaseFragmentAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

	public BaseFragmentAdapter(BaseActivity baseActivity) {
		super(baseActivity.getSupportFragmentManager());
	}

	@Override
	public Fragment getItem(int position) {
		return getFragments().get(position);
	}

	@Override
	public int getCount() {
		return getFragments().size();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	public abstract ArrayList<BaseFragment> getFragments();

}
