package com.xinyuan.xyorder.ui.stores;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.storedetail.CarAdapter;
import com.xinyuan.xyorder.adapter.storedetail.GoodDetailAdapter;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.GoodBusEven;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.widget.AddWidget;
import com.xinyuan.xyorder.widget.GoodListContainer;
import com.xinyuan.xyorder.widget.GoodsSpecDialog;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.xinyuan.xyorder.widget.ShopCarView;
import com.xinyuan.xyorder.widget.behaviors.DetailHeaderBehavior;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/1819:20
 * 商品详情页
 */

public class GoodDetailActivity extends BaseActivity implements AddWidget.OnAddClick {
    private GoodBean goodBean;
    private AddWidget detail_add;
    public BottomSheetBehavior behavior;
    private ShopCarView shopCarView;
    private CarAdapter carAdapter;
    private CoordinatorLayout detail_main;
    private DetailHeaderBehavior dhb;
    private View headerView;
    private int position = -1;
    private GoodDetailAdapter goodDetailAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_good_detail;
    }

    @Override
    public void initView() {
        goodBean = (GoodBean) getIntent().getSerializableExtra("good");
        position = getIntent().getIntExtra("position", -1);
        detail_main = (CoordinatorLayout) findViewById(R.id.detail_main);
        headerView = findViewById(R.id.headerview);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) headerView.getLayoutParams();
        dhb = (DetailHeaderBehavior) lp.getBehavior();
        ImageView iv_detail = (ImageView) findViewById(R.id.iv_detail);
        GlideImageLoader.setGoodUrlImg(this, Constants.IMAGE_HOST + goodBean.getGoodsImgUrl() + Constants.IMG_GOOD_MEDIUM, iv_detail);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(goodBean.getGoodsName());
        TextView detail_name = (TextView) findViewById(R.id.detail_name);
        detail_name.setText(goodBean.getGoodsName());
        TextView detail_sale = (TextView) findViewById(R.id.detail_sale);
        detail_sale.setText("月售" + goodBean.getGoodSales());
        TextView tv_good_content = (TextView) findViewById(R.id.tv_good_content);
        tv_good_content.setText(goodBean.getGoodsContent());
        TextView detail_eva = (TextView) findViewById(R.id.detail_eva);
        detail_eva.setText("好评率" + DataUtil.getgoodsRateApprise(goodBean.getGoodRateApprise()));
        TextView detail_price = (TextView) findViewById(R.id.detail_price);
        detail_price.setText(goodBean.getStrPrice(goodBean.getGoodsPrice()));
        detail_add = (AddWidget) findViewById(R.id.detail_add);
        initRecyclerView();
        initShopCar();

        boolean noRduce = false;
        int sameNum = 1;
        int selecCount = 0;
        for (ChooesCarGoodBean chooesCarGoodBean : StoreDetailActivity.carAdapter.getData()) {
            if (goodBean.getGoodsId() == chooesCarGoodBean.getGoodId()) {
                sameNum++;
                selecCount += chooesCarGoodBean.getChooesNum();
            }
        }
        if (sameNum > 2) {
            noRduce = true;
        }
        goodBean.setSelectCount(selecCount);
        goodBean.setSelectCount(selecCount);
        detail_add.setData(this, goodBean, false, false, noRduce);
        detail_add.setState(selecCount);
        detail_add.setListener();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.detail_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        goodDetailAdapter = new GoodDetailAdapter(GoodListContainer.commandList, this);
        View header = View.inflate(this, R.layout.item_good_detail_header, null);
        goodDetailAdapter.addHeaderView(header);
        TextView footer = new TextView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, XDensityUtils.dp2px(100));
        footer.setText("已经没有更多了");
        footer.setTextSize(12);
        footer.setTextColor(ContextCompat.getColor(this, R.color.detail_hint));
        footer.setGravity(Gravity.CENTER_HORIZONTAL);
        footer.setPadding(0, 30, 0, 0);
        footer.setLayoutParams(lp);
        goodDetailAdapter.addFooterView(footer);
        goodDetailAdapter.bindToRecyclerView(recyclerView);
    }

    private void initShopCar() {
        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
        View blackView = findViewById(R.id.blackview);
        shopCarView.setBehavior(behavior, blackView);
        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
        carRecView.setLayoutManager(new LinearLayoutManager(this));
        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
        ArrayList<ChooesCarGoodBean> flist = new ArrayList<>();
        flist.addAll(StoreDetailActivity.carAdapter.getData());
        carAdapter = new CarAdapter(flist, this);
        carAdapter.bindToRecyclerView(carRecView);
        handleCommand(goodBean);
//        shopCarView.post(new Runnable() {
//            @Override
//            public void run() {
//                dealCar(goodBean);
//            }
//        });
    }

    /**
     * 計算購物車
     *
     * @param foodBean
     */
    private void dealCar(GoodBean foodBean) {
        BigDecimal amount = new BigDecimal(0.0);    //实际价格（商品折后价+餐盒费）
        BigDecimal originalAmount = new BigDecimal(0.0);    //原价（商品原价+餐盒费）
        BigDecimal fee_price = new BigDecimal(0.0);//餐盒费
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
        int total = 0;
        boolean hasFood = false;
        boolean sameFood = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED || foodBean.getGoodsId() == this.goodBean.getGoodsId() && foodBean.getSelectCount() !=
                this.goodBean.getSelectCount()) {
            this.goodBean = foodBean;
            detail_add.expendAdd(foodBean.getSelectCount());
            handleCommand(foodBean);
        }

        List<ChooesCarGoodBean> indexCarGoodBeans = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < indexCarGoodBeans.size(); i++) {
            ChooesCarGoodBean indexCarGoodBean = indexCarGoodBeans.get(i);
            if (indexCarGoodBean.getGoodId() == chooesCarGoodBean.getGoodId()) {
                hasFood = true;
                if (foodBean.getSelectCount() == 0) {
                    p = i;
                } else if (indexCarGoodBean.getSpecId() == foodBean.getChooesSpecID().getGoodsSpecificationId()) {
                    sameFood = true;
                    if (foodBean.getSelectCount() > indexCarGoodBean.getChooesNum()) {
                        indexCarGoodBean.setChooesNum((indexCarGoodBean.getChooesNum() + 1));
                    } else {
                        indexCarGoodBean.setChooesNum((indexCarGoodBean.getChooesNum() - 1));
                    }
                }
            }
            total += indexCarGoodBean.getChooesNum();
        }

        if (p > 0) {
            carAdapter.remove(p);
            chooesCarGoodBean.setChooesNum(0);

        } else if (!sameFood && foodBean.getSelectCount() > 0) {
            chooesCarGoodBean.setChooesNum(1);
            chooesCarGoodBean.setFeelPrice(foodBean.getChooesSpecID().getBoxesMoney());
            chooesCarGoodBean.setSelecCount(foodBean.getSelectCount());
            carAdapter.addData(chooesCarGoodBean);
            total += chooesCarGoodBean.getChooesNum();
        }


        for (ChooesCarGoodBean chooesCarGoodBean1 : carAdapter.getData()) {
            amount = amount.add((chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice())).multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
            originalAmount = originalAmount.add((chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice())).multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
            fee_price = fee_price.add(chooesCarGoodBean1.getFeelPrice().multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
            if (!XEmptyUtils.isEmpty(chooesCarGoodBean1.getGoodActivityBean())) {
                originalAmount = originalAmount.add((chooesCarGoodBean1.getGoodPrice().add(chooesCarGoodBean1.getFeelPrice())).multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
                BigDecimal priceA = chooesCarGoodBean1.getGoodActivityBean().getGoodsPrice().multiply(chooesCarGoodBean1.getGoodActivityBean().getDiscount()).divide(new BigDecimal(10)).add(chooesCarGoodBean1.getFeelPrice());
                BigDecimal priceB = chooesCarGoodBean1.getGoodActivityBean().getGoodsPrice().add(chooesCarGoodBean1.getFeelPrice());
                if (chooesCarGoodBean1.getChooesNum() > chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity() && chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity() != 0) {
                    amount = amount.add(priceA.multiply(BigDecimal.valueOf(chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity())).add(priceB.multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum() - chooesCarGoodBean1.getGoodActivityBean().getLimitedQuantity()))));
                } else {
                    amount = amount.add(priceA.multiply(BigDecimal.valueOf(chooesCarGoodBean1.getChooesNum())));
                }
            }
        }
        shopCarView.showBadge(total);
        shopCarView.updateAmount(amount, originalAmount);


    }

    private void handleCommand(GoodBean foodBean) {
        for (int i = 0; i < goodDetailAdapter.getData().size(); i++) {
            GoodBean fb = goodDetailAdapter.getItem(i);
            if (fb.getGoodsId() == foodBean.getGoodsId() && fb.getSelectCount() != foodBean.getSelectCount()) {
                fb.setSelectCount(foodBean.getSelectCount());
                goodDetailAdapter.setData(i, fb);
                goodDetailAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void close(View view) {
        finish();
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private GoodsSpecDialog goodsSpecDialog;

    @Override
    public void showSpec(GoodBean goodBean) {
        goodsSpecDialog = new GoodsSpecDialog(this, 2);
        goodsSpecDialog.setChooseListener(new GoodsSpecDialog.ChooseListener() {
            @Override
            public void cancleListener() {
                goodsSpecDialog.dismiss();
            }
        });
        goodsSpecDialog.setGoodsBean(goodBean);
        goodsSpecDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    public void onGoodBeanEven(GoodBusEven even) {
        if (even.getFlag().equals(GoodBusEven.DETAILADDCAR)) {
            GoodBean goodBean = (GoodBean) even.getObj();
            onAddClick(detail_add, goodBean);
        }
    }


    @Override
    public void onAddClick(View view, GoodBean gb) {
        Intent intent = new Intent(StoreDetailActivity.CAR_ACTION);
        if (gb.getGoodsId() == this.goodBean.getGoodsId()) {
            intent.putExtra("position", position);
            gb.setPosition(position);
        }
        intent.putExtra("foodbean", gb);
        sendBroadcast(intent);
        CommUtil.addTvAnim(view, shopCarView.carLoc, this, detail_main);
        if (!dhb.canDrag) {
            ViewAnimator.animate(headerView).alpha(1, -4).duration(410).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {
                    finish();
                }
            }).start();
        }

    }

    @Override
    public void onSubClick(GoodBean gb) {
        Intent intent = new Intent(StoreDetailActivity.CAR_ACTION);
        if (gb.getGoodsId() == this.goodBean.getGoodsId()) {
            intent.putExtra("position", position);
            gb.setPosition(position);
        }
        intent.putExtra("foodbean", gb);
        sendBroadcast(intent);
        CommUtil.addTvAnim(detail_add, shopCarView.carLoc, this, detail_main);
        if (!dhb.canDrag) {
            ViewAnimator.animate(headerView).alpha(1, -4).duration(410).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {
                    finish();
                }
            }).start();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus(this);
    }
}
