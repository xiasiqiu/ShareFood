package com.xinyuan.xyorder.common.bean.order.orderAppraise;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/3015:27
 */

public class DeliveryEvaBean implements Serializable{
    private static final long serialVersionUID = 7575022824855025472L;
    private String appraiseContent; //评论内容
    private int appraiseLevel;  //评价等级
    private String appraiseTime;    //评价时间
    private long deliveryAppraiseId;    //配送评价ID
    private String tagDeliveryAppraise; //评价标签

    public String getAppraiseContent() {
        return appraiseContent;
    }

    public int getAppraiseLevel() {
        return appraiseLevel;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public long getDeliveryAppraiseId() {
        return deliveryAppraiseId;
    }

    public String getTagDeliveryAppraise() {
        return tagDeliveryAppraise;
    }

    public void setAppraiseContent(String appraiseContent) {
        this.appraiseContent = appraiseContent;
    }

    public void setAppraiseLevel(int appraiseLevel) {
        this.appraiseLevel = appraiseLevel;
    }

    public void setAppraiseTime(String appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public void setDeliveryAppraiseId(long deliveryAppraiseId) {
        this.deliveryAppraiseId = deliveryAppraiseId;
    }

    public void setTagDeliveryAppraise(String tagDeliveryAppraise) {
        this.tagDeliveryAppraise = tagDeliveryAppraise;
    }
}
