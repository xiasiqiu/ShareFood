package com.xinyuan.xyorder.common.even;

import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;

import java.util.List;

/**
 * Created by f-x on 2017/10/2111:21
 */

public class BuyBusEven {
    private String flag;
    private Object obj;
    public static final String ADDORDER = "ADDORDER";                   //添加订单
    public static final String CHOOESETIME = "CHOOESETIME";             //选择时间
    public static final String CHOOESEPAY = "CHOOESEPAY";             //选择支付方式
    public static final String CHOOESENUM = "CHOOESENUM";            //选择人数
    public static final String CHOOESEUSER = "CHOOESEUSER";         //选择收货地址
    public static final String CHOOESECOUPON = "CHOOESECOUPON";     //选择红包
    public static final String CHOOESEINVOICE = "CHOOESEINVOICE";   //选择发票
    public static final String NOCHOOESEINVOICE = "NOCHOOESEINVOICE";   //选择发票
    public static final String NOCHOOESECOUPON = "NOCHOOESECOUPON"; //不选择红包
    public static final String WXPAY = "FINISH_PAY";

    private List<ChooesCarGoodBean> selectGood;

    public BuyBusEven(String flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public BuyBusEven(String flag, List<ChooesCarGoodBean> selectGood) {
        this.flag = flag;
        this.selectGood = selectGood;
    }

    public List<ChooesCarGoodBean> getSelectGood() {
        return selectGood;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
