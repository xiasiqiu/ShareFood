package com.xinyuan.xyorder.ui.mine;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinyuan.xyorder.BuildConfig;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/3016:08
 */

public class AboutFragment extends BaseFragment {
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;

    @BindView(R.id.toolbar)
    Toolbar msg_toolbar;

    @BindView(R.id.tv_app_version)
    TextView tv_app_version;
    @BindView(R.id.ll_disclaimer)
    LinearLayout ll_disclaimer;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), msg_toolbar);
        tv_header_center.setText("关于共享点餐");
        tv_app_version.setText(BuildConfig.VERSION_NAME + "版");
        CommUtil.setBack(getActivity(), iv_header_left);
        ll_disclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(_mActivity, WebviewActivity.class));
            }
        });
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
        return R.layout.fragment_about;
    }


}
