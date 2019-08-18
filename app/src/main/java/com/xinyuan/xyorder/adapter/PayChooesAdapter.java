package com.xinyuan.xyorder.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.order.PaymentBean;
import com.xinyuan.xyorder.common.constant.Constants;

import java.util.List;

/**
 * Created by f-x on 2017/11/216:55
 */

public class PayChooesAdapter extends BaseQuickAdapter<PaymentBean, BaseViewHolder> {
    public PayChooesAdapter(int layoutResId, @Nullable List<PaymentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PaymentBean item) {
        Button button = helper.getView(R.id.bt_choese_pay);
        button.setText(item.getDisplayName());
        Drawable select = mContext.getResources().getDrawable(R.drawable.ic_checkd_true);
        Drawable unSelect = mContext.getResources().getDrawable(R.drawable.ic_checkd_false);
        select.setBounds(0, 0, select.getMinimumWidth(), select.getMinimumHeight());
        unSelect.setBounds(0, 0, unSelect.getMinimumWidth(), unSelect.getMinimumHeight());
        if (item.isCheck()) {
            button.setCompoundDrawables(null, null, select, null);
        } else {
            button.setCompoundDrawables(null, null, unSelect, null);

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PaymentBean paymentBean : getData()) {
                    if (paymentBean.isCheck()) {
                        if (!paymentBean.getCode().equals(item.getCode())) {
                            paymentBean.setCheck(false);
                            item.setCheck(true);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });


    }
}
