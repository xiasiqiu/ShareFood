package com.xinyuan.xyorder.common.bean.store.store;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/14.
 * 店铺分类
 */

public class StoreCategoryBean implements Serializable {

    private static final long serialVersionUID = -1897312898769188809L;
    private String icon;
    private long shopCategoryId;
    private String shopCategoryName;
    private int sortOrder;

    public String getIcon() {
        return icon;
    }

    public long getShopCategoryId() {
        return shopCategoryId;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return "StoreCategoryBean{" +
                "icon='" + icon + '\'' +
                ", shopCategoryId=" + shopCategoryId +
                ", shopCategoryName='" + shopCategoryName + '\'' +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
