package com.xinyuan.xyorder.common.even;

/**
 * Created by f-x on 2017/10/14.
 */

public class SearchStoreEven {
    private String flag;
    private Object obj;
    public static final String HAVECITY = "HAVECITY";


    public static final String KEYWORD = "KEYWORD";
    public static final String ORDER_SEARCH = "ORDER_SEARCH";
    public static final String AREAR_SEARCH = "AREAR_SEARCH";
    public static final String CLASS_SEARCH = "CLASS_SEARCH";
    public static final String SEARCH_CATEGORY = "SEARCH_CATEGORY";
    public static final String SEARCH_CLASS = "SEARCH_CLASS";

    public SearchStoreEven(String flag) {
        this.flag = flag;
    }

    public SearchStoreEven(String flag, Object obj) {
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
