package com.xinyuan.xyorder.adapter.buy;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.order.OrderContactBean;
import com.xinyuan.xyorder.common.even.AddressPageEvent;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by f-x on 2017/10/2716:11
 */

public class ContactAdapter extends BaseQuickAdapter<OrderContactBean, BaseViewHolder> {
    private int type; //1-可送 2-不可送

    public ContactAdapter(int layoutResId, int type, @Nullable List<OrderContactBean> data) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderContactBean item) {

        helper.setText(R.id.tv_user_name, item.getContactName())
                .setText(R.id.tv_sex, DataUtil.getGender(item.getGender()))
                .setText(R.id.tv_tel, item.getContactPhone());
        if (XEmptyUtils.isEmpty(item.getHouseNumber())) {
            helper.setText(R.id.tv_address, item.getAddress());
        } else {
            helper.setText(R.id.tv_address, item.getAddress() + " " + item.getHouseNumber());
        }
        if (type == 1) {
            helper.setOnClickListener(R.id.iv_address_edit, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AddressPageEvent(AddressPageEvent.EDITE, helper.getLayoutPosition()));
                }
            });
            helper.setOnClickListener(R.id.rl_choose_contact, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AddressPageEvent(AddressPageEvent.CHOOES, helper.getLayoutPosition()));
                }
            });
        } else {
            helper.setOnClickListener(R.id.iv_address_edit, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AddressPageEvent(AddressPageEvent.NOEDITE, helper.getLayoutPosition()));
                }
            });
        }

    }

}
