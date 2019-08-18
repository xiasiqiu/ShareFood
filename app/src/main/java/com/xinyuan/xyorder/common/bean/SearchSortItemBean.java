package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/14.
 */

public class SearchSortItemBean implements Serializable {
    private static final long serialVersionUID = 8459458975765742918L;
    private String itemName;
    private boolean isCheck;
    private String sortItemId;

    public SearchSortItemBean(String itemName, boolean isCheck, String sortItemId) {
        this.itemName = itemName;
        this.isCheck = isCheck;
        this.sortItemId = sortItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getSortItemId() {
        return sortItemId;
    }

    public void setSortItemId(String sortItemId) {
        this.sortItemId = sortItemId;
    }

    @Override
    public String toString() {
        return "SearchSortItemBean{" +
                "itemName='" + itemName + '\'' +
                ", isCheck=" + isCheck +
                ", sortItemId=" + sortItemId +
                '}';
    }
}
