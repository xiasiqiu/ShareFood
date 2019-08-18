package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by f-x on 2017/10/1811:07
 */

public class ChoeseTimeContentBean implements Serializable{
    private static final long serialVersionUID = 3556242120398439766L;
    private String time;
    private String price;
    private boolean isChecked;
    private Date date;
    private long contentTimeId;

    public ChoeseTimeContentBean(String time, Date date, String price, long contentTimeId) {
        this.time = time;
        this.price = price;
        this.date = date;
        this.contentTimeId = contentTimeId;
    }

    public Date getDate() {
        return date;
    }

    public long getContentTimeId() {
        return contentTimeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ChoeseTimeContentBean{" +
                "time='" + time + '\'' +
                ", price='" + price + '\'' +
                ", isChecked=" + isChecked +
                ", contentTimeId=" + contentTimeId +
                '}';
    }
}

