package com.xinyuan.xyorder.widget;


import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.ui.buy.ConfirmOrderActivity;
import com.xinyuan.xyorder.ui.login.LoginActivity;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

/**
 * Created by f-x on 2017/10/1815:50
 * 购物车按钮
 */


public class ShopCarView extends FrameLayout {
    private TextView tv_total_price, tv_reserve, tv_pay, tv_original_price, tv_send_price, tv_no_good, view_no_pay;
    public BGABadgeImageView iv_store_cart;
    private BottomSheetBehavior behavior;
    public boolean sheetScrolling;
    public View ll_cart;
    public int[] carLoc;

    public void setBehavior(final BottomSheetBehavior behavior, final View blackView) {
        this.behavior = behavior;
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetScrolling = false;
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    blackView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                sheetScrolling = true;
                blackView.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(blackView, slideOffset);
            }
        });
        blackView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });

    }

    public ShopCarView(@NonNull Context context) {
        super(context);
    }

    public ShopCarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    Map<String, String> params = new HashMap();

    public void setType(String type) {
        switch (type) {
            case Constants.STORE_RESERVE:
                tv_reserve.setVisibility(VISIBLE);
                break;
            case Constants.STORE_TAKEOUT:
                tv_pay.setVisibility(VISIBLE);
                break;
            case Constants.STORE_RESERVE_TAKEOUT:
                tv_reserve.setVisibility(VISIBLE);
                tv_pay.setVisibility(VISIBLE);
                break;

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (iv_store_cart == null) {
            iv_store_cart = findViewById(R.id.iv_store_cart);
            tv_total_price = findViewById(R.id.tv_total_price);
            tv_reserve = findViewById(R.id.tv_reserve);
            tv_original_price = findViewById(R.id.tv_original_price);
            tv_pay = findViewById(R.id.tv_pay);
            tv_no_good = findViewById(R.id.tv_no_good);
            ll_cart = findViewById(R.id.ll_cart);
            tv_send_price = findViewById(R.id.tv_send_price);
            ll_cart.setOnClickListener(new toggleCar());
            carLoc = new int[2];
            iv_store_cart.getLocationInWindow(carLoc);
            view_no_pay = findViewById(R.id.view_no_pay);
            carLoc[0] = carLoc[0] + iv_store_cart.getWidth() / 2 - XDensityUtils.dp2px(10);
            tv_reserve.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    postOrder(Constants.ORDER_RESERVCE);
                }
            });
            tv_pay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv_pay.getText().toString().equals("去结算")) {
                        postOrder(Constants.ORDER_NORML);

                    }
                }
            });
        }
    }

    /**
     * 提交订单
     *
     * @param orderType
     */
    private void postOrder(String orderType) {
        if (XEmptyUtils.isEmpty(DataUtil.getToken(getContext()))) {
            CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
            return;
        }
        if (openStatus(StoreDetailActivity.isOpen)) {
            params.put(Constants.ORDER_TYPE, orderType);
            params.put(Constants.STORE_ID, String.valueOf(StoreDetailActivity.shopId));
            EventBus.getDefault().postSticky(new BuyBusEven(BuyBusEven.ADDORDER, StoreDetailActivity.carAdapter.getData()));
            CommUtil.gotoActivity(getContext(), ConfirmOrderActivity.class, false, params);
        }
    }

    /**
     * 判断是否能下单支付
     *
     * @param isOpen
     * @return
     */
    private boolean openStatus(int isOpen) {
        boolean status = true;

        if (isOpen == 3) {
            XToast.error("店铺休息中，暂时无法接单");
            status = false;
        } else if (XEmptyUtils.isEmpty(StoreDetailActivity.carAdapter.getData())) {
            XToast.error("还未选择商品");
            status = false;
        }

        return status;
    }

    /**
     * 更新显示数量
     *
     * @param total
     */
    public void showBadge(int total) {
        if (total > 0) {
            iv_store_cart.showTextBadge(String.valueOf(total));
        } else {
            iv_store_cart.hiddenBadge();
        }
    }

    /**
     * 初始化支付按钮状态
     */
    public void updatePayStatus() {
        if (StoreDetailActivity.isOpen == 3) {
            view_no_pay.setVisibility(VISIBLE);
            view_no_pay.setText("店家歇业中");
            tv_original_price.setTextColor(getContext().getResources().getColor(R.color.tv_hint));
            tv_total_price.setTextColor(getContext().getResources().getColor(R.color.tv_hint));
        } else {
            view_no_pay.setVisibility(GONE);
            tv_pay.setText(getContext().getString(R.string.money_rmb) + CommUtil.getPriceString(StoreDetailActivity.minDeliveryPrice) + "起送");
        }


    }

    /**
     * 更新价格显示
     *
     * @param amount
     * @param originalamout
     */
    public void updateAmount(BigDecimal amount, BigDecimal originalamout) {
        //当未选择商品时
        if (XEmptyUtils.isEmpty(StoreDetailActivity.carAdapter.getData()) || amount.compareTo(new BigDecimal(0.0)) == 0) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            tv_total_price.setTextSize(12);
            tv_total_price.setTextColor(getContext().getResources().getColor(R.color.tv_hint));
            tv_no_good.setVisibility(VISIBLE);
            tv_original_price.setVisibility(GONE);
            tv_total_price.setVisibility(GONE);
            tv_send_price.setVisibility(GONE);
            tv_pay.setText(getContext().getString(R.string.money_rmb) + CommUtil.getPriceString(StoreDetailActivity.minDeliveryPrice) + "起送");

        } else {
            tv_total_price.setVisibility(VISIBLE);
            tv_no_good.setVisibility(GONE);
            tv_total_price.setTextSize(18);
            tv_total_price.setTextColor(getContext().getResources().getColor(R.color.bg_white));
            //当不满足起送价
            if (originalamout.compareTo(StoreDetailActivity.minDeliveryPrice) < 0) {
                tv_pay.setText("还差" + getContext().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(StoreDetailActivity.minDeliveryPrice.subtract(originalamout)) + "起送");
            } else {
                tv_pay.setText("去结算");
            }
            tv_send_price.setVisibility(VISIBLE);
            if (StoreDetailActivity.store_fee.compareTo(new BigDecimal(0.0)) == 0) {
                tv_send_price.setText("免配送费");
            } else {
                tv_send_price.setText("另需配送费" + getContext().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(StoreDetailActivity.store_fee));
            }
            //当商品价等于商品折后价（等于没优惠时）
            if (amount.compareTo(originalamout) == 0) {
                tv_total_price.setText("¥" + CommUtil.getPriceString(amount));
                tv_original_price.setVisibility(GONE);
            } else {    //普通情况下
                tv_original_price.setVisibility(VISIBLE);
                tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_total_price.setText("¥" + CommUtil.getPriceString(amount));
                tv_original_price.setText("¥" + CommUtil.getPriceString(originalamout));
            }
        }

    }

    private class toggleCar implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (!XEmptyUtils.isEmpty(StoreDetailActivity.carAdapter.getData())) {
                if (sheetScrolling) {
                    return;
                }
                if (XEmptyUtils.isEmpty(StoreDetailActivity.carAdapter)) {
                    return;
                }
                if (StoreDetailActivity.carAdapter.getItemCount() == 0) {
                    return;
                }
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

        }
    }
}