package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.order.OrderBean;

/**
 * Created by f-x on 2017/10/2020:25
 */

public interface ConfirmReOrderView {
    void showConfirmInfo(OrderBean orderBean);

    void postOrderState(OrderBean orderBean);

}
