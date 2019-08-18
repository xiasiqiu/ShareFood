package com.xinyuan.xyorder.common.bean.order.orderAppraise;

import java.io.Serializable;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/12/1
 */
public class ShopAppraiseImageBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private long orderId;
    private long shopAppraiseImageId;
    private String shopAppraiseImageUrl;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getShopAppraiseImageId() {
        return shopAppraiseImageId;
    }

    public void setShopAppraiseImageId(long shopAppraiseImageId) {
        this.shopAppraiseImageId = shopAppraiseImageId;
    }

    public String getShopAppraiseImageUrl() {
        return shopAppraiseImageUrl;
    }

    public void setShopAppraiseImageUrl(String shopAppraiseImageUrl) {
        this.shopAppraiseImageUrl = shopAppraiseImageUrl;
    }
}
