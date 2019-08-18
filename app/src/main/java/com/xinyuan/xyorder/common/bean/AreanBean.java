package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/14.
 */

public class AreanBean implements Serializable {
    private static final long serialVersionUID = -1380516336070940400L;
    private String areaName;
    private String areaId;
    private String cityId;

    public String getCityId() {
        return cityId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
