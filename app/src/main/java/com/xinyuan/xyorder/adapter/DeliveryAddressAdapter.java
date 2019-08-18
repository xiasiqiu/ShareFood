package com.xinyuan.xyorder.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/12/14
 */
public class DeliveryAddressAdapter extends BaseQuickAdapter<AddressInfo,BaseViewHolder> {

    public DeliveryAddressAdapter(int layoutResId, @Nullable List<AddressInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressInfo item) {
        helper.setText(R.id.tv_address,item.getAddress() + " " + item.getHouseNumber());
        helper.setText(R.id.tv_tel,item.getContactPhone());
        helper.setText(R.id.tv_user_name,item.getContactName());
    }
}
