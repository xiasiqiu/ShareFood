package com.xinyuan.xyorder.mvp.presenter;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.store.StoreDetailData;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.StoreGoodsView;
import com.xinyuan.xyorder.util.DataUtil;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/27
 */
public class StoreGoodsPresenter extends BasePresenter<StoreGoodsView> {
    private Context mContext;

    public StoreGoodsPresenter(Context context, StoreGoodsView view) {
        super(view);
        this.mContext = context;
    }

    public void getStoreGoodsData(long shopId) {
        OkGo.<HttpResponseData<StoreDetailData>>get(Constants.API_SHOP_DETAIL + shopId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<StoreDetailData>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<StoreDetailData>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            mView.ShowStoreInfo(response.body().getData().getDetail());
                            mView.showCategory(response.body().getData().getCategorys());
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<StoreDetailData>> response) {
                        super.onError(response);
                    }
                });
    }
}
