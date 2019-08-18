package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Description：优惠券
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class CouponBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean canPickUp;//是否可以领取
    private int couponId;
    private String couponName;
    private int couponNumber;
    private String endTime;
    private int maxPickUpNumber;//最大领取数量
    private int minimum;//最低消费额度
    private BigDecimal money;
    private String pickUpType;// 领取方式 = ['CONSUME', 'HAND'],
    private int pickUped;
    private String startTime;
    private String useSocpe;//优惠券使用范围 = ['SHOP', 'GLOBAL']

    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCanPickUp() {
        return canPickUp;
    }

    public void setCanPickUp(boolean canPickUp) {
        this.canPickUp = canPickUp;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(int couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMaxPickUpNumber() {
        return maxPickUpNumber;
    }

    public void setMaxPickUpNumber(int maxPickUpNumber) {
        this.maxPickUpNumber = maxPickUpNumber;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
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
}
