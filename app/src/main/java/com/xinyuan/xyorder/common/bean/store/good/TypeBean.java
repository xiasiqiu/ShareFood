package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/1818:21
 * 商品分类数据
 */

public class TypeBean implements Serializable{
    private static final long serialVersionUID = 9178214012596502182L;
    private String name;
    private long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "TypeBean{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
