package com.xinyuan.xyorder.common.even;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/12
 */
public class UserEvent {
    public String username;//手机号
    public long userId;
    public boolean initNicknameed;//是否修改过用户名
    public String nickname;//用户昵称

    public UserEvent(long userId) {
        this.userId = userId;
    }

    public UserEvent(boolean initNicknameed, String nickname,long userId) {
        this.initNicknameed = initNicknameed;
        this.nickname = nickname;
        this.userId=userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
