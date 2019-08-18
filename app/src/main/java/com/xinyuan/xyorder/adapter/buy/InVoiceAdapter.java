package com.xinyuan.xyorder.adapter.buy;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.InVoiceBean;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.common.even.InvoiceEven;
import com.xinyuan.xyorder.ui.buy.ChooesInvoiceFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by f-x on 2017/11/2215:38
 */

public class InVoiceAdapter extends BaseItemDraggableAdapter<InVoiceBean, BaseViewHolder> {


    public InVoiceAdapter(int layoutResId, @Nullable List<InVoiceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final InVoiceBean item) {
        if (item.getInvoiceType().equals(Constants.INDIVIDUAL)) {
            helper.setVisible(R.id.tv_invoice_person, true);
            helper.setVisible(R.id.tv_invoice_con, false);
        } else {
            helper.setVisible(R.id.tv_invoice_con, true);
            helper.setVisible(R.id.tv_invoice_person, false);
            helper.setText(R.id.tv_invoice_num, item.getEin());

        }
        helper.setText(R.id.tv_invoice_name, item.getTitle());
        helper.setOnClickListener(R.id.iv_address_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new InvoiceEven(InvoiceEven.EDITE, item));
            }
        });

        helper.setOnClickListener(R.id.rl_invoice_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isCheck()) {
                    item.setCheck(false);
                    ChooesInvoiceFragment.notUserRed = true;
                    ChooesInvoiceFragment.tv_invoice_no.setActivated(true);


                    EventBus.getDefault().post(new InvoiceEven(InvoiceEven.FINISH));
                    EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESEINVOICE, item));

                } else {
                    boolean haveCkeck = false;
                    for (InVoiceBean inVoiceBean : getData()) {
                        if (inVoiceBean.isCheck()) {
                            if (inVoiceBean.getInvoiceInfoId() != item.getInvoiceInfoId()) {
                                inVoiceBean.setCheck(false);
                                item.setCheck(true);
                                haveCkeck = true;
                                ChooesInvoiceFragment.tv_invoice_no.setActivated(false);
                                EventBus.getDefault().post(new InvoiceEven(InvoiceEven.FINISH));
                                EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESEINVOICE, item));

                            }
                        }
                    }
                    if (!haveCkeck) {
                        item.setCheck(true);
                        ChooesInvoiceFragment.tv_invoice_no.setActivated(false);
                        EventBus.getDefault().post(new InvoiceEven(InvoiceEven.FINISH));
                        EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESEINVOICE, item));
                    }

                }
                notifyDataSetChanged();
            }
        });
    }

}