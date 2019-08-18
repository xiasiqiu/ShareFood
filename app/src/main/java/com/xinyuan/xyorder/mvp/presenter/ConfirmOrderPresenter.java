package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.ConfirmOrderView;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

/**
 * Created by f-x on 2017/10/2020:25
 */

public class ConfirmOrderPresenter extends BasePresenter<ConfirmOrderView> {

    private Activity mActivity;

    public ConfirmOrderPresenter(Activity activity, ConfirmOrderView view) {
        super(view);
        this.mActivity = activity;
    }

    /**
     * 获取外卖订单预览信息
     *
     * @param json
     */
    public void getConfirmOrder(String json) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_TAKEPREVIEW)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .upJson(json)
                .execute(new JsonCallback<HttpResponseData<OrderBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (response.body().isStatus()) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showConfirmInfo(response.body().getData());

                            }
                        } else {
                            XToast.error(response.body().getMessage(),4000);
                            mView.errorBack();
                        }


                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);

                    }
                });
    }

    /**
     * 添加外卖订单
     *
     * @param json
     */
    public void postConfirmOrder(String json) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_TAKESAVE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .upJson(json)
                .execute(new DialogCallback<HttpResponseData<OrderBean>>(mActivity, "订单提交中") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.postOrderState(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                    }
                });
    }


    /**
     * 获取预定订单预览信息
     *
     * @param json
     */
    public void getReConfirmOrder(String json) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_PREVIEW)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .upJson(json)
                .execute(new JsonCallback<HttpResponseData<OrderBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (response.body().isStatus()) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showConfirmInfo(response.body().getData());
                            }
                        } else {
                            XToast.error(response.body().getMessage(),4000);
                            mView.errorBack();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                        // mActivity.onBackPressed();

                    }
                });
    }

    /**
     * 添加预定订单
     *
     * @param json
     */
    public void postReConfirmOrder(String json) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_SAVE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .upJson(json)
                .execute(new DialogCallback<HttpResponseData<OrderBean>>(mActivity, "订单提交中") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.postOrderState(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
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

    public void balancePay(long orderId) {
        OkGo.<HttpResponseData<Void>>get(Constants.API_ORDER_BALANLCE + orderId)
                .tag(this)
                .execute(new DialogCallback<HttpResponseData<Void>>(mActivity, "支付中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        mView.balanceResult(response.body().isStatus());
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                    }
                });
    }


}
