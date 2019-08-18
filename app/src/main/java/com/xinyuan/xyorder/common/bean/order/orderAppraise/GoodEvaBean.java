package com.xinyuan.xyorder.common.bean.order.orderAppraise;

import java.io.Serializable;
import java.util.List;


public class GoodEvaBean implements Serializable {
    private static final long serialVersionUID = -3396612195235328455L;
    private String appraiseContent; //评价内容
    private int appraiseLevel;  //评价等级
    private String appraiseTime;    //评价时间
    private long goodsAppraiseId;   //评价ID
    private long goodsId;   //商品ID
    private String goodsName;   //商品名称
    private String tagGoodsAppraise;//评价tag
    private List<GoodsAppraiseTag> goodsAppraiseTags;


    public List<GoodsAppraiseTag> getGoodsAppraiseTags() {
        return goodsAppraiseTags;
    }

    public void setGoodsAppraiseTags(List<GoodsAppraiseTag> goodsAppraiseTags) {
        this.goodsAppraiseTags = goodsAppraiseTags;
    }

    public String getAppraiseContent() {
        return appraiseContent;
    }

    public int getAppraiseLevel() {
        return appraiseLevel;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public long getGoodsAppraiseId() {
        return goodsAppraiseId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getTagGoodsAppraise() {
        return tagGoodsAppraise;
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

    public void setGoodsAppraiseId(long goodsAppraiseId) {
        this.goodsAppraiseId = goodsAppraiseId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setTagGoodsAppraise(String tagGoodsAppraise) {
        this.tagGoodsAppraise = tagGoodsAppraise;
    }

    @Override
    public String toString() {
        return "GoodEvaBean{" +
                "appraiseContent='" + appraiseContent + '\'' +
                ", appraiseLevel=" + appraiseLevel +
                ", appraiseTime='" + appraiseTime + '\'' +
                ", goodsAppraiseId=" + goodsAppraiseId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", tagGoodsAppraise='" + tagGoodsAppraise + '\'' +
                ", goodsAppraiseTags=" + goodsAppraiseTags +
                '}';
    }
}
