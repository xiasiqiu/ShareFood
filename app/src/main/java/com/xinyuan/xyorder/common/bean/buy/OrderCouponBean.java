package com.xinyuan.xyorder.common.bean.buy;

import com.xinyuan.xyorder.common.bean.order.CouponBean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2320:55
 */

public class OrderCouponBean implements Serializable {
    private static final long serialVersionUID = 1603726991506104995L;
    private boolean canUse;
    private long couponId;
    private long couponSNId;
    private boolean used;
    private long userId;
    private CouponBean coupon;

    public CouponBean getCoupon() {
        return coupon;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public long getCouponId() {
        return couponId;
    }

    public long getCouponSNId() {
        return couponSNId;
    }

    public boolean isUsed() {
        return used;
    }

    public long getUserId() {
        return userId;
    }
}
