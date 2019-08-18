package com.xinyuan.xyorder.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by fx on 2017/5/4 0004.
 * 首页模块视图实体类
 */


public class HomeMultipleItem implements MultiItemEntity {
	public static final int BANNER = 1;
	public static final int CATEGORY = 2;
	public static final int GOODS = 3;

	public static final int BANNER_SIZE = 4;
	public static final int CATEGORY_SPAN_SIZE = 4;
	public static final int GOODS_SPAN_SIZE = 2;

	public int itemType;
	public int spanSize;


	public HomeMultipleItem(int itemType, int spanSize) {
		this.itemType = itemType;
		this.spanSize = spanSize;

	}

	@Override
	public int getItemType() {
		return itemType;
	}

	public int getSpanSize() {
		return spanSize;
	}

	public void setSpanSize(int spanSize) {
		this.spanSize = spanSize;
	}

	@Override
	public String toString() {
		return "HomeMultipleItem{" +
				"itemType=" + itemType +
				", spanSize=" + spanSize +
				'}';
	}
}
