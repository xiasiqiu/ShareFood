package com.xinyuan.xyorder.adapter.order;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nex3z.flowlayout.FlowLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodEvaBean;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/30
 */
public class MineOrderEvaluationAdapter extends BaseQuickAdapter<GoodEvaBean, BaseViewHolder> {

    private List<GoodEvaBean> datas;

    public MineOrderEvaluationAdapter(int layoutResId, @Nullable List<GoodEvaBean> data) {
        super(layoutResId, data);
        this.datas = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodEvaBean item) {
        helper.setText(R.id.tv_goods_name, item.getGoodsName());
        final TextView tv_food = helper.getView(R.id.tv_food);
        if (XEmptyUtils.isEmpty(item.getAppraiseContent())) {
            tv_food.setVisibility(View.GONE);
        } else {
            tv_food.setText(item.getAppraiseContent());
            tv_food.setVisibility(View.VISIBLE);
        }
        helper.setImageResource(R.id.iv_store_eva_level, (Integer) DataUtil.getEvaStar(mContext, item.getAppraiseLevel()));
        FlowLayout fbl_tag = helper.getView(R.id.fl_tag);
        fbl_tag.removeAllViews();
        if (XEmptyUtils.isEmpty(item.getTagGoodsAppraise())) {
            fbl_tag.setVisibility(View.GONE);
        } else {
            fbl_tag.setVisibility(View.VISIBLE);
            List<String> goodAppraise = Arrays.asList(item.getTagGoodsAppraise().split(","));
            if (goodAppraise != null && goodAppraise.size() > 0) {
                for (int i = 0; i < goodAppraise.size(); i++) {
                    //创建一个Textview
                    final TextView view = new TextView(mContext);
                    view.setText(goodAppraise.get(i));
                    view.setTextSize(13);
                    view.setTextColor(mContext.getResources().getColor(R.color.tv_b1));
                    fbl_tag.addView(view);
                }
            }
        }
    }
}
