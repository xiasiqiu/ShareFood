package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.MyInvoiceData;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.ChooesInvoiceView;
import com.xinyuan.xyorder.util.DataUtil;

/**
 * Created by f-x on 2017/11/2814:10
 */

public class ChooesInvoicePresenter extends BasePresenter<ChooesInvoiceView> {
    Activity mActivity;

    public ChooesInvoicePresenter(Activity activity, ChooesInvoiceView view) {
        super(view);
        this.mActivity = activity;
    }

    public void getInvoiceList(int page) {
        OkGo.<HttpResponseData<MyInvoiceData>>get(Constants.API_INVOICEINFO + "?pageId=" + page + "&pageSize=" + 20)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mActivity))
                .execute(new JsonCallback<HttpResponseData<MyInvoiceData>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<MyInvoiceData>> response) {
                        if (HttpUtil.handleResponse(mActivity, response.body())) {
                            mView.showInvoiceList(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<MyInvoiceData>> response) {
                        HttpUtil.handleError(mActivity, response);
                        super.onError(response);
                    }
                });
    }


}
