package com.xinyuan.xyorder.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.bean.mine.NoticeInfo;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XDateUtils;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/16
 */
public class NoticeAdapter extends BaseQuickAdapter<NoticeInfo, BaseViewHolder> {
    public NoticeAdapter(int layoutResId, @Nullable List<NoticeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeInfo item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_detail, item.getContent());
        helper.setImageDrawable(R.id.iv_icon, DataUtil.getNoticeLogo(mContext, item.getContentType()));
        helper.setText(R.id.tv_time, XDateUtils.millis2String(Long.parseLong(item.getCreateTime()), "yyyy-MM-dd HH:mm"));
    }
}
