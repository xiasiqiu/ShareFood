package com.xinyuan.xyorder.ui.stores;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.storedetail.StoreEvaAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.store.store.StoreEvaluateBean;
import com.xinyuan.xyorder.common.bean.store.StoreEvaluateData;
import com.xinyuan.xyorder.mvp.contract.StoreEvaluateView;
import com.xinyuan.xyorder.mvp.presenter.StoreEvaluatePresenter;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/1819:20
 * 店铺评论
 */
public class StoreEvaluateFragment extends BaseFragment<StoreEvaluatePresenter> implements StoreEvaluateView, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_eva)
    RecyclerView rv_comment;
    @BindView(R.id.lodingView)
    XLoadingView lodingView;


    private StoreEvaAdapter storeEvaAdapter;
    private List<StoreEvaluateBean> evaBeanList;
    private long shopId;
    private int page = 1;

    public static StoreEvaluateFragment newInstance(long shopId) {
        StoreEvaluateFragment fragment = new StoreEvaluateFragment();
        fragment.shopId = shopId;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
    }

    @Override
    public void initData() {

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mPresenter.getStoreEvaluateList(page, shopId);
    }

    @Override
    public void showEvaluateList(StoreEvaluateData data) {
        if (XEmptyUtils.isEmpty(storeEvaAdapter)) {
            if (XEmptyUtils.isEmpty(data.getList())) {
                lodingView.showEmpty();
            } else {
                this.storeEvaAdapter = new StoreEvaAdapter(R.layout.item_store_eva, data.getList());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(1);
                rv_comment.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
                rv_comment.setLayoutManager(linearLayoutManager);
                storeEvaAdapter.openLoadAnimation();
                storeEvaAdapter.setOnLoadMoreListener(this);
                this.rv_comment.setAdapter(this.storeEvaAdapter);

                lodingView.showContent();
                page++;
            }
        } else {
            if (XEmptyUtils.isEmpty(data.getList())) {
                if (page == 1) {
                    lodingView.showEmpty();
                } else {
                    storeEvaAdapter.loadMoreEnd();
                }
            } else {
                if (page == 1) {
                    storeEvaAdapter.setNewData(data.getList());
                    storeEvaAdapter.setEnableLoadMore(true);
                } else {
                    storeEvaAdapter.addData(data.getList());
                    storeEvaAdapter.loadMoreComplete();
                }
            }
            page++;
        }


    }


    @Override
    protected StoreEvaluatePresenter createPresenter() {
        return new StoreEvaluatePresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_evaluate;
    }


    @Override
    public void onLoadMoreRequested() {
        rv_comment.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getStoreEvaluateList(page, shopId);

            }

        }, 500);
    }


}
