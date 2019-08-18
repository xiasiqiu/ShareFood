package com.xinyuan.xyorder.common.bean.buy;

import com.xinyuan.xyorder.adapter.buy.OrderGoodSpecBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/2020:44
 */

public class OrderPrewBean implements Serializable {
    private static final long serialVersionUID = -4086632082533026572L;
    private long contactId;
    private long couponSNId;
    private String deliveryTime;
    private long invoiceInfoId;
    private boolean isPrivateRoom;
    private String orderContent;
    private String payment;
    private int repastNum;
    private long shopId;
    private List<OrderGoodSpecBean> specs;


    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getCouponSNId() {
        return couponSNId;
    }

    public void setCouponSNId(long couponSNId) {
        this.couponSNId = couponSNId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public long getInvoiceInfoId() {
        return invoiceInfoId;
    }

    public void setInvoiceInfoId(long invoiceInfoId) {
        this.invoiceInfoId = invoiceInfoId;
    }

    public boolean isPrivateRoom() {
        return isPrivateRoom;
    }

    public void setPrivateRoom(boolean privateRoom) {
        isPrivateRoom = privateRoom;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getRepastNum() {
        return repastNum;
    }

    public void setRepastNum(int repastNum) {
        this.repastNum = repastNum;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public List<OrderGoodSpecBean> getSpecs() {
        return specs;
    }

    public void setSpecs(List<OrderGoodSpecBean> specs) {
        this.specs = specs;
    }


}
