package com.xinyuan.xyorder.common.bean.store.store;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/1916:59
 * 结算
 */

public class SettleBean implements Serializable {
    private static final long serialVersionUID = -8804609448596311926L;
    private String bankHouse;   //所属银行
    private String bankNumber;  //银行卡号
    private String openBank;    //开户支行
    private String openCity;    //开户城市
    private long shopId;        //店铺ID

    public String getBankHouse() {
        return bankHouse;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public String getOpenBank() {
        return openBank;
    }

    public String getOpenCity() {
        return openCity;
    }

    public long getShopId() {
        return shopId;
    }
}
