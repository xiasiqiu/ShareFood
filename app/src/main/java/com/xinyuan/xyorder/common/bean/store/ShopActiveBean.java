package com.xinyuan.xyorder.common.bean.store;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2109:54
 */

public class ShopActiveBean implements Serializable{
    private static final long serialVersionUID = -3939296931674422390L;
    private long activityId;
    private String activityName;
    private String activityType;
    private boolean isValid;
    private long shopId;

    public long getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public boolean isValid() {
        return isValid;
    }

    public long getShopId() {
        return shopId;
    }

    @Override
    public String toString() {
        return "ShopActiveBean{" +
                "activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", activityType='" + activityType + '\'' +
                ", isValid=" + isValid +
                ", shopId=" + shopId +
                '}';
    }
}
