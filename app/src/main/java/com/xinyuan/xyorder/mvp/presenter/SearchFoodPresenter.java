package com.xinyuan.xyorder.mvp.presenter;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.AreanBean;
import com.xinyuan.xyorder.common.bean.home.LocationBean;
import com.xinyuan.xyorder.common.bean.store.store.StoreCategoryBean;
import com.xinyuan.xyorder.common.bean.store.StoreListData;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.SearchFoodView;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;

import java.util.List;

/**
 * Created by f-x on 2017/10/13.
 */

public class SearchFoodPresenter extends BasePresenter<SearchFoodView> {
    private Context mContext;

    public SearchFoodPresenter(Context context, SearchFoodView view) {
        super(view);
        this.mContext = context;
    }

    /**
     * 获取店铺列表
     *
     * @param page           页数
     * @param pageSize       页面数据数量
     * @param keyword        关键字
     * @param sort           排序规则
     * @param iscollectioned 是否关注
     * @param shopCategoryId 分类ID
     */
    public void getStoreData(int page, int pageSize, String keyword, String sort, boolean iscollectioned, String shopCategoryId) {
        if (shopCategoryId.equals("0")) {
            shopCategoryId = "";
        }

        LocationBean locationBean;
        if(XEmptyUtils.isEmpty(mContext)){
            XLog.e("mCOntext空的");
        }
        if (DataUtil.checkLocation(mContext)) {
            locationBean = DataUtil.getLocation(mContext);
        } else {
            locationBean = new LocationBean();
        }

        OkGo.<HttpResponseData<StoreListData>>get(Constants.API_SHOP_DETAIL)
                .tag(this)
                .headers(Constants.API_TOKEN, DataUtil.getToken(mContext))
                .params("pageId", page)
                .params("pageSize", pageSize)
                .params("shopNameLike", keyword)
                .params("longitude", locationBean.getLongitude())
                .params("latitude", locationBean.getLatitude())
                .params("sort", sort)
//                .params("cityId", locationBean.getCityId())
                .params("collectioned", iscollectioned)
                .params("shopCategoryId", shopCategoryId)
                .execute(new JsonCallback<HttpResponseData<StoreListData>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<StoreListData>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showStoreList(response.body().getData());

                            }
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<StoreListData>> response) {
                        super.onError(response);
                        switch (HttpUtil.handleError(mContext, response)) {
                            case 1:
                                mView.showState(4);
                                break;
                            case 2:
                                mView.showState(2);
                                break;

                        }
                    }
                });
    }

    /**
     * 获取商品分类列表
     */
    public void getGoodClass() {
        OkGo.<HttpResponseData<List<StoreCategoryBean>>>get(Constants.API_SHOP_CATEGORY)
                .tag(this)
                .headers(Constants.API_TOKEN, DataUtil.getToken(MyApplication.mApplicationContext))
                .execute(new JsonCallback<HttpResponseData<List<StoreCategoryBean>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<List<StoreCategoryBean>>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            if (!XEmptyUtils.isEmpty(mView)) {
                                mView.showStoreCategory(response.body().getData());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<List<StoreCategoryBean>>> response) {
                        super.onError(response);

                    }
                });
    }
}
