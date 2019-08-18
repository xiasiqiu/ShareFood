package com.xinyuan.xyorder.common.bean.store;

import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/13.
 * 店铺列表数据
 */

public class StoreListData implements Serializable {
    private static final long serialVersionUID = -4096187764080478764L;
    private int count;
    private int pageSize;
    private List<StoreDetail> list;

    public int getCount() {
        return count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<StoreDetail> getList() {
        return list;
    }
}
