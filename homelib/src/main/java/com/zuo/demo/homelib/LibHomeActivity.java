package com.zuo.demo.homelib;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.zuo.demo.lib_common.arouter.ArouterConstans;
import com.zuo.demo.lib_common.arouter.ArouterKeyConstans;
import com.zuo.demo.lib_common.base.activity.BaseActivity;
import com.zuo.demo.lib_common.utils.ToolBarUtils;

import java.io.Serializable;


/**
 * @author zuo
 * @filename: LibHomeActivity
 * @date: 2020/4/22
 * @description: 描述
 * @version: 版本号
 */
@Route(path = ArouterConstans.HomeActivity_homelib)
public class LibHomeActivity extends BaseActivity implements View.OnClickListener {
	@Autowired(name = ArouterKeyConstans.NUMBER)
	public long my_num;

	@Autowired(name = ArouterKeyConstans.NAME)
	public Object news;
	/**
	 * 打开app首页
	 */
	private TextView mTvClickOn;


	@Override
	public int getLayoutId() {
		return R.layout.activity_lib_home;
	}

	@Override
	protected void initView() {
		ToolBarUtils.initTitle(mToolbar, "依赖数据", v -> onBackPressed());
		LogUtils.w("ARouter1", news);
		mTvClickOn = findViewById(R.id.tv_click_on);
		mTvClickOn.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lib_home);
		initView();
	}


	public void goSecond() {
		ARouter.getInstance().build(ArouterConstans.SecondActivity_MODEL)
				.withLong(ArouterKeyConstans.NUMBER, 666L)
				.withSerializable(ArouterKeyConstans.NAME, (Serializable) news)
				.navigation();

	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.tv_click_on) {

		}
	}
}
