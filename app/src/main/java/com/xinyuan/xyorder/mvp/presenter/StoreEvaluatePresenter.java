package com.xinyuan.xyorder.mvp.presenter;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.store.StoreEvaluateData;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.StoreEvaluateView;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

/**
 * Created by f-x on 2017/10/2015:38
 */

public class StoreEvaluatePresenter extends BasePresenter<StoreEvaluateView> {

    private Context mContext;

    public StoreEvaluatePresenter(Context context, StoreEvaluateView view) {
        super(view);
        this.mContext = context;
    }

    public void getStoreEvaluateList(int page, long shopId) {
        OkGo.<HttpResponseData<StoreEvaluateData>>get(Constants.API_SHOPAPPRAISE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .params("pageId", page)
                .params("pageSize", Constants.PAGE_SIZE)
                .params("shopId", shopId)
                .execute(new JsonCallback<HttpResponseData<StoreEvaluateData>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<StoreEvaluateData>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showEvaluateList(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<StoreEvaluateData>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);

                    }
                });
    }
}
