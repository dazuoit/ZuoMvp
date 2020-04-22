package com.zuo.demo.lib_common.base.present;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zuo.demo.lib_common.R;
import com.zuo.demo.lib_common.base.view.BaseView;
import com.zuo.demo.lib_common.glide.MyGlideEngine;
import com.zuo.demo.lib_common.model.Constans;
import com.zuo.demo.lib_common.model.ZuoGobal;
import com.zuo.demo.lib_common.net.MJsonUtils;
import com.zuo.demo.lib_common.net.MineObserver;
import com.zuo.demo.lib_common.net.RetrofitManager;
import com.zuo.demo.lib_common.utils.ImageUtil;

import java.util.List;
import java.util.Set;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.tool.image.DownloadPictureUtil;
import cc.shinichi.library.view.listener.OnOriginProgressListener;
import io.reactivex.Observable;


/**
 * @author zuo
 * @filename: BasePresenter
 * @date: 2020/4/13
 * @description: 基类present
 * @version: 1.0
 */

public class BasePresenter<V extends BaseView> extends MvpBasePresenter<V> {
	private LifecycleProvider<ActivityEvent> provider;
	protected V mMvpView;

	@Override
	public void attachView(V view) {
		super.attachView(view);
		mMvpView = view;
	}

	@Override
	public void detachView() {
		super.detachView();
	}

	public BasePresenter(LifecycleProvider<ActivityEvent> provider) {
		this.provider = provider;
	}

	public LifecycleProvider<ActivityEvent> getProvider() {
		return provider;
	}

	public <T> void forNet(Observable observable, MineObserver<T> observer) {
		if (mMvpView.checkNetWork()) {
			RetrofitManager.getInstance().forNet(observable, observer);
		}
	}

	public MJsonUtils getNetJson() {
		return new MJsonUtils();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
		if (requestCode == Constans.REQUEST_SCAN) {
			//处理扫描结果（在界面上显示）
			if (null != data) {
				Bundle bundle = data.getExtras();
				if (bundle == null) {
					ToastUtils.showShort(R.string.error_scan);
					mMvpView.onScanFailed();
					return;
				}
				if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
					String result = bundle.getString(CodeUtils.RESULT_STRING);
					mMvpView.onScanSuccess(result);
				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
					ToastUtils.showShort(R.string.fail_scan);
					mMvpView.onScanFailed();
				}
			}
		} else if (requestCode == Constans.REQUEST_IMAGE) {
			if (data != null) {
				Uri uri = data.getData();
				ContentResolver cr = context.getContentResolver();
				try {
					Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片
					CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(context, uri), new CodeUtils.AnalyzeCallback() {
						@Override
						public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
							mMvpView.onScanSuccess(result);
						}

						@Override
						public void onAnalyzeFailed() {
							ToastUtils.showShort(R.string.error_scan);
							mMvpView.onScanFailed();
						}
					});

					if (mBitmap != null) {
						mBitmap.recycle();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showShort(R.string.fail_scan);
					mMvpView.onScanFailed();
				}
			} else {
				ToastUtils.showShort(R.string.error_scan);
				mMvpView.onScanFailed();
			}
		}
		if (requestCode == Constans.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK && data != null) {
			if (data != null) {
				mMvpView.onMatisseSelectResult(Matisse.obtainPathResult(data));
			}
		}
	}

	public void matisseSelect(Set<MimeType> mimeTypes, int maxSelect, Activity context) {
		Matisse.from(context)
				.choose(mimeTypes)
				.countable(true)//有序选择图片
				.maxSelectable(maxSelect)//最大张数
				.captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
						new CaptureStrategy(true, ZuoGobal.MATISSE_FILE_PATH))
				.showSingleMediaType(true)
				.capture(true) //是否提供拍照功能
				.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
				.thumbnailScale(0.85f)//缩放比例
				.imageEngine(new MyGlideEngine())
				.theme(R.style.Matisse_Dracula)
				.forResult(Constans.REQUEST_CODE_CHOOSE);
	}

	public void imagePreviewLoader(Context context, int index, List<String> urls) {
		//调用前获取存储权限
		ImagePreview.getInstance()
				// 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
				.setContext(context)
				// 从第几张图片开始，索引从0开始哦~
				.setIndex(index)
				.setImageList(urls)
				.setFolderName(ZuoGobal.IMAGE_DOWN_PATH)//会在Picture目录进行文件夹的新建。
				.setBigImageLongClickListener((activity, view, position) -> {
					DownloadPictureUtil.downloadPicture(context.getApplicationContext(), urls.get(position));
					return false;
				})
				.setShowDownButton(false) //显示下载按钮
				// 是否启用点击图片关闭。默认启用
				.setEnableClickClose(false)
				// 是否启用上拉/下拉关闭。默认不启用
				.setEnableDragClose(true)
				.setEnableUpDragClose(true)
				//显示指示器
				.setShowIndicator(true)
				//加载策略，默认为手动模式
				.setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
				.setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
					@Override
					public void progress(View parentView, int progress) {
						ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
						TextView textView = parentView.findViewById(R.id.sh_progress_text);
						progressBar.setProgress(progress);
						String progressText = progress + "%";
						textView.setText(progressText);
					}

					@Override
					public void finish(View parentView) {
						ToastUtils.showShort("下载成功");
					}
				})
				.start();
	}
}
