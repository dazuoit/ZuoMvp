package com.zuo.demo.lib_common.model;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kongzue.dialog.util.DialogSettings;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zuo.demo.lib_common.R;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * @author zuo
 * @filename: ZuoApplication
 * @date: 2020/4/13
 * @description: 描述
 * @version: 版本号
 */
public class ZuoApplication extends Application {

	private volatile static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
		//Customize the adaptation parameters of the Fragment:
		autoSize();
		//dialog
		dialogSetting();
		//zixing
		ZXingLibrary.initDisplayOpinion(this);

		initSmartrefreshLayout();

		if (ZuoGobal.IS_DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
			ARouter.openLog();     // Print log
			ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
		}
		ARouter.init(this);
	}


	private void autoSize() {
		AutoSizeConfig.getInstance().setCustomFragment(true);
		AutoSizeConfig.getInstance().getUnitsManager()
				.setSupportDP(false)
				.setSupportSP(false)
				.setSupportSubunits(Subunits.MM);
	}

	private void dialogSetting() {
		DialogSettings.init();
		DialogSettings.DEBUGMODE = ZuoGobal.IS_DEBUG;
		DialogSettings.isUseBlur = true;
		DialogSettings.autoShowInputKeyboard = true;
		//DialogSettings.backgroundColor = Color.BLUE;
		//DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
		//DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
		DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
		DialogSettings.theme = DialogSettings.THEME.LIGHT;
		DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;
	}

	private void initSmartrefreshLayout() {
		//使用static代码段可以防止内存泄漏

		//设置全局默认配置（优先级最低，会被其他设置覆盖）
		SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
			@Override
			public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
				//开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
				layout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
				layout.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
				layout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
				layout.setEnableOverScrollBounce(false);//是否启用越界回弹
				layout.finishRefresh(3000);//延迟3000毫秒后结束刷新
				layout.finishLoadMore(3000);//延迟3000毫秒后结束加载
				layout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
				layout.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
				layout.setEnableFooterFollowWhenLoadFinished(true);
				layout.setPrimaryColorsId(R.color.colorPrimary, R.color.colorPrimaryDark);
				layout.setRefreshHeader(new ClassicsHeader(context));//设置Header
				layout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
			}
		});

	}


	public static Context getmContext() {
		return mContext;
	}
}
