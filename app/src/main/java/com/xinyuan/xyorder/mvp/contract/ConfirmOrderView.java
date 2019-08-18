package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.OrderBean;

/**
 * Created by f-x on 2017/10/2020:25
 */

public interface ConfirmOrderView {
    void showConfirmInfo(OrderBean orderBean);

    void postOrderState(OrderBean orderStatus);

    void aliPayResult(String data);

    void wxResult(WXPayBean wxPayBean);

    void balanceResult(Boolean data);

    void errorBack();
}
