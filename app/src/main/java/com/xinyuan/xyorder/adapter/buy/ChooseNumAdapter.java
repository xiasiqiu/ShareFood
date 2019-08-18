package com.xinyuan.xyorder.adapter.buy;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.NumBean;

import java.util.List;

/**
 * Created by f-x on 2017/10/1814:35
 */

public class ChooseNumAdapter extends BaseQuickAdapter<NumBean, BaseViewHolder> {
    public ChooseNumAdapter(int layoutResId, @Nullable List<NumBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NumBean item) {
        TextView tv_num_name = helper.getView(R.id.tv_num_name);
        tv_num_name.setText(item.getNumName());

        if (item.isCheck()) {
            helper.setVisible(R.id.iv_check, true);
        } else {
            helper.setVisible(R.id.iv_check, false);
        }
    }
}
