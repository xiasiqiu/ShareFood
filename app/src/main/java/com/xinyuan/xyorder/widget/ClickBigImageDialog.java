package com.xinyuan.xyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by f-x on 2017/12/109:45
 * 查看大图
 */

public class ClickBigImageDialog extends Dialog {
    @BindView(R.id.big_banner)
    Banner banner;
    private List<String> urlList;
    private String positionUrl;

    public ClickBigImageDialog(Context mContext, List<String> url, String positionUrl) {
        super(mContext, R.style.CommonDialog);
        this.urlList = url;
        this.positionUrl = positionUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.click_bigimage_list_show);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ButterKnife.bind((Dialog) this);
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i).trim();
            if (!url.contains(Constants.IMAGE_HOST)) {
                url = Constants.IMAGE_HOST + url + Constants.IMG_EVA_BIG;
            }
            urlList.set(i, url);
        }
        urlList.add(0, positionUrl);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setImages(urlList)
                .setViewPagerIsScroll(true)
                .isAutoPlay(false)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                dismiss();
            }
        });

    }


    @Override
    public void cancel() {
        super.cancel();
    }
}
