package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by f-x on 2017/10/2615:04
 */

public class SelectTimeBean implements Serializable {
    private static final long serialVersionUID = -1655386344881309433L;
    private List<String> times;
    private boolean today;
    private BigDecimal delivesryCost;
    private long date;
    private boolean isCheck;
    private List<SelectTimeContentBean> contentTime;
    private String day_name;


    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public BigDecimal getDeliveryCost() {
        return delivesryCost;
    }

    public void setDeliversyCost(BigDecimal deliveryCost) {
        this.delivesryCost = deliveryCost;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<SelectTimeContentBean> getContentTime() {
        return contentTime;
    }

    public void setContentTime(List<SelectTimeContentBean> contentTime) {
        this.contentTime = contentTime;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    @Override
    public String toString() {
        return "SelectTimeBean{" +
                "times=" + times +
                ", today=" + today +
                ", deliveryCost=" + delivesryCost +
                ", date=" + date +
                ", isCheck=" + isCheck +
                ", contentTime=" + contentTime +
                ", day_name='" + day_name + '\'' +
                '}';
    }
}
