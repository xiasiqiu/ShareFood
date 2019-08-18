package com.xinyuan.xyorder.common.bean.buy;

import com.xinyuan.xyorder.common.bean.store.good.GoodActivityBean;

import java.math.BigDecimal;

/**
 * Created by f-x on 2017/12/22  09:20
 * Description
 */

public class CarGoodBean {
    private String goodsName;
    private BigDecimal goodPrice;
    private GoodActivityBean goodActivityBean;
    private long goodStock;

    public CarGoodBean(String goodsName, BigDecimal goodPrice, GoodActivityBean goodActivityBean, long goodStock) {
        this.goodsName = goodsName;
        this.goodPrice = goodPrice;
        this.goodActivityBean = goodActivityBean;
        this.goodStock = goodStock;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public BigDecimal getGoodPrice() {
        return goodPrice;
    }

    public GoodActivityBean getGoodActivityBean() {
        return goodActivityBean;
    }

    public long getGoodStock() {
        return goodStock;
    }

    @Override
    public String toString() {
        return "CarGoodBean{" +
                "goodsName='" + goodsName + '\'' +
                ", goodPrice=" + goodPrice +
                ", goodActivityBean=" + goodActivityBean +
                ", goodStock=" + goodStock +
                '}';
    }
}
