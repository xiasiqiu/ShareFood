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
public class GoodsAppraiseTag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String goodsAppraiseTagName;
    private long goodsAppraiseTagId;
    private boolean checked;

    public String getGoodsAppraiseTagName() {
        return goodsAppraiseTagName;
    }

    public void setGoodsAppraiseTagName(String goodsAppraiseTagName) {
        this.goodsAppraiseTagName = goodsAppraiseTagName;
    }

    public long getGoodsAppraiseTagId() {
        return goodsAppraiseTagId;
    }

    public void setGoodsAppraiseTagId(long goodsAppraiseTagId) {
        this.goodsAppraiseTagId = goodsAppraiseTagId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "GoodsAppraiseTag{" +
                "goodsAppraiseTagName='" + goodsAppraiseTagName + '\'' +
                ", goodsAppraiseTagId=" + goodsAppraiseTagId +
                ", checked=" + checked +
                '}';
    }
}
