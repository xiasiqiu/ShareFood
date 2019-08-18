package com.xinyuan.xyorder.adapter.storedetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodEvaBean;
import com.xinyuan.xyorder.common.bean.store.store.StoreEvaluateBean;
import com.xinyuan.xyorder.common.AddViewHolder;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.BigPhoto.BigPhotoActivity;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.widget.ClickBigImageDialog;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by f-x on 2017/10/1717:26
 */

public class StoreEvaAdapter extends BaseQuickAdapter<StoreEvaluateBean, BaseViewHolder> {
    public StoreEvaAdapter(int layoutResId, @Nullable List<StoreEvaluateBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final StoreEvaluateBean item) {
        helper.setText(R.id.tv_eva_username, item.getUserName());
        helper.setText(R.id.tv_eva_time, XDateUtils.millis2String(Long.valueOf(item.getAppraiseTime())));
        helper.setText(R.id.tv_eva_content, XEmptyUtils.isSpace(item.getTagShopAppraise()) ? item.getContentShopAppraise() : item.getTagShopAppraise() + "\n" + item.getContentShopAppraise());
        helper.setImageResource(R.id.iv_store_eva_level, (Integer) DataUtil.getEvaStar(mContext, item.getShopAppraise()));
        CircleImageView circleImageView = helper.getView(R.id.iv_eva_userhead);
        GlideImageLoader.setCircleImageView(mContext, Constants.IMAGE_HOST + "/avator/" + item.getUserId() + ".png" + Constants.IMG_AVATOR, circleImageView);

        FlexboxLayout fb_goods = helper.getView(R.id.fl_goods);
        FlexboxLayout fb_imgs = helper.getView(R.id.fl_imgs);
        LinearLayout ll_replay = helper.getView(R.id.ll_replay);

        fb_goods.removeAllViews();
        if (!XEmptyUtils.isEmpty(item.getGoodsAppraiseList())) {
            fb_goods.setVisibility(View.VISIBLE);
            for (final GoodEvaBean goodEvaBean : item.getGoodsAppraiseList()) {
                AddViewHolder addViewHolder = new AddViewHolder(mContext, R.layout.view_text_simple);
                TextView tv_good_eva = addViewHolder.getView(R.id.tv_good_eva);


                if (XEmptyUtils.isEmpty(goodEvaBean.getTagGoodsAppraise())) {
                    tv_good_eva.setText(goodEvaBean.getGoodsName() + ":" + (XEmptyUtils.isEmpty(goodEvaBean.getAppraiseContent()) ? "" : goodEvaBean.getAppraiseContent()));

                } else {
                    tv_good_eva.setText(goodEvaBean.getGoodsName() + ":" + goodEvaBean.getTagGoodsAppraise() + (XEmptyUtils.isEmpty(goodEvaBean.getAppraiseContent()) ? "" : "," + goodEvaBean.getAppraiseContent()));
                }

                SpannableStringBuilder builder = new SpannableStringBuilder(tv_good_eva.getText().toString());

                ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary));

                builder.setSpan(redSpan, 0, goodEvaBean.getGoodsName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_good_eva.setText(builder);

                fb_goods.addView(addViewHolder.getCustomView());
            }
        } else {
            fb_goods.setVisibility(View.GONE);
        }
        fb_imgs.removeAllViews();

        if (!XEmptyUtils.isEmpty(item.getShopAppraiseImageList())) {
            fb_imgs.setVisibility(View.VISIBLE);

            for (int i = 0; i < item.getShopAppraiseImageList().size(); i++) {
                AddViewHolder addViewHolder = new AddViewHolder(mContext, R.layout.view_image_simple);
                ImageView ivImg = addViewHolder.getView(R.id.ivImg);
                GlideImageLoader.setUrlImg(mContext, Constants.IMAGE_HOST + item.getShopAppraiseImageList().get(i).trim() + Constants.IMG_STORE_DETAIL, ivImg);
                final int finalI = i;
                addViewHolder.getCustomView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> urlList = new ArrayList<>();
                        for (int j = 0; j < item.getShopAppraiseImageList().size(); j++) {
                            urlList.add(Constants.IMAGE_HOST + item.getShopAppraiseImageList().get(j) + Constants.IMG_EVA_BIG);
                        }
                        urlList.remove(item.getShopAppraiseImageList().get(finalI));
                        Intent i = new Intent(mContext, BigPhotoActivity.class);
                        i.putStringArrayListExtra("urlList", (ArrayList<String>) urlList);
                        i.putExtra("position", finalI);
                        mContext.startActivity(i);

//                        ClickBigImageDialog dialog = new ClickBigImageDialog(mContext, urlList, Constants.IMAGE_HOST + item.getShopAppraiseImageList().get(finalI).trim() + Constants.IMG_EVA_BIG);
//                        Window dialogWindow = dialog.getWindow();
//                        dialog.show();
//                        DisplayMetrics dm = new DisplayMetrics();
//                        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//                        wm.getDefaultDisplay().getMetrics(dm);
//                        dialogWindow.setLayout(dm.widthPixels, dialogWindow.getAttributes().height);

                    }
                });
                fb_imgs.addView(addViewHolder.getCustomView());
            }
        } else {
            fb_imgs.setVisibility(View.GONE);

        }
        ll_replay.removeAllViews();
        LinearLayout ll;
        if (!XEmptyUtils.isEmpty(item.getCommentList())) {
            ll_replay.setVisibility(View.VISIBLE);
            for (int i = 0; i < item.getCommentList().size(); i++) {
                AddViewHolder addViewHolder = new AddViewHolder(mContext, R.layout.item_store_eva_repay);
                TextView tv_store_replay = addViewHolder.getView(R.id.tv_store_replay);
                tv_store_replay.setText(item.getCommentList().get(i).getCommentContent());
                ll_replay.addView(addViewHolder.getCustomView());
            }

        } else {
            ll_replay.setVisibility(View.GONE);
        }


    }
}
