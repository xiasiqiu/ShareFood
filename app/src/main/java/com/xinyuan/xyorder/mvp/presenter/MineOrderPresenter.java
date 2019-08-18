package com.xinyuan.xyorder.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.MineOrderData;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.MineOrderView;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.widget.XToast;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class MineOrderPresenter extends BasePresenter<MineOrderView> {
    private Activity mContext;

    public MineOrderPresenter(Activity context, MineOrderView view) {
        super(view);
        this.mContext = context;
    }

    public void getMineOrder(int pageId) {
        OkGo.<HttpResponseData<MineOrderData>>get(Constants.API_MINE_ORDER)
                .tag(this)
                .headers("TOKEN",  DataUtil.getToken(mContext))
                .params("pageId", pageId)
                .params("pageSize", Constants.PAGE_SIZE)
                .execute(new JsonCallback<HttpResponseData<MineOrderData>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<MineOrderData>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            mView.showOrder(response.body().getData().getList());
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<MineOrderData>> response) {
                        super.onError(response);
                        XToast.error("请求错误");
                    }
                });
    }


}
