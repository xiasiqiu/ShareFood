package com.xinyuan.xyorder.adapter.buy;

import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.OrderCouponBean;
import com.xinyuan.xyorder.util.CommUtil;

import java.util.List;

/**
 * Created by f-x on 2017/10/2018:26
 */

public class RedPacketAdapter extends BaseQuickAdapter<OrderCouponBean, BaseViewHolder> {
    public RedPacketAdapter(int layoutResId, @Nullable List<OrderCouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderCouponBean item) {
        helper.setText(R.id.tv_red_name, item.getCoupon().getCouponName());
        helper.setText(R.id.tv_red_price, CommUtil.getPriceString(item.getCoupon().getMoney()));
        helper.setText(R.id.tv_red_need, "满" + item.getCoupon().getMinimum() + "元可用");
        helper.setText(R.id.tv_red_time, item.getCoupon().getEndTime() + "到期");
        CheckBox checkBox = helper.getView(R.id.check_box);
        checkBox.setChecked(item.getCoupon().isCheck());

    }
}
