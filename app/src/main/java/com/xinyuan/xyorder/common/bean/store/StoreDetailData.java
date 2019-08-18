package com.xinyuan.xyorder.common.bean.store;

import com.xinyuan.xyorder.common.bean.store.good.GoodsCategory;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description： 店铺详情页数据
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/26
 */
public class StoreDetailData implements Serializable {

    private static final long serialVersionUID = -1832420751771527812L;
    private List<GoodsCategory> categorys;
    private StoreDetail detail;



    public List<GoodsCategory> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<GoodsCategory> categorys) {
        this.categorys = categorys;
    }

    public StoreDetail getDetail() {
        return detail;
    }

    public void setDetail(StoreDetail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "StoreDetailData{" +
                "categorys=" + categorys +
                ", detail=" + detail +
                '}';
    }
}
