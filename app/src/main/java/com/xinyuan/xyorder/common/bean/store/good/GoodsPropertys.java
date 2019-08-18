package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HDQ on 2017/12/20.
 */

public class GoodsPropertys implements Serializable{
    private static final long serialVersionUID = 1L;
    private long goodsId;//商品id ,
    private long goodsPropertyId;// 商品属性id ,
    private String goodsPropertyName;//商品属性名称 ,
    private List<GoodsPropertyBean> goodsPropertyValueList;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getGoodsPropertyId() {
        return goodsPropertyId;
    }

    public void setGoodsPropertyId(long goodsPropertyId) {
        this.goodsPropertyId = goodsPropertyId;
    }

    public String getGoodsPropertyName() {
        return goodsPropertyName;
    }

    public void setGoodsPropertyName(String goodsPropertyName) {
        this.goodsPropertyName = goodsPropertyName;
    }

    public List<GoodsPropertyBean> getGoodsPropertyValueList() {
        return goodsPropertyValueList;
    }

    public void setGoodsPropertyValueList(List<GoodsPropertyBean> goodsPropertyValueList) {
        this.goodsPropertyValueList = goodsPropertyValueList;
    }
}
