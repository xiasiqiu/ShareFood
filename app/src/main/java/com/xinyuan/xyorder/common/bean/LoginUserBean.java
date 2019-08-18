package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/815:58
 */

public class LoginUserBean implements Serializable {
    private static final long serialVersionUID = -7376188747321394423L;
    private boolean ios;
    private String cid;
    private String code;
    private String username;

    public boolean isIos() {
        return ios;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    @Override
    public String toString() {
        return "LoginUserBean{" +
                "ios=" + ios +
                ", cid='" + cid + '\'' +
                ", code='" + code + '\'' +
                ", userName='" + username + '\'' +
                '}';
    }
}
