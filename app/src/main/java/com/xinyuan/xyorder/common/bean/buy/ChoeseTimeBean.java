package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/1810:39
 */

public class ChoeseTimeBean implements Serializable {
    private static final long serialVersionUID = -2992608837989258099L;
    private String day;
    private List<ChoeseTimeContentBean> timeContentList;
    private boolean isChecked;
    private long timeId;

    public ChoeseTimeBean(String day, List<ChoeseTimeContentBean> timeContentList, long timeId) {
        this.day = day;
        this.timeContentList = timeContentList;
        this.timeId = timeId;
    }

    public long getTimeId() {
        return timeId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDay() {
        return day;
    }

    public List<ChoeseTimeContentBean> getTimeContentList() {
        return timeContentList;
    }

    @Override
    public String toString() {
        return "ChoeseTimeBean{" +
                "day='" + day + '\'' +
                ", timeContentList=" + timeContentList +
                ", isChecked=" + isChecked +
                ", timeId=" + timeId +
                '}';
    }
}
