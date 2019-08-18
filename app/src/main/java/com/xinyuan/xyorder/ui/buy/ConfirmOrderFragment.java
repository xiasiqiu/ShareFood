package com.xinyuan.xyorder.ui.buy;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.BuyContactBean;
import com.xinyuan.xyorder.adapter.buy.ConfirmOrderGoodAdapter;
import com.xinyuan.xyorder.adapter.buy.OrderGoodSpecBean;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.buy.InVoiceBean;
import com.xinyuan.xyorder.common.bean.buy.NumBean;
import com.xinyuan.xyorder.common.bean.buy.OrderCouponBean;
import com.xinyuan.xyorder.common.bean.buy.OrderPrewBean;
import com.xinyuan.xyorder.common.bean.buy.PayResult;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeBean;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeContentBean;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.bean.order.OrderActivitysBean;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.bean.order.OrderContactBean;
import com.xinyuan.xyorder.common.bean.order.PaymentBean;
import com.xinyuan.xyorder.common.bean.store.StoreActivityShowTextBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.SpecialsGoodBean;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.common.even.GoodBusEven;
import com.xinyuan.xyorder.mvp.contract.ConfirmOrderView;
import com.xinyuan.xyorder.mvp.presenter.ConfirmOrderPresenter;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.ChooseNumDialog;
import com.xinyuan.xyorder.widget.ChoosePayDialog;
import com.xinyuan.xyorder.widget.ChooseTimeDialog;
import com.xinyuan.xyorder.widget.EditTextWithDel;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.xinyuan.xyorder.widget.RecyclerViewNoBugLinearLayoutManager;
import com.xinyuan.xyorder.widget.SwitchButton;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.XLoadingDialog;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by f-x on 2017/10/2609:14
 */

public class ConfirmOrderFragment extends BaseFragment<ConfirmOrderPresenter> implements ConfirmOrderView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rv_confirm_order)
    RecyclerView rv_confirm_order;
    @BindView(R.id.tv_user_address)
    TextView tv_user_address;                       //外卖-选择用户配送地址
    @BindView(R.id.tv_user_info)
    TextView tv_user_info;                          //外卖-选择用户联系人
    @BindView(R.id.ll_send_address)
    LinearLayout ll_send_address;                   //外卖-地址选择布局

    @BindView(R.id.ll_re_address)
    LinearLayout ll_re_address;                    //预定-选择地址布局
    @BindView(R.id.tv_re_user_info)
    TextView tv_re_user_info;                      //预定-选择用户联系人

    @BindView(R.id.ll_no_address)
    LinearLayout ll_no_address;                     //新增联系人布局
    @BindView(R.id.tv_new_user_info)
    TextView tv_new_user_info;                      //新增联系人View
    @BindView(R.id.tv_total_price)
    TextView tv_total_price;                        //底部订单原价
    @BindView(R.id.tv_coupon_price)
    TextView tv_coupon_price;                       //订单优惠价格

    public static ChooseTimeDialog chooseTimeDialog;      //时间选择Dialog
    public static ChoosePayDialog payDialog;        //支付选择Dialog
    public static ChooseNumDialog chooseNumDialog;  //选择人数Dialog
    private View topView;                        //顶部布局View
    private TextView tv_choose_time;            //选择时间View
    private TextView tv_choose_time_title;      //选择时间文字
    private LinearLayout ll_confirm_reserve;    //预定订单选择布局
    private TextView tv_choose_man_num;         //预定订单选择人数
    private SwitchButton sb_room;               //预定订单是否要包间

    private View bootomView;                    //底部布局View
    private LinearLayout ll_confirm_shippingFee;    //外卖配送费布局

    private EditTextWithDel ed_content;         //订单留言
    private TextView tv_pay;                    //选择支付方式
    private TextView tv_order_coupon_price;     //订单优惠价格
    private TextView tv_order_coupon_num;       //订单可用优惠券数量
    private TextView tv_order_price;           //订单总价格
    private RelativeLayout rl_choose_red;      //选择红包布局
    private LinearLayout ll_order_cut;          //店铺活动布局

    private BuyContactBean contactBean;

    private boolean isPrivateRoom = false;
    private ConfirmOrderGoodAdapter confirmOrderGoodAdapter;

    private HashMap<Long, Integer> goods = new HashMap<Long, Integer>();    //商品map
    private List<PaymentBean> paymentBeans;          //支付方式数据
    private List<ChooesCarGoodBean> selectGood;              //已经选择商品List
    private List<SelectTimeBean> selectTimeBean;    //时间数据
    private List<NumBean> numBeans;                 //人数数据

    private OrderPrewBean orderPrewBean;             //订单预览Bean
    private OrderBean orderBean;                     //订单数据Bean

    private long shopId;                             //店铺ID
    private String orderType;                        //TAKEOUT-外卖  RESERVE-预定
    private long couponSNId;                        //优惠券ID
    private long contactId;                         //联系人ID
    private String payTypeCode;                     //支付Code
    private AddressInfo addressInfo;                //联系人Bean
    private String deliveryTime;                    //配送时间
    private int resrepastNum = 1;                   //预定人数
    private boolean haveAddressFlag = true;


    public static ConfirmOrderFragment newInstance(String orderType, long shopId) {
        ConfirmOrderFragment confirmOrderFragment = new ConfirmOrderFragment();
        confirmOrderFragment.orderType = orderType;
        confirmOrderFragment.shopId = shopId;
        return confirmOrderFragment;
    }

    @Override
    public void initView(View rootView) {
        setRetainInstance(true);
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        if (Constants.ORDER_TAKEOUT.equals(orderType)) {
            tv_header_center.setText(getActivity().getResources().getString(R.string.str_order_take_area));
        } else {
            tv_header_center.setText(getActivity().getResources().getString(R.string.str_order_recom));
        }

        XLoadingDialog.with(getActivity())
                .setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框
        CommUtil.setBack(getActivity(), iv_header_left);
    }

    @Override
    public void initData() {

    }


    @Override
    protected ConfirmOrderPresenter createPresenter() {
        return new ConfirmOrderPresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_confirm_order;
    }


    @Override
    public void initListener() {
        //外卖选择地址
        ll_send_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ChooesContactFragment.newInstance(orderType, shopId));
            }
        });
        //预定点击地址
        ll_re_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ChooesContactFragment.newInstance(orderType, shopId));
            }
        });
        ll_no_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ChooesContactFragment.newInstance(orderType, shopId));

            }
        });


    }

    RecyclerViewNoBugLinearLayoutManager layoutManager;

    /**
     * 设置订单
     *
     * @param orderBean
     */
    @Override
    public void showConfirmInfo(OrderBean orderBean) {
        this.orderBean = orderBean;
        showAddress(orderBean.getOrderContact());
        /**商品列表*/
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(getActivity());
        layoutManager.setOrientation(1);
        layoutManager.setStackFromEnd(true);
        this.rv_confirm_order.setLayoutManager(layoutManager);
        confirmOrderGoodAdapter = new ConfirmOrderGoodAdapter(R.layout.item_confirmorder_good, orderBean.getOrderGoods());
        this.rv_confirm_order.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        /**支付方式*/
        if (!XEmptyUtils.isEmpty(orderBean.getPayments())) {
            paymentBeans = orderBean.getPayments();
        }
        tv_total_price.setText(getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderPrice()));
        tv_coupon_price.setText("|  已优惠" + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getFavourablePrice()));
        this.rv_confirm_order.setAdapter(confirmOrderGoodAdapter);
        addTop();
        addBottom();
//        lodingView.showContent();

        XLoadingDialog.with(getActivity()).dismiss();
        rv_confirm_order.scrollToPosition(0);
    }

    /**
     * 设置联系人
     *
     * @param contactBean
     */
    private void showAddress(OrderContactBean contactBean) {
        if (XEmptyUtils.isEmpty(contactBean)) {
            ll_no_address.setVisibility(View.VISIBLE);
            haveAddressFlag = false;
        } else {

            contactId = orderBean.getOrderContact().getContactId();
            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                ll_send_address.setVisibility(View.VISIBLE);
                tv_user_address.setText(orderBean.getOrderContact().getAddress());
                tv_user_info.setText(orderBean.getOrderContact().getContactName() + "( " + DataUtil.getGender(orderBean.getOrderContact().getGender()) + ") " + orderBean.getOrderContact().getContactPhone());
            } else {
                ll_re_address.setVisibility(View.VISIBLE);
                tv_re_user_info.setText(orderBean.getOrderContact().getContactName() + "( " + DataUtil.getGender(orderBean.getOrderContact().getGender()) + ") " + orderBean.getOrderContact().getContactPhone());
            }
        }
    }

    private String currentTime;

    /**
     * 添加头部布局
     */
    private void addTop() {
        topView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_confirm_tops, (ViewGroup) rv_confirm_order.getParent(), false);
        confirmOrderGoodAdapter.addHeaderView(topView, 0);

        tv_choose_time = topView.findViewById(R.id.tv_choose_time);
        tv_choose_time_title = topView.findViewById(R.id.tv_choose_time_title);

        if (orderType.equals(Constants.ORDER_TAKEOUT)) {
            selectTimeBean = DataUtil.getSendTime(orderType, orderBean.getTimeSelecters(), orderBean.getOrderTakeout().getShippingFee());
            tv_choose_time_title.setText(selectTimeBean.get(0).getContentTime().get(0).getTime());
            tv_choose_time.setText("");
            deliveryTime = XDateUtils.millis2String(selectTimeBean.get(0).getContentTime().get(0).getCurrentDate());
            currentTime = deliveryTime;
        } else {
            selectTimeBean = DataUtil.getSendTime(orderType, orderBean.getTimeSelecters(), new BigDecimal(-1));
            sb_room = topView.findViewById(R.id.sb_room);
            tv_choose_man_num = topView.findViewById(R.id.tv_choose_man_num);
            tv_choose_time_title.setText(getActivity().getResources().getString(R.string.str_order_recom_time));
            tv_choose_time.setText(selectTimeBean.get(0).getContentTime().get(0).getTime());
            deliveryTime = XDateUtils.millis2String(selectTimeBean.get(0).getContentTime().get(0).getCurrentDate());

            ll_confirm_reserve = topView.findViewById(R.id.ll_confirm_reserve);
            ll_confirm_reserve.setVisibility(View.VISIBLE);
            tv_choose_man_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChooseNum(numBeans);
                }
            });
            sb_room.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isPrivateRoom = true;
                    } else {
                        isPrivateRoom = false;
                    }
                }
            });
        }

        if (!XEmptyUtils.isEmpty(chooesTimeBean)) {
            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                tv_choose_time_title.setText(getActivity().getResources().getString(R.string.str_order_arriver_time_soon));
                tv_choose_time.setText(chooesTimeBean.getTime());
            } else {
                tv_choose_time_title.setText(getActivity().getResources().getString(R.string.str_order_recom_time));
                tv_choose_time.setText(chooesTimeBean.getTime());
            }

        }
        TextView tv_order_store_name = topView.findViewById(R.id.tv_order_store_name);
        tv_order_store_name.setText(orderBean.getShopName());
        //选择时间
        tv_choose_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseTime(selectTimeBean);
            }
        });
        numBeans = DataUtil.getManNum();

    }

    TextView tv_invoice;
    private String orderMsg;//订单留言

    /**
     * 添加底部布局
     */
    private void addBottom() {
        bootomView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_confirm_bottom, (ViewGroup) rv_confirm_order.getParent(), false);
        confirmOrderGoodAdapter.addFooterView(bootomView);
        rv_confirm_order.scrollToPosition(0);
        if (orderType.equals(Constants.ORDER_TAKEOUT)) {
            ll_confirm_shippingFee = bootomView.findViewById(R.id.ll_confirm_shippingFee);
            ll_confirm_shippingFee.setVisibility(View.VISIBLE);
            TextView shippingFee = bootomView.findViewById(R.id.shippingFee);
            TextView tv_mealFee = bootomView.findViewById(R.id.tv_mealFee);
            shippingFee.setText(getActivity().getResources().getText(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderTakeout().getShippingFee()));
            tv_mealFee.setText(getActivity().getResources().getText(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderTakeout().getMealFee()));
        }
        tv_invoice = bootomView.findViewById(R.id.tv_invoice);
        tv_order_price = bootomView.findViewById(R.id.tv_order_price);
        ed_content = bootomView.findViewById(R.id.ed_content);
        ed_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    rv_confirm_order.scrollToPosition(confirmOrderGoodAdapter.getItemCount());
                }
            }
        });
        if (!XEmptyUtils.isEmpty(orderMsg) || !XEmptyUtils.isSpace(orderMsg)) {
            ed_content.setText(orderMsg);
        }
        tv_pay = bootomView.findViewById(R.id.tv_pay);
        if (!XEmptyUtils.isEmpty(chooesPayMent)) {
            payTypeCode = chooesPayMent.getCode();
            tv_pay.setText(chooesPayMent.getDisplayName());
        } else {
            tv_pay.setText("请选择支付方式");
        }
        tv_order_coupon_price = bootomView.findViewById(R.id.tv_order_coupon_price);
        tv_order_coupon_num = bootomView.findViewById(R.id.tv_order_coupon_num);
        rl_choose_red = bootomView.findViewById(R.id.rl_choose_red);
        tv_order_price.setText(getActivity().getResources().getString(R.string.str_order_price_total) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderPrice()));
        if (orderBean.getCouponNum() == 0) {
            tv_order_coupon_num.setText(getActivity().getResources().getString(R.string.str_order_no_red));
        } else {
            if (XEmptyUtils.isEmpty(chooesCoupon)) {
                tv_order_coupon_num.setText(orderBean.getCouponNum() + getActivity().getResources().getString(R.string.str_order_red_num));
            } else {
                tv_order_coupon_num.setVisibility(View.GONE);
                tv_order_coupon_price.setVisibility(View.VISIBLE);
                tv_order_coupon_price.setText(getActivity().getResources().getString(R.string.str_reduce) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(chooesCoupon.getCoupon().getMoney()));
                tv_order_price.setText(getActivity().getResources().getString(R.string.str_order_price_total) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderPrice().subtract(chooesCoupon.getCoupon().getMoney())));
                tv_total_price.setText(getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOriginalPrice().subtract(chooesCoupon.getCoupon().getMoney())));
                tv_coupon_price.setText(getActivity().getResources().getString(R.string.str_order_coupon) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getFavourablePrice().add(chooesCoupon.getCoupon().getMoney())));
            }

        }
        if (orderBean.isCanDrawInvoice()) {
            tv_invoice.setVisibility(View.VISIBLE);
            if (!XEmptyUtils.isEmpty(chooesinVoiceBean)) {
                tv_invoice.setText(chooesinVoiceBean.getTitle());
            } else {
                tv_invoice.setText("不需要发票");
            }
        } else {
            tv_invoice.setVisibility(View.GONE);

        }

        ll_order_cut = bootomView.findViewById(R.id.ll_order_cut);
        showStoreActivity(orderBean.getOrderActivitys());

        //选择支付方式
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayType();
            }
        });
        //选择红包
        rl_choose_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(RedpacketFragment.newInstance(orderBean.getCanUseCouponList()));
            }
        });
        tv_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ChooesInvoiceFragment.newInstance());
            }
        });

    }


    /**
     * 显示店铺活动
     *
     * @param activitysBeans
     */
    private void showStoreActivity(List<OrderActivitysBean> activitysBeans) {
        if (!XEmptyUtils.isEmpty(activitysBeans) && activitysBeans.size() > 0) {
            ll_order_cut.setVisibility(View.VISIBLE);
            ll_order_cut.removeAllViews();

            for (int i = 0; i < activitysBeans.size(); i++) {
                RelativeLayout rl_view = new RelativeLayout(getActivity());

                TextView tv1 = new TextView(getActivity());
                TextView tv2 = new TextView(getActivity());
                TextView tv3 = new TextView(getActivity());
                StoreActivityShowTextBean activityShowTextBean = (DataUtil.getCouponText(getActivity(), activitysBeans.get(i).getActivityType()));
                tv3.setText(activityShowTextBean.getActivityType());
                tv3.setTextColor(getResources().getColor(R.color.bg_white));
                tv3.setBackgroundColor(activityShowTextBean.getAcitvityColor());
                tv3.setTextSize(11);
                tv3.setPadding(10, 0, 10, 0);

                tv1.setText(activitysBeans.get(i).getActivityContent());
                tv1.setTextColor(getResources().getColor(R.color.tv_hint));
                tv2.setTextColor(getResources().getColor(R.color.red));
                tv1.setTextSize(12);
                tv2.setTextSize(12);
                tv2.setText(getResources().getString(R.string.money_rmb) + activitysBeans.get(i).getActivityReduced());

                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp1.leftMargin = 100;
                lp1.topMargin = 18;
                lp1.bottomMargin = 18;
                lp1.rightMargin = 30;
                rl_view.addView(tv1, lp1);

                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp2.leftMargin = 30;
                lp2.topMargin = 18;
                lp2.bottomMargin = 18;
                lp2.rightMargin = 30;
                rl_view.addView(tv2, lp2);

                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp3.leftMargin = 30;
                lp3.topMargin = 20;
                lp3.bottomMargin = 18;
                lp3.rightMargin = 30;
                rl_view.addView(tv3, lp3);


                if (i < activitysBeans.size() - 1) {
                    View view = new View(getActivity());
                    RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    view.setBackgroundResource(R.color.gray_d9);
                    rl_view.addView(view, lp4);
                }
                ll_order_cut.addView(rl_view);
            }
        } else {
            ll_order_cut.setVisibility(View.GONE);
        }

    }

    /**
     * 重新支付
     */
    private void rePay() {
        if (XEmptyUtils.isEmpty(payTypeCode)) {
            showPayType();
            XToast.error(getActivity().getResources().getString(R.string.str_no_pay_chooes));
            return;
        }
        postOrderState(RePostorderBean);


    }

    @OnClick({R.id.tv_account})
    public void Onlick(View view) {
        switch (view.getId()) {
            case R.id.tv_account:
                if (XEmptyUtils.isEmpty(payTypeCode)) {
                    showPayType();
                    XToast.error(getActivity().getResources().getString(R.string.str_no_pay_chooes));
                    return;
                }

                if (!XEmptyUtils.isEmpty(RePostorderBean)) {
                    rePay();
                    return;
                }
                orderPrewBean.setContactId(contactId);
                orderPrewBean.setOrderContent(ed_content.getText().toString().trim());
                orderPrewBean.setShopId(shopId);
                orderPrewBean.setCouponSNId(couponSNId);
                orderPrewBean.setPayment(payTypeCode);
                orderPrewBean.setDeliveryTime(deliveryTime);
                if (!XEmptyUtils.isEmpty(chooesinVoiceBean)) {
                    orderPrewBean.setInvoiceInfoId(chooesinVoiceBean.getInvoiceInfoId());
                }
                if (orderType.equals(Constants.ORDER_RESERVCE)) {
                    orderPrewBean.setPrivateRoom(isPrivateRoom);
                    orderPrewBean.setRepastNum(resrepastNum);
                }
                Gson gson = new Gson();
                String json = gson.toJson(orderPrewBean);
                XLog.v(json);
                if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                    mPresenter.postConfirmOrder(json);
                } else {
                    mPresenter.postReConfirmOrder(json);

                }
                break;
        }

    }

    private SelectTimeContentBean chooesTimeBean;
    private PaymentBean chooesPayMent;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void BuyBusEven(BuyBusEven buyBusEven) {
        if (buyBusEven.getFlag().equals(BuyBusEven.ADDORDER)) {
            selectGood = buyBusEven.getSelectGood();
            orderPrewBean = new OrderPrewBean();
            List<OrderGoodSpecBean> goods = new ArrayList<>();
            for (ChooesCarGoodBean goodBean : selectGood) {
                OrderGoodSpecBean orderGoodSpecBean = new OrderGoodSpecBean();
                orderGoodSpecBean.setProperties(goodBean.getChooesProertys());
                orderGoodSpecBean.setQuantity(goodBean.getChooesNum());
                orderGoodSpecBean.setSpecId(goodBean.getSpecId());
                goods.add(orderGoodSpecBean);
            }
            orderPrewBean.setSpecs(goods);
            orderPrewBean.setShopId(shopId);
            Gson gson = new Gson();
            String json = gson.toJson(orderPrewBean);
            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                mPresenter.getConfirmOrder(json);
            } else {
                mPresenter.getReConfirmOrder(json);
            }


        } else if (buyBusEven.getFlag().equals(BuyBusEven.CHOOESETIME)) {
            chooesTimeBean = (SelectTimeContentBean) buyBusEven.getObj();
            deliveryTime = XDateUtils.millis2String(chooesTimeBean.getCurrentDate());
            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                tv_choose_time_title.setText(getActivity().getResources().getString(R.string.str_order_arriver_time));
                tv_choose_time.setText(chooesTimeBean.getTime());
            } else {
                tv_choose_time_title.setText(getActivity().getResources().getString(R.string.str_order_recom_time));
                tv_choose_time.setText(chooesTimeBean.getTime());
            }
        } else if (buyBusEven.getFlag().equals(BuyBusEven.CHOOESEPAY)) {
            chooesPayMent = (PaymentBean) buyBusEven.getObj();
            if (chooesPayMent.getCode().equals(Constants.WX)) {
                if (isWXAppInstall()) {
                    payTypeCode = chooesPayMent.getCode();
                    tv_pay.setText(chooesPayMent.getDisplayName());
                } else {
                    XToast.info("手机未安装微信，无法使用微信支付");
                }
            } else {
                payTypeCode = chooesPayMent.getCode();
                tv_pay.setText(chooesPayMent.getDisplayName());
            }


        } else if (buyBusEven.getFlag().equals(BuyBusEven.CHOOESENUM)) {
            NumBean numBean = (NumBean) buyBusEven.getObj();
            resrepastNum = numBean.getNum();
            tv_choose_man_num.setText(numBean.getNumName());
            for (NumBean numBean1 : numBeans) {
                if (numBean1.isCheck()) {
                    if (numBean1.getNum() != numBean.getNum()) {
                        numBean1.setCheck(false);
                    } else {
                        numBean1.setCheck(true);
                    }
                }
            }
        } else if (buyBusEven.getFlag().equals(BuyBusEven.CHOOESEUSER)) {
            XLoadingDialog.with(getActivity())
                    .setCanceled(false, true) //设置手动不可取消
                    .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                    .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                    .setMessageColor(Color.BLACK)//设置消息字体颜色
                    .show();//显示对话框
            addressInfo = (AddressInfo) buyBusEven.getObj();
            contactId = addressInfo.getContactId();
            if (haveAddressFlag) {
                if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                    tv_user_address.setText(addressInfo.getAddress());
                    tv_user_info.setText(addressInfo.getContactName() + "( " + DataUtil.getGender(addressInfo.getGender()) + ") " + addressInfo.getContactPhone());
                } else {
                    tv_re_user_info.setText(addressInfo.getContactName() + "( " + DataUtil.getGender(addressInfo.getGender()) + ") " + addressInfo.getContactPhone());
                }
            } else {
                ll_no_address.setVisibility(View.GONE);
                if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                    ll_send_address.setVisibility(View.VISIBLE);
                    tv_user_address.setText(addressInfo.getAddress());
                    tv_user_info.setText(addressInfo.getContactName() + "( " + DataUtil.getGender(addressInfo.getGender()) + ") " + addressInfo.getContactPhone());
                } else {
                    ll_re_address.setVisibility(View.VISIBLE);
                    tv_re_user_info.setText(addressInfo.getContactName() + "( " + DataUtil.getGender(addressInfo.getGender()) + ") " + addressInfo.getContactPhone());
                }
            }
            orderPrewBean.setContactId(contactId);
            Gson gson = new Gson();
            String json = gson.toJson(orderPrewBean);

            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                mPresenter.getConfirmOrder(json);
            } else {
                mPresenter.getReConfirmOrder(json);
            }
            mPresenter.getConfirmOrder(json);


        } else if (buyBusEven.getFlag().equals(BuyBusEven.CHOOESECOUPON)) {
            chooesCoupon = (OrderCouponBean) buyBusEven.getObj();
            couponSNId = chooesCoupon.getCouponSNId();
            tv_order_coupon_num.setVisibility(View.GONE);
            tv_order_coupon_price.setVisibility(View.VISIBLE);
            tv_order_coupon_price.setText(getActivity().getResources().getString(R.string.str_reduce) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(chooesCoupon.getCoupon().getMoney()));
            tv_order_price.setText(getActivity().getResources().getString(R.string.str_order_price_total) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderPrice().subtract(chooesCoupon.getCoupon().getMoney())));


            tv_total_price.setText(getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOriginalPrice().subtract(chooesCoupon.getCoupon().getMoney())));
            tv_coupon_price.setText(getActivity().getResources().getString(R.string.str_order_coupon) + getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getFavourablePrice().add(chooesCoupon.getCoupon().getMoney())));


            for (OrderCouponBean couponBean : orderBean.getCanUseCouponList()) {
                if (couponBean.getCoupon().isCheck()) {
                    if (couponBean.getCouponSNId() != chooesCoupon.getCouponSNId()) {
                        couponBean.getCoupon().setCheck(false);
                    } else {
                        couponBean.getCoupon().setCheck(true);
                    }
                }
            }

        } else if (buyBusEven.getFlag().equals(BuyBusEven.CHOOESEINVOICE)) {
            chooesinVoiceBean = (InVoiceBean) buyBusEven.getObj();
            tv_invoice.setText(chooesinVoiceBean.getTitle());
        } else if (buyBusEven.getFlag().equals(BuyBusEven.NOCHOOESEINVOICE)) {
            chooesinVoiceBean = null;
            tv_invoice.setText("不需要开发票");
        } else if (buyBusEven.getFlag().equals(BuyBusEven.NOCHOOESECOUPON)) {
            couponSNId = 0;
            tv_order_coupon_num.setVisibility(View.VISIBLE);
            tv_order_coupon_price.setVisibility(View.GONE);
            for (OrderCouponBean couponBean : orderBean.getCanUseCouponList()) {
                if (couponBean.getCoupon().isCheck()) {
                    couponBean.getCoupon().setCheck(false);
                }

            }
        } else if (buyBusEven.getFlag().equals(BuyBusEven.WXPAY)) {
            payTypeCode = null;
            boolean status = (boolean) buyBusEven.getObj();
            if (status) {
                paySuccess();
            } else {
                payError();
            }
        }
    }

    private OrderCouponBean chooesCoupon;
    private InVoiceBean chooesinVoiceBean;

    /**
     * 弹出选择时间
     *
     * @param timeBeans
     */
    private void showChooseTime(List<SelectTimeBean> timeBeans) {
        if (!XEmptyUtils.isEmpty(timeBeans)) {
            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                chooseTimeDialog = new ChooseTimeDialog(getActivity(), 1, timeBeans);
            } else {
                chooseTimeDialog = new ChooseTimeDialog(getActivity(), 2, timeBeans);
            }
            Window dialogWindow = chooseTimeDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            chooseTimeDialog.show();
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialogWindow.setLayout(dm.widthPixels, dialogWindow.getAttributes().height);
        }

    }

    /**
     * 弹出选择人数
     *
     * @param manNum
     */
    private void showChooseNum(List<NumBean> manNum) {
        chooseNumDialog = new ChooseNumDialog(getActivity(), getActivity().getResources().getString(R.string.str_order_recom_num), manNum);
        Window dialogWindow = chooseNumDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        chooseNumDialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialogWindow.setLayout(dm.widthPixels, dialogWindow.getAttributes().height);
    }

    /**
     * 弹出支付方式
     *
     * @param
     */
    private void showPayType() {
        if (XEmptyUtils.isEmpty(paymentBeans)) {
            XToast.error(getActivity().getResources().getString(R.string.str_no_pay_type));
            return;
        }
        payDialog = new ChoosePayDialog(getActivity(), paymentBeans);
        Window dialogWindow = payDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        payDialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialogWindow.setLayout(dm.widthPixels, dialogWindow.getAttributes().height);


    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private OrderBean RePostorderBean;

    @Override
    public void postOrderState(OrderBean orderStatus) {
        RePostorderBean = orderStatus;
        switch (payTypeCode) {
            case Constants.ALIPAY:
                mPresenter.alipay(orderStatus.getOrderId());
                break;
            case Constants.WX:
                mPresenter.wxpay(orderStatus.getOrderId());
                break;
            case Constants.BLANCE:
                mPresenter.balancePay(orderStatus.getOrderId());
                break;

        }

    }

    private boolean isWXAppInstall() {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
        api.registerApp(Constants.WX_APP_ID);
        boolean isWXAppInstalled = api.isWXAppInstalled();
        return isWXAppInstalled;
    }

    @Override
    public void aliPayResult(String data) {
        aliPay(data);
    }

    @Override
    public void wxResult(WXPayBean wxPayBean) {
        wxPay(wxPayBean);
    }

    @Override
    public void balanceResult(Boolean data) {
        if (data) {
            paySuccess();
        } else {
            payError();
        }
    }


    /**
     * 支付宝支付
     */
    private void aliPay(final String orderInfo) {
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
                paySuccess();
            } else {
                payError();
            }
        }

    };

    private void wxPay(WXPayBean orderIn) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
        iwxapi.registerApp(Constants.WX_APP_ID);
        PayReq payReq = new PayReq();
        payReq.appId = Constants.WX_APP_ID;
        payReq.partnerId = orderIn.getPartnerId();
        payReq.prepayId = orderIn.getPrepayId();
        payReq.packageValue = orderIn.getPackages();
        payReq.nonceStr = orderIn.getNonceStr();
        payReq.timeStamp = orderIn.getTimeStamp();
        payReq.sign = orderIn.getSign();
        iwxapi.sendReq(payReq);

    }

    /**
     * 支付成功
     */
    private void paySuccess() {
        XToast.info(getActivity().getResources().getString(R.string.str_pay_success));
        _mActivity.onBackPressed();
        EventBus.getDefault().postSticky(new GoodBusEven(GoodBusEven.ORDER_SUCCESS, null));

    }

    /**
     * 支付失败
     */
    private void payError() {
        XToast.info(getActivity().getResources().getString(R.string.str_pay_faile));
    }

    /**
     * 错误返回
     */
    @Override
    public void errorBack() {
        XLoadingDialog.with(getActivity()).dismiss();
        _mActivity.onBackPressed();
        EventBus.getDefault().postSticky(new GoodBusEven(GoodBusEven.ORDER_ERROR, null));
    }

    Bundle savedState;

//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        // Restore State Here
//        if (!restoreStateFromArguments()) {
//            // First Time running, Initialize something here
//        }
//    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!restoreStateFromArguments()) {
            // First Time running, Initialize something here
        }

    }

    /**
     * 保存状态
     *
     * @return
     */
    private Bundle saveState() {
        Bundle state = new Bundle();
        if (!XEmptyUtils.isEmpty(chooesCoupon)) {
            state.putSerializable("chooesCoupon", chooesCoupon);

        }
        if (!XEmptyUtils.isEmpty(chooesinVoiceBean)) {
            state.putSerializable("chooesinVoiceBean", chooesinVoiceBean);

        }
        if (!XEmptyUtils.isEmpty(chooesTimeBean)) {
            state.putSerializable("chooesTimeBean", chooesTimeBean);

        }
        if (!XEmptyUtils.isEmpty(chooesPayMent)) {
            state.putSerializable("chooesPayMent", chooesPayMent);

        }
        if (!XEmptyUtils.isEmpty(ed_content)) {
            if (!XEmptyUtils.isEmpty(ed_content.getText().toString()) || !XEmptyUtils.isSpace(ed_content.getText().toString())) {
                orderMsg = ed_content.getText().toString();
                state.putString("orderMsg", orderMsg);

            }
        }

        return state;
    }

    /**
     * 恢复状态
     */
    private void restoreState() {
        if (savedState != null) {
            chooesCoupon = (OrderCouponBean) savedState.getSerializable("chooesCoupon");
            chooesinVoiceBean = (InVoiceBean) savedState.getSerializable("chooesinVoiceBean");
            chooesTimeBean = (SelectTimeContentBean) savedState.getSerializable("chooesTimeBean");
            chooesPayMent = (PaymentBean) savedState.getSerializable("chooesPayMent");
            orderMsg = savedState.getString("orderMsg");
        }
    }

    /**
     * 恢复状态从arg
     */
    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        savedState = b.getBundle("CONFIRM_ORDER");
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    @Override
    protected void saveStateToArguments() {
        savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            b.putBundle("CONFIRM_ORDER", savedState);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

}
