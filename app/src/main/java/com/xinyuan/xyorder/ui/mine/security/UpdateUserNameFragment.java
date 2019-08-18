package com.xinyuan.xyorder.ui.mine.security;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.UserInfoBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.UserEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.ScreenUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * <p>
 * Description：更换昵称
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/11
 */
public class UpdateUserNameFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.et_user_name)
    EditText et_user_name;
    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;

    boolean isInitNicknameed;
    private UserInfoBean userInfoBean;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_update_user;
    }

    public static UpdateUserNameFragment newInstance(UserInfoBean userInfoBean) {
        UpdateUserNameFragment fragment = new UpdateUserNameFragment();
        fragment.userInfoBean = userInfoBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("用户名");
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInitNicknameed) {
                    if (XEmptyUtils.isEmpty(et_user_name.getText().toString())) {
                        XToast.error("请输入用户名");
                        return;
                    }
                    updateUserName(et_user_name.getText().toString());
                }
            }
        });
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenUtils.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
        isInitNicknameed = userInfoBean.isInitNicknameed();
        et_user_name.setText(userInfoBean.getNickname());
        if (isInitNicknameed) {
            tv_update.setBackgroundResource(R.drawable.rect_gray);
            et_user_name.setEnabled(false);
        } else {
            tv_update.setBackgroundResource(R.drawable.rect_green);
            et_user_name.setEnabled(true);
        }
    }

    public void updateUserName(String username) {
        OkGo.<HttpResponseData<UserInfoBean>>post(Constants.API_USER_UPDATE + "/" + username)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<UserInfoBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<UserInfoBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new UserEvent(response.body().getData().isInitNicknameed(), response.body().getData().getNickname(), response.body().getData().getUserId()));
                            ScreenUtils.hideSoftInput(_mActivity);
                            _mActivity.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<UserInfoBean>> response) {
                        super.onError(response);
                        ScreenUtils.hideSoftInput(_mActivity);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });

    }

}
