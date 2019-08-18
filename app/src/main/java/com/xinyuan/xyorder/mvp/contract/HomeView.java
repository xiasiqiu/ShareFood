package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.home.BannerBean;
import com.xinyuan.xyorder.common.bean.home.ShopCategory;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;

import java.util.List;

public interface HomeView {

    void showBanner(List<BannerBean> itemList);

    void showMenu(List<ShopCategory> itemList);

    void showShopList(List<StoreDetail> list);


    void showNoticeMsg(Boolean data);

    void showState(int state);
}
