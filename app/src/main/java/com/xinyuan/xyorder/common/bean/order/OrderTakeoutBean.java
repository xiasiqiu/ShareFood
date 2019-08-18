package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Description：外卖订单信息
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class OrderTakeoutBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String deliveryTime;//配送时间
    private BigDecimal mealFee;//餐盒费
    private int orderId;
    private BigDecimal shippingFee;//配送费
    private String distributionType;    //配送方式
    private String carrierDriverName;//配送员姓名
    private String carrierDriverphone;//配送员电话

    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public BigDecimal getMealFee() {
        return mealFee;
    }

    public void setMealFee(BigDecimal mealFee) {
        this.mealFee = mealFee;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getCarrierDriverName() {
        return carrierDriverName;
    }

    public void setCarrierDriverName(String carrierDriverName) {
        this.carrierDriverName = carrierDriverName;
    }

    public String getCarrierDriverphone() {
        return carrierDriverphone;
    }

    public void setCarrierDriverphone(String carrierDriverphone) {
        this.carrierDriverphone = carrierDriverphone;
    }
}
