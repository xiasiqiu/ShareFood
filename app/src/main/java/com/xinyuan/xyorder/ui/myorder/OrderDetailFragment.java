package com.xinyuan.xyorder.ui.myorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.github.florent37.viewanimator.ViewAnimator;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.ConfirmOrderGoodAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.buy.PayResult;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.DriverInfoBean;
import com.xinyuan.xyorder.common.bean.order.LatLngInfo;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.bean.order.OrderGoods;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.mvp.contract.MineOrderDetailView;
import com.xinyuan.xyorder.mvp.presenter.MineOrderDetailPresenter;
import com.xinyuan.xyorder.ui.buy.PayFragment;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.widget.CollapsingToolbarLayoutState;
import com.xinyuan.xyorder.widget.MyMapView;
import com.xinyuan.xyorder.widget.NormalDialog;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.xinyuan.xyorder.widget.SwitchButton;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XLoadingDialog;
import com.youth.xframe.widget.XToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class OrderDetailFragment extends BaseFragment<MineOrderDetailPresenter> implements MineOrderDetailView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.rv_order_detail)
    RecyclerView rv_order_detail;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tv_go_to_pay)
    TextView tv_go_to_pay;
    TextView tv_real_pay;
    TextView tv_arrive_time, tv_contact_phone, tv_address, tv_delivery_way, tv_order_in_time, tv_contact, tv_num, tv_order_num, tv_pay_way, tv_order_delivery_time;
    TextView tv_store_name;
    @BindView(R.id.tv_wait_pay)
    TextView tv_wait_pay;
    @BindView(R.id.rl_wait_pay)
    RelativeLayout rl_wait_pay;
    LinearLayout ll_view;
    @BindView(R.id.tv_evaluation)
    TextView tv_evaluation;
    @BindView(R.id.tv_sure_order)
    TextView tv_sure_order;
    RelativeLayout rl_store_name;
    TextView tv_contact_seller;
    SwitchButton sb_compartment;
    @BindView(R.id.iv_store_logo)
    ImageView iv_store_logo;
    ImageView iv_store_detail_logo;
    TextView tv_mealFee;//餐盒费
    TextView tv_shippingFee;//配送费
    LinearLayout ll_reserve;
    LinearLayout ll_takeout;
    LinearLayout ll_activity;
    TextView tv_staff_username;
    TextView tv_staff_tel;
    View vLine;
    View v_name;
    RelativeLayout rl_name;
    View v_phone;
    RelativeLayout rl_phone;
    MyMapView amapView;
    ImageView iv_map_refresh;
    private AMap aMap;
    TextView tv_invoice;
    TextView tv_invoice_num;
    RelativeLayout rl_invoice;
    LinearLayout ll_mine_evaluation;
    //    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.iv_resresh)
    ImageView iv_resresh;
    private Marker driverLocMaker;//骑手marker
    private Marker shopLocMaker;//店铺marker
    private Marker contactLocMaker;//收货人marker
    private MarkerOptions moptions;

    private ConfirmOrderGoodAdapter confirmOrderGoodAdapter;
    private List<OrderGoods> goodBeanList = new ArrayList<>();
    private long order_id;
    private OrderBean orderItemBean;
    private boolean isShowMapInfo = false;
    private String contactLat, contactLng;
    private String shopLat, shopLng;
    private CollapsingToolbarLayoutState state;
    private ScheduledExecutorService scheduledThreadPool;
    boolean mapRefresh = false;

    public static OrderDetailFragment newInstance(long order_id) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.order_id = order_id;
        return fragment;
    }

    private int toolbarHeight;

    @Override
    public void initView(View rootView) {
//        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
//        SystemBarHelper.setHeightAndPadding(getActivity(), tv_header_center);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        XLoadingDialog.with(
                getActivity()).setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(1);
        this.rv_order_detail.setLayoutManager(layoutManager);
        confirmOrderGoodAdapter = new ConfirmOrderGoodAdapter(R.layout.item_confirmorder_good, goodBeanList);
        rv_order_detail.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        rv_order_detail.setAdapter(confirmOrderGoodAdapter);
        addTop(savedInstanceState);
        addBottom();
        toolbarHeight = toolbar.getBottom();
    }

    @Override
    public void initListener() {
        super.initListener();
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        collapsingToolbar.setTitle("EXPANDED");//设置title为EXPANDED
                        tv_header_center.setVisibility(View.GONE);
                        ViewAnimator.animate(iv_store_logo).scale(0, 1f).decelerate().duration(500).start();
                    }

                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbar.setTitle("");//设置title不显示
                        tv_header_center.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        ViewAnimator.animate(iv_store_logo).scale(1f, 0).decelerate().duration(500).start();
                    }

                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            tv_header_center.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        tv_header_center.setVisibility(View.VISIBLE);//隐藏播放按钮
                        collapsingToolbar.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间

                    }
                }

            }
        });

        iv_header_left.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
//        orderId = _mActivity.getIntent().getStringExtra("orderId");
//        mPresenter.getOrderDetail(order_id + "");
    }

    @Override
    protected MineOrderDetailPresenter createPresenter() {
        return new MineOrderDetailPresenter(_mActivity, this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_order_detail;
    }


    private void addTop(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_detail_header, (ViewGroup) rv_order_detail.getParent(), false);
        confirmOrderGoodAdapter.addHeaderView(view);
        tv_store_name = view.findViewById(R.id.tv_store_name);
        rl_store_name = view.findViewById(R.id.rl_store_name);
        iv_store_detail_logo = view.findViewById(R.id.iv_store_logo1);
        amapView = view.findViewById(R.id.mapView);
        iv_map_refresh = view.findViewById(R.id.iv_map_refresh);
        amapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = amapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
//        uiSettings.setLogoBottomMargin(-50);//隐藏logo
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种

        rl_store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Long> params = new HashMap();
                params.put(Constants.STORE_ID, orderItemBean.getShopId());
                CommUtil.goActivity(getActivity(), StoreDetailActivity.class, false, params);
            }
        });
        iv_map_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!XEmptyUtils.isEmpty(contactLat) && !XEmptyUtils.isEmpty(contactLng)) {
                    mapRefresh = true;
                    showContactMarker(contactLat, contactLng);
                }
            }
        });
    }

    private void addBottom() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_detail_bottom, (ViewGroup) rv_order_detail.getParent(), false);
        confirmOrderGoodAdapter.addFooterView(view);
        tv_real_pay = view.findViewById(R.id.tv_real_pay);
        tv_arrive_time = view.findViewById(R.id.tv_arrive_time);
        tv_address = view.findViewById(R.id.tv_address);
        tv_delivery_way = view.findViewById(R.id.tv_delivery_way);
        tv_contact = view.findViewById(R.id.tv_contact);
        tv_order_in_time = view.findViewById(R.id.tv_order_in_time);
        tv_num = view.findViewById(R.id.tv_num);
        tv_order_num = view.findViewById(R.id.tv_order_num);
        tv_pay_way = view.findViewById(R.id.tv_pay_way);
        tv_order_delivery_time = view.findViewById(R.id.tv_order_delivery_time);//下单时间
        tv_contact_phone = view.findViewById(R.id.tv_contact_phone);
        ll_view = view.findViewById(R.id.ll_view);
        tv_contact_seller = view.findViewById(R.id.tv_contact_seller);
        sb_compartment = view.findViewById(R.id.sb_compartment);
        tv_mealFee = view.findViewById(R.id.tv_mealFee);
        tv_shippingFee = view.findViewById(R.id.tv_shippingFee);
        ll_takeout = view.findViewById(R.id.ll_takeout);
        ll_reserve = view.findViewById(R.id.ll_reserve);
        ll_activity = view.findViewById(R.id.ll_activity);
        vLine = view.findViewById(R.id.vLine);
        tv_staff_tel = view.findViewById(R.id.tv_staff_tel);
        tv_staff_username = view.findViewById(R.id.tv_staff_username);
        v_name = view.findViewById(R.id.v_name);
        rl_name = view.findViewById(R.id.rl_name);
        v_phone = view.findViewById(R.id.v_phone);
        rl_phone = view.findViewById(R.id.rl_phone);
        tv_invoice = view.findViewById(R.id.tv_invoice);
        tv_invoice_num = view.findViewById(R.id.tv_invoice_num);
        rl_invoice = view.findViewById(R.id.rl_invoice);
        ll_mine_evaluation = view.findViewById(R.id.ll_mine_evaluation);
        sb_compartment.setClickable(false);
        ll_mine_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(MineOrderEvaluationFragment.newInstance(orderItemBean));
            }
        });

    }

    @OnClick({R.id.tv_evaluation, R.id.tv_wait_pay, R.id.tv_cancle_order, R.id.tv_go_to_pay, R.id.tv_sure_order, R.id.iv_resresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_evaluation:
                start(OrderEvaluationFragment.newInstance(orderItemBean));
                break;
            case R.id.tv_go_to_pay:
                start(PayFragment.newInstance(orderItemBean));
                break;
            case R.id.tv_cancle_order:
                final NormalDialog calDialog = new NormalDialog(getActivity());
                calDialog.setClickListener(new NormalDialog.DialogClickListener() {
                    @Override
                    public void enterListener() {
                        mPresenter.cancleOrder(order_id);
//                        start(OrderCancleFragment.newInstance(order_id));
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
                calDialog.show();
                break;
            case R.id.tv_wait_pay:
                start(OrderTrackingFragment.newInstance(order_id));
//                start(OrderEvaluationFragment.newInstance(orderItemBean));
                break;
            case R.id.tv_sure_order:
                final NormalDialog calDialog1 = new NormalDialog(getActivity());
                calDialog1.setMessage("确定要收货吗?");
                calDialog1.setEnterText("确定");
                calDialog1.setCancleText("取消");
                calDialog1.setClickListener(new NormalDialog.DialogClickListener() {
                    @Override
                    public void enterListener() {
                        mPresenter.sureOrder(order_id);
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
                calDialog1.show();

                break;
            case R.id.iv_resresh:
                XLoadingDialog.with(getActivity())
                        .setCanceled(false, true) //设置手动不可取消
                        .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                        .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                        .setMessageColor(Color.BLACK)//设置消息字体颜色
                        .show();//显示对话框
                mPresenter.getOrderDetail(order_id + "");
                break;
            default:
                break;
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.getOrderDetail(order_id + "");
    }

    @Override
    public void showState(int state) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showOrderDetailInfo(final OrderBean mineOrderItemBean) {
        if (XEmptyUtils.isEmpty(mineOrderItemBean)) {
            return;
        }
        showCommon(mineOrderItemBean);
        if (Constants.ORDER_TAKEOUT.equals(mineOrderItemBean.getOrderType())) {//外卖
            showOrderTakeOut(mineOrderItemBean);
        } else if (Constants.ORDER_RESERVE.equals(mineOrderItemBean.getOrderType())) {//预定
            showOrderReserver(mineOrderItemBean);
        }
        XLoadingDialog.with(getActivity()).dismiss();
    }

    private void showCommon(final OrderBean mineOrderItemBean) {
        String payment = null;
        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getPayment())) {
            switch (mineOrderItemBean.getPayment()) {
                case Constants.ALIPAY:
                    payment = "支付宝支付";
                    break;
                case Constants.WX:
                    payment = "微信支付";
                    break;
                case Constants.BLANCE:
                    payment = "余额支付";
                    break;
                default:
                    break;
            }
        }
        orderItemBean = mineOrderItemBean;
        order_id = mineOrderItemBean.getOrderId();
        tv_store_name.setText(mineOrderItemBean.getShopName());
        if (mineOrderItemBean.isIsAppraise()) {
            ll_mine_evaluation.setVisibility(View.VISIBLE);
        } else {
            ll_mine_evaluation.setVisibility(View.GONE);
        }
        GlideImageLoader.setHeaderImageView(getActivity(), Constants.IMAGE_HOST + "/shopLogo/" + mineOrderItemBean.getShopId() + ".png" + Constants.IMG_STORE, iv_store_detail_logo);
        GlideImageLoader.setHeaderImageView(getActivity(), Constants.IMAGE_HOST + "/shopLogo/" + mineOrderItemBean.getShopId() + ".png" + Constants.IMG_STORE, iv_store_logo);
        tv_pay_way.setText(payment);
        tv_real_pay.setText("实付￥" + mineOrderItemBean.getOrderPrice() + "元");

        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getAddTime())) {
            tv_order_delivery_time.setText((XDateUtils.millis2String(Long.parseLong(mineOrderItemBean.getAddTime()), "yyyy-MM-dd HH:mm")));
        }

        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderContact())) {
            tv_address.setText(mineOrderItemBean.getOrderContact().getAddress());
            tv_contact.setText(mineOrderItemBean.getOrderContact().getContactName() + "  " + mineOrderItemBean.getOrderContact().getContactPhone());
            tv_contact_phone.setText(mineOrderItemBean.getOrderContact().getContactName() + "  " + mineOrderItemBean.getOrderContact().getContactPhone());
        }

        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderReserve())) {
            tv_num.setText(mineOrderItemBean.getOrderReserve().getRepastNum() + "人");
            if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderReserve().getRepastTime())) {
                tv_order_in_time.setText((XDateUtils.millis2String(Long.parseLong(mineOrderItemBean.getOrderReserve().getRepastTime()), "yyyy-MM-dd HH:mm")));
            }
            if (mineOrderItemBean.getOrderReserve().isIsPrivateRoom()) {
                sb_compartment.setChecked(true);
            } else {
                sb_compartment.setChecked(false);
            }
            tv_order_num.setText(mineOrderItemBean.getOrderNum());
        }

        confirmOrderGoodAdapter.setNewData(mineOrderItemBean.getOrderGoods());
        confirmOrderGoodAdapter.notifyDataSetChanged();

        showAcitivities(mineOrderItemBean);

        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderInvoiceInfo()) && !XEmptyUtils.isEmpty(mineOrderItemBean.getOrderInvoiceInfo().getTitle())) {
            rl_invoice.setVisibility(View.VISIBLE);
            tv_invoice.setText(mineOrderItemBean.getOrderInvoiceInfo().getTitle());
            if ("COMPANY".equals(mineOrderItemBean.getOrderInvoiceInfo().getInvoiceType())) {
                tv_invoice_num.setText(mineOrderItemBean.getOrderInvoiceInfo().getEin());
                tv_invoice_num.setVisibility(View.VISIBLE);
            } else {
                tv_invoice_num.setVisibility(View.GONE);
            }
        } else {
            rl_invoice.setVisibility(View.GONE);
        }
        tv_contact_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calPhone(mineOrderItemBean.getPhoneNum());
            }
        });

    }

    /**
     * 显示活动
     *
     * @param mineOrderItemBean
     */
    private void showAcitivities(OrderBean mineOrderItemBean) {
        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderActivitys()) && mineOrderItemBean.getOrderActivitys().size() > 0) {
//            ll_activity.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
            ll_view.removeAllViews();
            for (int i = 0; i < mineOrderItemBean.getOrderActivitys().size(); i++) {
                RelativeLayout rl_view = new RelativeLayout(getActivity());
                TextView tv1 = new TextView(getActivity());
                TextView tv2 = new TextView(getActivity());
                tv1.setText(mineOrderItemBean.getOrderActivitys().get(i).getActivityContent());
                tv1.setTextColor(getResources().getColor(R.color.order_detail));
                tv2.setTextColor(getResources().getColor(R.color.red));
                tv1.setTextSize(13);
                tv2.setTextSize(13);
                tv2.setText("-￥" + mineOrderItemBean.getOrderActivitys().get(i).getActivityReduced() + "");

                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp1.leftMargin = 30;
                lp1.topMargin = 30;
                lp1.bottomMargin = 30;
                lp1.rightMargin = 30;
                rl_view.addView(tv1, lp1);

                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp2.leftMargin = 30;
                lp2.topMargin = 30;
                lp2.bottomMargin = 30;
                lp2.rightMargin = 30;
                rl_view.addView(tv2, lp2);

                if (i < mineOrderItemBean.getOrderActivitys().size() - 1) {
                    View view = new View(getActivity());
                    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    view.setBackgroundResource(R.color.gray_d9);
                    rl_view.addView(view, lp3);
                }
                ll_view.addView(rl_view);
            }
        } else {
            vLine.setVisibility(View.GONE);
//            ll_activity.setVisibility(View.GONE);
        }
    }

    /**
     * 显示预定订单详情
     */
    private void showOrderReserver(OrderBean mineOrderItemBean) {
        ll_reserve.setVisibility(View.VISIBLE);
        ll_takeout.setVisibility(View.GONE);
        ll_activity.setVisibility(View.GONE);
        String convertOrderStatus = null;
        switch (mineOrderItemBean.getOrderStatus()) {
            case Constants.ORDER_WAIT_PAY:
                convertOrderStatus = "等待支付";
                rl_wait_pay.setVisibility(View.VISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                break;
            case Constants.ORDER_PAYED:
                convertOrderStatus = "等待接单";
                rl_wait_pay.setVisibility(View.VISIBLE);
                tv_go_to_pay.setVisibility(View.GONE);
                tv_evaluation.setVisibility(View.GONE);
                break;
            case Constants.ORDER_MERCHANT_CONFIRM_RECEIPT:
                convertOrderStatus = "商家确认接单";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                break;
            case Constants.ORDER_TRANSACT_FINISHED:
                convertOrderStatus = "已完成";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_sure_order.setVisibility(View.GONE);
                if (mineOrderItemBean.isIsAppraise()) {
                    tv_evaluation.setVisibility(View.GONE);
                } else {
                    tv_evaluation.setVisibility(View.VISIBLE);
                }
                break;
            case Constants.ORDER_CANCELLATIONY:
                convertOrderStatus = "已取消";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                break;
        }
        tv_wait_pay.setText(convertOrderStatus);

    }

    /**
     * 显示外卖订单详情
     */
    private void showOrderTakeOut(final OrderBean mineOrderItemBean) {
        ll_reserve.setVisibility(View.GONE);
        ll_takeout.setVisibility(View.VISIBLE);
        ll_activity.setVisibility(View.VISIBLE);
        String convertOrderStatus = null;
        switch (mineOrderItemBean.getOrderStatus()) {
            case Constants.ORDER_WAIT_PAY:
                convertOrderStatus = "等待支付";
                rl_wait_pay.setVisibility(View.VISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = false;
                break;
            case Constants.ORDER_PAYED:
                convertOrderStatus = "等待接单";
                rl_wait_pay.setVisibility(View.VISIBLE);
                tv_go_to_pay.setVisibility(View.GONE);
                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = false;
                break;
            case Constants.ORDER_SHIPPING:
                convertOrderStatus = "配送中";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = true;
                break;
            case Constants.ORDER_TRANSACT_FINISHED:
                convertOrderStatus = "已完成";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_sure_order.setVisibility(View.GONE);
                isShowMapInfo = false;
                if (mineOrderItemBean.isIsAppraise()) {
                    tv_evaluation.setVisibility(View.GONE);
                } else {
                    tv_evaluation.setVisibility(View.VISIBLE);
                }
                break;
            case Constants.ORDER_CANCELLATIONY:
                convertOrderStatus = "已取消";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = false;
                break;
            case Constants.ORDER_MERCHANT_CONFIRM_RECEIPT:
                convertOrderStatus = "商家确认接单";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_go_to_pay.setVisibility(View.GONE);

                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = false;
                break;
            case Constants.ORDER_WAIT_PICKUP:
                convertOrderStatus = "等待取货";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = true;
                break;
            case Constants.ORDER_PICKUPING:
                convertOrderStatus = "取货中";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                isShowMapInfo = true;
                break;
            case Constants.ORDER_DELIVERED:
                convertOrderStatus = "已送达";
                rl_wait_pay.setVisibility(View.INVISIBLE);
                tv_evaluation.setVisibility(View.GONE);
                tv_sure_order.setVisibility(View.VISIBLE);
                isShowMapInfo = false;
        }
        tv_wait_pay.setText(convertOrderStatus);
        if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderTakeout())) {//外卖信息非空
            tv_shippingFee.setText(getActivity().getResources().getText(R.string.money_rmb) + CommUtil.getPriceString(mineOrderItemBean.getOrderTakeout().getShippingFee()));
            tv_mealFee.setText(getActivity().getResources().getText(R.string.money_rmb) + CommUtil.getPriceString(mineOrderItemBean.getOrderTakeout().getMealFee()));
            tv_staff_username.setText(mineOrderItemBean.getOrderTakeout().getCarrierDriverName());
            tv_staff_tel.setText(mineOrderItemBean.getOrderTakeout().getCarrierDriverphone());

            if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderTakeout().getCarrierDriverphone())) {
                v_phone.setVisibility(View.VISIBLE);
                rl_phone.setVisibility(View.VISIBLE);
                tv_staff_tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calPhone(mineOrderItemBean.getOrderTakeout().getCarrierDriverphone());
                    }
                });
            }

            if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderTakeout().getCarrierDriverName())) {
                v_name.setVisibility(View.VISIBLE);
                rl_name.setVisibility(View.VISIBLE);
            }
            if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderTakeout().getDeliveryTime())) {
                tv_arrive_time.setText(XDateUtils.millis2String(Long.parseLong(mineOrderItemBean.getOrderTakeout().getDeliveryTime()), "HH:mm"));
            }
            if (!XEmptyUtils.isEmpty(mineOrderItemBean.getOrderTakeout().getDistributionType())) {
                switch (mineOrderItemBean.getOrderTakeout().getDistributionType()) {
                    case Constants.SELF_DELIVERY_BY_MERCHANTS:
                        tv_delivery_way.setText("商家自送");
                        break;
                    case Constants.ANUBIS:
                        tv_delivery_way.setText("蜂鸟配送");
                        break;
                    case Constants.SEND_ARES:
                        tv_delivery_way.setText("鑫圆自送");

                        break;
                }
            }
        }
        if (isShowMapInfo) {
            mPresenter.getLatLngInfo(order_id);//获取经纬度信息
        } else {
            amapView.setVisibility(View.GONE);
            iv_map_refresh.setVisibility(View.GONE);
            if (scheduledThreadPool != null) {
                scheduledThreadPool.shutdown();
            }
        }
    }

    private void showDriverMarker(String driverLat, String driverLng) {
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(driverLat), Double.valueOf(driverLng)),14));//地图移
        if (driverLocMaker == null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(driverLat), Double.valueOf(driverLng)), 14));//地图移
            moptions = new MarkerOptions();
            moptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.driver)));
            moptions.anchor(0.5f, 0.5f);
            moptions.position(new LatLng(Double.valueOf(driverLat), Double.valueOf(driverLng)));
            driverLocMaker = aMap.addMarker(moptions);
            driverLocMaker.showInfoWindow();
        } else {
            LatLng ll = new LatLng(Double.valueOf(driverLat), Double.valueOf(driverLng));
            driverLocMaker.setPosition(ll);
        }
    }

    private void showContactMarker(String contactLat, String contactLng) {
        XLog.e("contactLat" + contactLat + " " + contactLng);
        if(driverLocMaker==null){
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(contactLat), Double.valueOf(contactLng)), 14));//地图移
        }
        if (mapRefresh) {
            mapRefresh = false;
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(contactLat), Double.valueOf(contactLng)), 14));//地图移
        }
        if (contactLocMaker == null) {
            moptions = new MarkerOptions();
            moptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_address)));
            moptions.anchor(0.5f, 0.5f);
            moptions.position(new LatLng(Double.valueOf(contactLat), Double.valueOf(contactLng)));
            contactLocMaker = aMap.addMarker(moptions);
            contactLocMaker.showInfoWindow();

        } else {
            LatLng ll = new LatLng(Double.valueOf(contactLat), Double.valueOf(contactLng));
            contactLocMaker.setPosition(ll);
        }
    }

    private void showShopMarker(String shopLat, String shopLng) {
        XLog.e("shopLat" + shopLat + " " + shopLng);
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(shopLat), Double.valueOf(shopLng)), 14));//地图移
        if (shopLocMaker == null) {
            moptions = new MarkerOptions();
            moptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_shop)));
            moptions.anchor(0.5f, 0.5f);
            moptions.position(new LatLng(Double.valueOf(shopLat), Double.valueOf(shopLng)));
            shopLocMaker = aMap.addMarker(moptions);
            shopLocMaker.showInfoWindow();

        } else {
            LatLng ll = new LatLng(Double.valueOf(shopLat), Double.valueOf(shopLng));
            shopLocMaker.setPosition(ll);
        }
    }

    public void getDriverInfo() {
        scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (isShowMapInfo) {
                    mPresenter.getDriverInfo(order_id);
                }
            }
        }, 1, 10, TimeUnit.SECONDS);//延迟一秒后每10秒执行一次
    }

    @Override
    public void aliPayResult(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void wxResult(WXPayBean wxPayBean) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
        iwxapi.registerApp(Constants.WX_APP_ID);
        PayReq payReq = new PayReq();
        payReq.appId = Constants.WX_APP_ID;
        payReq.partnerId = wxPayBean.getPartnerId();
        payReq.prepayId = wxPayBean.getPrepayId();
        payReq.packageValue = wxPayBean.getPackages();
        payReq.nonceStr = wxPayBean.getNonceStr();
        payReq.timeStamp = wxPayBean.getTimeStamp();
        payReq.sign = wxPayBean.getSign();
        iwxapi.sendReq(payReq);
    }

    @Override
    public void cancleOrder() {
        start(OrderCancleFragment.newInstance(order_id));
    }

    @Override
    public void showDriverInfo(DriverInfoBean info) {
        if (!XEmptyUtils.isEmpty(info.getLatitude()) && !XEmptyUtils.isEmpty(info.getLongitude())) {
            showDriverMarker(info.getLatitude(), info.getLongitude());
        }
    }

    @Override
    public void showLatLng(LatLngInfo info) {
        amapView.setVisibility(View.VISIBLE);
        iv_map_refresh.setVisibility(View.VISIBLE);
        if (!XEmptyUtils.isEmpty(info.getCarrier()) && !XEmptyUtils.isEmpty(info.getCarrier().getLatitude()) && !XEmptyUtils.isEmpty(info.getCarrier().getLongitude())) {
            showDriverMarker(info.getCarrier().getLatitude(), info.getCarrier().getLongitude());
        }
        if (!XEmptyUtils.isEmpty(info.getBuyer()) && !XEmptyUtils.isEmpty(info.getBuyer().getLatitude()) && !XEmptyUtils.isEmpty(info.getBuyer().getLongitude())) {
            showContactMarker(info.getBuyer().getLatitude(), info.getBuyer().getLongitude());
            contactLat = info.getBuyer().getLatitude();
            contactLng = info.getBuyer().getLongitude();
        }
        if (!XEmptyUtils.isEmpty(info.getShop()) && !XEmptyUtils.isEmpty(info.getShop().getLatitude()) && !XEmptyUtils.isEmpty(info.getShop().getLongitude())) {
            showShopMarker(info.getShop().getLatitude(), info.getShop().getLongitude());
        }
        getDriverInfo();
    }

    /**
     * 接受支付宝回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                XToast.info("支付成功");
                mPresenter.getOrderDetail(order_id + "");
            } else {
                XToast.error("支付失败");
            }
        }

    };

    private void calPhone(final String phone) {
        XPermission.requestPermissions(getActivity(), 100, new String[]{Manifest.permission
                .CALL_PHONE}, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);

            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(getActivity());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (amapView != null) {
            amapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (amapView != null) {
            amapView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (amapView != null) {
            amapView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (amapView != null) {
            amapView.onDestroy();
        }
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

