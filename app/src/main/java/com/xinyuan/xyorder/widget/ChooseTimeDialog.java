package com.xinyuan.xyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.ChooesTimeAdapter;
import com.xinyuan.xyorder.adapter.buy.ChooesTimeContentAdapter;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeBean;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeContentBean;
import com.xinyuan.xyorder.common.bean.buy.TimchooesBean;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.youth.xframe.utils.XEmptyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by f-x on 2017/10/1810:34
 */

public class ChooseTimeDialog extends Dialog {

    @BindView(R.id.rv_goods_category)
    RecyclerView rv_goods_category;
    @BindView(R.id.rv_goods_detail)
    RecyclerView rv_goods_detail;
    @BindView(R.id.tv_choose_time_title)
    TextView tv_choose_time_title;

    ChooesTimeAdapter choesTimeAdapter;
    ChooesTimeContentAdapter choesTimeContentAdapter;
    public static List<SelectTimeBean> timeBeans;
    public static List<SelectTimeContentBean> contentBeans;
    private int type;

    public ChooseTimeDialog(@NonNull Context context, int type, List<SelectTimeBean> timeBean) {
        super(context, R.style.CommonDialog);
        timeBeans = timeBean;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_choestime);
        EventBus.getDefault().register(this);
        ButterKnife.bind((Dialog) this);
        if (type == 1) {
            tv_choose_time_title.setText("选择送达时间");
        } else if (type == 2) {
            tv_choose_time_title.setText("选择预定时间");
        }

        setDayList();
        setTimeList();
        showCategory(timeBeans);
    }

    /**
     * 设置一级时间列表
     */
    private void setDayList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(1);
        rv_goods_category.setLayoutManager(layoutManager);
    }

    /**
     * 设置二级具体时间
     */
    private void setTimeList() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(1);
        rv_goods_detail.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getContext().getResources().getColor(R.color.colorLine)));
        rv_goods_detail.setLayoutManager(layoutManager1);
    }

    @Subscribe
    public void onEventMainThread(TimchooesBean dissmiss) {
        this.dismiss();
    }


    public void showCategory(final List<SelectTimeBean> choeseTimeBeans) {
        if (!XEmptyUtils.isEmpty(choeseTimeBeans)) {
            contentBeans = new ArrayList<SelectTimeContentBean>();
            for (SelectTimeBean choeseTimeBean : choeseTimeBeans) {
                contentBeans.addAll(choeseTimeBean.getContentTime());
            }

            choesTimeAdapter = new ChooesTimeAdapter(R.layout.item_choes_time, choeseTimeBeans);
            rv_goods_category.setAdapter(choesTimeAdapter);
            choesTimeContentAdapter = new ChooesTimeContentAdapter(R.layout.item_choes_time_content, choeseTimeBeans.get(0).getContentTime());
            rv_goods_detail.setAdapter(choesTimeContentAdapter);
            choesTimeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (choesTimeContentAdapter != null && choesTimeContentAdapter.getData().size() > 0) {
                        choesTimeContentAdapter.setNewData(choeseTimeBeans.get(position).getContentTime());
                    }
                    for (int i = 0; i < choesTimeAdapter.getData().size(); i++) {
                        if (i == position) {
                            choesTimeAdapter.getData().get(i).setCheck(true);
                        } else {
                            choesTimeAdapter.getData().get(i).setCheck(false);
                        }
                    }
                    choesTimeAdapter.notifyDataSetChanged();
                }
            });
        }
    }


}
