package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/11/2814:31
 */

public class MyInvoiceData implements Serializable {
    private static final long serialVersionUID = -1207383220568968373L;
    private int count;
    private int pageSize;
    private List<InVoiceBean> list;

    public int getCount() {
        return count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<InVoiceBean> getList() {
        return list;
    }
}
