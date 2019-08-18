package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/2215:39
 */

public class InVoiceBean implements Serializable{
    private static final long serialVersionUID = -414730498617790140L;
    private long invoiceInfoId;
    private String title;
    private String invoiceType;
    private String ein;
    private boolean isCheck;

    public long getInvoiceInfoId() {
        return invoiceInfoId;
    }

    public void setInvoiceInfoId(long invoiceInfoId) {
        this.invoiceInfoId = invoiceInfoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "InVoiceBean{" +
                "invoiceInfoId=" + invoiceInfoId +
                ", title='" + title + '\'' +
                ", invoiceType='" + invoiceType + '\'' +
                ", ein='" + ein + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
