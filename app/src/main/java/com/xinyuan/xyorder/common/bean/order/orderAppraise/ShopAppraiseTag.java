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
public class ShopAppraiseTag implements Serializable {
    private static final long serialVersionUID = 1L;
    private long shopAppraiseTagId;
    private String shopAppraiseTagName;
    private boolean checked;

    public long getShopAppraiseTagId() {
        return shopAppraiseTagId;
    }

    public void setShopAppraiseTagId(long shopAppraiseTagId) {
        this.shopAppraiseTagId = shopAppraiseTagId;
    }

    public String getShopAppraiseTagName() {
        return shopAppraiseTagName;
    }

    public void setShopAppraiseTagName(String shopAppraiseTagName) {
        this.shopAppraiseTagName = shopAppraiseTagName;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
