package com.xinyuan.xyorder.adapter.buy;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeBean;

import java.util.List;

/**
 * Created by f-x on 2017/10/1810:42
 */

public class ChooesTimeAdapter extends BaseQuickAdapter<SelectTimeBean, BaseViewHolder> {
    public ChooesTimeAdapter(int layoutResId, @Nullable List<SelectTimeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SelectTimeBean item) {
        TextView tv_choose_time = helper.getView(R.id.tv_choose_time);
        helper.setText(R.id.tv_choose_time, item.getDay_name());
        if (item.isCheck()) {
            tv_choose_time.setBackgroundColor(mContext.getResources().getColor(R.color.bg_white));
        } else {
            tv_choose_time.setBackgroundColor(mContext.getResources().getColor(R.color.white_f2));
        }


    }
}
