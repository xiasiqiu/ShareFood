package com.xinyuan.xyorder.ui.myorder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.order.OrderTrackAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.OrderTrackingBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/19
 */
public class OrderTrackingFragment extends BaseFragment {

    @BindView(R.id.rv_order_tracking)
    RecyclerView rv_order_tracking;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.loadingView)
    XLoadingView xLoadingView;

    private OrderTrackAdapter orderTrackAdapter;
    private List<OrderTrackingBean> orderTrackBeanList = new ArrayList<>();
    private long order_id;

    public static OrderTrackingFragment newInstance(long order_id) {
        OrderTrackingFragment orderTrackingFragment = new OrderTrackingFragment();
        orderTrackingFragment.order_id = order_id;
        return orderTrackingFragment;
    }

    @Override
    public void initView(View rootView) {
        tv_header_center.setText("订单跟踪");
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
        rv_order_tracking.setLayoutManager(new LinearLayoutManager(getActivity()));
        getOrderTracking(order_id);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_order_tracking;
    }

    public void getOrderTracking(long orderId) {
        OkGo.<HttpResponseData<List<OrderTrackingBean>>>get(Constants.API_ORDER_TRACKING + orderId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<List<OrderTrackingBean>>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<List<OrderTrackingBean>>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body()))
                            if (!XEmptyUtils.isEmpty(response.body().getData()) && response.body().getData().size() > 0) {
                                xLoadingView.showContent();
                                orderTrackAdapter = new OrderTrackAdapter(R.layout.item_order_tracking, response.body().getData());
                                rv_order_tracking.setAdapter(orderTrackAdapter);
                            } else {
                                xLoadingView.showEmpty();
                            }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<List<OrderTrackingBean>>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }


}
