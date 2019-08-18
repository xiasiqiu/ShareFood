package com.xinyuan.xyorder.common.bean.buy;

import java.util.LinkedList;

/**
 * Created by f-x on 2017/12/22  09:16
 * Description
 */

public class SpecFlag {
    private long goodsId;
    private long sepcId;
    private LinkedList<String> chooesProertys;

    public SpecFlag(long goodsId, long sepcId, LinkedList<String> chooesProertys) {
        this.goodsId = goodsId;
        this.sepcId = sepcId;
        this.chooesProertys = chooesProertys;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public long getSepcId() {
        return sepcId;
    }

    public LinkedList<String> getChooesProertys() {
        return chooesProertys;
    }


    @Override
    public int hashCode() {
        int result = (int) (goodsId ^ (goodsId >>> 32));
        result = 31 * result + (int) (sepcId ^ (sepcId >>> 32));
        result = 31 * result + (chooesProertys != null ? chooesProertys.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpecFlag{" +
                "goodsId=" + goodsId +
                ", sepcId=" + sepcId +
                ", chooesProertys=" + chooesProertys +
                '}';
    }
}
