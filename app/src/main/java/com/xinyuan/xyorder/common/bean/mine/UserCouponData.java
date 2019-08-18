package com.xinyuan.xyorder.common.bean.mine;

import com.xinyuan.xyorder.common.bean.buy.OrderCouponBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/2411:54
 */

public class UserCouponData implements Serializable {
    private static final long serialVersionUID = 7222942632445107709L;
    private int count;
    private int pageSize;
    private List<OrderCouponBean> list;

    public int getCount() {
        return count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<OrderCouponBean> getList() {
        return list;
    }
}
