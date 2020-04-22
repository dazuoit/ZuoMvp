package com.zuo.demo.lib_common.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zuo.demo.lib_common.R;
import com.zuo.demo.lib_common.base.activity.BaseActivity;

/**
 * @author zuo
 * @filename: MineCaptureActivity
 * @date: 2020/4/13
 * @description: 扫码基类
 * @version: 1.0
 */
public class MineCaptureActivity extends BaseActivity {

	@Override
	public int getLayoutId() {
		return R.layout.layout_camera;
	}

	@Override
	protected void initView() {
		mToolbar.setVisibility(View.GONE);
		CaptureFragment captureFragment = new CaptureFragment();
		captureFragment.setAnalyzeCallback(analyzeCallback);
		getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
		findViewById(R.id.tv_select_image).setOnClickListener(v -> {
			selectImageScan();
		});
		findViewById(R.id.tv_back).setOnClickListener(v -> onBackPressed());
	}

	@Override
	protected void initData() {

	}

	/**
	 * 二维码解析回调函数
	 */
	CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
		@Override
		public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
			scanResult(result, CodeUtils.RESULT_SUCCESS);
		}

		@Override
		public void onAnalyzeFailed() {
			scanResult("", CodeUtils.RESULT_FAILED);
		}
	};

	private void scanResult(String result, int type) {
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt(CodeUtils.RESULT_TYPE, type);
		bundle.putString(CodeUtils.RESULT_STRING, result);
		resultIntent.putExtras(bundle);
		MineCaptureActivity.this.setResult(RESULT_OK, resultIntent);
		MineCaptureActivity.this.finish();
	}

	@Override
	public void onScanSuccess(String result) {
		scanResult(result, CodeUtils.RESULT_SUCCESS);
	}

	@Override
	public void onScanFailed() {
		scanResult("", CodeUtils.RESULT_FAILED);
	}
}