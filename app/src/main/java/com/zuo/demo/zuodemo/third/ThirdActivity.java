package com.zuo.demo.zuodemo.third;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zhihu.matisse.MimeType;
import com.zuo.demo.lib_common.base.activity.BaseActivity;
import com.zuo.demo.lib_common.glide.MyGlide;
import com.zuo.demo.lib_common.model.ZuoGobal;
import com.zuo.demo.lib_common.utils.ToolBarUtils;
import com.zuo.demo.zuodemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.shinichi.library.glide.ImageLoader;

public class ThirdActivity extends BaseActivity<ThirdView, ThirdPresanter> implements ThirdView {
	@BindView(R.id.tv_click)
	TextView tvClick;
	@BindView(R.id.iv_1)
	ImageView iv1;
	@BindView(R.id.iv_2)
	ImageView iv2;
	@BindView(R.id.iv_3)
	ImageView iv3;
	@BindView(R.id.iv_4)
	ImageView iv4;
	@BindView(R.id.iv_5)
	ImageView iv5;
	@BindView(R.id.tv_click_1)
	TextView tvClick1;

	@NonNull
	@Override
	public ThirdPresanter createPresenter() {
		return new ThirdPresanter(this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initView() {
		ToolBarUtils.initTitle(mToolbar, "图片展示", v -> onBackPressed());
	}

	@Override
	protected void initData() {
		String s = "http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/09/1fe6fe06f26e0989c806ce8ca556bfcb.jpg";
		MyGlide.disPlay(this, s, iv1);
		MyGlide.disPlayCircle(this, s, iv2);
		MyGlide.disPlayRoundedCorners(this, s, iv3, 20);
		MyGlide.disPlayPlace(this, s, iv4);
		MyGlide.disPlayPlace(this, s + 1, iv5);
	}

	@Override
	protected void rxPermissionsSuccess(String tip, String... permissions) {
		if (ZuoGobal.XPERMINSSION_TIP_CAMERA_AND_WRITE_EXTERNAL_STORAGE.equals(tip)) {
			matisseSelect(MimeType.ofImage(), 1);
		} else if (ZuoGobal.XPERMINSSION_TIP_WRITE_EXTERNAL_STORAGE.equals(tip)) {
			ArrayList<String> list = new ArrayList<>();
			String s = "http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/09/1fe6fe06f26e0989c806ce8ca556bfcb.jpg";
			String s1 = "http://rightinhome.oss-cn-hangzhou.aliyuncs.com/TestObjectFiles/headimages/201910/25/5db25bc8eeef4.jpeg";
			String s2 = "http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2018/08/23/383925da2b1bf8b5e20a459caad1b18d.jpg";
			String s3 = "http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2019/03/29/94dee22916e92382da62aed516be608f.png";
			String s4 = "http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/09/9569f132b142fb842dddd84223ef4f4b.jpg";

			list.add(s);
			list.add(s1);
			list.add(s2);
			list.add(s3);
			list.add(s4);
			presenter.imagePreviewLoader(this, list.size() - 1, list);
		}
	}

	@Override
	public void onScanSuccess(String result) {
		tvClick.setText(result);
	}

	@Override
	public void onMatisseSelectResult(List urls) {
		if (urls != null && urls.size() == 1) {
			MyGlide.disPlay(this, urls.get(0), iv1);
		}
	}

	@OnClick({R.id.tv_click,R.id.tv_click_1, R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.tv_click:
				ToastUtils.showShort("选择相片");
				rxPermissions(ZuoGobal.XPERMINSSION_TIP_CAMERA_AND_WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
				break;
			case R.id.tv_click_1:
				ToastUtils.showShort("选择相片");
				//图片展示
				rxPermissions(ZuoGobal.XPERMINSSION_TIP_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
				break;
			case R.id.iv_1:

				break;
			case R.id.iv_2:
				break;
			case R.id.iv_3:
				break;
			case R.id.iv_4:
				break;
			case R.id.iv_5:

				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoader.cleanDiskCache(this);
	}

}
