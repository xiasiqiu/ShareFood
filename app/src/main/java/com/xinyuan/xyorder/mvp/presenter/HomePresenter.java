package com.xinyuan.xyorder.mvp.presenter;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.HomeMultipleItem;
import com.xinyuan.xyorder.common.bean.LoginInfo;
import com.xinyuan.xyorder.common.bean.home.BannerBean;
import com.xinyuan.xyorder.common.bean.home.HomeModelBean;
import com.xinyuan.xyorder.common.bean.home.ShopCategory;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.HomeView;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SPUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */
public class HomePresenter extends BasePresenter<HomeView> {

    private static List<HomeMultipleItem> HomeMultipleList; //模块布局元素列表
    private List<BannerBean> bannerList;
    private List<ShopCategory> shopCategoryList;
    private List<StoreDetail> shopList;
    private Context mContext;

    public HomePresenter(Context context, HomeView view) {
        super(view);
        this.mContext = context;
    }

    public void getHomeData() {
        OkGo.<HttpResponseData<HomeModelBean>>get(Constants.API_HOME)
                .tag(this)
//                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<HomeModelBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<HomeModelBean>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            showModule(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<HomeModelBean>> response) {
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

    public void getHomeDataWithDistance(String json) {
        OkGo.<HttpResponseData<HomeModelBean>>post(Constants.API_HOME)
                .tag(this)
//                .headers("TOKEN", DataUtil.getToken(mContext))
                .upJson(json)
                .execute(new JsonCallback<HttpResponseData<HomeModelBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<HomeModelBean>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            showModule(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<HomeModelBean>> response) {
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

    private void showModule(HomeModelBean homeModelBean) {
        bannerList = new ArrayList<>();
        shopCategoryList = new ArrayList<>();
        shopList = new ArrayList<>();
        shopCategoryList = homeModelBean.getShopCategories();
        shopList = homeModelBean.getShopList();
        bannerList = homeModelBean.getBanners();
        mView.showBanner(bannerList);
        mView.showShopList(shopList);
        mView.showMenu(shopCategoryList);
    }

    public void refreshToken() {
        OkGo.<HttpResponseData<LoginInfo>>get(Constants.API_MINE_USER_REFRESHTOKEN)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .params("cid", MyApplication.cid)
                .params("ios", false)
                .execute(new JsonCallback<HttpResponseData<LoginInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<LoginInfo>> response) {
                        if (response.body().isStatus()) {
                            SPUtils.put(mContext, "token", response.body().getData().getJwt());
                        } else {
                            if (response.body().errorCode == 1111) {
                                SPUtils.remove(mContext, "token");
                            }
                        }


                    }

                    @Override
                    public void onError(Response<HttpResponseData<LoginInfo>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }

    public void checkMsg() {
        OkGo.<HttpResponseData<Boolean>>put(Constants.API_CHECK_NOTICE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<Boolean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<Boolean>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            mView.showNoticeMsg(response.body().getData());
                        }


                    }

                    @Override
                    public void onError(Response<HttpResponseData<Boolean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }

}
