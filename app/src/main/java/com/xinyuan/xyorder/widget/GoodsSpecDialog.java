package com.xinyuan.xyorder.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nex3z.flowlayout.FlowLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.storedetail.GoodsSpecAdapter;
import com.xinyuan.xyorder.common.AddViewHolder;
import com.xinyuan.xyorder.common.bean.buy.CarGoodBean;
import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.buy.SpecFlag;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodsPropertyBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodsPropertys;
import com.xinyuan.xyorder.common.bean.store.good.GoodsSpecifications;
import com.xinyuan.xyorder.common.bean.store.good.SpecialsGoodBean;
import com.xinyuan.xyorder.common.even.GoodBusEven;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by HDQ on 2017/12/19.
 */

public class GoodsSpecDialog extends Dialog implements View.OnClickListener {

    private Activity mActivity;
    private ImageView iv_del;
    private RecyclerView rv_goods_spec;
    private ChooseListener chooseListener;
    private GoodsSpecAdapter goodsSpecAdapter;
    private GoodBean goodBean;
    private FlowLayout fl_spec_tag;
    private TextView tv_price, tv_coupon_price, tv_t_coupon_price;
    private int count = 1;
    //    private AddWidget detail_add;
    private TextView tv_choose_ok;
    public static LinkedList<String> chooesProerty;
    private GoodsSpecifications goodsSpecifications;
    private int mode;   //1-列表页 2-详情页

    public GoodsSpecDialog(Activity activity, int mode) {
        super(activity);
        this.mActivity = activity;
        this.mode = mode;
    }

    public void setGoodsBean(GoodBean goodBean) {
        this.goodBean = goodBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_goods_spec);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        initView();

    }

    private void initView() {
        iv_del = findViewById(R.id.iv_del);
        rv_goods_spec = findViewById(R.id.rv_goods_spec);
        tv_coupon_price = findViewById(R.id.tv_coupon_price);
        tv_t_coupon_price = findViewById(R.id.tv_t_coupon_price);
        tv_price = findViewById(R.id.tv_price);
        tv_choose_ok = findViewById(R.id.tv_choose_ok);
        chooesProerty = new LinkedList<>();
        if (goodBean.getGoodsPropertys() != null && goodBean.getGoodsPropertys().size() > 0) {
            for (int i = 0; i < goodBean.getGoodsPropertys().size(); i++) {
                initCheck(goodBean.getGoodsPropertys().get(i));
                chooesProerty.add(goodBean.getGoodsPropertys().get(i).getGoodsPropertyValueList().get(0).getValue());
                goodBean.getGoodsPropertys().get(i).getGoodsPropertyValueList().get(0).setChecked(true);
            }
        }
        if (goodBean.getGoodsSpecifications() != null && goodBean.getGoodsSpecifications().size() > 1) {
            goodsSpecAdapter = new GoodsSpecAdapter(R.layout.item_goods_spec, goodBean.getGoodsPropertys(),true);
        }else {
            goodsSpecAdapter = new GoodsSpecAdapter(R.layout.item_goods_spec, goodBean.getGoodsPropertys(),false);
        }
        rv_goods_spec.setAdapter(goodsSpecAdapter);
        rv_goods_spec.setLayoutManager(new LinearLayoutManager(mActivity));
        iv_del.setOnClickListener(new ClickListener());
        tv_choose_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chooesFlag) {
                    goodsSpecifications.setCheckNum(1);
                    goodBean.setChooesSpecID(goodsSpecifications);
                    goodBean.setChooesProerty(chooesProerty);
                    goodBean.setSelectCount(goodBean.getSelectCount() + 1);
                    if (mode == 1) {
                        EventBus.getDefault().post(new GoodBusEven(GoodBusEven.ADDCAR, goodBean));
                    } else {
                        EventBus.getDefault().post(new GoodBusEven(GoodBusEven.DETAILADDCAR, goodBean));

                    }
                    dismiss();
                }

            }
        });
        addTop();
    }

    public void addTop() {

        if (goodBean.getGoodsSpecifications() != null && goodBean.getGoodsSpecifications().size() > 1) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_goods_spec, (ViewGroup) rv_goods_spec.getParent(), false);
            goodsSpecAdapter.addHeaderView(view);
            fl_spec_tag = view.findViewById(R.id.fl_spec_tag);
            if (!XEmptyUtils.isEmpty(goodBean.getActivityGoods())) {
                tv_t_coupon_price.setVisibility(View.VISIBLE);
                tv_coupon_price.setVisibility(View.VISIBLE);
                tv_price.setText(CommUtil.getPriceString(goodBean.getGoodsSpecifications().get(0).getGoodsSpecificationPrice().multiply(goodBean.getActivityGoods().getDiscount().divide(new BigDecimal(10)))));
                tv_coupon_price.setText(CommUtil.getPriceString(goodBean.getGoodsSpecifications().get(0).getGoodsSpecificationPrice()));
                tv_coupon_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tv_price.setText(CommUtil.getPriceString(goodBean.getGoodsSpecifications().get(0).getGoodsSpecificationPrice()));
            }

            initCheck(goodBean);
            goodBean.getGoodsSpecifications().get(0).setChecked(true);
            goodsSpecifications = goodBean.getGoodsSpecifications().get(0);
            initSpecificationData();
        } else {
            goodBean.getGoodsSpecifications().get(0).setChecked(true);
            goodsSpecifications = goodBean.getGoodsSpecifications().get(0);
        }

    }

    private boolean chooesFlag = false;

    private void initSpecificationData() {
        fl_spec_tag.removeAllViews();
        for (int i = 0; i < goodBean.getGoodsSpecifications().size(); i++) {
            AddViewHolder addViewHolder = new AddViewHolder(mActivity, R.layout.view_goods_spec_tv);
            final TextView tv_tag = addViewHolder.getView(R.id.tv_tag);
            tv_tag.setText(goodBean.getGoodsSpecifications().get(i).getGoodsSpecificationName());
            if (goodBean.getGoodsSpecifications().get(i).isChecked()) {
                tv_tag.setActivated(true);
                tv_tag.setTextColor(mActivity.getResources().getColorStateList(R.color.green));
            } else {
                tv_tag.setActivated(false);
                tv_tag.setTextColor(mActivity.getResources().getColorStateList(R.color.black_33));
            }

            final int finalI = i;
            tv_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tv_tag.isActivated()) {
                        tv_tag.setActivated(true);
                        initCheck(goodBean);
                        goodBean.getGoodsSpecifications().get(finalI).setChecked(true);
                        initSpecificationData();
                        goodsSpecifications = goodBean.getGoodsSpecifications().get(finalI);
                        if (!XEmptyUtils.isEmpty(goodBean.getActivityGoods())) {
                            tv_t_coupon_price.setVisibility(View.VISIBLE);
                            tv_coupon_price.setVisibility(View.VISIBLE);
                            tv_price.setText(CommUtil.getPriceString(goodBean.getGoodsSpecifications().get(finalI).getGoodsSpecificationPrice().multiply(goodBean.getActivityGoods().getDiscount().divide(new BigDecimal(10)))));
                            tv_coupon_price.setText(CommUtil.getPriceString(goodBean.getGoodsSpecifications().get(finalI).getGoodsSpecificationPrice()));
                            tv_coupon_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            tv_price.setText(CommUtil.getPriceString(goodBean.getGoodsSpecifications().get(finalI).getGoodsSpecificationPrice()));
                        }
//                        if (!goodsSpecifications.isInfiniteInventory()) {
//                            for (ChooesCarGoodBean chooesCarGoodBean : StoreDetailActivity.carAdapter.getData()) {
//                                if (chooesCarGoodBean.getSpecId() == goodBean.getGoodsSpecifications().get(finalI).getGoodsSpecificationId()) {
//                                    if (chooesCarGoodBean.getChooesNum() >= goodBean.getGoodsSpecifications().get(finalI).getStock()) {
//                                        tv_choose_ok.setBackgroundColor(getContext().getResources().getColor(R.color.bg_gray));
//                                        tv_choose_ok.setText("库存不足");
//                                        chooesFlag = true;
//                                    } else {
//                                        tv_choose_ok.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg_goods_spec));
//                                        tv_choose_ok.setText("选好了");
//                                        chooesFlag = false;
//                                    }
//
//                                }
//                            }
//                        } else {
//                            tv_choose_ok.setBackground(getContext().getResources().getDrawable(R.drawable.button_bg_goods_spec));
//                            tv_choose_ok.setText("选好了");
//                            chooesFlag = false;
//                        }
                    }
                }

            });
            fl_spec_tag.addView(addViewHolder.getCustomView());
        }
    }

    @Override
    public void onClick(View v) {

    }


    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_del:
                    chooseListener.cancleListener();
                    dismiss();
                    break;
            }
        }
    }

    public void setChooseListener(ChooseListener chooseListener) {
        this.chooseListener = chooseListener;
    }

    public interface ChooseListener {
        void cancleListener();

    }

    public void initCheck(GoodBean goodsBean) {
        for (GoodsSpecifications goodsSpec : goodsBean.getGoodsSpecifications()) {
            goodsSpec.setChecked(false);
        }
    }

    public void initCheck(GoodsPropertys item) {
        for (GoodsPropertyBean goodsSpec : item.getGoodsPropertyValueList()) {
            goodsSpec.setChecked(false);
        }
    }

}
