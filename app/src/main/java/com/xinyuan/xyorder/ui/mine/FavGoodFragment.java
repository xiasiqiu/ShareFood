package com.xinyuan.xyorder.ui.mine;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.mvp.contract.FavGoodView;
import com.xinyuan.xyorder.mvp.presenter.FavGoodPresenter;
import com.xinyuan.xyorder.adapter.order.FavGoodsAdapter;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.loadingview.XLoadingView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FavGoodFragment extends BaseFragment<FavGoodPresenter> implements FavGoodView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView rv_fav;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.tv_header_right)
    TextView tv_header_right;
    @BindView(R.id.toolbar)
    Toolbar msg_toolbar;
    @BindView(R.id.lodingView)
    XLoadingView loadingView;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rl_edit)
    RelativeLayout rl_edit;

    private FavGoodsAdapter adapter;
    private List<StoreDetail> storeList;
    private List<String> idList = new ArrayList<>();
    private boolean falg = true;
    private int page = 1;
    private static final int limit = 10;

    public static FavGoodFragment newInstance() {
        FavGoodFragment fragment = new FavGoodFragment();
        return fragment;
    }


    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), msg_toolbar);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        tv_header_right.setVisibility(View.GONE);
        tv_header_right.setText("编辑");
        tv_header_center.setText("我的收藏");
    }

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(1);
        rv_fav.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器
        rv_fav.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        adapter = new FavGoodsAdapter(R.layout.item_store_collection, storeList, false);
        rv_fav.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Map<String, Long> params = new HashMap();
                params.put(Constants.STORE_ID, storeList.get(position).getShopId());
                CommUtil.goActivity(getActivity(), StoreDetailActivity.class, false, params);
            }
        });
        mPresenter.getFavGoods(page, limit);
    }

    @Override
    protected FavGoodPresenter createPresenter() {
        return new FavGoodPresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_fav_goods;
    }

    @Override
    public void detelteToast(boolean res) {
        XToast.info("删除成功");
        adapter.choeseList.clear();
        idList.clear();
        if (XEmptyUtils.isEmpty(adapter.getData()) || adapter.getData().size() == 0) {
            tv_header_right.setVisibility(View.GONE);
            rl_edit.setVisibility(View.GONE);
        }
    }

    @Override
    public void showState(int state) {
        switch (state) {
            case 0:
                loadingView.showLoading();
                break;
            case 1:
                loadingView.showContent();
                break;
            case 2:
                loadingView.showError();
                break;
            case 3:
                loadingView.showEmpty();

        }
    }

    @Override
    public void showFavInfo(List<StoreDetail> shopDetails) {
//        storeList.addAll(shopDetails);
        mSwipeRefreshLayout.setRefreshing(false);
        if (XEmptyUtils.isEmpty(shopDetails)) {
            if (page == 1) {
                showState(3);
            } else {
                adapter.loadMoreEnd();
            }
            tv_header_right.setVisibility(View.GONE);
        } else {
            tv_header_right.setVisibility(View.VISIBLE);
            showState(1);
            if (page == 1) {
                this.storeList = shopDetails;
                adapter.setNewData(shopDetails);
                mSwipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
                adapter.setEnableLoadMore(true);
            } else {
                adapter.addData(shopDetails);
                adapter.loadMoreComplete();
                mSwipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick({R.id.bt_detelte, R.id.iv_header_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_detelte:
                if (!CommUtil.isFastClick()) {
                    Iterator iter = adapter.choeseList.entrySet().iterator();
                    List<Integer> position = new ArrayList<>();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        idList.add((String) entry.getValue().toString().trim());
                        position.add((Integer) entry.getKey());
//                    adapter.remove((Integer) entry.getKey());
                    }
                    Collections.sort(position, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            int i = o1 - o2;
                            if (i == 0) {
                                return o1 - o2;
                            }
                            return i;
                        }
                    });
                    for (int i = 0; i < position.size(); i++) {
                        adapter.remove(position.get(i) - i);
                    }
                    if (adapter.getData().size() == 0) {
                        loadingView.showEmpty();
                    }
                    adapter.notifyDataSetChanged();
                    if (idList.size() > 0) {
                        mPresenter.deteleFavGood(idList);
                    }
                    XLog.list(idList);
                }
                break;
            case R.id.iv_header_left:
                _mActivity.onBackPressed();
                break;
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                mPresenter.getFavGoods(page, limit);
            }
        }, 500);
    }

    @Override
    public void initListener() {
        loadingView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getFavGoods(page, limit);
            }
        });
        tv_header_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (falg) {
                    adapter.isDetele = true;
                    falg = false;
                    rl_edit.setVisibility(View.VISIBLE);
                    adapter.setNewData(storeList);
                    tv_header_right.setText("完成");
                } else {
                    adapter.isDetele = false;
                    falg = true;
                    rl_edit.setVisibility(View.GONE);
                    tv_header_right.setText("编辑");
                    adapter.setNewData(adapter.getData());
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        rv_fav.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getFavGoods(page, limit);
            }

        }, 500);
    }

}
