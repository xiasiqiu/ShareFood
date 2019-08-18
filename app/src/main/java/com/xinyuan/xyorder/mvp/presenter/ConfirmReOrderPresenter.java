package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.ConfirmReOrderView;
import com.xinyuan.xyorder.util.DataUtil;

/**
 * Created by f-x on 2017/10/2020:25
 */

public class ConfirmReOrderPresenter extends BasePresenter<ConfirmReOrderView> {

    private Activity mActivity;

    public ConfirmReOrderPresenter(Activity activity, ConfirmReOrderView view) {
        super(view);
        this.mActivity = activity;
    }

    /**
     * 获取订单预览信息
     *
     * @param json
     */
    public void getConfirmOrder(String json) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_PREVIEW)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .upJson(json)
                .execute(new JsonCallback<HttpResponseData<OrderBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.showConfirmInfo(response.body().getData());
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
     * 添加订单
     *
     * @param json
     */
    public void postConfirmOrder(String json) {
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_SAVE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .upJson(json)
                .execute(new DialogCallback<HttpResponseData<OrderBean>>(mActivity, "提交中...") {
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

}
