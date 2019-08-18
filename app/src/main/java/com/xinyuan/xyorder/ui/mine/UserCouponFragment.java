package com.xinyuan.xyorder.ui.mine;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.RedPacketAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.OrderCouponBean;
import com.xinyuan.xyorder.common.bean.mine.UserCouponData;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/2410:47
 */

public class UserCouponFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.lodingView)
    XLoadingView lodingView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    private RedPacketAdapter redPacketAdapter;

    private int page = 1;

    public static UserCouponFragment newInstance() {
        UserCouponFragment fragment = new UserCouponFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("我的优惠");
        CommUtil.setBack(getActivity(), iv_header_left);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(1);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器
        recycler_view.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void initData() {
        getCouponList(page);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_usercoupon;
    }

    private void showList(List<OrderCouponBean> data) {
        if (XEmptyUtils.isEmpty(redPacketAdapter)) {
            if (XEmptyUtils.isEmpty(data)) {
                lodingView.showEmpty();
            } else {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(1);
                recycler_view.setLayoutManager(layoutManager);
                redPacketAdapter = new RedPacketAdapter(R.layout.fragment_redpacket_item, data);
                recycler_view.setAdapter(redPacketAdapter);
                lodingView.showContent();
                page++;
            }
        } else {
            if (XEmptyUtils.isEmpty(data)) {
                if (page == 1) {
                    lodingView.showEmpty();

                } else {
                    redPacketAdapter.loadMoreEnd();

                }
            } else {
                if (page == 1) {
                    recycler_view.scrollToPosition(0);
                    redPacketAdapter.setNewData(data);
                    swipeRefreshLayout.setRefreshing(false);
                    redPacketAdapter.setEnableLoadMore(true);
                    lodingView.showContent();
                } else {
                    redPacketAdapter.addData(data);
                    swipeRefreshLayout.setRefreshing(false);
                    redPacketAdapter.loadMoreComplete();
                }
            }
            page++;
        }
    }

    private void getCouponList(int page) {
        OkGo.<HttpResponseData<UserCouponData>>get(Constants.API_MINE_USER_COUPON)
                .tag(this)
                .params("pageId", page)
                .params("pageSize", Constants.PAGE_SIZE)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<UserCouponData>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<UserCouponData>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showList(response.body().getData().getList());
                        }
                    }


                    @Override
                    public void onError(Response<HttpResponseData<UserCouponData>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                getCouponList(page);
            }
        }, 500);
    }

    @Override
    public void onLoadMoreRequested() {
        recycler_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCouponList(page);
            }

        }, 500);
    }


}
