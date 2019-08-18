package com.xinyuan.xyorder.common.bean.order.orderAppraise;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/29
 */
public class OrderAppraiseAllTag implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<DeliveryAppraiseTag> deliveryAppraiseTagList;
    private List<GoodsAppraiseTag> goodsAppraiseTagList;
    private List<ShopAppraiseTag> shopAppraiseTagList;

    public List<DeliveryAppraiseTag> getDeliveryAppraiseTagList() {
        return deliveryAppraiseTagList;
    }

    public void setDeliveryAppraiseTagList(List<DeliveryAppraiseTag> deliveryAppraiseTagList) {
        this.deliveryAppraiseTagList = deliveryAppraiseTagList;
    }

    public List<GoodsAppraiseTag> getGoodsAppraiseTagList() {
        return goodsAppraiseTagList;
    }

    public void setGoodsAppraiseTagList(List<GoodsAppraiseTag> goodsAppraiseTagList) {
        this.goodsAppraiseTagList = goodsAppraiseTagList;
    }

    public List<ShopAppraiseTag> getShopAppraiseTagList() {
        return shopAppraiseTagList;
    }

    public void setShopAppraiseTagList(List<ShopAppraiseTag> shopAppraiseTagList) {
        this.shopAppraiseTagList = shopAppraiseTagList;
    }
}
