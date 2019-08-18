package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * <p>
 * Description：订单进度跟踪
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/25
 */
public class OrderTrackingBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private long orderId;
    private String schedulesContent;
    private String schedulesTime;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getSchedulesContent() {
        return schedulesContent;
    }

    public void setSchedulesContent(String schedulesContent) {
        this.schedulesContent = schedulesContent;
    }

    public String getSchedulesTime() {
        return schedulesTime;
    }

    public void setSchedulesTime(String schedulesTime) {
        this.schedulesTime = schedulesTime;
    }
}
