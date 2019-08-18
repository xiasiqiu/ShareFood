package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Description：订单商品
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/28
 */
public class OrderGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderGoodsId;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private BigDecimal goodsPrice;

    private int goodsCount;

    private BigDecimal amount;//现价小计
    private String addTime;
    private BigDecimal oldAmount;//原价小计
    private BigDecimal resultPrice;//商品优惠价格

    private String goodsContent;

    @Override
    public boolean equals(
            Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OrderGoods)) {
            return false;
        }
        OrderGoods other = (OrderGoods) obj;
        if (orderGoodsId == null) {
            if (other.orderGoodsId != null) {
                return false;
            }
        } else if (!orderGoodsId.equals(other.orderGoodsId)) {
            return false;
        }
        return true;
    }

    /**
     * 返回 orderGoodsId
     *
     * @return orderGoodsId
     */
    public Long getOrderGoodsId() {
        return orderGoodsId;
    }

    /**
     * 设置orderGoodsId
     *
     * @param orderGoodsId orderGoodsId
     */
    public void setOrderGoodsId(
            Long orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    /**
     * 返回 orderId
     *
     * @return orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置orderId
     *
     * @param orderId orderId
     */
    public void setOrderId(
            Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 返回 goodsId
     *
     * @return goodsId
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * 设置goodsId
     *
     * @param goodsId goodsId
     */
    public void setGoodsId(
            Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 返回 goodsName
     *
     * @return goodsName
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 设置goodsName
     *
     * @param goodsName goodsName
     */
    public void setGoodsName(
            String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 返回 goodsPrice
     *
     * @return goodsPrice
     */
    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    /**
     * 设置goodsPrice
     *
     * @param goodsPrice goodsPrice
     */
    public void setGoodsPrice(
            BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    /**
     * 返回 goodsCount
     *
     * @return goodsCount
     */
    public int getGoodsCount() {
        return goodsCount;
    }

    /**
     * 设置goodsCount
     *
     * @param goodsCount goodsCount
     */
    public void setGoodsCount(
            int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public BigDecimal getResultPrice() {
        return resultPrice;
    }

    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }

    public String getGoodsContent() {
        return goodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        this.goodsContent = goodsContent;
    }

    /**
     * 返回 amount
     *
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置amount
     *
     * @param amount amount
     */
    public void setAmount(
            BigDecimal amount) {
        this.amount = amount;
    }
}
