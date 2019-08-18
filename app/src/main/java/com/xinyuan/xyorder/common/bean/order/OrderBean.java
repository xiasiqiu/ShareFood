package com.xinyuan.xyorder.common.bean.order;

import com.xinyuan.xyorder.common.bean.buy.InVoiceBean;
import com.xinyuan.xyorder.common.bean.buy.OrderCouponBean;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeBean;
import com.xinyuan.xyorder.common.constant.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Description：每一个订单信息
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 8850595876117951096L;
    private String addTime;
    private CouponBean coupon;
    private int couponNum;
    private int couponSNId;
    private BigDecimal favourablePrice;
    private String finishTime;
    private boolean isAppraise;
    private boolean isFinishPay;
    private OrderContactBean orderContact;
    private String orderContent;
    private long orderId;
    private String orderName;
    private String orderNum;
    private BigDecimal orderPrice;
    private OrderReserveBean orderReserve;
    private List<PaymentBean> payments;
    private List<OrderCouponBean> canUseCouponList;
    private String shopOpeningTime;
    private String shopClosingTime;
    private List<OrderContactBean> canUseContactList;
    private List<OrderContactBean> canNotUseContactList;
    private List<SelectTimeBean> timeSelecters;
    private long payTimeoutTime;
    private boolean canDrawInvoice; //是否开发票
    private InVoiceBean orderInvoiceInfo;
    /**
     * 订单状态 = ['WAIT_PAY', 'PAYED', 'SHIPPING', 'TRANSACT_FINISHED','CANCELLATION'],
     */
    private String orderStatus;
    private OrderTakeoutBean orderTakeout;
    /**
     * 订单类型 = ['RESERVE', 'TAKEOUT'],
     */
    private String orderType;
    private BigDecimal originalPrice;
    private String payTime;
    private String payment;
    private String paymentSN;
    private long shopId;
    private String shopName;
    private int userId;
    private List<OrderActivitysBean> orderActivitys;
    private List<OrderGoods> orderGoods;
    private String phoneNum;


    public String getShopOpeningTime() {
        return shopOpeningTime;
    }

    public String getShopClosingTime() {
        return shopClosingTime;
    }

    public List<OrderCouponBean> getCanUseCouponList() {
        return canUseCouponList;
    }

    public List<PaymentBean> getPayments() {
        return payments;
    }

    private boolean fromOrderList;

    public String getConvertOrderStatus() {
        String convertOrderStatus = null;
//        if(Constants.ORDER_RESERVE.equals(orderType)){//预定
//
//        }else{//外卖
//
//        }
        switch (orderStatus) {
            case Constants.ORDER_WAIT_PAY:
                convertOrderStatus = "待支付";
                break;
            case Constants.ORDER_PAYED:
                convertOrderStatus = "等待商家接单";
                break;
            case Constants.ORDER_SHIPPING:
                if (Constants.ORDER_RESERVE.equals(orderType)) {
                    convertOrderStatus = "待消费";
                } else {
                    convertOrderStatus = "配送中";
                }
                break;
            case Constants.ORDER_TRANSACT_FINISHED:
                convertOrderStatus = "已完成";
                break;
            case Constants.ORDER_MERCHANT_CONFIRM_RECEIPT:
                convertOrderStatus = "商家确认接单";
                break;
            case Constants.ORDER_WAIT_PICKUP:
                convertOrderStatus = "等待取货";
                break;
            case Constants.ORDER_PICKUPING:
                convertOrderStatus = "取货中";
                break;
            case Constants.ORDER_DELIVERED:
                convertOrderStatus = "已送达";
                break;
            case Constants.ORDER_CANCELLATIONY:
                convertOrderStatus = "已取消";
                break;
        }
        return convertOrderStatus;
    }


    public boolean isCanDrawInvoice() {
        return canDrawInvoice;
    }

    public boolean isAppraise() {
        return isAppraise;
    }

    public boolean isFinishPay() {
        return isFinishPay;
    }

    public long getPayTimeoutTime() {
        return payTimeoutTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<OrderContactBean> getCanUseContactList() {
        return canUseContactList;
    }

    public List<OrderContactBean> getCanNotUseContactList() {
        return canNotUseContactList;
    }

    public List<SelectTimeBean> getTimeSelecters() {
        return timeSelecters;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public CouponBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public int getCouponSNId() {
        return couponSNId;
    }

    public void setCouponSNId(int couponSNId) {
        this.couponSNId = couponSNId;
    }

    public BigDecimal getFavourablePrice() {
        return favourablePrice;
    }

    public void setFavourablePrice(BigDecimal favourablePrice) {
        this.favourablePrice = favourablePrice;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isIsAppraise() {
        return isAppraise;
    }

    public void setIsAppraise(boolean isAppraise) {
        this.isAppraise = isAppraise;
    }

    public boolean isIsFinishPay() {
        return isFinishPay;
    }

    public void setIsFinishPay(boolean isFinishPay) {
        this.isFinishPay = isFinishPay;
    }

    public OrderContactBean getOrderContact() {
        return orderContact;
    }

    public void setOrderContact(OrderContactBean orderContact) {
        this.orderContact = orderContact;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public OrderReserveBean getOrderReserve() {
        return orderReserve;
    }

    public void setOrderReserve(OrderReserveBean orderReserve) {
        this.orderReserve = orderReserve;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderTakeoutBean getOrderTakeout() {
        return orderTakeout;
    }

    public void setOrderTakeout(OrderTakeoutBean orderTakeout) {
        this.orderTakeout = orderTakeout;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentSN() {
        return paymentSN;
    }

    public void setPaymentSN(String paymentSN) {
        this.paymentSN = paymentSN;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<OrderActivitysBean> getOrderActivitys() {
        return orderActivitys;
    }

    public void setOrderActivitys(List<OrderActivitysBean> orderActivitys) {
        this.orderActivitys = orderActivitys;
    }

    public List<OrderGoods> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<OrderGoods> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public boolean isFromOrderList() {
        return fromOrderList;
    }

    public void setFromOrderList(boolean fromOrderList) {
        this.fromOrderList = fromOrderList;
    }

    public InVoiceBean getOrderInvoiceInfo() {
        return orderInvoiceInfo;
    }

    public void setOrderInvoiceInfo(InVoiceBean orderInvoiceInfo) {
        this.orderInvoiceInfo = orderInvoiceInfo;
    }
}
