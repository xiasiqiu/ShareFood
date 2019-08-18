package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2017:27
 * 折扣商品活动
 */

public class DiscountGoodBean implements Serializable {
    private static final long serialVersionUID = -7923911377780331438L;
    private int discount;   //折扣
    private long goodsId;   //商品ID
    private int limitedQuantity;    //限制数量

    public int getDiscount() {
        return discount;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public int getLimitedQuantity() {
        return limitedQuantity;
    }
}
