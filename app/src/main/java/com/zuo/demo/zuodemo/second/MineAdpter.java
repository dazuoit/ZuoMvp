package com.zuo.demo.zuodemo.second;

import com.chad.library.adapter.base.BaseViewHolder;
import net.NewsDetail;

import com.zuo.demo.lib_common.base.adapter.BaseRecycleAdaper;
import com.zuo.demo.zuodemo.R;

/**
 * @author zuo
 * @filename: MineAdpter
 * @date: 2020/4/20
 * @description: 描述
 * @version: 版本号
 */
public class MineAdpter extends BaseRecycleAdaper<NewsDetail.ListBean, BaseViewHolder> {
	public MineAdpter() {
		super(R.layout.layout_text_item);
	}

	@Override
	protected void convert(BaseViewHolder helper, NewsDetail.ListBean item) {
		if (item != null) {
			helper.setText(R.id.tv_demo, item.getName());
		}
	}


}
