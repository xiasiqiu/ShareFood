package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;

/**
 * Created by HDQ on 2017/12/20.
 */
public class GoodsPropertyBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String value;
    private boolean isChecked;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
