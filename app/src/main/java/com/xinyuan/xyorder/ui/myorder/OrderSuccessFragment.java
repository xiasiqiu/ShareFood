package com.xinyuan.xyorder.ui.myorder;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/30
 */
public class OrderSuccessFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.iv_store)
    CircleImageView civ_store;
    @BindView(R.id.tv_store_name)
    TextView tv_store_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_name)
    TextView tv_name;
    OrderBean orderBean;

    public static OrderSuccessFragment newInstance(OrderBean orderBean) {
        OrderSuccessFragment fragment = new OrderSuccessFragment();
        fragment.orderBean = orderBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderBean.isFromOrderList()) {
                    _mActivity.onBackPressed();
                } else {
                    _mActivity.finish();
                }
            }
        });
    }

    @Override
    public void initData() {
        if (!XEmptyUtils.isEmpty(orderBean)) {
            GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/shopLogo/" + orderBean.getShopId() + ".png" + Constants.IMG_STORE, civ_store);
            tv_store_name.setText(orderBean.getShopName());
            tv_name.setText(orderBean.getOrderName());
            tv_price.setText("￥" + orderBean.getOrderPrice() + "元");
            if (!XEmptyUtils.isEmpty(orderBean.getAddTime())) {
                tv_time.setText((XDateUtils.millis2String(Long.parseLong(orderBean.getAddTime()), "yyyy-MM-dd HH:mm")));
            }
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_order_success;
    }
}
