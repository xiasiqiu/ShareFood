package com.xinyuan.xyorder.common.bean.store.store;

import com.xinyuan.xyorder.common.bean.home.Carousel;
import com.xinyuan.xyorder.common.bean.mine.UserCardBean;
import com.xinyuan.xyorder.common.bean.store.ShopActiveBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Description：店铺详情
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/26
 */
public class StoreDetail implements Serializable {
    private static final long serialVersionUID = -8092382864569902758L;
    private String address;             //地址
    private long areaId;                //地区ID
    private String audit;               //审核状态['WAIT_AUDIT', 'AUDIT_ADOPT', 'AUDIT_UNADOPT'],
    private StoreLicenseBean bLicense;  //营业执照
    private String busBeginTime;     //营业开始时间
    private String busEndTime;       //营业结束时间
    private List<Carousel> carousel;    //轮播图
    private long cityId;                //城市ID
    private StoreFoodLicenseBean fLicense;//餐饮许可证
    private BigDecimal fee;             //配送费；
    private UserCardBean idCard;        //身份证
    private BigDecimal distance;              //距离
    private String introduce;           //介绍
    private BigDecimal latitude;        //纬度
    private BigDecimal longitude;       //经度
    private String logoUrl;             //店铺logo
    private String name;                //联系人姓名
    private BigDecimal preConsumption;         //人均
    private BigDecimal minDeliveryPrice ;         //起送价
    private String phoneNum;           //商户注册手机号
    private long provinceId;           //省ID
    private long shopMonthlySales;                 //销量
    private int shopRate;                 //评分
    private long sellerId;              //卖家ID
    private SettleBean settle;          //结算信息
    private Boolean shelves;            //上架与下架
    private List<Long> shopCategoryIdList;// 店铺分类ID集合
    private String shopCategoryName;    // 店铺分类
    private String shopFaceUrl;         // 店铺门脸照
    private long shopId;                //店铺ID
    private String shopInnerUrl;       // 店内照
    private String shopName;            //店铺名称
    private String shopType;            //店铺类型 = ['RESERVE', 'TAKEOUT', 'RESERVE_TAKEOUT'],
    private String takeOutPhone;        //外卖电话
    private String unauditReason;       //审核不通过原因
    private List<ShopActiveBean> shopActive;    //店铺活动
    private boolean collectioned;
    private boolean operatingState; //营业状态


    public boolean isOperatingState() {
        return operatingState;
    }

    public BigDecimal getMinDeliveryPrice() {
        return minDeliveryPrice;
    }

    public boolean isCollectioned() {
        return collectioned;
    }

    public void setCollectioned(boolean collectioned) {
        this.collectioned = collectioned;
    }

    public void setShopActive(List<ShopActiveBean> shopActive) {
        this.shopActive = shopActive;
    }

    public List<ShopActiveBean> getShopActive() {
        return shopActive;
    }

    public String getAddress() {
        return address;
    }

    public long getAreaId() {
        return areaId;
    }

    public String getAudit() {
        return audit;
    }

    public StoreLicenseBean getbLicense() {
        return bLicense;
    }

    public String getBusBeginTime() {
        return busBeginTime;
    }

    public String getBusEndTime() {
        return busEndTime;
    }

    public List<Carousel> getCarousel() {
        return carousel;
    }

    public long getCityId() {
        return cityId;
    }

    public StoreFoodLicenseBean getfLicense() {
        return fLicense;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public UserCardBean getIdCard() {
        return idCard;
    }

    public BigDecimal getInstance() {
        return distance;
    }

    public String getIntroduce() {
        return introduce;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPerson() {
        return preConsumption;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public long getSales() {
        return shopMonthlySales;
    }

    public int getScore() {
        return shopRate;
    }

    public long getSellerId() {
        return sellerId;
    }

    public SettleBean getSettle() {
        return settle;
    }

    public Boolean getShelves() {
        return shelves;
    }

    public List<Long> getShopCategoryIdList() {
        return shopCategoryIdList;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public String getShopFaceUrl() {
        return shopFaceUrl;
    }

    public long getShopId() {
        return shopId;
    }

    public String getShopInnerUrl() {
        return shopInnerUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public String getTakeOutPhone() {
        return takeOutPhone;
    }

    public String getUnauditReason() {
        return unauditReason;
    }


    @Override
    public String toString() {
        return "StoreDetail{" +
                "address='" + address + '\'' +
                ", areaId=" + areaId +
                ", audit='" + audit + '\'' +
                ", bLicense=" + bLicense +
                ", busBeginTime=" + busBeginTime +
                ", busEndTime=" + busEndTime +
                ", carousel=" + carousel +
                ", cityId=" + cityId +
                ", fLicense=" + fLicense +
                ", fee=" + fee +
                ", idCard=" + idCard +
                ", distance=" + distance +
                ", introduce='" + introduce + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", logoUrl='" + logoUrl + '\'' +
                ", name='" + name + '\'' +
                ", person=" + preConsumption +
                ", phoneNum='" + phoneNum + '\'' +
                ", provinceId=" + provinceId +
                ", shopMonthlySales=" + shopMonthlySales +
                ", shopRate =" + shopRate +
                ", sellerId=" + sellerId +
                ", settle=" + settle +
                ", shelves=" + shelves +
                ", shopCategoryIdList=" + shopCategoryIdList +
                ", shopCategoryName='" + shopCategoryName + '\'' +
                ", shopFaceUrl='" + shopFaceUrl + '\'' +
                ", shopId=" + shopId +
                ", shopInnerUrl='" + shopInnerUrl + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopType='" + shopType + '\'' +
                ", takeOutPhone='" + takeOutPhone + '\'' +
                ", unauditReason='" + unauditReason + '\'' +
                '}';
    }
}



