package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/719:32
 */

public class CidBean implements Serializable {
    private static final long serialVersionUID = -1983534420798886605L;
    private String cid;
    private boolean ios;
    private int objectId;
    private boolean seller;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean isIos() {
        return ios;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public boolean isSeller() {
        return seller;
    }

    public void setSeller(boolean seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "CidBean{" +
                "cid='" + cid + '\'' +
                ", ios=" + ios +
                ", objectId=" + objectId +
                ", seller=" + seller +
                '}';
    }
}
