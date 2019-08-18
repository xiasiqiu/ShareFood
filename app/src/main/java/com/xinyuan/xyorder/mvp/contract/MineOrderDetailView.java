package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.DriverInfoBean;
import com.xinyuan.xyorder.common.bean.order.LatLngInfo;
import com.xinyuan.xyorder.common.bean.order.OrderBean;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/18
 */
public interface MineOrderDetailView {
    void showState(int state);

    void showOrderDetailInfo(OrderBean mineOrderItemBean);
    void aliPayResult(String data);

    void wxResult(WXPayBean wxPayBean);
    void cancleOrder();
    void showDriverInfo(DriverInfoBean info);//显示骑手位置
    void showLatLng(LatLngInfo info);//显示经纬度信息
}
