package com.xinyuan.xyorder.common.bean.store.store;    //

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/1916;    //50
 * 店铺餐饮许可证
 */

public class StoreFoodLicenseBean implements Serializable {

    private static final long serialVersionUID = -932319509497732758L;
    private String beginTime;    //起始日期 ,

    private String endTime;    //结束日期 ,

    private String foodUrl;    //餐饮许可证Url ,

    private String legal;    //法定代表人 ,

    private String licenseAddress;    //许可证地址 ,

    private String licenseNumber;    //许可证编号 ,

    private boolean longTerm;    //是否长期 ,

    private long shopId;    //店铺ID ,

    private String unitName;    //单位名称

    private String intelligence;//行业资质

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getFoodUrl() {
        return foodUrl;
    }

    public String getLegal() {
        return legal;
    }

    public String getLicenseAddress() {
        return licenseAddress;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public boolean isLongTerm() {
        return longTerm;
    }

    public long getShopId() {
        return shopId;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getIntelligence() {
        return intelligence;
    }
}
