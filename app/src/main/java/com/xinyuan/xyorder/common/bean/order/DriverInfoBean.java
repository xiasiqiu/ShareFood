package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * <p>
 * Description：骑手信息
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/23
 */
public class DriverInfoBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String carrierName;
    private String carrierPhone;
    private String latitude;
    private String longitude;

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierPhone() {
        return carrierPhone;
    }

    public void setCarrierPhone(String carrierPhone) {
        this.carrierPhone = carrierPhone;
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
