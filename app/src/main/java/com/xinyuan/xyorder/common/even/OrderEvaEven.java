package com.xinyuan.xyorder.common.even;

/**
 * Created by f-x on 2017/11/3011:37
 */

public class OrderEvaEven {
    private String flag;
    private Object obj;
    private long id;
    public static final String CHOOESTAG = "CHOOESTAG";
    public static final String REMOVETAG = "REMOVETAG";

    public OrderEvaEven(String flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public OrderEvaEven(String flag, long id, Object obj) {
        this.flag = flag;
        this.obj = obj;
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
