package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by f-x on 2017/10/2017:27
 * 特价商品活动
 */

public class SpecialsGoodBean implements Serializable {
    private static final long serialVersionUID = 4812849070812158730L;
    private long goodsId;                   //商品ID
    private int limitedQuantity;           //限制数量
    private BigDecimal specialsPrice;      //特价

    public long getGoodsId() {
        return goodsId;
    }

    public int getLimitedQuantity() {
        return limitedQuantity;
    }

    public BigDecimal getSpecialsPrice() {
        return specialsPrice;
    }
}
