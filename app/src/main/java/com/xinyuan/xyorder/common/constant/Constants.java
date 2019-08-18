package com.xinyuan.xyorder.common.constant;

import android.os.Environment;

import com.xinyuan.xyorder.BuildConfig;

/**
 * <p>
 * Description：常量及urls
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/15
 */

public class Constants {


    public static final boolean ISTEST = false;

    //    public static final String HOSTNAME = "http://api.test.gongxiangdiancan.com";
    public static final String HOSTNAME = BuildConfig.API_URL;
    //    public static final String HOSTNAME = "http://101.207.125.108:7575";
//    public static final String IMAGE_HOST = "http://cdn.test.gongxiangdiancan.com";
    public static final String IMAGE_HOST = "http://cdn.gongxiangdiancan.com";
    public static final String MINE_PIC = "http://cdn.gongxiangdiancan.com/operation/buy_vip_inlet.png";

    public static final String APK_DOWN = "http://apkuploads.gjgxjj.com/";
    public static final String APK = "http://apk.gjgxjj.com/";
    public static final String userRegistrationProtocol = "http://support.gongxiangdiancan.com/RegistrationAgreement.html";//用户协议                                                                                                                                                                                                                                  `diancan.com/RegistrationAgreement.html";//用户注册协议
    public static final String disclaimer = "http://support.gongxiangdiancan.com/LegalClaim.html";//免责申明

    public static final String API_GET_CODE = HOSTNAME + "/commons/phoneCode";
    public static final String API_HOME = HOSTNAME + "/api/index/";
    public static final String API_SHOP_DETAIL = HOSTNAME + "/api/shopDetail/";
    public static final String API_SHOP_CATEGORY = HOSTNAME + "/api/shopCategory";//店铺分类
    public static final String API_TAKEPREVIEW = HOSTNAME + "/api/order/takePreview";//外卖订单预览

    public static final String API_RES_CONTACT = HOSTNAME + "/api/contact/getUseInfo/all";//获取预定订单联系人列表
    public static final String API_SEND_CONTACT = HOSTNAME + "/api/contact/getUseInfo/";//获取外卖联系人列表

    public static final String API_PREVIEW = HOSTNAME + "/api/order/preview";//预定订单预览
    public static final String API_TAKESAVE = HOSTNAME + "/api/order/takeSave";//外卖订单提交
    public static final String API_SAVE = HOSTNAME + "/api/order/save";//预定订单提交
    public static final String API_ORDER_ALIPAY = HOSTNAME + "/pay/alipay/app/";//支付宝支付
    public static final String API_ORDER_WX = HOSTNAME + "/pay/wx/app/";//微信支付
    public static final String API_ORDER_SUER_ORDER = HOSTNAME + "/api/order/orderReceipt";//确认收货
    public static final String API_ORDER_BALANLCE = HOSTNAME + "/pay/balance/app/";//微信支付
    public static final String API_NOTICE = HOSTNAME + "/api/notice";
    public static final String API_MINE_ORDER = HOSTNAME + "/api/order";
    public static final String API_SHOPAPPRAISE = HOSTNAME + "/api/shopAppraise";
    public static final String API_CANCLE_ORDER = HOSTNAME + "/api/order/orderCancel";
    public static final String API_CANCLE_ORDER_REASON = HOSTNAME + "/api/order/orderCancelReason";
    public static final String API_ORDER_TRACKING = HOSTNAME + "/api/order/orderSchedules/";
    public static final String API_MINE_COLLECTION = HOSTNAME + "/api/collection";
    public static final String API_COLLECTION_BATCH_DELETE = HOSTNAME + "/api/collection/batchDelete";
    public static final String API_INVOICEINFO = HOSTNAME + "/api/invoiceInfo";//发票信息
    public static final String API_GET_ORDER_EVELUATION = HOSTNAME + "/api/shopAppraise/order/";//通过订单查询评价详情


    public static final String API_UPLOAD_PHOTO = HOSTNAME + "/api/user/avator";        //修改用户头像
    public static final String API_ADD_USER = HOSTNAME + "/api/contact";                //收货地址的增，删，编辑
    public static final String API_USER_Login = HOSTNAME + "/api/user/login";           //用户登录
    public static final String API_USER_LogOut = HOSTNAME + "/api/user/logout";           //用户退出登录
    public static final String API_USER_UPDATE = HOSTNAME + "/api/user/nickname";       //修改用户名
    public static final String API_MINE_USER = HOSTNAME + "/api/user";                  //获取用户信息
    public static final String API_MINE_USER_COUPON = HOSTNAME + "/api/couponSN";              //获取用户优惠券列表
    public static final String API_MINE_USER_REFRESHTOKEN = HOSTNAME + "/api/user/refreshToken";              //刷新用户Token
    public static final String API_CHECK_NOTICE = HOSTNAME + "/api/notice/getNotRead";              //检查有无新消息
    public static final String API_PAY_TYPE = HOSTNAME + "/api/order/payment/";              //支付方式
    public static final String API_APP_UPDATE = APK + "/commons/versions";              //更新
    public static final String API_GET_DRIVER = HOSTNAME + "/api/order/carrier/";//获取骑手位置
    public static final String API_GET_LATLNGINFO = HOSTNAME + "/api/order/geoInfo/";//获取经纬度信息
    public static final String API_GET_APPRAISE_TAG = HOSTNAME + "/api/appraisetag";//获取所有评价标签
    public static final String API_UPLOAD_IMG = HOSTNAME + "/commons/upload/uploadFileList";//上传图片

    /**
     * 店铺活动状态=["SALE"打折，"FIRST"首减，"DELGOLD"满减，"COMPLIMENTARY"满送优惠券，"SPECIALPRICES"特价，"SPECIFIC"全场五折]
     */
    public static final String SHOP_ACTIVE_SALE = "SALE";
    public static final String SHOP_ACTIVE_FIRST = "FIRST";
    public static final String SHOP_ACTIVE_DELGOLD = "DELGOLD";
    public static final String SHOP_ACTIVE_COMPLIMENTARY = "COMPLIMENTARY";
    public static final String SHOP_ACTIVE_SPECIALPRICES = "SPECIALPRICES";
    public static final String SHOP_ACTIVE_SPECIFIC = "SPECIFIC";

    /**
     * 订单状态 = ['WAIT_PAY', 'PAYED', 'SHIPPING', 'TRANSACT_FINISHED','CANCELLATION'],
     */
    public static final String ORDER_WAIT_PAY = "WAIT_PAY";//	等待支付
    public static final String ORDER_PAYED = "PAYED";//	支付完成,等待发货
    public static final String ORDER_SHIPPING = "SHIPPING";//	配送中
    public static final String ORDER_TRANSACT_FINISHED = "TRANSACT_FINISHED";//	交易完成
    public static final String ORDER_CANCELLATIONY = "CANCELLATION";//	订单取消
    public static final String ORDER_MERCHANT_CONFIRM_RECEIPT = "MERCHANT_CONFIRM_RECEIPT";// 商家确认接单
    public static final String ORDER_WAIT_PICKUP = "WAIT_PICKUP";//等待取货
    public static final String ORDER_PICKUPING = "PICKUPING";//取货中
    public static final String ORDER_DELIVERED = "DELIVERED";//已送达

    //通知类型
    public static final String NOTICE_ORDER_CANCELLATION = "ORDER_CANCELLATION";
    public static final String NOTICE_ORDER_DELIVERED = "ORDER_DELIVERED";
    public static final String NOTICE_ORDER_CONFIRM = "ORDER_CONFIRM";
    public static final String NOTICE_ORDER_SHIPPING = "ORDER_SHIPPING";
    public static final String NOTICE_ORDER_CREATE = "ORDER_CREATE";
    public static final String NOTICE_ORDER_FINISHED = "ORDER_FINISHED";


    /**
     * 订单类型 = ['RESERVE', 'TAKEOUT']
     */
    public static final String ORDER_RESERVE = "RESERVE";//预定订单
    public static final String ORDER_TAKEOUT = "TAKEOUT";//外卖订单

    /**
     * 店铺类型=RESERVE，TAKEOUT，RESERVE_TAKEOUT
     */
    public static final String STORE_RESERVE = "RESERVE";
    public static final String STORE_TAKEOUT = "TAKEOUT";
    public static final String STORE_RESERVE_TAKEOUT = "RESERVE_TAKEOUT";
    public static final int PAGE_SIZE = 10;
    public static final String API_TOKEN = "TOKEN";
    public static final String SELF_DELIVERY_BY_MERCHANTS = "SELF_DELIVERY_BY_MERCHANTS";
    public static final String SEND_ARES = "ARES";
    public static final String ANUBIS = "ANUBIS";
    public static final String STORE_ID = "STORE_ID";               //店铺ID
    public static final String ORDER_ID = "ORDER_ID";                 //订单ID
    public static final String ORDER_TYPE = "ORDER_TYPE";           //订单类型
    public static final String ORDER_NORML = "ORDER_NORML";         //外卖订单
    public static final String ORDER_RESERVCE = "ORDER_RESERVCE";   //预定订单

    /***
     * 支付方式
     */
    public static final String WX = "WX";//微信
    public static final String ALIPAY = "ALIPAY";//支付宝
    public static final String BLANCE = "BALANCE";//钱包


    /**
     * 发票类型
     */
    public static final String COMPANY = "COMPANY";//公司
    public static final String INDIVIDUAL = "INDIVIDUAL";//个人

    /**
     * 缩略图
     */

    public static final String IMG_GOODS = "/goods.png";//商品及店铺缩略图
    public static final String IMG_STORE = "/shopLogo.png";//店铺缩略图
    public static final String IMG_AVATOR = "/avator.png";//头像缩略图
    public static final String IMG_STORE_DETAIL = "/shopDetail.png";//店铺详情图
    public static final String IMG_BANNER = "/banner.png";//banner图

    public static final String IMG_EVA_BIG = "";//评论大圖
    public static final String IMG_GOOD_MEDIUM = "";//商品详情图

    /**
     * Banner类型
     */
    public static final String NOT_REDIRECT = "NOT_REDIRECT";//不跳链接
    public static final String REDIRECT = "REDIRECT";//跳链接
    public static final String SHOP_INFO = "SHOP_INFO";//店铺

    /**
     * 微信ID
     */
    public static final String WX_APP_ID = "wx45585d3d8118052d";

    public static final String ROOT_DIR;

    static {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            ROOT_DIR = Environment.getRootDirectory().getAbsolutePath();
        }
    }

    public static final String SYSTEM_INIT_FILE_NAME = "sysini";
    public static final String APP_DIR = (ROOT_DIR + "/xinyuan");
    public static final String CACHE_DIR = (APP_DIR + "/cachePic");
    public static final String LOG_DIR = (APP_DIR + "/log");
    public static final String APK_DIR = (APP_DIR + "/apk");
}
