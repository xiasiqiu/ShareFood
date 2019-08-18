package com.xinyuan.xyorder.common.even;

import android.view.View;

/**
 * Created by f-x on 2017/10/1820:08
 */

public class GoodBusEven {
    private String flag;
    private Object obj;
    public static final String ADDCART = "ADDCART";
    public static final String FINISH = "FINISH";

    public static final String LOADING = "LOADING";

    public static final String ORDER_ERROR = "ORDER_ERROR";
    public static final String ORDER_SUCCESS = "ORDER_SUCCESS";

    public static final String ADDCAR = "ADDCAR";
    public static final String DETAILADDCAR = "DETAILADDCAR";
    public static final String SUBCAR = "SUBCAR";
    private View view;

    public GoodBusEven(String flag) {
        this.flag = flag;
    }

    public GoodBusEven(String flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public GoodBusEven(String flag, Object obj, View view) {
        this.flag = flag;
        this.obj = obj;
        this.view = view;

    }

    public View getView() {
        return view;
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
