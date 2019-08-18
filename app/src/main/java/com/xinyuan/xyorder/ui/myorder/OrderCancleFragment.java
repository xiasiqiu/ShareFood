package com.xinyuan.xyorder.ui.myorder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.OrderBean;

import com.xinyuan.xyorder.common.bean.order.OrderCancleBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.even.OrderRefreshEven;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/21
 */
public class OrderCancleFragment extends BaseFragment {

    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rg_reason)
    RadioGroup rg_reason;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.et_other_reason)
    EditText et_other_reason;
    @BindView(R.id.rb_other_reason)
    RadioButton rb_other_reason;

    long order_id;
    String reason;
    boolean isOtherReason;

    public static OrderCancleFragment newInstance(long order_id) {
        OrderCancleFragment fragment = new OrderCancleFragment();
        fragment.order_id = order_id;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        tv_header_center.setText("取消订单");

        rg_reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

                switch (checkId) {
                    case R.id.rb_pay:
                        reason = "PAYMENT_PROBLEMS";
                        et_other_reason.setVisibility(View.GONE);
                        isOtherReason = false;
                        break;
                    case R.id.rb_thing:

                        reason = "TEMPORARY_EMERGENCY";
                        et_other_reason.setVisibility(View.GONE);
                        isOtherReason = false;
                        break;
                    case R.id.rb_error_info:

                        reason = "WRONG_INFORMATION";
                        et_other_reason.setVisibility(View.GONE);
                        isOtherReason = false;
                        break;
                    case R.id.rb_error_chooes:

                        reason = "CLICK_ERRORLY";
                        et_other_reason.setVisibility(View.GONE);
                        isOtherReason = false;
                        break;
                    case R.id.rb_other_reason:
                        et_other_reason.setVisibility(View.VISIBLE);
                        isOtherReason = true;
                        reason = "OTHER";
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_header_left, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header_left:
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, OrderFragment.newInstance()));
                _mActivity.finish();
                break;
            case R.id.btn_submit:
                cancleOrder(order_id);
                break;
            default:
                break;
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_order_cancle;
    }

    /**
     * 取消订单(详细原因)
     *
     * @param order_id
     */
    public void cancleOrder(final long order_id) {
        if (XEmptyUtils.isEmpty(reason)) {
            XToast.error("请选择原因");
            return;
        }
        OrderCancleBean cancleBean = new OrderCancleBean();

        if (isOtherReason) {
            if (XEmptyUtils.isEmpty(et_other_reason.getText().toString())) {
                XToast.error("请输入其他原因");
                return;
            } else {
                cancleBean.setCancelContent(et_other_reason.getText().toString());

            }
        }
        cancleBean.setOrderCancelReason(reason);
        cancleBean.setOrderId(order_id);
        cancleBean.setCancelType("USER");
        OkGo.<HttpResponseData<OrderBean>>put(Constants.API_CANCLE_ORDER_REASON)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .upJson(new Gson().toJson(cancleBean))
                .execute(new DialogCallback<HttpResponseData<OrderBean>>(getActivity(), "提交中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<OrderBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new OrderRefreshEven(true));
                            _mActivity.finish();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<OrderBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });

    }
}
