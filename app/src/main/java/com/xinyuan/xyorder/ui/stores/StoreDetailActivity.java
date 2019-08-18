package com.xinyuan.xyorder.ui.stores;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.github.florent37.viewanimator.ViewAnimator;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.ItemTitlePagerAdapter;
import com.xinyuan.xyorder.adapter.storedetail.CarAdapter;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.common.bean.CartAmoutBean;
import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.store.StoreActivityShowTextBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodsCategory;
import com.xinyuan.xyorder.common.bean.store.good.GoodsPropertys;
import com.xinyuan.xyorder.common.bean.store.good.TypeBean;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.GoodBusEven;
import com.xinyuan.xyorder.mvp.contract.StoreGoodsView;
import com.xinyuan.xyorder.mvp.presenter.StoreGoodsPresenter;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.widget.AddWidget;
import com.xinyuan.xyorder.widget.GoodsSpecDialog;
import com.xinyuan.xyorder.widget.NoScrollViewPager;
import com.xinyuan.xyorder.widget.ShopCarView;
import com.xinyuan.xyorder.widget.ShopInfoContainer;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XLoadingDialog;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/1815:50
 * 店铺主页面
 */

public class StoreDetailActivity extends BaseActivity<StoreGoodsPresenter> implements StoreGoodsView, AddWidget.OnAddClick {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.store__tabs)
    SlidingTabLayout psts_tabs;
    @BindView(R.id.vp_content)
    NoScrollViewPager vp_content;
    @BindView(R.id.scroll_container)
    LinearLayout scroll_container;
    @BindView(R.id.shopInfoContainer)
    ShopInfoContainer shopInfoContainer;
    @BindView(R.id.ll_cut)
    LinearLayout ll_cut;
    @BindView(R.id.tv_store_act_num)
    TextView tv_store_act_num;
    private CoordinatorLayout rootview;
    public BottomSheetBehavior behavior;
    public static CarAdapter carAdapter;
    private StoreGoodsFragment storeGoodsFragment;
    private StoreEvaluateFragment storeEvaluateFragment;
    private StoreDetailFragment storeDetailFragment;
    private ShopCarView shopCarView;
    private TextView tv_fee_price;  //配送费
    private RelativeLayout rl_cart_fee;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<TypeBean> typeList;
    private List<GoodBean> goodList;
    private StoreDetail storeDetail;
    public static long shopId;
    public static int topActivityheight;
    public static String busBeginTime;     //营业开始时间
    public static String busEndTime;       //营业结束时间
    private BigDecimal fee_price;//餐盒费
    public static BigDecimal minDeliveryPrice;  //起送价
    public static BigDecimal store_fee;         //配送费
    public static final String CAR_ACTION = "handleCar";
    public static int isOpen = 0;  //0：正常，1，还未营业，2，已结束营业，3，休息
    private CartAmoutBean cartAmoutBean = new CartAmoutBean();  //购物价格

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_store;
    }

    @Override
    public void initView() {
        topActivityheight = 0;
        XLoadingDialog.with(StoreDetailActivity.this)
                .setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框
        rootview = (CoordinatorLayout) findViewById(R.id.rootview);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    public void initData() {
        shopId = getIntent().getLongExtra(Constants.STORE_ID, 0);
        mPresenter.getStoreGoodsData(shopId);
    }

    @Override
    protected StoreGoodsPresenter createPresenter() {
        return new StoreGoodsPresenter(this, this);
    }

    /**
     * 设置ViewPager
     */
    private void initViewpager() {
        fragmentList.add(storeGoodsFragment = StoreGoodsFragment.newInstance(typeList, goodList));
        fragmentList.add(storeEvaluateFragment = StoreEvaluateFragment.newInstance(shopId));
        fragmentList.add(storeDetailFragment = StoreDetailFragment.newInstance(storeDetail));
        vp_content.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "评价", "详情"}));
        vp_content.setOffscreenPageLimit(4);
        psts_tabs.setViewPager(vp_content);

    }

    /**
     * 设置购物车
     */
    private void initShopCar() {
        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
        View blackView = findViewById(R.id.blackview);
        shopCarView.setBehavior(behavior, blackView);
        shopCarView.setType(storeDetail.getShopType());
        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
        carRecView.setLayoutManager(new LinearLayoutManager(this));
        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
        carAdapter = new CarAdapter(new ArrayList<ChooesCarGoodBean>(), this);
        carAdapter.bindToRecyclerView(carRecView);
        shopCarView.updatePayStatus();

        View bottomView = LayoutInflater.from(this).inflate(R.layout.item_cat_bottom, (ViewGroup) carRecView.getParent(), false);
        rl_cart_fee = bottomView.findViewById(R.id.rl_cart_fee);
        tv_fee_price = bottomView.findViewById(R.id.tv_fee_price);
        carAdapter.addFooterView(bottomView);
    }


    /**
     * 设置店铺活动
     */
    private void initStoreActive() {
        if (!XEmptyUtils.isEmpty(storeDetail.getShopActive())) {
            topActivityheight = storeDetail.getShopActive().size() * 75;
            tv_store_act_num.setText(storeDetail.getShopActive().size() + "个活动");
            for (int i = 0; i < storeDetail.getShopActive().size(); i++) {
                LinearLayout rl_view = new LinearLayout(this);
                rl_view.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv1 = new TextView(this);
                ImageView tv3 = new ImageView(this);
                StoreActivityShowTextBean activityShowTextBean = (DataUtil.getCouponText(this, storeDetail.getShopActive().get(i).getActivityType()));
                tv3.setImageDrawable(this.getResources().getDrawable(activityShowTextBean.getAcitvityColor()));
                tv1.setText(storeDetail.getShopActive().get(i).getActivityName());
                tv1.setTextColor(this.getResources().getColor(R.color.tv_91));
                tv1.setTextSize(11);
                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp3.topMargin = 5;
                lp3.bottomMargin = 10;
                rl_view.addView(tv3, lp3);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.topMargin = 5;
                lp1.leftMargin = 20;
                lp1.bottomMargin = 10;
                rl_view.addView(tv1, lp1);
                ll_cut.addView(rl_view);
            }
        } else {
            tv_store_act_num.setText("店铺暂无活动");
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoodBeanEven(GoodBusEven even) {
        if (even.getFlag().equals(GoodBusEven.FINISH)) {
            finish();
        } else if (even.getFlag().equals(GoodBusEven.LOADING)) {
            XLoadingDialog.with(StoreDetailActivity.this).dismiss();
        } else if (even.getFlag().equals(GoodBusEven.ORDER_SUCCESS)) {
            cleanCar();
        } else if (even.getFlag().equals(GoodBusEven.ADDCAR)) {
            GoodBean goodBean = (GoodBean) even.getObj();
            onAddClick(storeGoodsFragment.getFoodAdapter().getViewByPosition(storeGoodsFragment.getFoodAdapter().getData().indexOf(goodBean), R.id.addwidget), (GoodBean) even.getObj());
        } else if (even.getFlag().equals(GoodBusEven.SUBCAR)) {
            onSubClick((GoodBean) even.getObj());
        }
    }


    /**
     * 添加操作
     *
     * @param view
     * @param gb
     */
    @Override
    public void onAddClick(final View view, final GoodBean gb) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommUtil.addTvAnim(view, shopCarView.carLoc, StoreDetailActivity.this, rootview);
            }
        }, 400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dealCar(gb);
            }
        }, 700);
    }


    /**
     * 删减操作
     *
     * @param gb
     */
    @Override
    public void onSubClick(GoodBean gb) {
        int selecCount = 0;
        for (ChooesCarGoodBean chooesCarGoodBean : carAdapter.getData()) {
            if (chooesCarGoodBean.getGoodId() == gb.getGoodsId()) {
                selecCount += chooesCarGoodBean.getChooesNum();
            }
        }
        if (selecCount != 0) {
            gb.setSelectCount(selecCount - 1);
        }

        dealCar(gb);
    }

    /**
     * 展开活动列表
     *
     * @param view
     */

    public void expendCut(View view) {
        float cty = scroll_container.getTranslationY();
        if (!CommUtil.isFastClick()) {
            ViewAnimator.animate(scroll_container)
                    //  translationY(cty, cty == 0 ? AppBarBehavior.cutExpHeight : 0)
                    .translationY(cty, cty == 0 ? topActivityheight : 0)
                    .decelerate()
                    .duration(100)
                    .start();
        }
    }


    /**
     * 计算购物车
     *
     * @param foodBean
     */
    private void dealCar(GoodBean foodBean) {


        //判断购物车
        ChooesCarGoodBean chooesCarGoodBean = new ChooesCarGoodBean(foodBean.getGoodsId());
        chooesCarGoodBean.setChooesProertys(foodBean.getChooesProerty());
        chooesCarGoodBean.setSelecCount(chooesCarGoodBean.getSelecCount());
        chooesCarGoodBean.setGoodActivityBean(foodBean.getActivityGoods());
        chooesCarGoodBean.setGoodPrice(foodBean.getChooesSpecID().getGoodsSpecificationPrice());
        chooesCarGoodBean.setGoodsName(foodBean.getGoodsName());
        chooesCarGoodBean.setGoodStock(foodBean.getChooesSpecID().getStock());
        chooesCarGoodBean.setFeelPrice(foodBean.getChooesSpecID().getBoxesMoney());
        chooesCarGoodBean.setSpecId(foodBean.getChooesSpecID().getGoodsSpecificationId());
        chooesCarGoodBean.setSepcName(foodBean.getChooesSpecID().getGoodsSpecificationName());

        boolean hasFood = false;
        boolean sameFood = false;
        fee_price = new BigDecimal(0.0);    //餐盒费

        int total = 0;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            storeGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        }
        int p = -1;
        for (int i = 0; i < carAdapter.getData().size(); i++) {
            ChooesCarGoodBean carIndexGoodBean = carAdapter.getData().get(i);
            if (foodBean.getGoodsId() == carIndexGoodBean.getGoodId()) {
                hasFood = true;
                if (chooesCarGoodBean.getSpecId() == carIndexGoodBean.getSpecId()) {
                    if (XEmptyUtils.isEmpty(chooesCarGoodBean.getChooesProertys()) && XEmptyUtils.isEmpty(carIndexGoodBean.getChooesProertys())) {
                        sameFood = true;
                        if (foodBean.getSelectCount() == 0) {
                            carIndexGoodBean.setChooesNum(0);
                            p = i;
                        } else {
                            if (foodBean.getSelectCount() > carIndexGoodBean.getSelecCount()) {
                                carIndexGoodBean.setChooesNum((carIndexGoodBean.getChooesNum() + 1));
                            } else {
                                if (carIndexGoodBean.getChooesNum() - 1 == 0) {
                                    carIndexGoodBean.setChooesNum(0);
                                    p = i;
                                } else {
                                    carIndexGoodBean.setChooesNum((carIndexGoodBean.getChooesNum() - 1));
                                }
                            }
                            carAdapter.setData(i, carIndexGoodBean);
                            for (ChooesCarGoodBean chooesCarGoodBean1 : carAdapter.getData()) {
                                if (chooesCarGoodBean1.getGoodId() == chooesCarGoodBean.getGoodId()) {
                                    carAdapter.notifyItemChanged(carAdapter.getData().indexOf(chooesCarGoodBean1));
                                }
                            }
                        }
                    } else if (chooesCarGoodBean.getChooesProertys().equals(carIndexGoodBean.getChooesProertys())) {
                        sameFood = true;
                        if (foodBean.getSelectCount() == 0) {
                            carIndexGoodBean.setChooesNum(0);
                            p = i;
                        } else {
                            if (foodBean.getSelectCount() > carIndexGoodBean.getSelecCount() && carIndexGoodBean.getSelecCount() != 0) {
                                carIndexGoodBean.setChooesNum((carIndexGoodBean.getChooesNum() + 1));
                            } else {
                                if (carIndexGoodBean.getChooesNum() - 1 == 0) {
                                    carIndexGoodBean.setChooesNum(0);
                                    p = i;
                                } else {
                                    carIndexGoodBean.setChooesNum((carIndexGoodBean.getChooesNum() - 1));

                                }
                            }
                            carAdapter.setData(i, carIndexGoodBean);
                            for (ChooesCarGoodBean chooesCarGoodBean1 : carAdapter.getData()) {
                                if (chooesCarGoodBean1.getGoodId() == chooesCarGoodBean.getGoodId()) {
                                    carAdapter.notifyItemChanged(carAdapter.getData().indexOf(chooesCarGoodBean1));
                                }
                            }
                        }

                    }

                }
            }
            total += carIndexGoodBean.getChooesNum();
        }
        if (p >= 0) {
            carAdapter.remove(p);
            for (GoodBean goodBean : storeGoodsFragment.getFoodAdapter().getData()) {
                if (goodBean.getGoodsId() == foodBean.getGoodsId()) {
                    goodBean.setSelectCount(0);
                    storeGoodsFragment.getFoodAdapter().notifyItemChanged(storeGoodsFragment.getFoodAdapter().getData().indexOf(goodBean));
                }
            }
        } else if (!sameFood && foodBean.getSelectCount() > 0) {   //没有该商品
            chooesCarGoodBean.setChooesNum(1);
            chooesCarGoodBean.setFeelPrice(foodBean.getChooesSpecID().getBoxesMoney());
            chooesCarGoodBean.setSelecCount(foodBean.getSelectCount());
            carAdapter.addData(chooesCarGoodBean);
            for (ChooesCarGoodBean chooesCarGoodBean1 : carAdapter.getData()) {
                if (chooesCarGoodBean1.getGoodId() == chooesCarGoodBean.getGoodId()) {
                    carAdapter.notifyItemChanged(carAdapter.getData().indexOf(chooesCarGoodBean1));
                }
            }
            total += chooesCarGoodBean.getChooesNum();
        }


//            fee_price = fee_price.add(chooesCarGoodBean1.getFeelPrice().multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
//            if (!XEmptyUtils.isEmpty(chooesCarGoodBean1.getGoodActivityBean())) {
//                originalAmount = originalAmount.add((chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice())).multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
//                BigDecimal priceA = chooesCarGoodBean1.getGoodPrice().multiply(chooesCarGoodBean1.getGoodActivityBean().getDiscount()).divide(new BigDecimal(10)).add(chooesCarGoodBean1.getFeelPrice());
//                BigDecimal priceB = chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice());
//                if (chooesCarGoodBean1.getSelecCount() > chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity() && chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity() != 0) {
//                    amount = amount.add(priceA.multiply(BigDecimal.valueOf(chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity())).add(priceB.multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum() - chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity()))));
//                } else {
//                    amount = amount.add(priceA.multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
//                }
//            } else {
//                amount = amount.add((chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice())).multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
//                originalAmount = originalAmount.add((chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice())).multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
//            }


        for (GoodBean goodBean : storeGoodsFragment.getFoodAdapter().getData()) {
            int selecCount = 0;
            for (ChooesCarGoodBean carGoodBean : carAdapter.getData()) {
                if (goodBean.getGoodsId() == carGoodBean.getGoodId()) {
                    selecCount += carGoodBean.getChooesNum();
                    goodBean.setSelectCount(selecCount);
                    storeGoodsFragment.getFoodAdapter().notifyItemChanged(storeGoodsFragment.getFoodAdapter().getData().indexOf(goodBean));
                }
            }

        }
        shopCarView.showBadge(total);
        updateFeePrice();
    }

    /**
     * 检查配送费
     */
    private ConcurrentHashMap<Long, List<ChooesCarGoodBean>> goodsList;

    private void updateFeePrice() {
        HashMap<String, Integer> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);    //实际价格（商品折后价+餐盒费）
        BigDecimal originalAmount = new BigDecimal(0.0);    //原价（商品原价+餐盒费）
        goodsList = new ConcurrentHashMap<Long, List<ChooesCarGoodBean>>();


        for (ChooesCarGoodBean chooesCarGoodBean1 : carAdapter.getData()) {
            if (goodsList.containsKey(chooesCarGoodBean1.getGoodId())) {
                goodsList.get(chooesCarGoodBean1.getGoodId()).add(chooesCarGoodBean1);
            } else {
                List<ChooesCarGoodBean> chooesCarGoodBeans = new ArrayList<>();
                chooesCarGoodBeans.add(chooesCarGoodBean1);
                goodsList.put(chooesCarGoodBean1.getGoodId(), chooesCarGoodBeans);
            }

//
//            if (goodsList.size() == 0) {
//                List<ChooesCarGoodBean> chooesCarGoodBeans = new ArrayList<>();
//                chooesCarGoodBeans.add(chooesCarGoodBean1);
//                goodsList.put(chooesCarGoodBean1.getGoodId(), chooesCarGoodBeans);
//            } else {
//                Iterator it = goodsList.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry<Long, List<ChooesCarGoodBean>> entry = (Map.Entry<Long, List<ChooesCarGoodBean>>) it.next();
//                    if (entry.getKey() == chooesCarGoodBean1.getGoodId()) {
//                        entry.getValue().add(chooesCarGoodBean1);
//                    } else {
//                        List<ChooesCarGoodBean> chooesCarGoodBeans = new ArrayList<>();
//                        chooesCarGoodBeans.add(chooesCarGoodBean1);
//                        goodsList.put(chooesCarGoodBean1.getGoodId(), chooesCarGoodBeans);
//                    }
//                }
//
//
//            }
        }
        for (Map.Entry<Long, List<ChooesCarGoodBean>> entry : goodsList.entrySet()) {
            int couponCount = 0;
            if (!XEmptyUtils.isEmpty(entry.getValue().get(0).getGoodActivityBean())) {
                couponCount = entry.getValue().get(0).getGoodActivityBean().getLimitedQuantity();
            }
            for (ChooesCarGoodBean carGoodBean : entry.getValue()) {
                fee_price = fee_price.add(carGoodBean.getFeelPrice().multiply(BigDecimal.valueOf(carGoodBean.getChooesNum())));
                originalAmount = originalAmount.add((carGoodBean.getGoodPrice().add(carGoodBean.getFeelPrice())).multiply(BigDecimal.valueOf(carGoodBean.getChooesNum())));
                BigDecimal priceB = carGoodBean.getGoodPrice().add(carGoodBean.getFeelPrice());

                if (!XEmptyUtils.isEmpty(carGoodBean.getGoodActivityBean())) {
                    BigDecimal priceA = carGoodBean.getGoodPrice().multiply(carGoodBean.getGoodActivityBean().getDiscount()).divide(new BigDecimal(10)).add(carGoodBean.getFeelPrice());
                    if (carGoodBean.getChooesNum() > couponCount) {
                        amount = amount.add(priceA.multiply(BigDecimal.valueOf(couponCount)).add(priceB.multiply(BigDecimal.valueOf(carGoodBean.getChooesNum() - couponCount))));
                        couponCount = 0;
                    } else {
                        amount = amount.add(priceA.multiply(BigDecimal.valueOf(carGoodBean.getChooesNum())));
                        couponCount -= carGoodBean.getChooesNum();
                    }
                } else {
                    amount = amount.add(priceB.multiply(BigDecimal.valueOf(carGoodBean.getChooesNum())));
                }

            }
        }
        shopCarView.updateAmount(amount, originalAmount);
        carAdapter.toString();
        rl_cart_fee.setVisibility(View.VISIBLE);
        tv_fee_price.setText(getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(fee_price));


    }

    /**
     * 清空购物车（购物车按钮）
     *
     * @param view
     */
    public void clearCar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView tv = new TextView(this);
        tv.setText("清空购物车?");
        tv.setTextSize(14);
        tv.setPadding(XDensityUtils.dp2px(16), XDensityUtils.dp2px(16), 0, 0);
        tv.setTextColor(Color.parseColor("#757575"));
        AlertDialog alertDialog = builder
                .setNegativeButton("取消", null)
                .setCustomTitle(tv)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanCar();

                    }
                })
                .show();
        Button nButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        nButton.setTypeface(Typeface.DEFAULT_BOLD);
        Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        pButton.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 清空购物车（逻辑清空）
     */
    private void cleanCar() {
        for (int i = 0; i < carAdapter.getData().size(); i++) {
            ChooesCarGoodBean fb = carAdapter.getData().get(i);
            fb.setChooesNum(0);
        }
        carAdapter.setNewData(new ArrayList<ChooesCarGoodBean>());
        for (GoodBean goodBean : storeGoodsFragment.getFoodAdapter().getData()) {
            if (goodBean.getSelectCount() != 0) {
                goodBean.setSelectCount(0);
            }
        }
        storeGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        shopCarView.showBadge(0);
        storeGoodsFragment.getTypeAdapter().updateBadge(new HashMap<String, Integer>());
        shopCarView.updateAmount(new BigDecimal(0.0), new BigDecimal(0.0));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        topActivityheight = 0;
        isOpen = 0;
        store_fee = BigDecimal.valueOf(0);
        minDeliveryPrice = BigDecimal.valueOf(0);
        busBeginTime = null;
        busEndTime = null;
        carAdapter = null;
        unregisterEventBus(this);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus(this);
    }

    private GoodsSpecDialog goodsSpecDialog;

    @Override
    public void showSpec(GoodBean gb) {
        goodsSpecDialog = new GoodsSpecDialog(this, 1);
        goodsSpecDialog.setChooseListener(new GoodsSpecDialog.ChooseListener() {

            @Override
            public void cancleListener() {
                goodsSpecDialog.dismiss();
            }
        });
        goodsSpecDialog.setGoodsBean(gb);
        goodsSpecDialog.show();
    }

    /**
     * 显示商品分类
     *
     * @param goodsCategoryList
     */
    @Override
    public void showCategory(List<GoodsCategory> goodsCategoryList) {
        if (!XEmptyUtils.isEmpty(goodsCategoryList)) {
            typeList = new ArrayList<TypeBean>();
            goodList = new ArrayList<GoodBean>();
            for (GoodsCategory category : goodsCategoryList) {
                TypeBean typeBean = new TypeBean();
                typeBean.setCategoryId(category.getGoodsCategoryId());
                typeBean.setName(category.getGoodsCategoryName());
                typeList.add(typeBean);
                for (GoodBean goodBean : category.getGoodsList()) {
                    goodBean.setGoodsClassNames(category.getGoodsCategoryName());
                    goodBean.setChooesSpecID(goodBean.getGoodsSpecifications().get(0));
                    LinkedList<String> chooproperty = new LinkedList<>();
                    if (!XEmptyUtils.isEmpty(goodBean.getGoodsPropertys())) {
                        for (GoodsPropertys goodsPropertys : goodBean.getGoodsPropertys()) {
                            chooproperty.add(goodsPropertys.getGoodsPropertyValueList().get(0).getValue());
                        }
                    }
                    goodBean.setChooesProerty(chooproperty);
                }

                goodList.addAll(category.getGoodsList());
            }
        }
        initViewpager();
    }


    /**
     * 显示店铺信息
     *
     * @param storeDetail
     */
    @Override
    public void ShowStoreInfo(StoreDetail storeDetail) {
        if (!XEmptyUtils.isEmpty(storeDetail)) {
            this.storeDetail = storeDetail;
            if (XEmptyUtils.isEmpty(shopInfoContainer)) {
                return;
            }
            shopInfoContainer.showInfo(storeDetail);    //头部显示信息
            busBeginTime = storeDetail.getBusBeginTime();   //初始化营业开始时间
            busEndTime = storeDetail.getBusEndTime();       //初始化营业结束时间
            minDeliveryPrice = storeDetail.getMinDeliveryPrice();   //初始化最低起送价
            store_fee = storeDetail.getFee();               //初始化店铺配送费
//            isOpen = DataUtil.isOpen(storeDetail.getBusBeginTime(), storeDetail.getBusEndTime());   //初始化店铺营业状态

            if (!storeDetail.isOperatingState()) {
                isOpen = 3;
            }

//            if (StoreDetailActivity.isOpen == 1) {
//                XToast.info("店铺还未开始营业，请耐心等待", 4000);
//            } else if (StoreDetailActivity.isOpen == 2) {
//                XToast.info("店铺已经休息啦", 4000);
//            }
            initShopCar();
            initStoreActive();
        }

    }

    /**
     * 商品详情购物车广播
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CAR_ACTION.equals(intent.getAction())) {
                GoodBean foodBean = (GoodBean) intent.getSerializableExtra("foodbean");
                GoodBean fb = foodBean;
                int p = intent.getIntExtra("position", -1);
                if (p >= 0 && p < storeGoodsFragment.getFoodAdapter().getItemCount()) {
                    fb = storeGoodsFragment.getFoodAdapter().getItem(p);
                    fb.setSelectCount(foodBean.getSelectCount());
                    storeGoodsFragment.getFoodAdapter().setData(p, fb);
                } else {
                    for (int i = 0; i < storeGoodsFragment.getFoodAdapter().getItemCount(); i++) {
                        fb = storeGoodsFragment.getFoodAdapter().getItem(i);
                        if (fb.getGoodsId() == foodBean.getGoodsId()) {
                            fb.setSelectCount(foodBean.getSelectCount());
                            storeGoodsFragment.getFoodAdapter().setData(i, fb);
                            break;
                        }
                    }
                }
                dealCar(fb);
            }
        }
    };

    @Override
    public void onBackPressedSupport() {
        finish();
    }


}
