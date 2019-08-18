package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.AreanBean;
import com.xinyuan.xyorder.common.bean.store.store.StoreCategoryBean;
import com.xinyuan.xyorder.common.bean.store.StoreListData;

import java.util.List;

/**
 * Created by f-x on 2017/10/13.
 */

public interface SearchFoodView {
    void showStoreList(StoreListData data);

    void showStoreCategory(List<StoreCategoryBean> data);

    void showState(int state);
}
