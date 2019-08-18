package com.xinyuan.xyorder.common.even;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/21
 */
public class OrderRefreshEven {

    public OrderRefreshEven(boolean Refresh) {
        this.refresh = Refresh;
    }

    private boolean refresh;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
