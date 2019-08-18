package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2615:04
 */

public class SelectTimeContentBean implements Serializable {
    private String time;
    private boolean isCheck;
    private long date;
    private long currentDate;
    private String price;

    public SelectTimeContentBean(String time, boolean isCheck, long date, long currentDate, String price) {
        this.time = time;
        this.isCheck = isCheck;
        this.date = date;
        this.currentDate = currentDate;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(long currentDate) {
        this.currentDate = currentDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SelectTimeContentBean{" +
                "time='" + time + '\'' +
                ", isCheck=" + isCheck +
                ", date=" + date +
                ", currentDate=" + currentDate +
                ", price='" + price + '\'' +
                '}';
    }
}
