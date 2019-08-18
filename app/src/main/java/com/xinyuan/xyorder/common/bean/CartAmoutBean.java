package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by f-x on 2017/12/209:33
 */

public class CartAmoutBean implements Serializable {
    private static final long serialVersionUID = 5435758350476614717L;
    private BigDecimal amount;//实际价格（商品折后价+餐盒费）
    private BigDecimal originalAmount; //原价（商品原价+餐盒费）

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }
}
