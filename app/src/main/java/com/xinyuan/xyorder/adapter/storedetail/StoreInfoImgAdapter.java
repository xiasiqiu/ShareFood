package com.xinyuan.xyorder.adapter.storedetail;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.bean.home.Carousel;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.BigPhoto.BigPhotoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f-x on 2017/10/1718:13
 */

public class StoreInfoImgAdapter extends BaseQuickAdapter<Carousel, BaseViewHolder> {
    public StoreInfoImgAdapter(int layoutResId, @Nullable List<Carousel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Carousel item) {
        ImageView iv_store_img = helper.getView(R.id.iv_store_img);
        GlideImageLoader.setUrlImg(mContext, Constants.IMAGE_HOST + item.getImgUrl() + Constants.IMG_STORE_DETAIL, iv_store_img);

        helper.setOnClickListener(R.id.iv_store_img, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> urls = new ArrayList<String>();
                for (Carousel carousel : getData()) {
                    urls.add(Constants.IMAGE_HOST + carousel.getImgUrl() + Constants.IMG_GOOD_MEDIUM);
                }
                urls.remove(item.getImgUrl());
                Intent i = new Intent(mContext, BigPhotoActivity.class);
                i.putStringArrayListExtra("urlList", (ArrayList<String>) urls);
                i.putExtra("position", helper.getLayoutPosition());
                mContext.startActivity(i);

//                ClickBigImageDialog dialog = new ClickBigImageDialog(mContext, urls, Constants.IMAGE_HOST + item.getImgUrl() + Constants.IMG_GOOD_MEDIUM);
//                Window dialogWindow = dialog.getWindow();
//                dialog.show();
//                DisplayMetrics dm = new DisplayMetrics();
//                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//                wm.getDefaultDisplay().getMetrics(dm);
//                dialogWindow.setLayout(dm.widthPixels, dialogWindow.getAttributes().height);
            }
        });

    }
}
