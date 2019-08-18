package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Description：订单活动信息
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class OrderActivitysBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String activityContent;
    private int activityId;
    private BigDecimal activityReduced;//活动优惠金额
    private int orderId;
    private String activityType;

    public String getActivityType() {
        return activityType;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public BigDecimal getActivityReduced() {
        return activityReduced;
    }

    public void setActivityReduced(BigDecimal activityReduced) {
        this.activityReduced = activityReduced;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
