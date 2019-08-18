package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

public class SearchBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sort;
    private String key;
    private String shopCategoryId;
    private boolean fromHome;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(String shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public boolean isFromHome() {
        return fromHome;
    }

    public void setFromHome(boolean fromHome) {
        this.fromHome = fromHome;
    }
}
