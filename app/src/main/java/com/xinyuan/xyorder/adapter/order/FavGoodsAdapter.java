package com.xinyuan.xyorder.adapter.order;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/20
 */
public class FavGoodsAdapter extends BaseQuickAdapter<StoreDetail, BaseViewHolder> {

    public static boolean isDetele = false;
    public static HashMap<Integer, String> choeseList = new HashMap<Integer, String>();

    public FavGoodsAdapter(int layoutResId, @Nullable List<StoreDetail> data, boolean isDetele) {
        super(layoutResId, data);
        this.isDetele = isDetele;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final StoreDetail item) {
        CheckBox chekbox = helper.getView(R.id.chekbox);
        if (isDetele) { //是否编辑状态
            chekbox.setVisibility(View.VISIBLE);
            if (choeseList.containsValue(item.getShopId())) {
                chekbox.setChecked(true);
            } else {
                chekbox.setChecked(false);
            }
            chekbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        choeseList.put(helper.getPosition(), String.valueOf(item.getShopId()));
                    }
                }
            });
        } else {
            chekbox.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_shop_name, item.getShopName());
        helper.setText(R.id.tv_sell_num, "月售" + item.getSales() + "单");
        helper.setText(R.id.tv_avg_price, "人均  " + mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(item.getPerson()));
//        StarBar starBar = helper.getView(R.id.sb_shop_eva);
        ImageView iv_shop = helper.getView(R.id.iv_shop);
        GlideImageLoader.setUrlImg(mContext, Constants.IMAGE_HOST + item.getLogoUrl()+Constants.IMG_GOODS, iv_shop);
        helper.setImageResource(R.id.iv_store_eva_level, (Integer) DataUtil.getEvaStar(mContext, item.getScore()));
//        starBar.setStarMark(4.30f);
//        starBar.setClickable(false);
    }
}
