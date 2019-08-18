package com.xinyuan.xyorder.common.bean.home;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/2518:47
 */

public class LocationBean implements Serializable {
    private static final long serialVersionUID = 2712988764876701999L;
    private Double latitude=0.0;
    private Double longitude=0.0;
    private String currentyCity;

    public LocationBean() {
    }

    public LocationBean(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCurrentyCity() {
        return currentyCity;
    }

    public void setCurrentyCity(String currentyCity) {
        this.currentyCity = currentyCity;
    }


    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
