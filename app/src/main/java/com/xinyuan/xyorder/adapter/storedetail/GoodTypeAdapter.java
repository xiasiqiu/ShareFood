package com.xinyuan.xyorder.adapter.storedetail;


import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.good.TypeBean;

import java.util.HashMap;
import java.util.List;

public class GoodTypeAdapter extends BaseQuickAdapter<TypeBean, BaseViewHolder> {
    private int checked;
    private List<TypeBean> data;
    public boolean fromClick;
    private String typeStr;
    private HashMap<String, Integer> badges = new HashMap<>();

    public GoodTypeAdapter(@Nullable List<TypeBean> data) {
        super(R.layout.item_type, data);
        this.data = data;
    }

    public void updateBadge(HashMap<String, Integer> badges) {
        this.badges = badges;
        notifyDataSetChanged();
    }


    @Override
    protected void convert(BaseViewHolder helper, TypeBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .setTag(R.id.item_main, item.getName());
        if (helper.getAdapterPosition() == checked) {
            helper.setBackgroundColor(R.id.item_main, Color.WHITE)
                    .setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
                    .setTypeface(R.id.tv_name, Typeface.DEFAULT_BOLD)
            ;
        } else {
            helper.setBackgroundColor(R.id.item_main, ContextCompat.getColor(mContext, R.color.type_gray))
                    .setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.gray_4d))
                    .setTypeface(R.id.tv_name, Typeface.DEFAULT)
            ;
        }
        // TODO: 2017/10/22 此段代码暂时注释隐藏，功能为显示该分类已选择数量
//        if (badges.containsKey(item.getName()) && badges.get(item.getName()) > 0) {
//            helper.setVisible(R.id.item_badge, true).setText(R.id.item_badge, String.valueOf(badges.get(item.getName())));
//        } else {
//            helper.setVisible(R.id.item_badge, false);
//        }

    }

    public void setChecked(int checked) {
        this.checked = checked;
        typeStr = data.get(checked).getName();
        notifyDataSetChanged();
    }

    public void setType(String type) {
        if (fromClick) {
            fromClick = !type.equals(typeStr);
            return;
        }
        if (type.equals(typeStr)) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(type) && i != checked) {
                setChecked(i);
                moveToPosition(i);
                break;
            }
        }
    }

    private void moveToPosition(int i) {
        LinearLayoutManager linlm = (LinearLayoutManager) getRecyclerView().getLayoutManager();
        int firstItem = linlm.findFirstVisibleItemPosition();
        int lastItem = linlm.findLastVisibleItemPosition();
        if (getItemCount() > 5) {//提前把9滑出来
            lastItem -= 3;
        }
        if (i <= firstItem) {
            getRecyclerView().scrollToPosition(i);
        } else if (i <= lastItem) {
            //当要置顶的项已经在屏幕上显示时不处理
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            getRecyclerView().scrollToPosition(i);
            int n = i - linlm.findFirstVisibleItemPosition();
            if (0 <= n && n < getRecyclerView().getChildCount()) {
                int top = getRecyclerView().getChildAt(n).getTop();
                getRecyclerView().smoothScrollBy(0, top);
            }
        }


    }

}
