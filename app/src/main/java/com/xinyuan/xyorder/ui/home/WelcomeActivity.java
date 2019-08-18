package com.xinyuan.xyorder.ui.home;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.igexin.sdk.PushManager;
import com.xinyuan.xyorder.BuildConfig;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.home.LocationBean;
import com.xinyuan.xyorder.common.service.IntentService;
import com.xinyuan.xyorder.common.service.PushService;
import com.xinyuan.xyorder.main.MainActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.LocationManager;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.util.ThreeDRotateAnimation;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/2314:06
 * 闪屏页面
 */

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.iv_welcome_logo)
    ImageView iv_welcome_logo;
    @BindView(R.id.iv_welcome_bottom)
    ImageView iv_welcome_bottom;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        SystemBarHelper.immersiveStatusBar(WelcomeActivity.this, 0); //设置状态栏透明
        setAnimation();
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);
        checkPerission();

    }

    @Override
    public void initData() {

    }

    /**
     * 设置动画
     */
    private void setAnimation() {
        startCoin();
        setWallet();
    }

    /**
     */
    private void checkPerission() {
        XPermission.requestPermissions(this, 100, new String[]{Manifest.permission
                .ACCESS_FINE_LOCATION, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                initLocation();
                showButton();
                if (!XNetworkUtils.isAvailable()) {
                    XToast.error("没有网络连接，请检查网络状态");
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toMain();
                    }
                }, 1500);

            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(WelcomeActivity.this);
            }
        });


    }


    /**
     * 获取定位
     */
    private void initLocation() {
        LocationManager.getInstance().start();
        LocationManager.getInstance().setListener(new LocationManager.LocationListener() {
            @Override
            public void onSuccessLocationListener(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    XLog.v("获取定位中");
                    LocationBean locationBean;
                    if (DataUtil.checkLocation(WelcomeActivity.this)) {
                        locationBean = DataUtil.getLocation(WelcomeActivity.this);
                    } else {
                        locationBean = new LocationBean();
                    }
                    locationBean.setLatitude(aMapLocation.getLatitude());
                    locationBean.setLongitude(aMapLocation.getLongitude());
                    locationBean.setCurrentyCity(aMapLocation.getAoiName());
                    String s = aMapLocation.getAdCode();
                    s = s.substring(0, 4) + "00";
                    DataUtil.setLocation(WelcomeActivity.this, locationBean);
                    EventBus.getDefault().postSticky(locationBean.getCurrentyCity());
                    LocationManager.getInstance().stop();

                }
            }
        });
    }

    /**
     * 显示进入button
     */
    private void showButton() {
        TranslateAnimation mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAnim.setDuration(1000);
        tv_version.setText("开发版本  " + BuildConfig.VERSION_NAME);
        tv_version.startAnimation(mShowAnim);
        tv_version.setVisibility(View.VISIBLE);
    }

    /**
     * 开始Logo动画
     */
    private void startCoin() {
        Animation animationTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_top_in);
        ThreeDRotateAnimation animation3D = new ThreeDRotateAnimation();
        animation3D.setRepeatCount(1);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(600);
        animationSet.addAnimation(animation3D);
        animationSet.addAnimation(animationTranslate);
        iv_welcome_logo.startAnimation(animationSet);
    }

    /**
     * 设置底部动画
     */
    private void setWallet() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (fraction >= 0.75) {
                    valueAnimator.cancel();
                    startWallet();
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 开始底部动画
     */
    private void startWallet() {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(iv_welcome_bottom, "scaleX", 1, 1.1f, 0.9f, 1);
        objectAnimator1.setDuration(600);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(iv_welcome_bottom, "scaleY", 1, 0.75f, 1.25f, 1);
        objectAnimator2.setDuration(600);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.start();
    }

    /**
     * 进入首页
     */
    public void toMain() {
        CommUtil.gotoActivity(WelcomeActivity.this, MainActivity.class, true, null);
    }

}
