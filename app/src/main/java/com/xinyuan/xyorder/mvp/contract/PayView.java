package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.buy.WXPayBean;

/**
 * Created by f-x on 2017/11/217:33
 */

public interface PayView {

    void aliPayResult(String data);

    void wxResult(WXPayBean wxPayBean);

    void balanceResult(boolean status);
}
