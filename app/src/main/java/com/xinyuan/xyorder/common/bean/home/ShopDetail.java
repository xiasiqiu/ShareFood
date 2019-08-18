package com.xinyuan.xyorder.common.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：店铺
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/20
 */
public class ShopDetail implements Serializable{
    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String number;

    /**
     * 营业开始时间
     */
    private String beginTime;
    /**
     * 营业结束时间
     */
    private String endTime;
    /**
     * 缩略图
     */
    private String image;
    /**
     * 轮播图
     */
    private List<Carousel> carousel;
    /**
     * 介绍
     */
    private String introduce;
    private long instance;
    private double score;
    private long sales;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Carousel> getCarousel() {
        return carousel;
    }

    public void setCarousel(List<Carousel> carousel) {
        this.carousel = carousel;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public long getInstance() {
        return instance;
    }

    public void setInstance(long instance) {
        this.instance = instance;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }
}
