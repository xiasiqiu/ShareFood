package com.xinyuan.xyorder.common.bean.home;

import java.io.Serializable;

/**
 * <p>
 * Description：店铺详情图
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/20
 */
public class Carousel implements Serializable{

    private Long imgId;
    private Long shopId;
    private String imgName;
    private String imgUrl;

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
