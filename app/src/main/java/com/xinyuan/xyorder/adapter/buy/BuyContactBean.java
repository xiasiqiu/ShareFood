package com.xinyuan.xyorder.adapter.buy;

import com.xinyuan.xyorder.common.bean.order.OrderContactBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/2715:54
 */

public class BuyContactBean implements Serializable{
    private static final long serialVersionUID = 6772004703251693698L;
    private List<OrderContactBean> canNotUse;
    private List<OrderContactBean> canUse;

    public List<OrderContactBean> getCanUseContactList() {
        return canUse;
    }

    public List<OrderContactBean> getCanNotUseContactList() {
        return canNotUse;
    }
}
