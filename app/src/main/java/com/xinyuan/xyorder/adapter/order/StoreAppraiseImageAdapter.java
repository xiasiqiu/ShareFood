package com.xinyuan.xyorder.adapter.order;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/12/1
 */
public class StoreAppraiseImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public StoreAppraiseImageAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv_store_img = helper.getView(R.id.iv_store_img);
        if(!XEmptyUtils.isEmpty(item)){
            GlideImageLoader.setUrlImg(mContext, Constants.IMAGE_HOST+item.trim(), iv_store_img);
        }

    }
}
