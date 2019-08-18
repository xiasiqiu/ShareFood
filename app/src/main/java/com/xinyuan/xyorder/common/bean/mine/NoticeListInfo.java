package com.xinyuan.xyorder.common.bean.mine;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class NoticeListInfo implements Serializable{
    private static final long serialVersionUID = 5379710426439437967L;
    private List<NoticeInfo> list;

    public List<NoticeInfo> getList() {
        return list;
    }

    public void setList(List<NoticeInfo> list) {
        this.list = list;
    }
}
