package com.xinyuan.xyorder.common.bean.buy;

import com.xinyuan.xyorder.common.bean.store.good.GoodActivityBean;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Created by f-x on 2017/12/22  10:34
 * Description
 */

public class ChooesCarGoodBean {
    private long goodId;
    private long specId;
    private LinkedList<String> chooesProertys;
    private String goodsName;
    private BigDecimal goodPrice;
    private GoodActivityBean goodActivityBean;
    private long goodStock;
    private int chooesNum;
    private int selecCount;
    private BigDecimal feelPrice;
    private String sepcName;
    private boolean isShowSpecSelecter;

    public ChooesCarGoodBean(long goodId) {
        this.goodId = goodId;
    }

    public ChooesCarGoodBean(long goodId, long specId, String sepcName, LinkedList<String> chooesProertys, String goodsName,
                             BigDecimal goodPrice, GoodActivityBean goodActivityBean, long goodStock, int chooesNum, int selecCount,
                             BigDecimal feelPrice) {
        this.goodId = goodId;
        this.specId = specId;
        this.sepcName = sepcName;
        this.chooesProertys = chooesProertys;
        this.goodsName = goodsName;
        this.goodPrice = goodPrice;
        this.goodActivityBean = goodActivityBean;
        this.goodStock = goodStock;
        this.chooesNum = chooesNum;
        this.selecCount = selecCount;
        this.feelPrice = feelPrice;
    }

    public boolean isShowSpecSelecter() {
        return isShowSpecSelecter;
    }

    public void setShowSpecSelecter(boolean showSpecSelecter) {
        isShowSpecSelecter = showSpecSelecter;
    }

    public String getSepcName() {
        return sepcName;
    }

    public void setSepcName(String sepcName) {
        this.sepcName = sepcName;
    }

    public void setFeelPrice(BigDecimal feelPrice) {
        this.feelPrice = feelPrice;
    }

    public BigDecimal getFeelPrice() {
        return feelPrice;
    }

    public int getSelecCount() {
        return selecCount;
    }

    public void setSelecCount(int selecCount) {
        this.selecCount = selecCount;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public void setSpecId(long specId) {
        this.specId = specId;
    }

    public void setChooesProertys(LinkedList<String> chooesProertys) {
        this.chooesProertys = chooesProertys;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodPrice(BigDecimal goodPrice) {
        this.goodPrice = goodPrice;
    }

    public void setGoodActivityBean(GoodActivityBean goodActivityBean) {
        this.goodActivityBean = goodActivityBean;
    }

    public void setGoodStock(long goodStock) {
        this.goodStock = goodStock;
    }

    public void setChooesNum(int chooesNum) {
        this.chooesNum = chooesNum;
    }

    public int getChooesNum() {
        return chooesNum;
    }

    public long getGoodId() {
        return goodId;
    }

    public long getSpecId() {
        return specId;
    }

    public LinkedList<String> getChooesProertys() {
        return chooesProertys;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public BigDecimal getGoodPrice() {
        return goodPrice;
    }

    public GoodActivityBean getGoodActivityBean() {
        return goodActivityBean;
    }

    public long getGoodStock() {
        return goodStock;
    }
}
