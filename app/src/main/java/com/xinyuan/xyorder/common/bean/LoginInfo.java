package com.xinyuan.xyorder.common.bean;

import com.xinyuan.xyorder.common.bean.mine.UserInfoBean;

import java.io.Serializable;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/18
 */
public class LoginInfo implements Serializable{
    private static final long serialVersionUID = 5174762434767558547L;
    private String jwt;
    private UserInfoBean user;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "jwt='" + jwt + '\'' +
                ", user=" + user +
                '}';
    }
}
