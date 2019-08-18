package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/1916:47
 */

public class LocalTime implements Serializable {
    private static final long serialVersionUID = -45893451285693569L;
    private int hour;
    private int minute;
    private int nano;
    private int second;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getNano() {
        return nano;
    }

    public int getSecond() {
        return second;
    }
}
