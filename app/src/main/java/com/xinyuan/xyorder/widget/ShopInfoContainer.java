package com.xinyuan.xyorder.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.ui.login.LoginActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.XToast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by f-x on 2017/10/1816:18
 * 店铺主页头部信息栏
 */

public class ShopInfoContainer extends RelativeLayout {
    public TextView shop_name, tv_top_store_name_top;
    TextView shop_announce;
    TextView shop_address;
    //    TextView shop_fav;
    CircleImageView ci_store_head;
    TextView tv_good_fv;
    //    ImageView iv_store_back;
    //    ImageView iv_store_back;
    private Context mContext;
    private StoreDetail storeDetail;
    private long store_id;
    private boolean isFav;

    public ShopInfoContainer(Context context) {
        super(context);
        this.mContext = getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShopInfoContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = getContext();
        inflate(context, R.layout.view_store_top_info, this);
        shop_name = findViewById(R.id.tv_top_store_name);
        tv_top_store_name_top = findViewById(R.id.tv_top_store_name_top);
        shop_announce = findViewById(R.id.tv_store_announce);
        shop_address = findViewById(R.id.tv_store_address);
//        shop_fav = findViewById(R.id.tv_store_collect);
        tv_top_store_name_top.setAlpha(0);
        ci_store_head = findViewById(R.id.ci_store_head);
        tv_good_fv = findViewById(R.id.tv_good_fv);
//        iv_store_back = findViewById(R.id.iv_store_back);


        tv_good_fv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFav) { //若该商品已被收藏
                    deteleFavGood(store_id); //取消收藏
                } else {
                    addFavStore(store_id); //添加收藏
                }
            }
        });


    }

    public void showInfo(StoreDetail storeDetail) {
        if (!XEmptyUtils.isEmpty(storeDetail)) {
            GlideImageLoader.setCircleImageView(getContext(), Constants.IMAGE_HOST + storeDetail.getLogoUrl() + Constants.IMG_STORE, ci_store_head);
            shop_name.setText(storeDetail.getShopName());
            tv_top_store_name_top.setText(storeDetail.getShopName());
            shop_address.setText(storeDetail.getAddress());
            if (XEmptyUtils.isEmpty(storeDetail.getIntroduce())) {
                shop_announce.setText("暂无店铺简介");

            } else {
                shop_announce.setText(storeDetail.getIntroduce());

            }
            store_id = storeDetail.getShopId();
            if (storeDetail.isCollectioned()) {
                tv_good_fv.setText("已收藏");
                tv_good_fv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_faved, 0, 0);
//            cb_good_fv.setChecked(true);
            } else {
                tv_good_fv.setText("未收藏");
                tv_good_fv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_faved_de, 0, 0);
//            cb_good_fv.setChecked(false);
            }
        }


    }

    public void setWgAlpha(float alpha) {
        shop_name.setAlpha(alpha);
        shop_announce.setAlpha(alpha);
        shop_address.setAlpha(alpha);
        tv_good_fv.setAlpha(alpha);
        ci_store_head.setAlpha(alpha);
        tv_top_store_name_top.setAlpha(1 - alpha);
    }

    /**
     * 收藏
     *
     * @param shopId
     */
    public void addFavStore(long shopId) {
        if (XEmptyUtils.isEmpty(DataUtil.getToken(mContext))) {
            CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
        } else {
            OkGo.<HttpResponseData<Void>>put(Constants.API_MINE_COLLECTION + "/" + shopId)
                    .tag(this)
                    .headers("TOKEN", DataUtil.getToken(mContext))
                    .params("shopId", shopId)
                    .execute(new JsonCallback<HttpResponseData<Void>>() {
                        @Override
                        public void onSuccess(Response<HttpResponseData<Void>> response) {
                            XLog.v(response.body().toString());
                            if (HttpUtil.handleResponse(mContext, response.body())) {
                                isFav = true;
                                tv_good_fv.setText("已收藏");
                                tv_good_fv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_faved, 0, 0);
//                                cb_good_fv.setChecked(true);
                                XToast.info("您已经收藏成功");
                            }
                        }

                        @Override
                        public void onError(Response<HttpResponseData<Void>> response) {
                            super.onError(response);
                            HttpUtil.handleError(mContext, response);
                        }
                    });
        }

    }

    /**
     * 取消收藏商品
     *
     * @param id
     */
    public void deteleFavGood(long id) {
        OkGo.<HttpResponseData<List<StoreDetail>>>delete(Constants.API_MINE_COLLECTION + "/" + id)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<List<StoreDetail>>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<List<StoreDetail>>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            isFav = false;
                            tv_good_fv.setText("未收藏");
                            tv_good_fv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_faved_de, 0, 0);
                            XToast.info("您已经取消收藏");

                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<List<StoreDetail>>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }

}
