package com.xinyuan.xyorder.common.bean.buy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2618:27
 */

public class WXPayBean implements Serializable {
    private static final long serialVersionUID = -4214819392889278811L;
    private String appId;
    private String nonceStr;
    @SerializedName("package")
    private String packages;
    private String partnerId;
    private String prepayId;
    private String sign;
    private String timeStamp;

    public String getAppId() {
        return appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getPackages() {
        return packages;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getSign() {
        return sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "WXPayBean{" +
                "appId='" + appId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", packages='" + packages + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", sign='" + sign + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
