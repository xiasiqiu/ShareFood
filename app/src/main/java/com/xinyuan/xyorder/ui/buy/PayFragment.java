package com.xinyuan.xyorder.ui.buy;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.PayChooesAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.buy.PayResult;
import com.xinyuan.xyorder.common.bean.buy.WXPayBean;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.bean.order.PaymentBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.PayView;
import com.xinyuan.xyorder.mvp.presenter.PayPresenter;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.loadingview.XLoadingView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by f-x on 2017/11/216:17
 */

public class PayFragment extends BaseFragment<PayPresenter> implements PayView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rv_pay_type)
    RecyclerView rv_pay_type;
    @BindView(R.id.tv_order_name)
    TextView tv_order_name;
    @BindView(R.id.tv_order_price)
    TextView tv_order_price;
    @BindView(R.id.tv_pay_time_hour)
    TextView tv_pay_time_hour;
    @BindView(R.id.tv_pay_time_min)
    TextView tv_pay_time_min;
    @BindView(R.id.bt_pay)
    Button bt_pay;
    @BindView(R.id.loadingView)
    XLoadingView loadingView;
    private OrderBean orderBean;

    private PayChooesAdapter chooesPayAdapter;
    private boolean isCheck = false;
    private boolean isRun = true;
    private static long mMin;
    private static long mSec;

    public static PayFragment newInstance(OrderBean orderBean) {
        PayFragment fragment = new PayFragment();
        fragment.orderBean = orderBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("等待支付");
        CommUtil.setBack(getActivity(), iv_header_left);

        tv_order_name.setText(orderBean.getOrderName());
        tv_order_price.setText(getActivity().getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(orderBean.getOrderPrice()));
        long currentTime = XDateUtils.string2Millis(XDateUtils.getCurrentDate());


        if (orderBean.getPayTimeoutTime() > currentTime) {
            long time = (orderBean.getPayTimeoutTime() - currentTime) / 1000;
            XLog.v("" + time);

            if (time > 60) {
                mMin = (int) time / 60;
                mSec = (int) time - mMin * 60;
            } else {
                mMin = 0;
                mSec = time;
            }

            tv_pay_time_hour.setText("" + mMin);
            tv_pay_time_min.setText("" + mSec);
            startRun();
        } else {
            XToast.error("支付已超时");
            _mActivity.onBackPressed();
        }
        getPay();
    }

    @Override
    public void initListener() {
        bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pay = "";
                for (PaymentBean paymentBean : chooesPayAdapter.getData()) {
                    if (paymentBean.isCheck()) {
                        Pay = paymentBean.getCode();
                    }
                }
                switch (Pay) {
                    case Constants.ALIPAY:
                        mPresenter.alipay(orderBean.getOrderId());
                        break;
                    case Constants.WX:
                        if (isWXAppInstall()) {
                            mPresenter.wxpay(orderBean.getOrderId());
                        } else {
                            XToast.info("手机未安装微信，无法使用微信支付");
                        }
                        break;
                    case Constants.BLANCE:
                        mPresenter.balancePay(orderBean.getOrderId());

                        break;
                }

            }
        });

    }

    private boolean isWXAppInstall() {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
        api.registerApp(Constants.WX_APP_ID);
        boolean isWXAppInstalled = api.isWXAppInstalled();
        return isWXAppInstalled;
    }

    @Override
    public void aliPayResult(String data) {
        aliPay(data);
    }

    @Override
    public void wxResult(WXPayBean wxPayBean) {
        wxPay(wxPayBean);
    }

    @Override
    public void balanceResult(boolean status) {
        if (status) {
            _mActivity.onBackPressed();
            XToast.info("支付成功");
        } else {
            _mActivity.onBackPressed();
            XToast.error("支付失败");
        }
    }

    private void showPay(final List<PaymentBean> data) {
        data.get(0).setCheck(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(1);
        rv_pay_type.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getContext().getResources().getColor(R.color.colorLine)));
        rv_pay_type.setLayoutManager(layoutManager);
        chooesPayAdapter = new PayChooesAdapter(R.layout.item_pay, data);
        rv_pay_type.setAdapter(chooesPayAdapter);
        loadingView.showContent();
    }


    /**
     * 支付宝支付
     */
    private void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * 接受支付宝回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                _mActivity.onBackPressed();
                XToast.info("支付成功");
            } else {
                XToast.error("支付失败");
            }
        }

    };


    private void wxPay(WXPayBean orderIn) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
        iwxapi.registerApp(Constants.WX_APP_ID);
        PayReq payReq = new PayReq();
        payReq.appId = Constants.WX_APP_ID;
        payReq.partnerId = orderIn.getPartnerId();
        payReq.prepayId = orderIn.getPrepayId();
        payReq.packageValue = orderIn.getPackages();
        payReq.nonceStr = orderIn.getNonceStr();
        payReq.timeStamp = orderIn.getTimeStamp();
        payReq.sign = orderIn.getSign();
        iwxapi.sendReq(payReq);
    }


    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                if (isRun) {
                    tv_pay_time_hour.setText(mMin + "");
                    tv_pay_time_min.setText(mSec + "");
                }

            }
        }
    };

    private void computeTime() {
        mSec--;
        if (mSec < 0) {
            mMin--;
            mSec = 59;
            if (mMin < 0) {
                _mActivity.onBackPressed();
            }

        }

    }

    @Override
    public void onDestroyView() {
        isRun = false;
        super.onDestroyView();
    }

    @Override
    public void initData() {

    }

    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_pay;
    }


    private void getPay() {
        OkGo.<HttpResponseData<List<PaymentBean>>>get(Constants.API_PAY_TYPE + orderBean.getOrderId())
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<List<PaymentBean>>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<List<PaymentBean>>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showPay(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<List<PaymentBean>>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }


}
