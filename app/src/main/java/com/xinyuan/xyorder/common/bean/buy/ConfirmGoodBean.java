package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/1719:30
 */

public class ConfirmGoodBean implements Serializable {
    private static final long serialVersionUID = -1730234540474517532L;
    private String goodName;
    private String goodhit;
    private int goodCount;
    private String couponPrice;
    private String goodPrice;

    public ConfirmGoodBean(String goodName, String goodhit, int goodCount, String couponPrice, String goodPrice) {
        this.goodName = goodName;
        this.goodhit = goodhit;
        this.goodCount = goodCount;
        this.couponPrice = couponPrice;
        this.goodPrice = goodPrice;
    }

    public String getGoodName() {
        return goodName;
    }

    public String getGoodhit() {
        return goodhit;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public String getGoodPrice() {
        return goodPrice;
    }
}
