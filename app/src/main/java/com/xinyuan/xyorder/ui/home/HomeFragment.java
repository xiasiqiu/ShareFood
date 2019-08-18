package com.xinyuan.xyorder.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.MenuGridAdapter;
import com.xinyuan.xyorder.adapter.MyViewPagerAdapter;
import com.xinyuan.xyorder.adapter.ShopStoreAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.SearchSortItemBean;
import com.xinyuan.xyorder.common.bean.home.BannerBean;
import com.xinyuan.xyorder.common.bean.home.ShopCategory;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.AroundStoreEven;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.even.SearchStoreEven;
import com.xinyuan.xyorder.mvp.contract.HomeView;
import com.xinyuan.xyorder.mvp.presenter.HomePresenter;
import com.xinyuan.xyorder.ui.login.LoginActivity;
import com.xinyuan.xyorder.ui.mine.NoticeFragment;
import com.xinyuan.xyorder.ui.mine.WebviewActivity;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeImageView;

/**
 * <p>
 * Description：首页
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.home_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.home_swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.home_loadingView)
    XLoadingView xLoadingView;
    @BindView(R.id.btn_search_food)
    Button btn_search_food;
    @BindView(R.id.home_toolbar)
    Toolbar home_toolbar;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.act_home_btn_msg)
    BGABadgeImageView act_home_btn_msg;

    private View headView;              //头部View布局
    private ViewPager vp_menu;          //菜单列表
    private LinearLayout group;         //圆点指示
    private List<View> viewPagerList;   //GridView作为一个View对象添加到ViewPager集合中
    private ImageView[] ivPoints;       //圆点图片集合
    private ShopStoreAdapter shopStoreAdapter;
    private ArrayList<String> bannerImages = new ArrayList<>();
    private int totalPage;//总页数
    private int mPageSize = 8;//每页显示的最大的数量
    private Banner banner;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_home;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView(View rootView) {
        registerEventBus(this);
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), home_toolbar);
        /**设置刷新**/

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(29, 160, 57));

        /**添加头部View布局**/
        headView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_top, (ViewGroup) mRecyclerView.getParent(), false);
        banner = headView.findViewById(R.id.banner);
        vp_menu = headView.findViewById(R.id.vp_menu);
        group = headView.findViewById(R.id.ll_points);
    }

    @Override
    public void initData() {
        /**根据定位获取首页数据*/
        if (DataUtil.checkLocation(getActivity())) {
            mPresenter.getHomeDataWithDistance(new Gson().toJson(DataUtil.getLocation(getActivity()))); //获取首页数据带经纬度
        } else {
            mPresenter.getHomeData(); //获取首页数据
        }
        if (DataUtil.checkToken(getActivity())) {
            mPresenter.refreshToken();
        }

    }

    @Override
    public void initListener() {
        xLoadingView.setOnRetryClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showState(0);
                        if (DataUtil.checkLocation(MyApplication.mApplicationContext)) {
                            mPresenter.getHomeDataWithDistance(new Gson().toJson(DataUtil.getLocation(MyApplication.mApplicationContext))); //获取首页数据带经纬度

                        } else {
                            mPresenter.getHomeData(); //获取首页数据

                        }
                        if (DataUtil.checkToken(MyApplication.mApplicationContext)) {
                            mPresenter.refreshToken();
                        }
                    }
                });
    }

//    private void patch() {
//        new PatchExecutor(getActivity().getApplicationContext(), new PatchManipulateImp(), new RobustCallBackSample()).start();
//    }

    @Override
    public void onEnterAnimationEnd(Bundle saveInstanceState) {
        if (DataUtil.checkLocation(MyApplication.mApplicationContext)) {
            tv_city.setText(DataUtil.getLocation(MyApplication.mApplicationContext).getCurrentyCity());
        } else {
            tv_city.setText(getActivity().getText(R.string.str_location_ing));
        }

    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getActivity(), this);
    }


    /**
     * 显示banner内容
     *
     * @param itemList
     */
    @Override
    public void showBanner(final List<BannerBean> itemList) {
        if (XEmptyUtils.isEmpty(bannerImages)) {
            for (BannerBean itemData : itemList) {
                bannerImages.add(Constants.IMAGE_HOST + itemData.getImageUrl());
            }
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new GlideImageLoader())
                    .setImages(bannerImages)
                    .setBannerAnimation(Transformer.Default)
                    .isAutoPlay(true)
                    .setDelayTime(3000)
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (itemList != null) {
                        BannerBean itemData = itemList.get(position);
                        OnImageViewClick(banner, itemData);
                    }
                }
            });
        } else {
            bannerImages.clear();
            for (BannerBean itemData : itemList) {
                bannerImages.add(Constants.IMAGE_HOST + itemData.getImageUrl());
            }
            banner.setImages(bannerImages);
        }


    }

    /***
     * 显示菜单
     * @param itemList
     */
    @Override
    public void showMenu(final List<ShopCategory> itemList) {
        if (XEmptyUtils.isEmpty(viewPagerList)) {
            //总的页数向上取整
            totalPage = (int) Math.ceil(itemList.size() * 1.0 / mPageSize);
            viewPagerList = new ArrayList<View>();
            for (int i = 0; i < totalPage; i++) {
                //每个页面都是inflate出一个新实例
                final GridView gridView = (GridView) View.inflate(getActivity(), R.layout.item_gridview, null);
                gridView.setAdapter(new MenuGridAdapter(getActivity(), itemList, i, mPageSize));
                //添加item点击监听
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Object obj = gridView.getItemAtPosition(position);
                        if (obj != null && obj instanceof ShopCategory) {
                            SearchSortItemBean sortItemBean = new SearchSortItemBean(((ShopCategory) obj).getShopCategoryName(), true, String.valueOf(((ShopCategory) obj).getShopCategoryId()));
                            EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, AroundStoreFragment.newInstance(true, sortItemBean)));
                        }
                    }
                });
                //每一个GridView作为一个View对象添加到ViewPager集合中
                viewPagerList.add(gridView);
            }
            //设置ViewPager适配器
            vp_menu.setAdapter(new MyViewPagerAdapter(viewPagerList));

            //添加小圆点
            ivPoints = new ImageView[totalPage];
            for (int i = 0; i < totalPage; i++) {
                //循坏加入点点图片组
                ivPoints[i] = new ImageView(getActivity());
                if (i == 0) {
                    ivPoints[i].setImageResource(R.drawable.page_focuese);
                } else {
                    ivPoints[i].setImageResource(R.drawable.page_unfocused);
                }
                ivPoints[i].setPadding(8, 8, 8, 8);
                group.addView(ivPoints[i]);
            }
            //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
            vp_menu.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < totalPage; i++) {
                        if (i == position) {
                            ivPoints[i].setImageResource(R.drawable.page_focuese);
                        } else {
                            ivPoints[i].setImageResource(R.drawable.page_unfocused);
                        }
                    }
                }
            });
        }


    }

    /**
     * 显示店铺列表
     *
     * @param list
     */
    @Override
    public void showShopList(final List<StoreDetail> list) {
        if (XEmptyUtils.isEmpty(shopStoreAdapter)) {
            GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
            mRecyclerView.setLayoutManager(manager);
            this.mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 1, getActivity().getResources().getColor(R.color.gray_cc)));
            shopStoreAdapter = new ShopStoreAdapter(R.layout.item_shop_store, list);
            mRecyclerView.setAdapter(shopStoreAdapter);
            shopStoreAdapter.openLoadAnimation();
            shopStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Map<String, Long> params = new HashMap();
                    params.put(Constants.STORE_ID, shopStoreAdapter.getData().get(position).getShopId());
                    CommUtil.goActivity(getActivity(), StoreDetailActivity.class, false, params);
                }
            });
            shopStoreAdapter.addHeaderView(headView);
            xLoadingView.showContent();

        } else {
            shopStoreAdapter.setNewData(list);
            xLoadingView.showContent();
        }


    }

    /**
     * 显示是否有消息小红点
     *
     * @param data
     */
    @Override
    public void showNoticeMsg(Boolean data) {
        if (data) {
            act_home_btn_msg.showCirclePointBadge();
        } else {
            act_home_btn_msg.hiddenBadge();
        }
    }

    /**
     * 当页面显示时检查是否有通知
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
            mPresenter.checkMsg();
        } else {
            act_home_btn_msg.hiddenBadge();
        }
    }


    @OnClick({R.id.btn_search_food, R.id.tv_city, R.id.act_home_btn_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_food:
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, AroundStoreFragment.newInstance(true, null)));
                break;
            case R.id.tv_city:
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, ChooseDeliveryAddressFragment.newInstance()));
                break;
            case R.id.act_home_btn_msg:
//                patch();
                if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
                    CommUtil.gotoActivity(getActivity(), LoginActivity.class, false, null);
                } else {
                    EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, NoticeFragment.newInstance()));

                }

                break;
        }

    }


    @Subscribe
    public void onEvent(String cityName) {
        tv_city.setText(cityName);  //收到位置信息更新首页左上城市
        showState(0);
        onRefresh();
//        EventBus.getDefault().post(new SearchStoreEven(SearchStoreEven.HAVECITY));
//        EventBus.getDefault().post(new AroundStoreEven(AroundStoreEven.HAVECITY));

    }

//    @Subscribe
//    public void onEvent(AddressInfo addressInfo) {
//        tv_city.setText(addressInfo.getSimpleAddress());
//    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**根据定位获取首页数据*/
                if (DataUtil.checkLocation(MyApplication.mApplicationContext)) {
                    mPresenter.getHomeDataWithDistance(new Gson().toJson(DataUtil.getLocation(MyApplication.mApplicationContext))); //获取首页数据带经纬度

                } else {
                    mPresenter.getHomeData(); //获取首页数据

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //计算Recyclerview第一个item的位置是否可见
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();

                //当第一个item不可见时，设置SwipeRefreshLayout不可用
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    /**
     * 监听点击
     *
     * @param view
     * @param itemData
     */
    public void OnImageViewClick(View view, BannerBean itemData) {
        if (Constants.SHOP_INFO.equals(itemData.getBannerType())) {
            Map<String, Long> params = new HashMap();
            params.put(Constants.STORE_ID, itemData.getShopId());
            CommUtil.goActivity(getActivity(), StoreDetailActivity.class, false, params);
        } else if (Constants.REDIRECT.equals(itemData.getBannerType())) {
            if (!XEmptyUtils.isEmpty(itemData.getUrl())) {
                startActivity(new Intent(getActivity(), WebviewActivity.class)
                        .putExtra("flag", "banner").putExtra("url", itemData.getUrl()));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus(this);
    }

    @Override
    public void showState(int state) {
        switch (state) {
            case 0:
                xLoadingView.showLoading();
                break;
            case 1:
                xLoadingView.showContent();
                break;
            case 2:
                xLoadingView.showError();
                break;
            case 3:
                xLoadingView.showEmpty();
            case 4:
                xLoadingView.showNoNetwork();
                break;

        }

    }
}
