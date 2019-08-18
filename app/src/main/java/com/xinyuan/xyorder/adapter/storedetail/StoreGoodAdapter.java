package com.xinyuan.xyorder.adapter.storedetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.CarGoodBean;
import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.buy.SpecFlag;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.bean.store.good.GoodsPropertys;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.ui.stores.GoodDetailActivity;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.widget.AddButton;
import com.xinyuan.xyorder.widget.AddWidget;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

/**
 * Created by f-x on 2017/10/1818:26
 */

public class StoreGoodAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;
    private List<GoodBean> flist;
    private AddWidget.OnAddClick onAddClick;

    public StoreGoodAdapter(@Nullable List<GoodBean> data, AddWidget.OnAddClick onAddClick) {
        super(R.layout.item_store_good, data);
        this.flist = data;
        this.onAddClick = onAddClick;
    }

    public void setOnAddClick(AddWidget.OnAddClick onAddClick) {
        this.onAddClick = onAddClick;
    }

    @Override
    protected void convert(final BaseViewHolder helper, GoodBean item) {
        TextView tv_goods_name = helper.getView(R.id.tv_goods_name);
        TextView tv_goods_sell = helper.getView(R.id.tv_goods_sell);
        TextView tv_goods_eva = helper.getView(R.id.tv_goods_eva);
        TextView tv_goods_content = helper.getView(R.id.tv_goods_content);
        TextView tv_goods_price_str = helper.getView(R.id.tv_goods_price_str);
        item.setPosition(getData().indexOf(item));
        tv_goods_name.setText(item.getGoodsName());
        tv_goods_sell.setText("月售" + item.getGoodSales() + "份");
        boolean isShowSpec=false;
        if(item.getGoodsPropertys()!=null&&item.getGoodsPropertys().size()>0) {
            for (GoodsPropertys item1 : item.getGoodsPropertys()) {
                if (item1.getGoodsPropertyValueList() != null && item1.getGoodsPropertyValueList().size() > 1) {
                    isShowSpec = true;
                    break;
                }
            }
        }
        if(isShowSpec||(item.getGoodsSpecifications()!=null&&item.getGoodsSpecifications().size()>1)){
            tv_goods_price_str.setVisibility(View.VISIBLE);
        }else {
            tv_goods_price_str.setVisibility(View.GONE);
        }
        tv_goods_eva.setText(mContext.getString(R.string.str_good_eva).concat(String.valueOf(DataUtil.getgoodsRateApprise(item.getGoodRateApprise()))));
        if (!XEmptyUtils.isEmpty(item.getGoodsContent()) && !XEmptyUtils.isSpace(item.getGoodsContent())) {
            tv_goods_content.setText(item.getGoodsContent());
        } else {
            tv_goods_content.setText("暂无商品介绍");
        }
        boolean noRduce = false;
        int sameNum = 1;
        int selecCount = 0;
        for (ChooesCarGoodBean chooesCarGoodBean : StoreDetailActivity.carAdapter.getData()) {
            if (item.getGoodsId() == chooesCarGoodBean.getGoodId()) {
                sameNum++;
                selecCount += chooesCarGoodBean.getChooesNum();
            }
        }
        if (sameNum > 2) {
            noRduce = true;
        }
        AddWidget addWidget = helper.getView(R.id.addwidget);
        addWidget.setData(onAddClick, item, false, false, noRduce);
        addWidget.setListener();

        ImageView iv_food = helper.getView(R.id.iv_food);
        GlideImageLoader.setGoodUrlImg(mContext, Constants.IMAGE_HOST + item.getGoodsImgUrl() + Constants.IMG_GOODS, iv_food);
        TextView tv_goods_price = helper.getView(R.id.tv_goods_price);
        TextView tv_goods_coupon_price = helper.getView(R.id.tv_goods_coupon_price);

        if (!XEmptyUtils.isEmpty(item.getActivityGoods())) {
            helper.setVisible(R.id.tv_goods_coupon_price, true);
            helper.setVisible(R.id.tv_goods_activity, true);
            helper.setVisible(R.id.tv_goods_activity_limit, true);
            helper.setVisible(R.id.ll_good_activity, true);
            tv_goods_coupon_price.setText(CommUtil.getPriceString(item.getGoodsPrice()));
            tv_goods_coupon_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if (item.getActivityGoods().getLimitedQuantity() == 0) {
                helper.setText(R.id.tv_goods_activity_limit, "每单优惠不限");
            } else {
                helper.setText(R.id.tv_goods_activity_limit, mContext.getString(R.string.str_good_activity_limit_str).concat(String.valueOf(item.getActivityGoods().getLimitedQuantity())).concat(mContext.getString(R.string.str_good_activity_limit_end)));
            }

            if (!XEmptyUtils.isEmpty(item.getActivityGoods().getDiscount())) {
                helper.setText(R.id.tv_goods_activity, CommUtil.getPriceString(item.getActivityGoods().getDiscount()).concat(mContext.getString(R.string.str_zhe)));
                tv_goods_price.setText(CommUtil.getPriceString(item.getActivityGoods().getGoodsPrice().multiply(item.getActivityGoods().getDiscount()).divide(new BigDecimal(10))));


            } else if (!XEmptyUtils.isEmpty(item.getActivityGoods().getSpecialsPrice())) {
                helper.setText(R.id.tv_goods_activity, R.string.str_good_activity_spec);
                tv_goods_price.setText(CommUtil.getPriceString(item.getActivityGoods().getSpecialsPrice()));
            }
        } else {
            helper.setVisible(R.id.tv_goods_coupon_price, false);
            helper.setVisible(R.id.tv_goods_activity, false);
            helper.setVisible(R.id.tv_goods_activity_limit, false);
            helper.setVisible(R.id.ll_good_activity, false);
            tv_goods_price.setText(CommUtil.getPriceString(item.getGoodsPrice()));
        }

        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.stick_header, true)
                    .setText(R.id.tv_header, item.getGoodsClassNames())
                    .setTag(R.id.food_main, FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(item.getGoodsClassNames(), flist.get(helper.getAdapterPosition() - 1).getGoodsClassNames())) {
                helper.setVisible(R.id.stick_header, true)
                        .setText(R.id.tv_header, item.getGoodsClassNames())
                        .setTag(R.id.food_main, HAS_STICKY_VIEW);
            } else {
                helper.setVisible(R.id.stick_header, false)
                        .setTag(R.id.food_main, NONE_STICKY_VIEW);
            }
        }
        helper.addOnClickListener(R.id.ll_good);
        helper.getConvertView().setContentDescription(item.getGoodsClassNames());
    }

}
