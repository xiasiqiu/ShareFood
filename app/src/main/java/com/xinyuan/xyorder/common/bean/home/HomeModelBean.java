package com.xinyuan.xyorder.common.bean.home;

import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：首页总数据
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/20
 */
public class HomeModelBean implements Serializable{
    private List<BannerBean> banners;
    private List<ShopCategory> shopCategories;
    private List<StoreDetail> shopList;

    public List<BannerBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerBean> banners) {
        this.banners = banners;
    }

    public List<ShopCategory> getShopCategories() {
        return shopCategories;
    }

    public void setShopCategories(List<ShopCategory> shopCategories) {
        this.shopCategories = shopCategories;
    }

    public List<StoreDetail> getShopList() {
        return shopList;
    }

    public void setShopList(List<StoreDetail> shopList) {
        this.shopList = shopList;
    }
}
