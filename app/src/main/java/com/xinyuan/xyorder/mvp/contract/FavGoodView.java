package com.xinyuan.xyorder.mvp.contract;


import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;

import java.util.List;

public interface FavGoodView {

	void showState(int state);
	void showFavInfo(List<StoreDetail> shopDetails);
	void detelteToast(boolean res);
}
