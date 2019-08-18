package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/3
 */
public class CityBean implements Serializable {

    private static final long serialVersionUID = 6287146199130930428L;
    private String cityName;
    private long cityId;
    private long provinceId;
    private boolean hot;
    private String pinyin;

    public CityBean() {

    }

    public CityBean(String name, String pinyin, long num) {
        super();
        this.cityName = name;
        this.pinyin = pinyin;
        this.cityId = num;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "cityName='" + cityName + '\'' +
                ", cityId=" + cityId +
                ", provinceId=" + provinceId +
                ", hot=" + hot +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
