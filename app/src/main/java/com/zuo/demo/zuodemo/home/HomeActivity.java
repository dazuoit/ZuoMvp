package com.zuo.demo.zuodemo.home;

import android.Manifest;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.zuo.demo.lib_common.arouter.ArouterConstans;
import com.zuo.demo.lib_common.base.activity.BaseActivity;
import com.zuo.demo.lib_common.base.adapter.BaseFragmentAdapter;
import com.zuo.demo.lib_common.base.fragment.BaseFragment;
import com.zuo.demo.lib_common.model.ZuoGobal;
import com.zuo.demo.zuodemo.Demofragment;
import com.zuo.demo.zuodemo.R;

import java.util.ArrayList;

/**
 * @author zuo
 * @filename: HomeFragment
 * @date: 2020/4/21
 * @description: 描述
 * @version: 版本号
 */
@Route(path = ArouterConstans.HomeActivity_MODEL)
public class HomeActivity extends BaseActivity {
	private AlphaTabsIndicator alphaTabsIndicator;
	private String[] titles = {"微信", "通讯录", "发现", "我"};
	private ViewPager mViewPger;
	private long lastBackPress;

	@Override
	public int getLayoutId() {
		return R.layout.activity_home;
	}

	@Override
	protected void initView() {
		mToolbar.setVisibility(View.GONE);
		mViewPger = findViewById(R.id.mViewPager);
		alphaTabsIndicator = findViewById(R.id.alphaIndicator);
		initFragmentAdapter();

	}

	private void initFragmentAdapter() {
		BaseFragmentAdapter mainAdapter = new BaseFragmentAdapter(this) {
			@Override
			public ArrayList<BaseFragment> getFragments() {
				ArrayList<BaseFragment> list = new ArrayList<>();
				for (int i = 0; i < titles.length; i++) {
					list.add(Demofragment.newInstance(titles[i]));
				}
				return list;
			}

			@Override
			public void onPageSelected(int position) {
				if (0 == position) {
					rxPermissions(ZuoGobal.XPERMINSSION_TIP_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
				} else if(1 == position){
					alphaTabsIndicator.getTabView(0).showNumber(6);
					alphaTabsIndicator.getTabView(1).showNumber(888);
					alphaTabsIndicator.getTabView(2).showNumber(88);
					alphaTabsIndicator.getTabView(3).showPoint();
				}else if (2 == position) {
					alphaTabsIndicator.getCurrentItemView().removeShow();
				} else if (3 == position) {
					alphaTabsIndicator.removeAllBadge();
				}
			}
		};
		mViewPger.setAdapter(mainAdapter);
		mViewPger.addOnPageChangeListener(mainAdapter);
		alphaTabsIndicator.setViewPager(mViewPger);
	}

	@Override
	public void onBackPressed() {
		if (!BackHandlerHelper.handleBackPress(this)) {
			if (System.currentTimeMillis() - lastBackPress < 2000) {
				AppUtils.exitApp();
				super.onBackPressed();
			} else {
				lastBackPress = System.currentTimeMillis();
				ToastUtils.showShort(R.string.exit_app);
			}
		}
	}
}
