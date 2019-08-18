package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by HDQ on 2017/12/20.
 */

public class GoodsSpecifications implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal boxesMoney;// 餐盒金额 ,
    private long boxesNumber;// 餐盒数量 ,
    private long goodsId;//商品id ,
    private long goodsSpecificationId;//规格id ,
    private String goodsSpecificationName;//商品规格名称 ,
    private BigDecimal goodsSpecificationPrice;//商品规格对应价格 ,
    private boolean infiniteInventory;//无限库存 ,
    private long stock;// 库存
    private int checkNum;
    private boolean isChecked;

    public BigDecimal getBoxesMoney() {
        return boxesMoney;
    }

    public void setBoxesMoney(BigDecimal boxesMoney) {
        this.boxesMoney = boxesMoney;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public long getBoxesNumber() {
        return boxesNumber;
    }

    public void setBoxesNumber(long boxesNumber) {
        this.boxesNumber = boxesNumber;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getGoodsSpecificationId() {
        return goodsSpecificationId;
    }

    public void setGoodsSpecificationId(long goodsSpecificationId) {
        this.goodsSpecificationId = goodsSpecificationId;
    }

    public String getGoodsSpecificationName() {
        return goodsSpecificationName;
    }

    public void setGoodsSpecificationName(String goodsSpecificationName) {
        this.goodsSpecificationName = goodsSpecificationName;
    }

    public BigDecimal getGoodsSpecificationPrice() {
        return goodsSpecificationPrice;
    }

    public void setGoodsSpecificationPrice(BigDecimal goodsSpecificationPrice) {
        this.goodsSpecificationPrice = goodsSpecificationPrice;
    }

    public boolean isInfiniteInventory() {
        return infiniteInventory;
    }

    public void setInfiniteInventory(boolean infiniteInventory) {
        this.infiniteInventory = infiniteInventory;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
