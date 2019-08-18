package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/18
 */
public class Usessr implements Serializable {

    private static final long serialVersionUID = 7810405161827388219L;
    private String username;//手机号
    private String secretkey;
    private long userId;
    private String lastLoginTime;
    private String registrationTime;
    private String code;//验证码
    private boolean initNicknameed;//是否修改过用户名
    private String nickname;//用户昵称
    private String avatorUrl;   //用户头像

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isInitNicknameed() {
        return initNicknameed;
    }

    public void setInitNicknameed(boolean initNicknameed) {
        this.initNicknameed = initNicknameed;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
