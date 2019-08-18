package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * <p>
 * Description：预订订单信息
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class OrderReserveBean implements Serializable{

    private static final long serialVersionUID = 1L;
    private boolean isPrivateRoom;//是否包房
    private int orderId;
    private int repastNum;
    private String repastTime;

    public boolean isIsPrivateRoom() {
        return isPrivateRoom;
    }

    public void setIsPrivateRoom(boolean isPrivateRoom) {
        this.isPrivateRoom = isPrivateRoom;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRepastNum() {
        return repastNum;
    }

    public void setRepastNum(int repastNum) {
        this.repastNum = repastNum;
    }

    public String getRepastTime() {
        return repastTime;
    }

    public void setRepastTime(String repastTime) {
        this.repastTime = repastTime;
    }
}
