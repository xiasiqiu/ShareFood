package com.xinyuan.xyorder.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.StoreActivityShowTextBean;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.List;

/**
 * <p>
 * Description：店铺adapter
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/20
 */
public class ShopStoreAdapter extends BaseQuickAdapter<StoreDetail, BaseViewHolder> {
    public ShopStoreAdapter(int layoutResId, @Nullable List<StoreDetail> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreDetail item) {

        helper.setText(R.id.tv_shop_name, item.getShopName());

        helper.setImageResource(R.id.iv_store_eva_level, (Integer) DataUtil.getEvaStar(mContext, item.getScore()));
        if (item.isOperatingState()) {
            helper.setVisible(R.id.tv_store_status, false);
        } else {
            helper.setVisible(R.id.tv_store_status, true);

        }

        ImageView iv_shop = helper.getView(R.id.iv_shop);
        GlideImageLoader.setUrlImg(mContext, Constants.IMAGE_HOST + item.getLogoUrl() + Constants.IMG_STORE, iv_shop);

        helper.setText(R.id.tv_sell_num, "月售" + item.getSales() + "单");
        if (!XEmptyUtils.isEmpty(item.getInstance())) {
            helper.setText(R.id.tv_distance, DataUtil.getDistance(item.getInstance()));
        }

        if (XEmptyUtils.isEmpty(item.getMinDeliveryPrice())) {
            helper.setText(R.id.tv_price_send, "起送价" + mContext.getResources().getString(R.string.money_rmb) + 0 + " |");
        } else {
            helper.setText(R.id.tv_price_send, "起送价 " + mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(item.getMinDeliveryPrice()) + " |");

        }
        helper.setText(R.id.tv_avg_price, "人均 " + mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(item.getPerson()));


        LinearLayout ll_sale = helper.getView(R.id.ll_sale);

        ll_sale.removeAllViews();

        if (!XEmptyUtils.isEmpty(item.getShopActive())) {
            ll_sale.setVisibility(View.VISIBLE);
            for (int i = 0; i < item.getShopActive().size(); i++) {
                if (i == 2) {
                    break;
                }
                LinearLayout rl_view = new LinearLayout(mContext);
                rl_view.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv1 = new TextView(mContext);
                ImageView tv3 = new ImageView(mContext);
                StoreActivityShowTextBean activityShowTextBean = (DataUtil.getCouponText(mContext, item.getShopActive().get(i).getActivityType()));
//                tv3.setText(activityShowTextBean.getActivityType());
//                tv3.setTextColor(mContext.getResources().getColor(R.color.bg_white));
                tv3.setImageDrawable(mContext.getResources().getDrawable(activityShowTextBean.getAcitvityColor()));
//                tv3.setTextSize(11);
//                tv3.setPadding(10, 5, 10, 5);
                tv1.setText(item.getShopActive().get(i).getActivityName());
                tv1.setTextColor(mContext.getResources().getColor(R.color.tv_91));
                tv1.setTextSize(11);


                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp3.topMargin = 5;
                lp3.bottomMargin = 10;
                rl_view.addView(tv3, lp3);

                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.topMargin = 5;
                lp1.leftMargin = 20;
                lp1.bottomMargin = 10;
                rl_view.addView(tv1, lp1);


                ll_sale.addView(rl_view);


            }


        } else {
            ll_sale.setVisibility(View.GONE);
        }


    }
}
