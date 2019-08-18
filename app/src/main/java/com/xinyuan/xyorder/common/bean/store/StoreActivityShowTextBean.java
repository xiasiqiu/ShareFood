package com.xinyuan.xyorder.common.bean.store;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2714:02
 */

public class StoreActivityShowTextBean implements Serializable {
    private static final long serialVersionUID = -6642354868302434867L;
    private String activityType;
    private int acitvityColor;

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setAcitvityColor(int acitvityColor) {
        this.acitvityColor = acitvityColor;
    }

    public String getActivityType() {
        return activityType;
    }

    public int getAcitvityColor() {
        return acitvityColor;
    }
}
