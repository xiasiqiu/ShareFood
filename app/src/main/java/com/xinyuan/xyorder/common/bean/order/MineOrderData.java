package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：我的订单列表
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class MineOrderData implements Serializable{

    private static final long serialVersionUID = 1539459198066655044L;
    private int count;
    private int pageSize;
    private List<OrderBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderBean> list) {
        this.list = list;
    }
}
