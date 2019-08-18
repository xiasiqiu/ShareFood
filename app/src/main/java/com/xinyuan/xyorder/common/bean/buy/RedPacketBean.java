package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by f-x on 2017/10/2018:27
 */

public class RedPacketBean implements Serializable {
    private static final long serialVersionUID = 7888324183119368744L;
    private boolean canPickUp;      //是否可以领取
    private long couponId;          //优惠券ID
    private String couponName;      //优惠券名称
    private long couponNumber;      //优惠券数量
    private String endTime;         //结束时间
    private long maxPickUpNumber;   //最大领取数量
    private BigDecimal minimum;     //最低消费额度
    private BigDecimal money;       //金额
    private String pickUpType;      //领取方式 = ['CONSUME', 'HAND'],
    private int pickUped;           //已领取数量
    private String startTime;       //开始时间
    private String useSocpe;        //优惠券使用范围 = ['SHOP', 'GLOBAL']

    private boolean isCheck;        //是否选择

    public boolean isCanPickUp() {
        return canPickUp;
    }

    public void setCanPickUp(boolean canPickUp) {
        this.canPickUp = canPickUp;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public long getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(long couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getMaxPickUpNumber() {
        return maxPickUpNumber;
    }

    public void setMaxPickUpNumber(long maxPickUpNumber) {
        this.maxPickUpNumber = maxPickUpNumber;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPickUpType() {
        return pickUpType;
    }

    public void setPickUpType(String pickUpType) {
        this.pickUpType = pickUpType;
    }

    public int getPickUped() {
        return pickUped;
    }

    public void setPickUped(int pickUped) {
        this.pickUped = pickUped;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUseSocpe() {
        return useSocpe;
    }

    public void setUseSocpe(String useSocpe) {
        this.useSocpe = useSocpe;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "RedPacketBean{" + ", isCheck=" + isCheck +
                "canPickUp=" + canPickUp +
                ", couponId=" + couponId +
                ", couponName='" + couponName + '\'' +
                ", couponNumber=" + couponNumber +
                ", endTime='" + endTime + '\'' +
                ", maxPickUpNumber=" + maxPickUpNumber +
                ", minimum=" + minimum +
                ", money=" + money +
                ", pickUpType='" + pickUpType + '\'' +
                ", pickUped=" + pickUped +
                ", startTime='" + startTime + '\'' +
                ", useSocpe='" + useSocpe +
                '}';
    }
}
