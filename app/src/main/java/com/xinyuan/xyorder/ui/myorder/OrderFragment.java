package com.xinyuan.xyorder.ui.myorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.order.MineOrderAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.LoginPageBusEven;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.even.OrderRefreshEven;
import com.xinyuan.xyorder.common.even.TabSelectedEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.main.MainFragment;
import com.xinyuan.xyorder.mvp.contract.MineOrderView;
import com.xinyuan.xyorder.mvp.presenter.MineOrderPresenter;
import com.xinyuan.xyorder.ui.buy.PayFragment;
import com.xinyuan.xyorder.ui.login.LoginActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */
public class OrderFragment extends BaseFragment<MineOrderPresenter> implements MineOrderView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, MineOrderAdapter.PayListenner {

    @BindView(R.id.rv_order)
    RecyclerView rv_order;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.lodingView)
    XLoadingView lodingView;
    @BindView(R.id.tv_header)
    TextView tv_header;

    private MineOrderAdapter mineOrderAdapter;
    private List<OrderBean> datas = new ArrayList<>();
    private int page = 1;

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        registerEventBus(this);
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), tv_header);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

//        if (!XEmptyUtils.isEmpty(MyApplication.Token)) {
//            mPresenter.getMineOrder(page);
//        }
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(1);
        rv_order.setLayoutManager(layoutManager);
        lodingView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getMineOrder(1);

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected MineOrderPresenter createPresenter() {
        return new MineOrderPresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_order;
    }


    @Override
    public void showOrder(List<OrderBean> itemList) {
        if (XEmptyUtils.isEmpty(mineOrderAdapter)) {
            if (XEmptyUtils.isEmpty(itemList)) {
                showState(3);
            } else {
                mineOrderAdapter = new MineOrderAdapter(R.layout.item_mine_order, itemList);
                rv_order.setAdapter(mineOrderAdapter);
                mineOrderAdapter.setPayListenner(this);
                mineOrderAdapter.setOnLoadMoreListener(this, rv_order);
                mineOrderAdapter.disableLoadMoreIfNotFullPage();
                mineOrderAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                mineOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        startActivity(new Intent(_mActivity, OrderInfoActivity.class).putExtra("orderId", ((OrderBean) adapter.getItem(position)).getOrderId()));
                    }
                });
                page++;
                showState(1);
            }
        } else {
            if (XEmptyUtils.isEmpty(itemList)) {
                if (page == 1) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showState(3);
                } else {
                    mineOrderAdapter.loadMoreEnd();
                }
            } else {

                if (page == 1) {
                    if (itemList.size() < Constants.PAGE_SIZE) {
                        mineOrderAdapter.loadMoreEnd(true);

                    }
                    mineOrderAdapter.setNewData(itemList);
                    mSwipeRefreshLayout.setRefreshing(false);
                    showState(1);
                } else {
                    mineOrderAdapter.addData(itemList);
//                    mSwipeRefreshLayout.setRefreshing(false);
                    mineOrderAdapter.loadMoreComplete();
                    showState(1);
                }
                page++;
            }

        }
    }


    @Override
    public void onRefresh() {
        mineOrderAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                mPresenter.getMineOrder(page);
            }
        }, 500);
    }

    @Override
    public void onLoadMoreRequested() {
        rv_order.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getMineOrder(page);
            }

        }, 500);
    }

    @Override
    public void showState(int state) {
        switch (state) {
            case 0:
                lodingView.showLoading();
                break;
            case 1:
                lodingView.showContent();
                break;
            case 2:
                lodingView.showError();
                break;
            case 3:
                lodingView.showEmpty();
                break;
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!XEmptyUtils.isEmpty(mineOrderAdapter)) {
            mineOrderAdapter.setEnableLoadMore(false);
        }
        page = 1;
        mPresenter.getMineOrder(page);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OrderBusEven(LoginPageBusEven event) {
        if (event.getFlag().equals(LoginPageBusEven.ISLOGIN)) {
            mPresenter.getMineOrder(page);
        } else if (event.getFlag().equals(LoginPageBusEven.LOGINOUT)) {
            lodingView.showEmpty();
        } else if (event.getFlag().equals(LoginPageBusEven.NO_LOGIN)) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderRefreshEven orderRefreshEven) {
        if (orderRefreshEven.isRefresh()) {
            onRefresh();
        }
    }

    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) { //当在主页面点击个订单，未登录则跳转至登录界面，登录则加载个人资料
        if (event.position != MainFragment.CAR) {
            return;
        }
        if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
            CommUtil.gotoActivity(getActivity(), LoginActivity.class, false, null);
        } else {
//            onRefresh();
        }
    }

    @Override
    public void pay(OrderBean info) {
        EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, PayFragment.newInstance(info)));
    }

    @Override
    public void sureOrder(OrderBean info) {
        OkGo.<HttpResponseData<WXPayBean>>put(Constants.API_ORDER_SUER_ORDER + "/" + info.getOrderId())
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new DialogCallback<HttpResponseData<WXPayBean>>(_mActivity, "确认中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<WXPayBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<WXPayBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }
}
