package com.xinyuan.xyorder.common.bean.store;

import com.xinyuan.xyorder.common.bean.store.store.StoreEvaluateBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/2015:47
 * 店铺评论列表数据
 */

public class StoreEvaluateData implements Serializable {
    private static final long serialVersionUID = 3147066924644955127L;
    private int count;
    private int pageSize;
    private List<StoreEvaluateBean> list;

    public int getCount() {
        return count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<StoreEvaluateBean> getList() {
        return list;
    }
}
