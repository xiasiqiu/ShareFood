package com.xinyuan.xyorder.adapter.order;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.ui.myorder.OrderEvaluationFragment;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.widget.NormalDialog;
import com.youth.xframe.utils.XDateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class MineOrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {

    private PayListenner payListenner;

    public MineOrderAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    public void setPayListenner(PayListenner payListenner) {
        this.payListenner = payListenner;
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderBean item) {
        helper.setText(R.id.tv_store_name, item.getShopName());
        helper.setText(R.id.tv_time, XDateUtils.millis2String(Long.parseLong(item.getAddTime()), "yyyy-MM-dd HH:mm"));
        helper.setText(R.id.tv_price, "￥" + item.getOrderPrice() + "");
        helper.setText(R.id.tv_status, item.getConvertOrderStatus());
        helper.setText(R.id.tv_name, item.getOrderName());

        ImageView iv_store = helper.getView(R.id.iv_store);
        GlideImageLoader.setUrlImg(mContext, Constants.IMAGE_HOST + "/shopLogo/" + item.getShopId() + ".png" + Constants.IMG_STORE, iv_store);
        TextView tv_operate = helper.getView(R.id.tv_operate);
        TextView tv_status = helper.getView(R.id.tv_status);
        if (Constants.ORDER_WAIT_PAY.equals(item.getOrderStatus())) {
            tv_operate.setText("去支付");
            tv_operate.setVisibility(View.VISIBLE);
            tv_status.setTextColor(mContext.getResources().getColor(R.color.menu_de_color));
            tv_operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payListenner.pay(item);
                }
            });
        } else if (Constants.ORDER_TRANSACT_FINISHED.equals(item.getOrderStatus())) {
            if (item.isIsAppraise()) {
                tv_operate.setVisibility(View.GONE);
            } else {
                tv_operate.setText("去评价");
                tv_operate.setVisibility(View.VISIBLE);
            }
            tv_status.setTextColor(mContext.getResources().getColor(R.color.order_finished));
            tv_operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOrderDetail(item.getOrderId());
                }
            });
        } else if (Constants.ORDER_DELIVERED.equals(item.getOrderStatus())) {
            tv_status.setTextColor(mContext.getResources().getColor(R.color.menu_de_color));
            tv_operate.setText("确认收货");
            tv_operate.setVisibility(View.VISIBLE);
            tv_operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final NormalDialog calDialog = new NormalDialog(mContext);
                    calDialog.setMessage("确定要收货吗?");
                    calDialog.setEnterText("确定");
                    calDialog.setCancleText("取消");
                    calDialog.setClickListener(new NormalDialog.DialogClickListener() {
                        @Override
                        public void enterListener() {
                            payListenner.sureOrder(item);
                        }

                        @Override
                        public void cancelListener() {
                        }
                    });
                    calDialog.show();
                }
            });
        } else {
            tv_status.setTextColor(mContext.getResources().getColor(R.color.menu_de_color));
            tv_operate.setVisibility(View.GONE);
            tv_operate.setOnClickListener(null);
        }
    }

    public interface PayListenner {
        void pay(OrderBean info);

        void sureOrder(OrderBean info);
    }

    public void getOrderDetail(long orderId) {
        OkGo.<HttpResponseData<OrderBean>>get(Constants.API_MINE_ORDER + "/" + orderId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(mContext))
                .execute(new JsonCallback<HttpResponseData<OrderBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(mContext, response.body())) {
                            response.body().getData().setFromOrderList(true);
                            EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, OrderEvaluationFragment.newInstance(response.body().getData())));
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(mContext, response);
                    }
                });
    }

}
