package com.xinyuan.xyorder.common.bean.store.good;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import com.youth.xframe.utils.XDensityUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/26
 */
public class GoodBean implements Serializable {

    private static final long serialVersionUID = 2635057089007004008L;
    private BigDecimal goodsRateApprise;                            //好评率
    private long goodsSales;                                 //商品销量
    private String goodsAddTime;                            //添加时间
    private long goodsBigClassId;                           //总分类id
    private List<Long> goodsClassIds;                        //分类id集
    private String goodsClassNames;                          // 分类名称集
    private String goodsContent;                            //商品描述
    private long goodsId;                                    //商品ID
    private String goodsImgUrl;                               //商品图片
    private String goodsName;                                  //商品名称
    private BigDecimal goodsPrice;                              //商品价格
    private String goodsStatus; //商品状态 = ['PUTAWAY', 'SOLD_OUT', 'DELETE'],
    private String goodsTag;    //商品标签
    private long shopId;    //店铺ID
    private String shopName;    //店铺名称

    /* 自定义属性*/
    private int selectCount;   //选择数量
    private int position;//位置信息
    private GoodActivityBean activityGoods;
    private List<GoodsSpecifications> goodsSpecifications;//商品规格
    private List<GoodsPropertys> goodsPropertys;//商品属性
    private boolean showSpecSelecter;//是否显示规格选择
    private GoodsSpecifications chooesSpecID;
    private LinkedList<String> chooesProerty;
    private boolean haveAnim;
    private boolean infiniteInventory;  //无限库存
    private int chooesNum;

    public int getChooesNum() {
        return chooesNum;
    }

    public void setChooesNum(int chooesNum) {
        this.chooesNum = chooesNum;
    }

    public boolean isInfiniteInventory() {
        return infiniteInventory;
    }

    public void setInfiniteInventory(boolean infiniteInventory) {
        this.infiniteInventory = infiniteInventory;
    }

    public void setHaveAnim(boolean haveAnim) {
        this.haveAnim = haveAnim;
    }

    public boolean isHaveAnim() {
        return haveAnim;
    }


    public GoodsSpecifications getChooesSpecID() {
        return chooesSpecID;
    }

    public void setChooesSpecID(GoodsSpecifications chooesSpecID) {
        this.chooesSpecID = chooesSpecID;
    }

    public LinkedList<String> getChooesProerty() {
        return chooesProerty;
    }

    public void setChooesProerty(LinkedList<String> chooesProerty) {
        this.chooesProerty = chooesProerty;
    }

    public BigDecimal getGoodsRateApprise() {
        return goodsRateApprise;
    }

    public long getGoodsSales() {
        return goodsSales;
    }

    public GoodActivityBean getActivityGoods() {
        return activityGoods;
    }

    public BigDecimal getGoodRateApprise() {
        return goodsRateApprise;
    }

    public void setGoodRateApprise(BigDecimal goodRateApprise) {
        this.goodsRateApprise = goodRateApprise;
    }

    public long getGoodSales() {
        return goodsSales;
    }

    public void setGoodSales(long goodSales) {
        this.goodsSales = goodSales;
    }

    public String getGoodsAddTime() {
        return goodsAddTime;
    }

    public void setGoodsAddTime(String goodsAddTime) {
        this.goodsAddTime = goodsAddTime;
    }

    public long getGoodsBigClassId() {
        return goodsBigClassId;
    }

    public void setGoodsBigClassId(long goodsBigClassId) {
        this.goodsBigClassId = goodsBigClassId;
    }

    public List<Long> getGoodsClassIds() {
        return goodsClassIds;
    }

    public void setGoodsClassIds(List<Long> goodsClassIds) {
        this.goodsClassIds = goodsClassIds;
    }

    public String getGoodsClassNames() {
        return goodsClassNames;
    }

    public void setGoodsClassNames(String goodsClassNames) {
        this.goodsClassNames = goodsClassNames;
    }

    public String getGoodsContent() {
        return goodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        this.goodsContent = goodsContent;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<GoodsSpecifications> getGoodsSpecifications() {
        return goodsSpecifications;
    }

    public void setGoodsSpecifications(List<GoodsSpecifications> goodsSpecifications) {
        this.goodsSpecifications = goodsSpecifications;
    }

    public List<GoodsPropertys> getGoodsPropertys() {
        return goodsPropertys;
    }

    public void setGoodsPropertys(List<GoodsPropertys> goodsPropertys) {
        this.goodsPropertys = goodsPropertys;
    }

    public boolean isShowSpecSelecter() {
        return showSpecSelecter;
    }

    public void setShowSpecSelecter(boolean showSpecSelecter) {
        this.showSpecSelecter = showSpecSelecter;
    }

    public SpannableString getStrPrice() {
        String priceStr = String.valueOf(getGoodsPrice());
        SpannableString spanString = new SpannableString("¥" + priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(XDensityUtils.sp2px(11));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    public SpannableString getStrPrice(BigDecimal price) {
        String priceStr = String.valueOf(price);
        SpannableString spanString = new SpannableString("¥" + priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(XDensityUtils.sp2px(11));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    @Override
    public String toString() {
        return "GoodBean{" +
                ", goodsRateApprise=" + goodsRateApprise +
                ", goodsSales=" + goodsSales +
                ", goodsAddTime='" + goodsAddTime + '\'' +
                ", goodsBigClassId=" + goodsBigClassId +
                ", goodsClassIds=" + goodsClassIds +
                ", goodsClassNames='" + goodsClassNames + '\'' +
                ", goodsContent='" + goodsContent + '\'' +
                ", goodsId=" + goodsId +
                ", goodsImgUrl='" + goodsImgUrl + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsStatus='" + goodsStatus + '\'' +
                ", goodsTag='" + goodsTag + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", selectCount=" + selectCount +
                ", position=" + position +
                ", activityGoods=" + activityGoods +
                '}';
    }
}
