package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2418:59
 */

public class NumBean implements Serializable {
    private static final long serialVersionUID = 1186065172039693240L;
    private String numName;
    private boolean isCheck;
    private int num;

    public NumBean(String numName, int num, boolean isCheck) {
        this.numName = numName;
        this.isCheck = isCheck;
        this.num = num;
    }



    public String getNumName() {
        return numName;
    }

    public void setNumName(String numName) {
        this.numName = numName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
