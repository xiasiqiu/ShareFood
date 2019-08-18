package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.PayView;

/**
 * Created by f-x on 2017/11/217:33
 */

public class PayPresenter extends BasePresenter<PayView> {
    private Activity mActivity;

    public PayPresenter(Activity activity, PayView view) {
        super(view);
        this.mActivity = activity;
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
