package com.xinyuan.xyorder.adapter.storedetail;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.good.GoodsCategory;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/27
 */
public class StoreCategoryAdapter extends BaseQuickAdapter<GoodsCategory,BaseViewHolder> {

    public StoreCategoryAdapter(int layoutResId, @Nullable List<GoodsCategory> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void convert(BaseViewHolder helper, GoodsCategory item) {
        helper.setText(R.id.tv_category,item.getGoodsCategoryName());
        RelativeLayout rl_view=helper.getView(R.id.rl_view);
//        if (item.isChecked()){
//            rl_view.setBackgroundColor(mContext.getResources().getColor(R.color.white_fa));
//            helper.setTextColor(R.id.tv_category,mContext.getResources().getColor(R.color.green));
//        }else{
//            rl_view.setBackgroundColor(mContext.getResources().getColor(R.color.white_f2));
//            helper.setTextColor(R.id.tv_category,mContext.getResources().getColor(R.color.gray_cc));
//        }
    }
}
