package com.xinyuan.xyorder.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.ItemTitlePagerAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.AreanBean;
import com.xinyuan.xyorder.common.bean.SearchBean;
import com.xinyuan.xyorder.common.bean.SearchSortItemBean;
import com.xinyuan.xyorder.common.bean.store.StoreListData;
import com.xinyuan.xyorder.common.bean.store.store.StoreCategoryBean;
import com.xinyuan.xyorder.common.even.AroundStoreEven;
import com.xinyuan.xyorder.main.MainActivity;
import com.xinyuan.xyorder.mvp.contract.SearchFoodView;
import com.xinyuan.xyorder.mvp.presenter.SearchFoodPresenter;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.EditTextWithDel;
import com.xinyuan.xyorder.widget.NoScrollViewPager;
import com.xinyuan.xyorder.widget.SupportPopupWindow;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/23
 * 附近页面
 */
public class AroundStoreFragment extends BaseFragment<SearchFoodPresenter> implements SearchFoodView, OnTabSelectListener {

    @BindView(R.id.tv_distance_sort)
    TextView tv_distance_sort;
    @BindView(R.id.tb_sell_sort)
    TextView tb_sell_sort;
    @BindView(R.id.tb_high_sort)
    TextView tb_high_sort;
    @BindView(R.id.mtoolbar)
    Toolbar mtoolbar;

    @BindView(R.id.et_search_food)
    EditTextWithDel et_search_food;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    //    @BindView(R.id.tb_type)
//    TabLayout tb_type;
    @BindView(R.id.slb_type)
    SlidingTabLayout slb_type;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    public static String categoryId = "";

    private List<SearchSortItemBean> storeCategoryList; //店铺分类
    private List<SearchSortItemBean> areaList;  //地区列表
    private List<SearchSortItemBean> sortList; //排序规则列表
    public static SupportPopupWindow storeCategoryWindow;
    public static SupportPopupWindow areaListWindow;
    public static SupportPopupWindow sortListWindow;

    private LinearLayoutManager manager;

    private int page = 1;
    private int limit = 10;
    private String keyWord = "";
    private String sort = "";
    private String areaId = "";
    private String shopCategoryId = "";
    private boolean iscollectioned;
    boolean isFromHome;
    SearchSortItemBean searchCategory;
    ItemTitlePagerAdapter itemTitlePagerAdapter;

    public static AroundStoreFragment newInstance(boolean isFromHome, SearchSortItemBean searchCategory) {
        AroundStoreFragment fragment = new AroundStoreFragment();
        fragment.isFromHome = isFromHome;
        fragment.searchCategory = searchCategory;
        return fragment;
    }

    @Override
    protected SearchFoodPresenter createPresenter() {
        return new SearchFoodPresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search_food;
    }


    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), mtoolbar);
        vp.setNoScroll(true);
        if (isFromHome) {
            iv_back.setVisibility(View.VISIBLE);
            CommUtil.setBack(_mActivity, iv_back);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        sort = "DISTANCE";
        if (!XEmptyUtils.isEmpty(searchCategory)) {
            shopCategoryId = searchCategory.getSortItemId();
            categoryId = shopCategoryId;
        }
        mPresenter.getGoodClass();
    }

    @Override
    public void initListener() {
        onTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //login_auto_account.setCursorVisible(false);
                imm.hideSoftInputFromWindow(et_search_food.getWindowToken(), 0);
                return false;
            }
        };
        ((MainActivity) getActivity()).registerMyOnTouchListener(onTouchListener);
    }

    MainActivity.MyOnTouchListener onTouchListener;

    /**
     * 显示店铺列表
     *
     * @param data
     */
    @Override
    public void showStoreList(StoreListData data) {

    }

    @OnClick({R.id.tv_distance_sort, R.id.tb_sell_sort, R.id.tb_high_sort, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                keyWord = et_search_food.getText().toString().trim();
                search();
                break;
            case R.id.tb_sell_sort:
                sort = "SALES";
                search();
                tb_sell_sort.setTextColor(getActivity().getResources().getColor(R.color.green));
                tv_distance_sort.setTextColor(getActivity().getResources().getColor(R.color.gray_4d));
                tb_high_sort.setTextColor(getActivity().getResources().getColor(R.color.gray_4d));
                break;
            case R.id.tb_high_sort:
                sort = "SCORE";
                search();
                tb_high_sort.setTextColor(getActivity().getResources().getColor(R.color.green));
                tv_distance_sort.setTextColor(getActivity().getResources().getColor(R.color.gray_4d));
                tb_sell_sort.setTextColor(getActivity().getResources().getColor(R.color.gray_4d));
                break;
            case R.id.tv_distance_sort:
                sort = "DISTANCE";
                search();
                tv_distance_sort.setTextColor(getActivity().getResources().getColor(R.color.green));
                tb_sell_sort.setTextColor(getActivity().getResources().getColor(R.color.gray_4d));
                tb_high_sort.setTextColor(getActivity().getResources().getColor(R.color.gray_4d));
                break;
        }

    }

    private void search() {
        SearchBean searchBean = new SearchBean();
        searchBean.setKey(keyWord);
//        searchBean.setShopCategoryId(shopCategoryId);
        searchBean.setSort(sort);
        if(isFromHome){
            EventBus.getDefault().post(new AroundStoreEven(AroundStoreEven.SEARCH_SELECT, searchBean,AroundStoreEven.SEARCH_FROM_HOME));
        }else {
            EventBus.getDefault().post(new AroundStoreEven(AroundStoreEven.SEARCH_SELECT, searchBean,AroundStoreEven.SEARCH_FROM_NEAR));
        }

    }

    /**
     * 获取分类列表
     *
     * @param data
     */
    @Override
    public void showStoreCategory(List<StoreCategoryBean> data) {
        storeCategoryList = new ArrayList<>();
        if (!XEmptyUtils.isEmpty(data)) {
            List<String> listtitle = new ArrayList<>();
            listtitle.add("全部分类");
            int check = 0;//默认选中
            for (int i = 0; i < data.size(); i++) {
                SearchSortItemBean sortItemBean = new SearchSortItemBean(data.get(i).getShopCategoryName(), false, String.valueOf(data.get(i).getShopCategoryId()));
                storeCategoryList.add(sortItemBean);
                listtitle.add(sortItemBean.getItemName());
                if (!XEmptyUtils.isEmpty(shopCategoryId)) {
                    String id = String.valueOf(data.get(i).getShopCategoryId());
                    if (shopCategoryId.equals(id)) {
                        check = (i + 1);
                    }
                }
            }
            mFragments.clear();
            for (int i = 0; i < listtitle.size(); i++) {
                if(i==0) {
                    if(isFromHome) {
                        mFragments.add(AroundStoreListFragment.newInstance("", AroundStoreEven.SEARCH_FROM_HOME));
                    }else{
                        mFragments.add(AroundStoreListFragment.newInstance("", AroundStoreEven.SEARCH_FROM_NEAR));
                    }
                }else {
                    if(isFromHome) {
                        mFragments.add(AroundStoreListFragment.newInstance(String.valueOf(data.get(i - 1).getShopCategoryId()),AroundStoreEven.SEARCH_FROM_HOME));
                    }else{
                        mFragments.add(AroundStoreListFragment.newInstance(String.valueOf(data.get(i - 1).getShopCategoryId()),AroundStoreEven.SEARCH_FROM_NEAR));
                    }
                }
            }
            itemTitlePagerAdapter=new ItemTitlePagerAdapter(getChildFragmentManager(),
                    mFragments, listtitle.toArray(new String[listtitle.size()]));
            vp.setAdapter(itemTitlePagerAdapter);
            slb_type.setViewPager(vp);
            vp.setCurrentItem(check);
            vp.setOffscreenPageLimit(mFragments.size()-1);
            slb_type.setCurrentTab(check);
            slb_type.setOnTabSelectListener(this);
        }

    }

    /**
     * 获取通讯消息
     *
     * @param goodBusEven
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSearchStoreEvent(AroundStoreEven goodBusEven) {
        if(goodBusEven.getFlag().equals(AroundStoreEven.AREAR_SEARCH)){
            itemTitlePagerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).unregisterMyOnTouchListener(onTouchListener);

    }

    @Override
    public void onStart() {
        super.onStart();
//        registerEventBus(this);

    }

    @Override
    public void onStop() {
        super.onStop();
//        unregisterEventBus(this);

    }

    @Override
    public void showState(int state) {

    }

    @Override
    public void onTabSelect(int position) {
//        if(position==0){
//            shopCategoryId="";
//        }else {
//            shopCategoryId = storeCategoryList.get(position-1).getSortItemId();
//        }
//        search();
    }

    @Override
    public void onTabReselect(int position) {
//        if(position==0){
//            shopCategoryId="";
//        }else {
//            shopCategoryId = storeCategoryList.get(position-1).getSortItemId();
//        }
//        search();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

    }
}
