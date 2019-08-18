package com.xinyuan.xyorder.adapter.storedetail;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.ShopActiveBean;
import com.xinyuan.xyorder.common.bean.store.StoreActivityShowTextBean;
import com.xinyuan.xyorder.util.DataUtil;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/27
 */
public class StoreActAdapter extends BaseQuickAdapter<ShopActiveBean, BaseViewHolder> {
    public StoreActAdapter(int layoutResId, @Nullable List<ShopActiveBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopActiveBean item) {
        StoreActivityShowTextBean activityShowTextBean = (DataUtil.getCouponText(mContext, item.getActivityType()));
        ImageView tv_acy_type = helper.getView(R.id.tv_acy_type);
        tv_acy_type.setImageDrawable(mContext.getResources().getDrawable(activityShowTextBean.getAcitvityColor()));
        helper.setText(R.id.tv_acy_name, item.getActivityName());


    }
}
