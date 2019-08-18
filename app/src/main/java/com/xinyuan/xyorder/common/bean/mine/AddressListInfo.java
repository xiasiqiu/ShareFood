package com.xinyuan.xyorder.common.bean.mine;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/14
 */
public class AddressListInfo implements Serializable{
    private static final long serialVersionUID = 8569296588461000024L;
    private int count;
    private List<AddressInfo> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AddressInfo> getList() {
        return list;
    }

    public void setList(List<AddressInfo> list) {
        this.list = list;
    }
}
