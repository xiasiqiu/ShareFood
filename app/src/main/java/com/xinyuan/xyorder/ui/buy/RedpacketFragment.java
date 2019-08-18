package com.xinyuan.xyorder.ui.buy;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.RedPacketAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.OrderCouponBean;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/2017:53
 */

public class RedpacketFragment extends BaseFragment {
    @BindView(R.id.rv_redpacket)
    RecyclerView rv_redpacket;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.tv_header_right)
    TextView tv_header_right;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.loadingView)
    XLoadingView loadingView;

    private RedPacketAdapter redPacketAdapter;
    private List<OrderCouponBean> redPacketBeans;
    TextView tv_red_no;
    private boolean notUserRed = true;

    public static RedpacketFragment newInstance(List<OrderCouponBean> redPacketBeans) {
        RedpacketFragment redpacketFragment = new RedpacketFragment();
        redpacketFragment.redPacketBeans = redPacketBeans;
        return redpacketFragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("选择红包");
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void onEnterAnimationEnd(Bundle saveInstanceState) {
        if (XEmptyUtils.isEmpty(redPacketBeans)) {
            loadingView.showEmpty();
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(1);
            rv_redpacket.setLayoutManager(layoutManager);
            redPacketAdapter = new RedPacketAdapter(R.layout.fragment_redpacket_item, redPacketBeans);
            rv_redpacket.setAdapter(redPacketAdapter);
            redPacketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (redPacketBeans.get(position).getCoupon().isCheck()) {
                        redPacketAdapter.getData().get(position).getCoupon().setCheck(false);
                        notUserRed = true;
                        tv_red_no.setActivated(true);
                        EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESECOUPON, redPacketAdapter.getData().get(position)));
                        _mActivity.onBackPressed();

                    } else {
                        boolean haveCkeck = false;
                        for (OrderCouponBean redPacketBean : redPacketAdapter.getData()) {
                            if (redPacketBean.getCoupon().isCheck()) {
                                if (redPacketBean.getCouponId() != redPacketAdapter.getData().get(position).getCouponId()) {
                                    redPacketBean.getCoupon().setCheck(false);
                                    redPacketAdapter.getData().get(position).getCoupon().setCheck(true);
                                    haveCkeck = true;
                                    tv_red_no.setActivated(false);
                                    EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESECOUPON, redPacketAdapter.getData().get(position)));
                                    _mActivity.onBackPressed();

                                }
                            }
                        }
                        if (!haveCkeck) {
                            redPacketAdapter.getData().get(position).getCoupon().setCheck(true);
                            tv_red_no.setActivated(false);
                            EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESECOUPON, redPacketAdapter.getData().get(position)));
                            _mActivity.onBackPressed();
                        }

                    }
                    redPacketAdapter.notifyDataSetChanged();
                }
            });

            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_redpacket_top, (ViewGroup) rv_redpacket.getParent(), false);
            TextView tv_red_num = view.findViewById(R.id.tv_red_num);
            int num = redPacketBeans.size();
            String s = "有" + num + "个红包可用";
            String bs = s.substring(s.indexOf("有") + 1, s.indexOf("个"));
            SpannableString ss1 = new SpannableString(s);
            ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), s.indexOf("有"), s.indexOf("个"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            tv_red_no = view.findViewById(R.id.tv_red_no);
            tv_red_no.setActivated(true);
            tv_red_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notUserRed) {
                        notUserRed = false;
                        tv_red_no.setActivated(false);
                        EventBus.getDefault().post(new BuyBusEven(BuyBusEven.NOCHOOESECOUPON, ""));
                    } else {
                        for (OrderCouponBean redPacketBean : redPacketAdapter.getData()) {
                            if (redPacketBean.getCoupon().isCheck()) {
                                redPacketBean.getCoupon().setCheck(false);
                            }
                        }
                        redPacketAdapter.notifyDataSetChanged();
                        notUserRed = true;
                        tv_red_no.setActivated(true);
                        EventBus.getDefault().post(new BuyBusEven(BuyBusEven.NOCHOOESECOUPON, ""));

                    }
                }
            });
            redPacketAdapter.addHeaderView(view);
            loadingView.showContent();
        }

    }

    @Override
    public void initData() {


    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_redpacket;
    }


}
