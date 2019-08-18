package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/27
 */
public class GeoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String latitude;
    private String longitude;

    public GeoBean(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
