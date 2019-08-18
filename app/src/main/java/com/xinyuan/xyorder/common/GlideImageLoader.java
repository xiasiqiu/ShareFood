package com.xinyuan.xyorder.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.xinyuan.xyorder.R;
import com.youth.banner.loader.ImageLoader;
import com.youth.xframe.utils.XEmptyUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fx on 2017/5/4 0004.
 */

public class GlideImageLoader extends ImageLoader {


    private static final long serialVersionUID = -8887196687198312416L;

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder((int) R.drawable.img_defaule)
                .error((int) R.drawable.img_defaule)
                .into(imageView);


    }

    public static void setUrlImg(Context context, String path, ImageView imageView) {
        if (XEmptyUtils.isEmpty(path)) {
            Glide.with(context)
                    .load("")
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade()
                    .placeholder((int) R.drawable.img_defaule)
                    .error((int) R.drawable.img_defaule)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(path)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder((int) R.drawable.img_defaule)
                    .error((int) R.drawable.img_defaule).crossFade()
                    .into(imageView);
        }

    }

    public static void setGoodUrlImg(Context context, String path, ImageView imageView) {
        if (XEmptyUtils.isEmpty(path)) {
            Glide.with(context)
                    .load("")
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder((int) R.drawable.img_defaule)
                    .error((int) R.drawable.img_defaule)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(path)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder((int) R.drawable.img_defaule)
                    .into(imageView);
        }

    }

    public static void setHeaderImageView(final Context context, String path, final ImageView imageView) {
        Glide.with(context)
                .load(path)
                .asBitmap()
                .centerCrop()
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .placeholder(R.drawable.img_defaule)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(10);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static void setCircleImageView(final Context context, String path, final CircleImageView imageView) {
        Glide.with(context)
                .load(path)
                .asBitmap()
                .centerCrop()
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .placeholder(R.drawable.img_defaule)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

//
//        if (XEmptyUtils.isEmpty(path)) {
//            Glide.with(context)
//                    .load("")
//                    .crossFade()
//                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .error((int) R.drawable.img_defaule)
//                    .placeholder(R.drawable.img_defaule)
//                    .into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource,
//                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            imageView.setImageDrawable(resource);
//                        }
//                    });
//        } else {
//            Glide.with(context)
//                    .load(path)
//                    .crossFade()
//                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .error((int) R.drawable.img_defaule)
//                    .placeholder(R.drawable.img_defaule)
//                    .into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource,
//                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            imageView.setImageDrawable(resource);
//                        }
//                    });
        //       }

    }


}
