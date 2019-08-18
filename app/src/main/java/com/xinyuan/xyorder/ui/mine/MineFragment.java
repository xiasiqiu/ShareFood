package com.xinyuan.xyorder.ui.mine;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.UserInfoBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.LoginPageBusEven;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.even.TabSelectedEvent;
import com.xinyuan.xyorder.common.even.UserEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.main.MainFragment;
import com.xinyuan.xyorder.ui.login.LoginActivity;
import com.xinyuan.xyorder.ui.mine.address.AddressListFragment;
import com.xinyuan.xyorder.ui.mine.security.SecurityFragment;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeImageView;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */
public class MineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @BindView(R.id.tv_coupon)
    TextView tv_coupon;
    @BindView(R.id.tv_package)
    TextView tv_package;
    @BindView(R.id.tv_mine_tell)
    TextView tv_mine_tell;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_user_rate)
    TextView tv_user_rate;
    @BindView(R.id.tv_service_center)
    TextView tv_service_center;
    @BindView(R.id.tv_my_collect)
    TextView tv_my_collect;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.customer_image)
    CircleImageView customer_image;
    @BindView(R.id.mine_toolbar)
    Toolbar mine_toolbar;
    @BindView(R.id.iv_mine_notice)
    BGABadgeImageView iv_mine_notice;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.iv_mine_pic)
    ImageView iv_mine_pic;

    String packageData;
    String couponData;
    String integralData;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        EventBus.getDefault().register(this);
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), mine_toolbar);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
    }


    @Override
    public void initData() {
        if (!XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
            getUserInfo();
        }

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_mine;
    }


    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) { //当在主页面点击个人中心时，未登录则跳转至登录界面，登录则加载个人资料
        if (event.position != MainFragment.MINE) {
            return;
        }
        if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
            CommUtil.gotoActivity(getActivity(), LoginActivity.class, false, null);
        }
    }

    @Subscribe
    public void UserEven(UserEvent user) {
        tv_user_name.setText(user.getNickname());
        GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/avator/" + user.getUserId() + ".png" + Constants.IMG_AVATOR, customer_image);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //当登录后或退出登录后
    public void LoginPageBusEven(LoginPageBusEven pageBusEven) {
        if (pageBusEven.getFlag().equals(LoginPageBusEven.ISLOGIN)) {
            getUserInfo();
        } else if (pageBusEven.getFlag().equals(LoginPageBusEven.LOGINOUT)) {
            tv_user_name.setText("");
            tv_mine_tell.setText("");
            packageData = "0.00元";
            tv_package.setText(CommUtil.changeTextSize(packageData, packageData.length() - 1, packageData.length(), 40));
            couponData = "0个";
            tv_coupon.setText(CommUtil.changeTextSize(couponData, couponData.length() - 1, couponData.length(), 40));
            integralData = "0分";
            tv_integral.setText(CommUtil.changeTextSize(integralData, integralData.length() - 1, integralData.length(), 40));
        } else if (pageBusEven.getFlag().equals(LoginPageBusEven.NO_LOGIN)) {
            EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoHomeIndex));
        }
    }

    private UserInfoBean userInfoBean;

    private void showUserInfo(UserInfoBean data) {
        this.userInfoBean = data;
        GlideImageLoader.setUrlImg(getActivity(), Constants.MINE_PIC, iv_mine_pic);
        GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/avator/" + data.getUserId() + ".png" + Constants.IMG_AVATOR, customer_image);
        tv_user_name.setText(data.getNickname());
        tv_mine_tell.setText(data.getUsername());

        packageData = new DecimalFormat("0.00").format(data.getBalance()) + "元";
        tv_package.setText(CommUtil.changeTextSize(packageData, packageData.length() - 1, packageData.length(), 40));
        couponData = data.getCouponNumber() + "个";
        tv_coupon.setText(CommUtil.changeTextSize(couponData, couponData.length() - 1, couponData.length(), 40));
        integralData = data.getIntegral() + "分";
        tv_integral.setText(CommUtil.changeTextSize(integralData, integralData.length() - 1, integralData.length(), 40));

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick({R.id.rl_next, R.id.tv_address, R.id.iv_mine_notice, R.id.tv_my_collect, R.id.rl_integral, R.id.rl_coupon, R.id.rl_package, R.id.iv_mine_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_next:
                if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity())) || XEmptyUtils.isEmpty(userInfoBean)) {
                    CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
                    return;
                }
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, SecurityFragment.newInstance(userInfoBean)));
                break;
            case R.id.tv_address:
                if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
                    CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
                    return;
                }
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, AddressListFragment.newInstance(true)));
                break;
            case R.id.iv_mine_notice:
                if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
                    CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
                    return;
                }
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, NoticeFragment.newInstance()));
                break;
            case R.id.tv_my_collect:
                if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
                    CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
                    return;
                }
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, FavGoodFragment.newInstance()));
                break;
            case R.id.rl_package:
                break;
            case R.id.rl_coupon:
                if (XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
                    CommUtil.gotoActivity(getContext(), LoginActivity.class, false, null);
                    return;
                }
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, UserCouponFragment.newInstance()));
                break;
            case R.id.rl_integral:
                break;
            case R.id.iv_mine_setting:
                EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoFragment, SettingFragment.newInstance()));
                break;

        }
    }

    public void showNoticeMsg(Boolean data) {
        if (data) {
            iv_mine_notice.showCirclePointBadge();
        } else {
            iv_mine_notice.hiddenBadge();
        }
        if (XEmptyUtils.isSpace(tv_user_name.getText().toString())) {
            getUserInfo();
        }
    }


    /**
     * 当页面显示时检查是否有通知
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
            checkMsg();
        } else {
            iv_mine_notice.hiddenBadge();
        }
    }

    private void getUserInfo() {
        OkGo.<HttpResponseData<UserInfoBean>>get(Constants.API_MINE_USER)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<UserInfoBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<UserInfoBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showUserInfo(response.body().getData());
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<UserInfoBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    public void checkMsg() {
        OkGo.<HttpResponseData<Boolean>>put(Constants.API_CHECK_NOTICE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<Boolean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<HttpResponseData<Boolean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showNoticeMsg(response.body().getData());
                        }


                    }

                    @Override
                    public void onError(Response<HttpResponseData<Boolean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserInfo();
            }
        }, 500);
    }
}
