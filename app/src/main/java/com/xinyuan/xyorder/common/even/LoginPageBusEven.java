package com.xinyuan.xyorder.common.even;

/**
 * Created by f-x on 2017/10/2318:50
 */

public class LoginPageBusEven {
    private String flag;
    private Object obj;
    public static final String ISLOGIN = "ISLOGIN";
    public static final String LOGINOUT = "LOGINOUT";
    public static final String ALIPAY = "ALIPAY";
    public static final String WXPAY = "WXPAY";
    public static final String NO_LOGIN = "NO_LOGIN";


    public LoginPageBusEven(String flag, Object obj) {
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
