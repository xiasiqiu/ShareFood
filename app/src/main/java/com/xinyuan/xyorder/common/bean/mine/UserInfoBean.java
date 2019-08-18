package com.xinyuan.xyorder.common.bean.mine;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by f-x on 2017/10/2319:03
 */

public class UserInfoBean implements Serializable {
    private static final long serialVersionUID = 3058271722913782351L;
    private BigDecimal accountFrozen;
    private String avatorUrl;
    private BigDecimal balance;
    private int couponNumber;
    private boolean initNicknameed;
    private long integral;
    private String lastLoginTime;
    private String nickname;
    private String registrationTime;
    private long userId;
    private String username;


    public BigDecimal getAccountFrozen() {
        return accountFrozen;
    }

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getCouponNumber() {
        return couponNumber;
    }

    public boolean isInitNicknameed() {
        return initNicknameed;
    }

    public long getIntegral() {
        return integral;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
