package com.xinyuan.xyorder.common.even;

/**
 * Created by f-x on 2017/10/14.
 */

public class AroundStoreEven {
    private String flag;
    private Object obj;
    private String tag;
    public static final String HAVECITY = "HAVECITY";


    public static final String KEYWORD = "KEYWORD";
    public static final String ORDER_SEARCH = "ORDER_SEARCH";
    public static final String AREAR_SEARCH = "AREAR_SEARCH";
    public static final String CLASS_SEARCH = "CLASS_SEARCH";
    public static final String SEARCH_CATEGORY = "SEARCH_CATEGORY";
    public static final String SEARCH_CLASS = "SEARCH_CLASS";

    public static final String SEARCH_SELECT = "SEARCH_SELECT";
    public static final String SEARCH_FROM_HOME = "SEARCH_FROM_HOME";
    public static final String SEARCH_FROM_NEAR = "SEARCH_FROM_NEAR";

    public AroundStoreEven(String flag) {
        this.flag = flag;
    }

    public AroundStoreEven(String flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public AroundStoreEven(String flag, Object obj,String tag) {
        this.flag = flag;
        this.obj = obj;
        this.tag=tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
