package com.xinyuan.xyorder.common.bean.store.good;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description： 商品分类
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/26
 */
public class GoodsCategory implements Serializable {
    private static final long serialVersionUID = 2314293035719208920L;
    private String goodsCategoryName;   //商品分类名称
    private int sortOrder;              // 排序值
    private List<GoodBean> goodsList;   //商品列表
    private long goodsCategoryId;       //分类ID


    public String getGoodsCategoryName() {
        return goodsCategoryName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public List<GoodBean> getGoodsList() {
        return goodsList;
    }

    public long getGoodsCategoryId() {
        return goodsCategoryId;
    }

}
