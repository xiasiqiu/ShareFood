package com.xinyuan.xyorder.adapter.buy;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeContentBean;
import com.xinyuan.xyorder.common.bean.buy.TimchooesBean;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.widget.ChooseTimeDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by f-x on 2017/10/1810:43
 */

public class ChooesTimeContentAdapter extends BaseQuickAdapter<SelectTimeContentBean, BaseViewHolder> {
    public ChooesTimeContentAdapter(int layoutResId, @Nullable List<SelectTimeContentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SelectTimeContentBean item) {
        helper.setText(R.id.tv_choose_time, item.getTime());
        if (item.equals("-1")) {
            helper.setText(R.id.tv_choose_time, item.getTime());
        } else {
            if (item.getPrice().contains("-1")) {
                helper.setText(R.id.tv_choose_time, item.getTime());
            } else {
                helper.setText(R.id.tv_choose_time, item.getTime() + "(" + item.getPrice() + "元配送费)");

            }

        }
        if (item.isCheck()) {
            helper.setVisible(R.id.iv_check, true);
        } else {
            helper.setVisible(R.id.iv_check, false);
        }

        LinearLayout rl_choose_time = helper.getView(R.id.rl_choose_time);
        rl_choose_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SelectTimeContentBean bean : ChooseTimeDialog.contentBeans) {
                    if (bean.isCheck()) {
                        if (bean.getCurrentDate() != item.getCurrentDate()) {
                            bean.setCheck(false);
                            item.setCheck(true);
                            EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESETIME, item));
                            EventBus.getDefault().post(new TimchooesBean(true));
                        } else {
                            EventBus.getDefault().post(new TimchooesBean(true));

                        }
                    }
                }
            }
        });

    }

}
