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
public class LatLngInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private GeoBean buyer;
    private GeoBean carrier;
    private GeoBean shop;

    public GeoBean getBuyer() {
        return buyer;
    }

    public void setBuyer(GeoBean buyer) {
        this.buyer = buyer;
    }

    public GeoBean getCarrier() {
        return carrier;
    }

    public void setCarrier(GeoBean carrier) {
        this.carrier = carrier;
    }

    public GeoBean getShop() {
        return shop;
    }

    public void setShop(GeoBean shop) {
        this.shop = shop;
    }
}
