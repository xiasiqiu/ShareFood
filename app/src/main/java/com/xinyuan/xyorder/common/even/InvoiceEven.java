package com.xinyuan.xyorder.common.even;

/**
 * Created by f-x on 2017/11/2217:51
 */

public class InvoiceEven {
    private String flag;
    private Object obj;
    public static final String FINISH = "FINISH";
    public static final String EDITE = "EDITE";
    public static final String FLASH = "FLASH";

    public InvoiceEven(String flag) {
        this.flag = flag;
    }

    public InvoiceEven(String flag, Object obj) {
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
