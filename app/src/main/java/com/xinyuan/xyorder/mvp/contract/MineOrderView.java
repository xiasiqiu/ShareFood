package com.xinyuan.xyorder.mvp.contract;

import com.xinyuan.xyorder.common.bean.order.OrderBean;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public interface MineOrderView {
    void showState(int state);

    void showOrder(List<OrderBean> itemList);

}
