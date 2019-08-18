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
import com.xinyuan.xyorder.adapter.buy.ChooseNumAdapter;
import com.xinyuan.xyorder.common.bean.buy.NumBean;
import com.xinyuan.xyorder.common.even.BuyBusEven;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by f-x on 2017/10/1810:34
 */

public class ChooseNumDialog extends Dialog {
    @BindView(R.id.rv_choose_num)
    RecyclerView rv_choose_num;
    @BindView(R.id.tv_choose_time_title)
    TextView tv_choose_time_title;
    private ChooseNumAdapter chooseNumAdapter;
    private List<NumBean> datas;
    private String title;

    public ChooseNumDialog(@NonNull Context context, String title, List<NumBean> manNum) {
        super(context, R.style.CommonDialog);
        this.title = title;
        this.datas = manNum;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_choesnuum);
        ButterKnife.bind((Dialog) this);
        tv_choose_time_title.setText(title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(1);
        rv_choose_num.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getContext().getResources().getColor(R.color.colorLine)));
        rv_choose_num.setLayoutManager(layoutManager);
        chooseNumAdapter = new ChooseNumAdapter(R.layout.item_text, datas);
        rv_choose_num.setAdapter(chooseNumAdapter);
        chooseNumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (NumBean numBean : datas) {
                    if (numBean.isCheck()) {
                        if (numBean.getNum() != datas.get(position).getNum()) {
                            numBean.setCheck(false);
                            datas.get(position).setCheck(true);
                            EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESENUM, datas.get(position)));
                            dismiss();
                        } else {
                            EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESENUM, datas.get(position)));
                            datas.get(position).setCheck(true);
                            dismiss();
                        }
                    }
                }
            }
        });
    }


}
