package com.xinyuan.xyorder.adapter.buy;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by f-x on 2017/12/23  17:25
 * Description
 */

public class OrderGoodSpecBean implements Serializable {
    private static final long serialVersionUID = 4324204356065259558L;
    private LinkedList<String> properties;
    private int quantity;
    private long specId;

    public LinkedList<String> getProperties() {
        return properties;
    }

    public void setProperties(LinkedList<String> properties) {
        this.properties = properties;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getSpecId() {
        return specId;
    }

    public void setSpecId(long specId) {
        this.specId = specId;
    }
}
