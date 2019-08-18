package com.xinyuan.xyorder.common.bean.buy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by f-x on 2017/10/2320:30
 */

public class RedPacketData implements Serializable {
    private static final long serialVersionUID = 8398602073100644577L;
    private int count;
    private List<RedPacketBean> list;
    private int pageSize;

    public int getCount() {
        return count;
    }

    public List<RedPacketBean> getList() {
        return list;
    }

    public int getPageSize() {
        return pageSize;
    }
}
