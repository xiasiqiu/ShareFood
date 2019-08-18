package com.xinyuan.xyorder.adapter.buy;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.order.OrderGoods;
import com.xinyuan.xyorder.util.CommUtil;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.List;

/**
 * Created by f-x on 2017/10/1719:29
 */

public class ConfirmOrderGoodAdapter extends BaseQuickAdapter<OrderGoods, BaseViewHolder> {
    public ConfirmOrderGoodAdapter(int layoutResId, @Nullable List<OrderGoods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderGoods item) {
        helper.setText(R.id.tv_goods_name, item.getGoodsName());
        helper.setText(R.id.tv_num, "X" + item.getGoodsCount());
        if (XEmptyUtils.isEmpty(item.getGoodsContent())) {
            helper.setVisible(R.id.tv_goods_hint, false);
        } else {
            helper.setText(R.id.tv_goods_hint, item.getGoodsContent());
        }
        if (item.getOldAmount().subtract(item.getAmount()).intValue() == 0) {
            TextView tv_coupon_price = helper.getView(R.id.tv_coupon_price);
            tv_coupon_price.setTextColor(mContext.getResources().getColor(R.color.tv_name));
            tv_coupon_price.setText(mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(item.getOldAmount()));
            helper.setVisible(R.id.tv_good_price, false);
        } else {
            helper.setText(R.id.tv_good_price, mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(item.getOldAmount()));
            TextView tv_good_price = helper.getView(R.id.tv_good_price);
            tv_good_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            helper.setText(R.id.tv_coupon_price, mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(item.getAmount()));
        }
    }
}
