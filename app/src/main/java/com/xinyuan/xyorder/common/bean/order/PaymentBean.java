package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2311:02
 */

public class PaymentBean implements Serializable {
    private static final long serialVersionUID = -2160758866190068429L;
    private String code;
    private String displayName;
    private String urlPrefix;
    private boolean blancePay;
    private boolean isCheck;
    public PaymentBean(String code, String displayName,boolean isCheck) {
        this.code = code;
        this.displayName = displayName;
        this.isCheck=isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public boolean isBlancePay() {
        return blancePay;
    }

    @Override
    public String toString() {
        return "PaymentBean{" +
                "code='" + code + '\'' +
                ", displayName='" + displayName + '\'' +
                ", urlPrefix='" + urlPrefix + '\'' +
                ", blancePay=" + blancePay +
                ", isCheck=" + isCheck +
                '}';
    }
}
