package com.xinyuan.xyorder.common.bean.order.orderAppraise;

import java.io.Serializable;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/29
 */
public class DeliveryAppraiseTag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deliveryAppraiseTagName;
    private long deliveryAppraiseTagId;
    private boolean isCheck;


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getDeliveryAppraiseTagName() {
        return deliveryAppraiseTagName;
    }

    public void setDeliveryAppraiseTagName(String deliveryAppraiseTagName) {
        this.deliveryAppraiseTagName = deliveryAppraiseTagName;
    }

    public long getDeliveryAppraiseTagId() {
        return deliveryAppraiseTagId;
    }

    public void setDeliveryAppraiseTagId(long deliveryAppraiseTagId) {
        this.deliveryAppraiseTagId = deliveryAppraiseTagId;
    }
}
