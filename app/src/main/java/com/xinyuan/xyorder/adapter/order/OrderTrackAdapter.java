package com.xinyuan.xyorder.adapter.order;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.order.OrderTrackingBean;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/19
 */
public class OrderTrackAdapter extends BaseQuickAdapter<OrderTrackingBean, BaseViewHolder> {
    List<OrderTrackingBean> data;

    public OrderTrackAdapter(int layoutResId, @Nullable List<OrderTrackingBean> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderTrackingBean item) {
        if (!XEmptyUtils.isEmpty(item.getSchedulesTime())) {
            helper.setText(R.id.tv_tracking_time, XDateUtils.millis2String(Long.parseLong(item.getSchedulesTime()), "yyyy-MM-dd HH:mm"));
        }
        helper.setText(R.id.tv_tracking,item.getSchedulesContent());
        View iv_bottom_line = helper.getView(R.id.iv_bottom_line);
        View iv_top_line = helper.getView(R.id.iv_top_line);
        ImageView iv_dot=helper.getView(R.id.iv_dot);
        TextView tv_tracking = helper.getView(R.id.tv_tracking);
        TextView tv_describle=helper.getView(R.id.tv_describle);

        if (data.size() == 1) {
            iv_top_line.setVisibility(View.GONE);
            iv_bottom_line.setVisibility(View.GONE);
            tv_tracking.setTextColor(mContext.getResources().getColor(R.color.green));
            iv_dot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_green_circle));
            tv_describle.setVisibility(View.VISIBLE);
            tv_describle.setText(item.getSchedulesContent());
        } else {
            if (helper.getLayoutPosition() == 0) {
                iv_top_line.setVisibility(View.GONE);
                iv_bottom_line.setVisibility(View.VISIBLE);
                iv_dot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_green_circle));
                tv_tracking.setTextColor(mContext.getResources().getColor(R.color.green));
                tv_describle.setVisibility(View.VISIBLE);
                tv_describle.setText(item.getSchedulesContent());
            } else if (helper.getLayoutPosition() == data.size()-1) {
                iv_bottom_line.setVisibility(View.GONE);
                iv_top_line.setVisibility(View.VISIBLE);
                iv_dot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_gray_circle));
                tv_tracking.setTextColor(mContext.getResources().getColor(R.color.tv_hint));
                tv_describle.setVisibility(View.GONE);
            } else {
                iv_bottom_line.setVisibility(View.VISIBLE);
                iv_top_line.setVisibility(View.VISIBLE);
                iv_dot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_gray_circle));
                tv_tracking.setTextColor(mContext.getResources().getColor(R.color.tv_hint));
                tv_describle.setVisibility(View.GONE);
            }
        }
    }
}
