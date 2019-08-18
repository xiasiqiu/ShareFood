//package com.xinyuan.xyorder.ui.home;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.PopupWindow;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.xinyuan.xyorder.R;
//import com.xinyuan.xyorder.adapter.ShopStoreAdapter;
//import com.xinyuan.xyorder.adapter.area.AreaListAdapter;
//import com.xinyuan.xyorder.app.MyApplication;
//import com.xinyuan.xyorder.app.base.BaseFragment;
//import com.xinyuan.xyorder.common.bean.AreanBean;
//import com.xinyuan.xyorder.common.bean.SearchSortItemBean;
//import com.xinyuan.xyorder.common.bean.store.StoreListData;
//import com.xinyuan.xyorder.common.bean.store.store.StoreCategoryBean;
//import com.xinyuan.xyorder.common.constant.Constants;
//import com.xinyuan.xyorder.common.even.SearchStoreEven;
//import com.xinyuan.xyorder.main.MainActivity;
//import com.xinyuan.xyorder.mvp.contract.SearchFoodView;
//import com.xinyuan.xyorder.mvp.presenter.SearchFoodPresenter;
//import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
//import com.xinyuan.xyorder.util.CommUtil;
//import com.xinyuan.xyorder.util.SystemBarHelper;
//import com.xinyuan.xyorder.widget.EditTextWithDel;
//import com.xinyuan.xyorder.widget.RecycleViewDivider;
//import com.xinyuan.xyorder.widget.SupportPopupWindow;
//import com.youth.xframe.utils.XEmptyUtils;
//import com.youth.xframe.widget.loadingview.XLoadingView;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by f-x on 2017/11/1614:12
// */
//
//public class SearchStoreFragment extends BaseFragment<SearchFoodPresenter> implements SearchFoodView, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
//    @BindView(R.id.cb_area_sort)
//    CheckBox cb_area_sort;
//    @BindView(R.id.cb_class_sort)
//    CheckBox cb_class_sort;
//    @BindView(R.id.cb_distance_sort)
//    CheckBox cb_distance_sort;
//    @BindView(R.id.mtoolbar)
//    Toolbar mtoolbar;
//    @BindView(R.id.loadingView)
//    XLoadingView loadingView;
//    @BindView(R.id.et_search_food)
//    EditTextWithDel et_search_food;
//    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
//    @BindView(R.id.rvStoreList)
//    RecyclerView rvStoreList;
//    @BindView(R.id.iv_back)
//    ImageView iv_back;
//
//
//    private ShopStoreAdapter shopStoreAdapter;
//    private List<SearchSortItemBean> storeCategoryList; //店铺分类
//    private List<SearchSortItemBean> areaList;  //地区列表
//    private List<SearchSortItemBean> sortList; //排序规则列表
//    public static SupportPopupWindow storeCategoryWindow;
//    public static SupportPopupWindow areaListWindow;
//    public static SupportPopupWindow sortListWindow;
//
//    private LinearLayoutManager manager;
//
//    private int page = 1;
//    private int limit = 10;
//    private String keyWord = "";
//    private String sort = "";
//    private String areaId = "";
//    private String shopCategoryId = "";
//    private boolean iscollectioned;
//    private SearchSortItemBean searchSortItemBean;
//
//    public static SearchStoreFragment newInstance(SearchSortItemBean sortItemBean) {
//        SearchStoreFragment fragment = new SearchStoreFragment();
//        fragment.searchSortItemBean = sortItemBean;
//        return fragment;
//    }
//
//    @Override
//    protected SearchFoodPresenter createPresenter() {
//        return new SearchFoodPresenter(mContext, this);
//    }
//
//    @Override
//    protected int provideContentViewId() {
//        return R.layout.activity_search_food;
//    }
//
//    @Override
//    public void initView(View rootView) {
//        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
//        SystemBarHelper.setHeightAndPadding(getActivity(), mtoolbar);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
//        CommUtil.setBack(getActivity(), iv_back);
//
//    }
//
//    @Override
//    public void initData() {
//
//
//    }
//
//    @Override
//    public void onEnterAnimationEnd(Bundle savedInstanceState) {
//        super.onEnterAnimationEnd(savedInstanceState);
//        if (!XEmptyUtils.isEmpty(searchSortItemBean)) {
//            cb_class_sort.setText(searchSortItemBean.getItemName());
//            cb_class_sort.setSelected(true);
//            shopCategoryId = searchSortItemBean.getSortItemId();
//        }
//        Search();
//        mPresenter.getGoodClass();
//        getSortList();
//        if (!XEmptyUtils.isEmpty(MyApplication.currentCityCode)) {
//            mPresenter.getAreaList();
//        }
//    }
//
//    private void Search() {
//        keyWord = et_search_food.getText().toString().trim();
//        mPresenter.getStoreData(page, limit, keyWord, sort, iscollectioned, areaId, shopCategoryId);
//    }
//
//
//    @Override
//    public void initListener() {
//        loadingView.setOnRetryClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Search();
//            }
//        });
//        cb_area_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (XEmptyUtils.isEmpty(areaList)) {
//                    return;
//                }
//                if (isChecked) {
//                    cb_area_sort.setChecked(true);
//                    cb_class_sort.setChecked(false);
//                    cb_distance_sort.setChecked(false);
//                    if (!XEmptyUtils.isEmpty(areaListWindow)) {
//                        areaListWindow.showAsDropDown(cb_area_sort);
//                    }
//
//                } else {
//                    areaListWindow.dismiss();
//                    cb_area_sort.setChecked(false);
//
//
//                }
//            }
//        });
//        cb_class_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (XEmptyUtils.isEmpty(storeCategoryList)) {
//                    return;
//                }
//                if (isChecked) {
//                    cb_area_sort.setChecked(false);
//                    cb_class_sort.setChecked(true);
//                    cb_distance_sort.setChecked(false);
//                    if (!XEmptyUtils.isEmpty(storeCategoryWindow)) {
//                        storeCategoryWindow.showAsDropDown(cb_class_sort);
//
//                    }
//                } else {
//                    storeCategoryWindow.dismiss();
//                    cb_class_sort.setChecked(false);
//
//
//                }
//            }
//        });
//        cb_distance_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    cb_area_sort.setChecked(false);
//                    cb_class_sort.setChecked(false);
//                    cb_distance_sort.setChecked(true);
//                    if (!XEmptyUtils.isEmpty(sortListWindow)) {
//                        sortListWindow.showAsDropDown(cb_class_sort);
//                    }
//                } else {
//                    sortListWindow.dismiss();
//                    cb_distance_sort.setChecked(false);
//
//                }
//            }
//        });
//        onTouchListener = new MainActivity.MyOnTouchListener() {
//            @Override
//            public boolean onTouchEvent(MotionEvent ev) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(et_search_food.getWindowToken(), 0);
//                return false;
//            }
//        };
//        ((MainActivity) getActivity()).registerMyOnTouchListener(onTouchListener);
//
//
//    }
//
//    MainActivity.MyOnTouchListener onTouchListener;
//
//    @Override
//    public void showArea(List<AreanBean> data) {
//        areaList = new ArrayList<SearchSortItemBean>();
//        for (AreanBean s : data) {
//            SearchSortItemBean sortItemBean = new SearchSortItemBean(s.getAreaName(), false, s.getAreaId());
//            areaList.add(sortItemBean);
//        }
//        areaList.add(0, new SearchSortItemBean("全部区域", true, "0"));
//        areaListWindow = setViewPopSort();
//        setSortDataList(1, areaListWindow, areaList);
//        cb_area_sort.setText(areaList.get(0).getItemName());
//    }
//
//    @Override
//    public void showStoreList(StoreListData data) {
//        if (XEmptyUtils.isEmpty(shopStoreAdapter)) {
//            if (XEmptyUtils.isEmpty(data.getList())) {
//                loadingView.showEmpty();
//            } else {
//                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                layoutManager.setOrientation(1);
//                rvStoreList.setLayoutManager(layoutManager);
//                this.rvStoreList.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 1, mContext.getResources().getColor(R.color.gray_cc)));
//                shopStoreAdapter = new ShopStoreAdapter(R.layout.item_shop_store, data.getList());
//                shopStoreAdapter.setOnLoadMoreListener(this, rvStoreList);
//                shopStoreAdapter.setAutoLoadMoreSize(4);
//                rvStoreList.setAdapter(shopStoreAdapter);
//
//                shopStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        Map<String, Long> params = new HashMap();
//                        params.put(Constants.STORE_ID, shopStoreAdapter.getData().get(position).getShopId());
//                        CommUtil.goActivity(getActivity(), StoreDetailActivity.class, false, params);
//                    }
//                });
//                loadingView.showContent();
//                page++;
//            }
//
//        } else {
//            if (XEmptyUtils.isEmpty(data.getList())) {
//                if (page == 1) {
//                    shopStoreAdapter.setEnableLoadMore(false);
//                    loadingView.showEmpty();
//                } else {
//                    shopStoreAdapter.loadMoreEnd();
//                }
//            } else {
//                if (page == 1) {
//                    rvStoreList.scrollToPosition(0);
//                    shopStoreAdapter.setNewData(data.getList());
//                    shopStoreAdapter.setEnableLoadMore(true);
//                    loadingView.showContent();
//                } else {
//                    shopStoreAdapter.addData(data.getList());
//                    shopStoreAdapter.loadMoreComplete();
//
//                }
//                page++;
//            }
//
//
//        }
//    }
//
//    @Override
//    public void showStoreCategory(List<StoreCategoryBean> data) {
//        storeCategoryList = new ArrayList<>();
//        if (!XEmptyUtils.isEmpty(data)) {
//            for (StoreCategoryBean bean : data) {
//                SearchSortItemBean sortItemBean = new SearchSortItemBean(bean.getShopCategoryName(), false, String.valueOf(bean.getShopCategoryId()));
//                storeCategoryList.add(sortItemBean);
//            }
//            if (XEmptyUtils.isSpace(shopCategoryId)) {
//                storeCategoryList.add(0, new SearchSortItemBean("全部分类", true, "0"));
//            } else {
//                cb_class_sort.setText(searchSortItemBean.getItemName());
//
//                for (SearchSortItemBean itemBean : storeCategoryList) {
//                    if (String.valueOf(itemBean.getSortItemId()).equals(shopCategoryId)) {
//                        itemBean.setCheck(true);
//                    }
//                }
//                storeCategoryList.add(0, new SearchSortItemBean("全部分类", false, "0"));
//            }
//            storeCategoryWindow = setViewPopSort();
//            setSortDataList(2, storeCategoryWindow, storeCategoryList);
//        }
//    }
//
//
//    /**
//     * 设置sort排序列表
//     */
//    private void getSortList() {
//        sortList = new ArrayList<>();
//        sortList.add(new SearchSortItemBean("排序不限", false, ""));
//        sortList.add(new SearchSortItemBean("距离优先", false, "DISTANCE"));
//        sortList.add(new SearchSortItemBean("销量优先", false, "SALES"));
//        sortList.add(new SearchSortItemBean("评分最高", false, "SCORE"));
//        sortList.get(0).setCheck(true);
//        sortListWindow = setViewPopSort();
//        setSortDataList(3, sortListWindow, sortList);
//        cb_distance_sort.setText(sortList.get(0).getItemName());
//
//    }
//
//    /**
//     * 设置下拉框
//     *
//     * @param type
//     * @param popupWindow
//     * @param list
//     */
//    private void setSortDataList(int type, final PopupWindow popupWindow, final List<SearchSortItemBean> list) {
//        if (!XEmptyUtils.isEmpty(list)) {
//            RecyclerView rv_area_list = popupWindow.getContentView().findViewById(R.id.rv_area_list);
//            AreaListAdapter areaListAdapter = new AreaListAdapter(R.layout.item_area, 2, type, list);
//            rv_area_list.setAdapter(areaListAdapter);
//            rv_area_list.setLayoutManager(new LinearLayoutManager(mContext) {
//                @Override
//                public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//                    if (getChildCount() > 0) {
//                        View firstChildView = recycler.getViewForPosition(0);
//                        measureChild(firstChildView, widthSpec, heightSpec);
//                        if (list.size() > 5) {
//                            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * 5);
//                        } else {
//                            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * list.size());
//                        }
//                    } else {
//                        super.onMeasure(recycler, state, widthSpec, heightSpec);
//                    }
//                }
//            });
//        }
//
//
//    }
//
//    /**
//     * 设置popView
//     *
//     * @return
//     */
//    private SupportPopupWindow setViewPopSort() {
//        View viewPopSort = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_sort_popwindow, null);
//        final SupportPopupWindow popSort = new SupportPopupWindow(viewPopSort, WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);
//        popSort.setTouchable(true);
//        popSort.setOutsideTouchable(true);
//        popSort.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        viewPopSort.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (popSort != null && popSort.isShowing()) {
//                    popSort.dismiss();
//                    if (!XEmptyUtils.isEmpty(cb_area_sort)) {
//                        if (cb_area_sort.isChecked()) {
//                            cb_area_sort.setChecked(false);
//                        }
//                    }
//                    if (!XEmptyUtils.isEmpty(cb_class_sort)) {
//                        if (cb_class_sort.isChecked()) {
//                            cb_class_sort.setChecked(false);
//                        }
//                    }
//                    if (!XEmptyUtils.isEmpty(cb_distance_sort)) {
//                        if (cb_distance_sort.isChecked()) {
//                            cb_distance_sort.setChecked(false);
//                        }
//                    }
//                }
//                return false;
//            }
//        });
//        return popSort;
//    }
//
//    /**
//     * 获取通讯消息
//     *
//     * @param goodBusEven
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSearchStoreEvent(SearchStoreEven goodBusEven) {
//        if (goodBusEven.getFlag().equals(SearchStoreEven.HAVECITY)) {
//            mPresenter.getAreaList();
//            cb_area_sort.setText("全部区域");
//        } else if (goodBusEven.getFlag().equals(SearchStoreEven.KEYWORD)) {
//            keyWord = (String) goodBusEven.getObj();
//        } else if (goodBusEven.getFlag().equals(SearchStoreEven.AREAR_SEARCH)) {
//            loadingView.showLoading();
//            page = 1;
//            SearchSortItemBean sortItemBean = (SearchSortItemBean) goodBusEven.getObj();
//            this.areaId = sortItemBean.getSortItemId();
//            cb_area_sort.setText(sortItemBean.getItemName());
//            Search();
//        } else if (goodBusEven.getFlag().equals(SearchStoreEven.CLASS_SEARCH)) {
//            loadingView.showLoading();
//            page = 1;
//            SearchSortItemBean sortItemBean = (SearchSortItemBean) goodBusEven.getObj();
//            this.shopCategoryId = sortItemBean.getSortItemId();
//            cb_class_sort.setText(sortItemBean.getItemName());
//
//            Search();
//        } else if (goodBusEven.getFlag().equals(SearchStoreEven.ORDER_SEARCH)) {
//            loadingView.showLoading();
//            page = 1;
//            SearchSortItemBean sortItemBean = (SearchSortItemBean) goodBusEven.getObj();
//            this.sort = sortItemBean.getSortItemId();
//            cb_distance_sort.setText(sortItemBean.getItemName());
//
//            Search();
//        } else if (goodBusEven.getFlag().equals(SearchStoreEven.SEARCH_CLASS)) {
//            loadingView.showLoading();
//            page = 1;
//            SearchSortItemBean sortItemBean = (SearchSortItemBean) goodBusEven.getObj();
//            this.shopCategoryId = sortItemBean.getSortItemId();
//            Search();
//            if (!XEmptyUtils.isEmpty(cb_class_sort)) {
//                cb_class_sort.setSelected(true);
//                cb_area_sort.setSelected(false);
//                cb_distance_sort.setSelected(false);
//                cb_class_sort.setText(sortItemBean.getItemName());
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ((MainActivity) getActivity()).unregisterMyOnTouchListener(onTouchListener);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        registerEventBus(this);
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        unregisterEventBus(this);
//
//    }
//
//    @OnClick({R.id.cb_area_sort, R.id.cb_class_sort, R.id.cb_distance_sort, R.id.iv_search})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_search:
////                if (XEmptyUtils.isEmpty(et_search_food.getText().toString().trim()) || XEmptyUtils.isSpace(et_search_food.getText().toString().trim())) {
////                    return;
////                }
//                page = 1;
//                keyWord = et_search_food.getText().toString().trim();
//                loadingView.showLoading();
//                Search();
//                break;
//        }
//
//    }
//
//
//    @Override
//    public void showState(int state) {
//        switch (state) {
//            case 0:
//                loadingView.showLoading();
//                break;
//            case 1:
//                loadingView.showContent();
//                break;
//            case 2:
//                loadingView.showError();
//                break;
//            case 3:
//                loadingView.showEmpty();
//            case 4:
//                loadingView.showNoNetwork();
//                break;
//
//        }
//    }
//
//
//    @Override
//    public void onRefresh() {
//        mSwipeRefreshLayout.setRefreshing(false);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                page = 1;
//                Search();
//            }
//        }, 500);
//
//    }
//
//
//    @Override
//    public void onLoadMoreRequested() {
//        rvStoreList.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Search();
//            }
//
//        }, 500);
//
//    }
//}