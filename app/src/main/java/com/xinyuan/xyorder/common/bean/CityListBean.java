package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：城市列表
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/3
 */
public class CityListBean implements Serializable {

    private static final long serialVersionUID = -789768640128874994L;
    private List<CityBean> cities;
    private List<CityBean> hots;

    public List<CityBean> getCities() {
        return cities;
    }

    public void setCities(List<CityBean> cities) {
        this.cities = cities;
    }

    public List<CityBean> getHots() {
        return hots;
    }

    public void setHots(List<CityBean> hots) {
        this.hots = hots;
    }

    @Override
    public String toString() {
        return "CityListBean{" +
                "cities=" + cities +
                ", hots=" + hots +
                '}';
    }
}
