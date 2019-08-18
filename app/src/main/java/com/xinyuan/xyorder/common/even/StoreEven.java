package com.xinyuan.xyorder.common.even;

/**
 * Created by f-x on 2017/10/1716:30
 */

public class StoreEven {
    public static final String StoreInfo = "StoreInfo";
    public static final String AddCartInfo = "AddCart";
    private String flag;
    private Object obj;

    public StoreEven(String flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
