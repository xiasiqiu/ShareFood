package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.OrderAppraiseAllTag;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.OrderEvaluationView;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/29
 */
public class OrderEvaluationPresenter extends BasePresenter<OrderEvaluationView>{

    private Activity mContext;
    public OrderEvaluationPresenter(Activity context, OrderEvaluationView view) {
        super(view);
        this.mContext=context;
    }
    public void getOrderAppraiseTag() {
        OkGo.<HttpResponseData<OrderAppraiseAllTag>>get(Constants.API_GET_APPRAISE_TAG )
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<OrderAppraiseAllTag>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderAppraiseAllTag>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showAppraiseTag(response.body().getData());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderAppraiseAllTag>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }
}
