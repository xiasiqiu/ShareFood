package com.xinyuan.xyorder.common.even;

import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodsCategory;

import java.util.List;

/**
 * <p>
 * Description：选中的商品
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/9
 */
public class ChosesGoodsEvent {
    private List<GoodBean> checkList;
    private List<GoodsCategory> data;

    public ChosesGoodsEvent(List<GoodBean> checkList, List<GoodsCategory> data) {
        this.checkList = checkList;
        this.data = data;
    }

    public List<GoodBean> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<GoodBean> checkList) {
        this.checkList = checkList;
    }

    public List<GoodsCategory> getData() {
        return data;
    }

    public void setData(List<GoodsCategory> data) {
        this.data = data;
    }
}

