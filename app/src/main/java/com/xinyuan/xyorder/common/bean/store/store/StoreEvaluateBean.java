package com.xinyuan.xyorder.common.bean.store.store;

import com.xinyuan.xyorder.common.bean.order.orderAppraise.DeliveryEvaBean;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.EvaReplyBean;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodEvaBean;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：店铺评价
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/19
 */
public class StoreEvaluateBean implements Serializable {

    private static final long serialVersionUID = -2101203567820912624L;
    private long appraisePageView;  //评论浏览量
    private long appraisePraiseCount;   //评论点赞量
    private String appraiseTime;    //评价时间
    private String avatorUrl;   //头像地址
    private List<EvaReplyBean> commentList;    //评价回复
    private String contentShopAppraise; //评价内容
    private DeliveryEvaBean deliveryAppraise;   //配送评价信息
    private List<GoodEvaBean> goodsAppraiseList;  //商品评价信息
    private long orderId;                   //订单ID
    private String orderName;   //订单名称
    private boolean praise; //当前用户是否点赞
    private boolean reply;  //是否回复
    private int shopAppraise;   //总评价星级
    private long shopAppraiseId;    //评价ID
    private long shopId;    //店铺ID
    private String tagShopAppraise; //评价标签
    private long userId;    //uersID
    private String userName;
    private List<String> shopAppraiseImageUrlList;


    public List<String> getShopAppraiseImageList() {
        return shopAppraiseImageUrlList;
    }

    public void setShopAppraiseImageList(List<String> shopAppraiseImageList) {
        this.shopAppraiseImageUrlList = shopAppraiseImageList;
    }

    public void setAppraisePageView(long appraisePageView) {
        this.appraisePageView = appraisePageView;
    }

    public void setAppraisePraiseCount(long appraisePraiseCount) {
        this.appraisePraiseCount = appraisePraiseCount;
    }

    public void setAppraiseTime(String appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }

    public void setCommentList(List<EvaReplyBean> commentList) {
        this.commentList = commentList;
    }

    public void setContentShopAppraise(String contentShopAppraise) {
        this.contentShopAppraise = contentShopAppraise;
    }


    public void setDeliveryAppraise(DeliveryEvaBean deliveryAppraise) {
        this.deliveryAppraise = deliveryAppraise;
    }

    public void setGoodsAppraiseList(List<GoodEvaBean> goodsAppraiseList) {
        this.goodsAppraiseList = goodsAppraiseList;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public void setShopAppraise(int shopAppraise) {
        this.shopAppraise = shopAppraise;
    }

    public void setShopAppraiseId(long shopAppraiseId) {
        this.shopAppraiseId = shopAppraiseId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public void setTagShopAppraise(String tagShopAppraise) {
        this.tagShopAppraise = tagShopAppraise;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getAppraisePageView() {
        return appraisePageView;
    }

    public long getAppraisePraiseCount() {
        return appraisePraiseCount;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public List<EvaReplyBean> getCommentList() {
        return commentList;
    }

    public String getContentShopAppraise() {
        return contentShopAppraise;
    }

    public DeliveryEvaBean getDeliveryAppraise() {
        return deliveryAppraise;
    }

    public List<GoodEvaBean> getGoodsAppraiseList() {
        return goodsAppraiseList;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public boolean isPraise() {
        return praise;
    }

    public boolean isReply() {
        return reply;
    }

    public int getShopAppraise() {
        return shopAppraise;
    }

    public long getShopAppraiseId() {
        return shopAppraiseId;
    }

    public long getShopId() {
        return shopId;
    }

    public String getTagShopAppraise() {
        return tagShopAppraise;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }


}
