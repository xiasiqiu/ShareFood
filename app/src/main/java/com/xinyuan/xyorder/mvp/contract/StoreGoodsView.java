package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.store.good.GoodsCategory;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/27
 */
public interface StoreGoodsView {
    void showCategory(List<GoodsCategory> goodsCategoryList);
    void ShowStoreInfo(StoreDetail storeDetail);
}
