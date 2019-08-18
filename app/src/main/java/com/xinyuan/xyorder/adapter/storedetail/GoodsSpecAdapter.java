package com.xinyuan.xyorder.adapter.storedetail;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nex3z.flowlayout.FlowLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.AddViewHolder;
import com.xinyuan.xyorder.common.bean.store.good.GoodsPropertyBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodsPropertys;
import com.xinyuan.xyorder.widget.GoodsSpecDialog;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.List;

/**
 * Created by HDQ on 2017/12/19.
 */

public class GoodsSpecAdapter extends BaseQuickAdapter<GoodsPropertys, BaseViewHolder> {

    boolean hasHeadview;
    public GoodsSpecAdapter(int layoutResId, @Nullable List<GoodsPropertys> data,boolean hasHeadview) {
        super(layoutResId, data);
        this.hasHeadview=hasHeadview;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsPropertys item) {
        helper.setText(R.id.tv_property, item.getGoodsPropertyName() + "：");
        FlowLayout fbl_tag = helper.getView(R.id.fl_spec_tag);
        fbl_tag.removeAllViews();
        if (item.getGoodsPropertyValueList() != null && item.getGoodsPropertyValueList().size() > 0) {
            for (int i = 0; i < item.getGoodsPropertyValueList().size(); i++) {
                AddViewHolder addViewHolder = new AddViewHolder(mContext, R.layout.view_goods_spec_tv);
                final TextView tv_tag = addViewHolder.getView(R.id.tv_tag);
                if (XEmptyUtils.isEmpty(item.getGoodsPropertyValueList().get(i).getValue())) {
                    tv_tag.setText("X");
                } else {
                    tv_tag.setText(item.getGoodsPropertyValueList().get(i).getValue());
                }
                if (item.getGoodsPropertyValueList().get(i).isChecked()) {
                    tv_tag.setActivated(true);
                    tv_tag.setTextColor(mContext.getResources().getColorStateList(R.color.green));
                } else {
                    tv_tag.setActivated(false);
                    tv_tag.setTextColor(mContext.getResources().getColorStateList(R.color.black_33));
                }
                final int finalI = i;
                tv_tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tv_tag.isActivated()) {
                            tv_tag.setActivated(true);
                            initCheck(item);
                            if(hasHeadview) {
                                GoodsSpecDialog.chooesProerty.set(helper.getLayoutPosition() - 1, item.getGoodsPropertyValueList().get(finalI).getValue());
                            }else{
                                GoodsSpecDialog.chooesProerty.set(helper.getLayoutPosition(), item.getGoodsPropertyValueList().get(finalI).getValue());
                            }
                            item.getGoodsPropertyValueList().get(finalI).setChecked(true);
                            notifyDataSetChanged();
                        }
                    }

                });
                fbl_tag.addView(addViewHolder.getCustomView());
            }
        }
    }

    public void initCheck(GoodsPropertys item) {
        for (GoodsPropertyBean goodsSpec : item.getGoodsPropertyValueList()) {
            goodsSpec.setChecked(false);
        }
    }
}
