package com.xinyuan.xyorder.adapter.storedetail;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.widget.AddWidget;

import java.util.List;

/**
 * Created by f-x on 2017/10/1819:44
 */

public class GoodDetailAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> {
    private AddWidget.OnAddClick onAddClick;

    public GoodDetailAdapter(@Nullable List<GoodBean> data, AddWidget.OnAddClick onAddClick) {
        super(R.layout.item_good_detail, data);
        this.onAddClick = onAddClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodBean item) {
        helper.setText(R.id.textView6, item.getGoodsName())
                .setText(R.id.textView7, "月售" + item.getGoodSales())
                .setText(R.id.textView8, item.getStrPrice(item.getGoodsPrice()));
        ImageView imageView2 = helper.getView(R.id.imageView2);
        GlideImageLoader.setUrlImg(mContext, item.getGoodsImgUrl(), imageView2);
        AddWidget addWidget = helper.getView(R.id.detail_addwidget);
        addWidget.setData(onAddClick, item, false, false, false);
    }
}
