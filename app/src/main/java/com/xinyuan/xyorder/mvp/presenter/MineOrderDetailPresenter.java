package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.DriverInfoBean;
import com.xinyuan.xyorder.common.bean.order.LatLngInfo;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.MineOrderDetailView;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/18
 */
public class MineOrderDetailPresenter extends BasePresenter<MineOrderDetailView> {

    private Activity mActivity;

    public MineOrderDetailPresenter(Activity mActivity, MineOrderDetailView view) {
        super(view);
        this.mActivity = mActivity;
    }

    /**
     * 获取订单详情
     *
     * @param orderId
     */
    public void getOrderDetail(String orderId) {
        OkGo.<HttpResponseData<OrderBean>>get(Constants.API_MINE_ORDER + "/" + orderId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .execute(new JsonCallback<HttpResponseData<OrderBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showOrderDetailInfo(response.body().getData());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mActivity, response);
                    }
                });
    }

    /**
     * 取消订单
     *
     * @param order_id
     */
    public void cancleOrder(final long order_id) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_CANCLE_ORDER + "/" + order_id)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .execute(new DialogCallback<HttpResponseData<OrderBean>>(mActivity, "取消中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.cancleOrder();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mActivity, response);
                    }
                });

    }


    /**
     * 支付宝支付
     *
     * @param orderId
     */
    public void alipay(long orderId) {
        OkGo.<HttpResponseData<String>>get(Constants.API_ORDER_ALIPAY + orderId)
                .tag(this)
                .execute(new DialogCallback<HttpResponseData<String>>(mActivity, "支付中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<String>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.aliPayResult(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<String>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                    }
                });


    }

    /**
     * 微信支付
     *
     * @param orderId
     */
    public void wxpay(long orderId) {
        OkGo.<HttpResponseData<WXPayBean>>get(Constants.API_ORDER_WX + orderId)
                .tag(this)
                .execute(new DialogCallback<HttpResponseData<WXPayBean>>(mActivity, "支付中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<WXPayBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.wxResult(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<WXPayBean>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                    }
                });
    }

    /**
     * 确认收货
     */
    public void sureOrder(final long orderId) {
        OkGo.<HttpResponseData<WXPayBean>>put(Constants.API_ORDER_SUER_ORDER + "/" + orderId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .execute(new DialogCallback<HttpResponseData<WXPayBean>>(mActivity, "确认中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<WXPayBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            getOrderDetail(String.valueOf(orderId));
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<WXPayBean>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                    }
                });
    }

    /**
     * 获取骑手信息位置
     */
    public void getDriverInfo(long orderId) {
        OkGo.<HttpResponseData<DriverInfoBean>>get(Constants.API_GET_DRIVER + orderId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .execute(new JsonCallback<HttpResponseData<DriverInfoBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<DriverInfoBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showDriverInfo(response.body().getData());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<DriverInfoBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mActivity, response);
                    }
                });
    }

    /**
     * 获取订单经纬度信息
     */
    public void getLatLngInfo(long orderId) {
        OkGo.<HttpResponseData<LatLngInfo>>get(Constants.API_GET_LATLNGINFO + orderId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .execute(new JsonCallback<HttpResponseData<LatLngInfo>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<LatLngInfo>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showLatLng(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<LatLngInfo>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mActivity, response);
                    }
                });
    }

}
