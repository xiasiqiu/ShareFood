package com.xinyuan.xyorder.ui.stores;

import android.view.View;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.storedetail.GoodTypeAdapter;
import com.xinyuan.xyorder.adapter.storedetail.StoreGoodAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.TypeBean;
import com.xinyuan.xyorder.widget.GoodListContainer;

import java.util.List;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/1818:04
 * 商品列表Fragment
 */

public class StoreGoodsFragment extends BaseFragment {
    @BindView(R.id.listcontainer)
    GoodListContainer listContainer;
    public static List<TypeBean> goodCategoryList;
    public static List<GoodBean> goodList;

    public static StoreGoodsFragment newInstance(List<TypeBean> typeBeans, List<GoodBean> goodBeanList) {
        StoreGoodsFragment fragment = new StoreGoodsFragment();
        goodCategoryList = typeBeans;
        goodList = goodBeanList;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        listContainer.setAddClick((StoreDetailActivity) getActivity());
    }

    public StoreGoodAdapter getFoodAdapter() {
        return listContainer.storeGoodAdapter;
    }

    public GoodTypeAdapter getTypeAdapter() {
        return listContainer.typeAdapter;
    }


    @Override
    public void initData() {

    }

    public static List<GoodBean> getGoodDatas() {
        return goodList;

    }

    public static List<TypeBean> getTypeDatas() {
        return goodCategoryList;
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_store_good;
    }


}
