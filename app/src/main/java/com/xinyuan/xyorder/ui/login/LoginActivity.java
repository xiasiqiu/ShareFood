package com.xinyuan.xyorder.ui.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.LoginInfo;
import com.xinyuan.xyorder.common.bean.LoginUserBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.LoginPageBusEven;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.ui.mine.WebviewActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SPUtils;
import com.xinyuan.xyorder.util.ScreenUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.EditTextWithDel;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XRegexUtils;
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
 * @CreateDate 2017/9/18
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.phone_et)
    EditTextWithDel et_phone;
    @BindView(R.id.psw_et)
    EditTextWithDel et_pwd;
    @BindView(R.id.get_tv)
    TextView tv_get_code;
    @BindView(R.id.tv_prompt)
    TextView tv_prompt;
    MyCountTimer timer;
    private boolean checkFlag = true;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        SystemBarHelper.immersiveStatusBar(this, 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(this, toolbar);
        tv_header_center.setText("快速登录");
        iv_header_left.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        tv_prompt.setText(CommUtil.changeTextColor(tv_prompt.getText().toString(), tv_prompt.getText().toString().length() - 8, tv_prompt.getText().toString().length(), getResources().getColor(R.color.green)));
    }

    @OnClick({R.id.login_tv, R.id.get_tv, R.id.iv_header_left, R.id.tv_prompt})
    public void onClick(View view) {
        String phone = et_phone.getText().toString().trim();
        String code = et_pwd.getText().toString().trim();
        switch (view.getId()) {
            case R.id.login_tv:
                if (XEmptyUtils.isEmpty(phone)) {
                    XToast.error("请输入手机号码");
                    return;
                }
                if (!XRegexUtils.checkMobile(phone)) {
                    XToast.error("手机号格式不正确");
                    return;
                }
                if (XEmptyUtils.isEmpty(code)) {
                    XToast.error("请输入动态密码");
                    return;
                }
                login(phone, code);
                break;
            case R.id.get_tv:
                if (XEmptyUtils.isEmpty(phone)) {
                    XToast.error("请输入手机号码");
                    return;
                }
                if (!XRegexUtils.checkMobile(phone)) {
                    XToast.error("手机号格式不正确");
                    return;
                }
                getCode(phone);
                break;
            case R.id.iv_header_left:
                finish();
                break;
            case R.id.tv_prompt:
                startActivity(new Intent(LoginActivity.this, WebviewActivity.class).putExtra("flag", "login"));
                break;
        }
    }


    private void getCode(String phone) {
        if (checkFlag) {
            checkFlag = false;
            OkGo.<HttpResponseData<Void>>post(Constants.API_GET_CODE + "/" + phone).tag(this)
                    .execute(new DialogCallback<HttpResponseData<Void>>(this, "发送中") {
                        @Override
                        public void onSuccess(Response<HttpResponseData<Void>> response) {
                            if (HttpUtil.handleResponse(LoginActivity.this, response.body())) {
                                XToast.info("短信发送成功，请注意查收！");
                                et_pwd.requestFocus();
                                timer = new MyCountTimer(60000, 1000);
                                timer.start();
                            }

                        }

                        @Override
                        public void onError(Response<HttpResponseData<Void>> response) {
                            HttpUtil.handleError(LoginActivity.this, response);
                        }
                    });
        }

    }

    private void login(String name, String code) {
        LoginUserBean user = new LoginUserBean();
        user.setCode(code);
        user.setUserName(name);
        user.setCid(MyApplication.cid);
        user.setIos(false);
        OkGo.<HttpResponseData<LoginInfo>>post(Constants.API_USER_Login)
                .tag(this)
                .upJson(new Gson().toJson(user))
                .execute(new DialogCallback<HttpResponseData<LoginInfo>>(this, "登录中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<LoginInfo>> response) {
                        if (HttpUtil.handleResponse(LoginActivity.this, response.body())) {
                            DataUtil.setToken(LoginActivity.this, response.body().getData().getJwt());
                            EventBus.getDefault().post(new LoginPageBusEven(LoginPageBusEven.ISLOGIN, ""));
                            finish();
                            XToast.info("登录成功！");
                            ScreenUtils.hideSoftInput(LoginActivity.this);
                        }

                    }

                    @Override
                    public void onError(Response<HttpResponseData<LoginInfo>> response) {
                        HttpUtil.handleError(LoginActivity.this, response);
                    }
                });
    }


    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!XEmptyUtils.isEmpty(tv_get_code)) {
                tv_get_code.setText((millisUntilFinished / 1000) + "秒后重发");
                tv_get_code.setClickable(false);
            }

        }

        @Override
        public void onFinish() {
            if (!XEmptyUtils.isEmpty(tv_get_code)) {
                tv_get_code.setText("重新发送");
                tv_get_code.setClickable(true);
                checkFlag = true;
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            EventBus.getDefault().postSticky(new MainFragmentStartEvent(MainFragmentStartEvent.GoHomeIndex));
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


}
