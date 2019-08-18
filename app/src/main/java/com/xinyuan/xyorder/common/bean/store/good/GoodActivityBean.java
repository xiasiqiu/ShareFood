package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by f-x on 2017/10/1817:25
 * 商品活动
 */

public class GoodActivityBean implements Serializable {
    private static final long serialVersionUID = -1770496559411328384L;
    private long activityId;
    private long goodsId;
    private String goodsName;
    private BigDecimal goodsPrice;
    private BigDecimal specialsPrice;
    private BigDecimal discount;
    private int limitedQuantity;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getSpecialsPrice() {
        return specialsPrice;
    }

    public void setSpecialsPrice(BigDecimal specialsPrice) {
        this.specialsPrice = specialsPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public int getLimitedQuantity() {
        return limitedQuantity;
    }

    public void setLimitedQuantity(int limitedQuantity) {
        this.limitedQuantity = limitedQuantity;
    }

    @Override
    public String toString() {
        return "GoodActivityBean{" +
                "activityId=" + activityId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", specialsPrice=" + specialsPrice +
                ", discount=" + discount +
                ", limitedQuantity=" + limitedQuantity +
                '}';
    }
}
