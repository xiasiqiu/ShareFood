package com.xinyuan.xyorder.ui.home;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.ShopStoreAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.SearchBean;
import com.xinyuan.xyorder.common.bean.store.StoreListData;
import com.xinyuan.xyorder.common.bean.store.store.StoreCategoryBean;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.AroundStoreEven;
import com.xinyuan.xyorder.mvp.contract.SearchFoodView;
import com.xinyuan.xyorder.mvp.presenter.SearchFoodPresenter;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/12/15
 */
public class AroundStoreListFragment extends BaseFragment<SearchFoodPresenter> implements SearchFoodView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rvStoreList)
    RecyclerView rvStoreList;
    @BindView(R.id.loadingView)
    XLoadingView loadingView;
    private ShopStoreAdapter shopStoreAdapter;
    private int page = 1;
    private int limit = 10;
    private String keyWord = "";
    private String sort = "";
    private String areaId = "";
    private String shopCategoryId = "";
    private boolean iscollectioned;
    private String tag;

    public static AroundStoreListFragment newInstance(String shopCategoryId, String tag) {
        AroundStoreListFragment fragment = new AroundStoreListFragment();

        fragment.shopCategoryId = shopCategoryId;
        fragment.tag = tag;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        loadingView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });
    }

    @Override
    public void initData() {
        sort = "DISTANCE";
//        shopCategoryId=AroundStoreFragment.categoryId;
        Search();
    }

    @Override
    protected SearchFoodPresenter createPresenter() {
        return new SearchFoodPresenter(getActivity(), this);
    }

    private void Search() {
        mPresenter.getStoreData(page, limit, keyWord, sort, iscollectioned, shopCategoryId);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_around_list;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                Search();
            }
        }, 500);

    }

    @Override
    public void onLoadMoreRequested() {
        rvStoreList.postDelayed(new Runnable() {
            @Override
            public void run() {
                Search();
            }

        }, 500);
    }

    @Override
    public void showStoreList(StoreListData data) {
        if (XEmptyUtils.isEmpty(shopStoreAdapter)) {
            if (XEmptyUtils.isEmpty(data.getList())) {
                loadingView.showEmpty();
            } else {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(1);
                rvStoreList.setLayoutManager(layoutManager);
                this.rvStoreList.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 1, getActivity().getResources().getColor(R.color.gray_cc)));
                shopStoreAdapter = new ShopStoreAdapter(R.layout.item_shop_store, data.getList());
                shopStoreAdapter.setOnLoadMoreListener(this, rvStoreList);
                shopStoreAdapter.setAutoLoadMoreSize(4);
                rvStoreList.setAdapter(shopStoreAdapter);

                shopStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Map<String, Long> params = new HashMap();
                        params.put(Constants.STORE_ID, shopStoreAdapter.getData().get(position).getShopId());
                        CommUtil.goActivity(getActivity(), StoreDetailActivity.class, false, params);
                    }
                });
                loadingView.showContent();
                page++;
            }

        } else {
            if (XEmptyUtils.isEmpty(data.getList())) {
                if (page == 1) {
                    shopStoreAdapter.setEnableLoadMore(false);
                    loadingView.showEmpty();
                } else {
                    shopStoreAdapter.loadMoreEnd();
                }
            } else {
                if (page == 1) {
//                    rvStoreList.scrollToPosition(0);
                    shopStoreAdapter.setNewData(data.getList());
                    shopStoreAdapter.setEnableLoadMore(true);
                    loadingView.showContent();
                } else {
                    shopStoreAdapter.addData(data.getList());
                    shopStoreAdapter.loadMoreComplete();
                }
                page++;
            }
        }
    }

    @Override
    public void showStoreCategory(List<StoreCategoryBean> data) {

    }

    @Override
    public void showState(int state) {
        switch (state) {
            case 0:
                loadingView.showLoading();
                break;
            case 1:
                loadingView.showContent();
                break;
            case 2:
                loadingView.showError();
                break;
            case 3:
                loadingView.showEmpty();
            case 4:
                loadingView.showNoNetwork();
                break;
        }
    }

    /**
     * 获取通讯消息
     *
     * @param goodBusEven
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchStoreEvent(AroundStoreEven goodBusEven) {
        if (goodBusEven.getFlag().equals(AroundStoreEven.SEARCH_SELECT) && goodBusEven.getTag().equals(tag)) {
            loadingView.showLoading();
            page = 1;
            SearchBean searchBean = (SearchBean) goodBusEven.getObj();
//            this.shopCategoryId = searchBean.getShopCategoryId();
            this.sort = searchBean.getSort();
            this.keyWord = searchBean.getKey();
            Search();
        } else if (goodBusEven.getFlag().equals(AroundStoreEven.AREAR_SEARCH)) {
            page = 1;
            Search();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus(this);
    }
}
