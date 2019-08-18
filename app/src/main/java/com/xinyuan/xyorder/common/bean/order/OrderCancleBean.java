package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * <p>
 * Description：取消订单
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/20
 */
public class OrderCancleBean implements Serializable{

    private static final long serialVersionUID = 1L;
    private String cancelContent;
    private String cancelType;//取消类型 = ['USER', 'SHOP'],
    private String orderCancelReason;// 取消原因 = ['TEMPORARY_EMERGENCY', 'WRONG_INFORMATION', 'PAYMENT_PROBLEMS', 'OTHER',
    private long orderId;

    public String getCancelContent() {
        return cancelContent;
    }

    public void setCancelContent(String cancelContent) {
        this.cancelContent = cancelContent;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
