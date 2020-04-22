package com.zuo.demo.zuodemo.second;

import com.zuo.demo.lib_common.base.view.BaseRecycleView;
import net.NewsDetail;

/**
 * @author zuo
 * @filename: SecondView
 * @date: 2020/4/20
 * @description: 描述
 * @version: 版本号
 */
public interface SecondView extends BaseRecycleView {
	void  onNewsReturn(NewsDetail newsDetailRespData);
}
