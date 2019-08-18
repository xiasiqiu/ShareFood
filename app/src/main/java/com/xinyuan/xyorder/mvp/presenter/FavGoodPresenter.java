package com.xinyuan.xyorder.mvp.presenter;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.store.StoreListData;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.FavGoodView;
import com.xinyuan.xyorder.util.DataUtil;

import java.util.List;


public class FavGoodPresenter extends BasePresenter<FavGoodView> {
    private Context mContext;

    public FavGoodPresenter(Context context, FavGoodView view) {
        super(view);
        this.mContext = context;
    }

    /**
     * 获取收藏商品数据
     *
     * @param ship
     * @param limit
     */
    public void getFavGoods(int ship, int limit) {
        OkGo.<HttpResponseData<StoreListData>>get(Constants.API_MINE_COLLECTION)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<StoreListData>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<StoreListData>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            mView.showFavInfo(response.body().getData().getList());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<StoreListData>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }

    /**
     * 取消收藏商品
     *
     * @param idList
     */
    public void deteleFavGood(List<String> idList) {
        String str = "";
        for (String s : idList) {
            str += s.trim() + ",";
        }
        str = str.substring(0, str.length() - 1);
        str = "[" + str + "]";  //服务端格式[112,123]
        OkGo.<HttpResponseData<List<StoreDetail>>>post(Constants.API_COLLECTION_BATCH_DELETE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .upJson(str)
                .execute(new JsonCallback<HttpResponseData<List<StoreDetail>>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<List<StoreDetail>>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            mView.detelteToast(true);
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<List<StoreDetail>>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }


//
//		OkGo.<LzyResponse<FavGoodModel>>post(Urls.URL_USER_FAV_GOODS_DETELE)
//				.tag(this)
//				.headers("token", MyShopApplication.Token)
//				.params("id", MyShopApplication.userId)
//				.params("favorteId", str)
//				.execute(new JsonCallback<LzyResponse<FavGoodModel>>() {
//					@Override
//					public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<FavGoodModel>> response) {
//						if (HttpUtil.handleResponse(mContext, response.body())) {
//							mView.detelteRes(true);
//						}
//					}
//
//					@Override
//					public void onError(com.lzy.okgo.model.Response<LzyResponse<FavGoodModel>> response) {
//						HttpUtil.handleError(mContext, response);
//					}
//				});
}
