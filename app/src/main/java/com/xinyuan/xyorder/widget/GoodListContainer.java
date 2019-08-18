package com.xinyuan.xyorder.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.storedetail.GoodTypeAdapter;
import com.xinyuan.xyorder.adapter.storedetail.StoreGoodAdapter;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.TypeBean;
import com.xinyuan.xyorder.common.even.GoodBusEven;
import com.xinyuan.xyorder.ui.stores.GoodDetailActivity;
import com.xinyuan.xyorder.ui.stores.StoreGoodsFragment;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f-x on 2017/10/1818:21
 * 商品列表
 */

public class GoodListContainer extends LinearLayout {

    public GoodTypeAdapter typeAdapter;
    private RecyclerView recyclerView2;
    private LinearLayoutManager linearLayoutManager;
    private List<GoodBean> foodBeanList;
    private boolean move;
    private int index;
    private Context mContext;
    public StoreGoodAdapter storeGoodAdapter;
    public static List<GoodBean> commandList = new ArrayList<>();

    private List<TypeBean> typeBeans;

    public GoodListContainer(Context context) {
        super(context);
    }

    public GoodListContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.typeBeans = StoreGoodsFragment.getTypeDatas();
        this.foodBeanList = StoreGoodsFragment.getGoodDatas();
        inflate(mContext, R.layout.view_good_listcontainer, this);
        if (XEmptyUtils.isEmpty(typeBeans)) {
            TextView tv_good_empty = findViewById(R.id.tv_good_empty);
            tv_good_empty.setVisibility(VISIBLE);
        }

        RecyclerView recyclerView1 = findViewById(R.id.recycler1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        typeAdapter = new GoodTypeAdapter(typeBeans);
        View view = new View(mContext);
        view.setMinimumHeight(XDensityUtils.dp2px(55));
        typeAdapter.addFooterView(view);
        typeAdapter.bindToRecyclerView(recyclerView1);
        ((SimpleItemAnimator) recyclerView1.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (recyclerView2.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    typeAdapter.fromClick = true;
                    typeAdapter.setChecked(i);
                    String type = view.getTag().toString();
                    for (int ii = 0; ii < foodBeanList.size(); ii++) {
                        GoodBean goodBean = foodBeanList.get(ii);
                        if (goodBean.getGoodsClassNames().equals(type)) {
                            index = ii;
                            moveToPosition(index);
                            break;
                        }
                    }
                }
            }
        });


    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerView2.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerView2.getChildAt(n - firstItem).getTop();
            recyclerView2.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerView2.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    public void setAddClick(AddWidget.OnAddClick onAddClick) {
        recyclerView2 = findViewById(R.id.recycler2);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView2.setLayoutManager(linearLayoutManager);
        ((DefaultItemAnimator) recyclerView2.getItemAnimator()).setSupportsChangeAnimations(false);

        storeGoodAdapter = new StoreGoodAdapter(foodBeanList, onAddClick);
        View view = new View(mContext);
        view.setMinimumHeight(XDensityUtils.dp2px(55));
        recyclerView2.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        storeGoodAdapter.addFooterView(view);
        storeGoodAdapter.bindToRecyclerView(recyclerView2);
        storeGoodAdapter.openLoadAnimation();
        storeGoodAdapter.isFirstOnly(false);
        final View stickView = findViewById(R.id.stick_header);
        final TextView tvStickyHeaderView = (TextView) stickView.findViewById(R.id.tv_header);
        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = index - linearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerView.getChildAt(n).getTop();
                        //最后的移动
                        recyclerView.smoothScrollBy(0, top);
                    }
                } else {
                    View stickyInfoView = recyclerView.findChildViewUnder(stickView.getMeasuredWidth() / 2, 5);
                    if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                        tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
                        typeAdapter.setType(String.valueOf(stickyInfoView.getContentDescription()));
                    }

                    View transInfoView = recyclerView.findChildViewUnder(stickView.getMeasuredWidth() / 2, stickView.getMeasuredHeight
                            () + 1);
                    if (transInfoView != null && transInfoView.getTag() != null) {
                        int transViewStatus = (int) transInfoView.getTag();
                        int dealtY = transInfoView.getTop() - stickView.getMeasuredHeight();
                        if (transViewStatus == StoreGoodAdapter.HAS_STICKY_VIEW) {
                            if (transInfoView.getTop() > 0) {
                                stickView.setTranslationY(dealtY);
                            } else {
                                stickView.setTranslationY(0);
                            }
                        } else if (transViewStatus == StoreGoodAdapter.NONE_STICKY_VIEW) {
                            stickView.setTranslationY(0);
                        }
                    }
                }
            }
        });
        storeGoodAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final GoodBean orderTestBean = (GoodBean) adapter.getData().get(position);
                if (view.getId() == R.id.ll_good) {
                    Intent intent = new Intent(mContext, GoodDetailActivity.class);
                    intent.putExtra("good", orderTestBean);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

            }
        });
        EventBus.getDefault().post(new GoodBusEven(GoodBusEven.LOADING));

    }


}
