package com.xinyuan.xyorder.common.bean.order.orderAppraise;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/3015:26
 * 评价回复
 */


public class EvaReplyBean implements Serializable {
    private static final long serialVersionUID = 3981021199758870435L;
    private String commentContent;
    private String commentTime;
    private String replyId;
    private long shopAppraiseId;

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getReplyId() {
        return replyId;
    }

    public long getShopAppraiseId() {
        return shopAppraiseId;
    }
}
