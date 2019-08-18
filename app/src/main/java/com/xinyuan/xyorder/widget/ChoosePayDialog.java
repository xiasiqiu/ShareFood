package com.xinyuan.xyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.PayChooesAdapter;
import com.xinyuan.xyorder.common.bean.order.PaymentBean;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.youth.xframe.utils.XEmptyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by f-x on 2017/10/1810:34
 */

public class ChoosePayDialog extends Dialog {
    @BindView(R.id.rv_choose_num)
    RecyclerView rv_choose_num;
    @BindView(R.id.tv_choose_time_title)
    TextView tv_choose_time_title;
    private PayChooesAdapter chooesPayAdapter;
    private List<PaymentBean> datas;

    public ChoosePayDialog(@NonNull Context context, List<PaymentBean> paymentBeans) {
        super(context, R.style.CommonDialog);
        this.datas = paymentBeans;
    }

    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_choesnuum);
        ButterKnife.bind((Dialog) this);
        tv_choose_time_title.setText("选择支付方式");
        if (!XEmptyUtils.isEmpty(datas)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(1);
            rv_choose_num.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getContext().getResources().getColor(R.color.colorLine)));
            rv_choose_num.setLayoutManager(layoutManager);
            chooesPayAdapter = new PayChooesAdapter(R.layout.item_pay, datas) {
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
                            item.setCheck(true);
                            for (PaymentBean paymentBean : getData()) {
                                if (paymentBean.isCheck()) {
                                    if (!paymentBean.getCode().equals(item.getCode())) {
                                        paymentBean.setCheck(false);
                                        EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESEPAY, item));
                                        dismiss();
                                    } else {
                                        EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESEPAY, item));

                                        dismiss();
                                    }
                                }
                            }
                            notifyDataSetChanged();
                        }
                    });

                }
            };
            rv_choose_num.setAdapter(chooesPayAdapter);
        }

    }


}
