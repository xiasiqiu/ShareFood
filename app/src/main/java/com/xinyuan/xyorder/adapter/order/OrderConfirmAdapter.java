package com.xinyuan.xyorder.adapter.order;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.common.bean.order.OrderGoods;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/28
 */
public class OrderConfirmAdapter extends BaseQuickAdapter<OrderGoods,BaseViewHolder>{

    public OrderConfirmAdapter(int layoutResId, @Nullable List<OrderGoods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderGoods item) {

    }
}
