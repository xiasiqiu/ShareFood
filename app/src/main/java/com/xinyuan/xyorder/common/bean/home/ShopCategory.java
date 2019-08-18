package com.xinyuan.xyorder.common.bean.home;

import java.io.Serializable;

/**
 * <p>
 * Description：商品分类
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/20
 */
public class ShopCategory implements Serializable{
    /**
     * 店铺分类ID
     */
    private Long shopCategoryId;

    /**
     * 店铺分类名称
     */
    private String shopCategoryName;

    /**
     * 排序值
     */
    private Integer sortOrder;

    /**
     * 图标
     */
    private String icon;

    public Long getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Long shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "ShopCategory{" +
                "shopCategoryId=" + shopCategoryId +
                ", shopCategoryName='" + shopCategoryName + '\'' +
                ", sortOrder=" + sortOrder +
                ", icon='" + icon + '\'' +
                '}';
    }
}
