package com.xinyuan.xyorder.ui.mine.security;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.bean.LoginInfo;
import com.xinyuan.xyorder.common.bean.mine.UserInfoBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.LoginPageBusEven;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.even.UserEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SPUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.BottomPopupImage;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>
 * Description：账户与安全
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/10
 */
public class SecurityFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.rl_user_name)
    RelativeLayout rl_user_name;
    @BindView(R.id.rl_user_head)
    RelativeLayout rl_user_head;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.iv_user_head)
    CircleImageView iv_user_head;
    @BindView(R.id.tv_exit_login)
    TextView tv_exit_login;
    @BindView(R.id.tv_tel_bind)
    TextView tv_tel_bind;

    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int REQUEST_CODE_UPDATE_USER = 102;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_security;
    }

    public static SecurityFragment newInstance(UserInfoBean loginInfo) {
        SecurityFragment fragment = new SecurityFragment();
        fragment.loginInfo = loginInfo;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("账户与安全");
        EventBus.getDefault().register(this);
    }

    private UserInfoBean loginInfo;

    @Override
    public void initData() {
        if (!XEmptyUtils.isEmpty(loginInfo)) {
            tv_user_name.setText(loginInfo.getNickname());
            GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/avator/" + loginInfo.getUserId() + ".png" + Constants.IMG_AVATOR, iv_user_head);
            tv_tel_bind.setText(loginInfo.getUsername() + "（已绑定）");
        }
    }

    @OnClick({R.id.rl_user_name, R.id.rl_user_head, R.id.tv_exit_login, R.id.iv_header_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                _mActivity.onBackPressed();
                break;
            case R.id.rl_user_name:
                start(UpdateUserNameFragment.newInstance(loginInfo));
                break;
            case R.id.tv_exit_login:
                loginOut();
                break;
            case R.id.rl_user_head:
                XPermission.requestPermissions(getActivity(), 100, new String[]{Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new XPermission.OnPermissionListener() {
                    //权限申请成功时调用
                    @Override
                    public void onPermissionGranted() {
                        final BottomPopupImage bottomPopupOption = new BottomPopupImage(getActivity());
                        bottomPopupOption.setItemText(getString(R.string.take_pic), getString(R.string.choose_pic));
                        bottomPopupOption.showPopupWindow();
                        bottomPopupOption.setItemClickListener(new BottomPopupImage.onPopupWindowItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                switch (position) {
                                    case 0:
                                        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                                        bottomPopupOption.dismiss();
                                        break;
                                    case 1:
                                        Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
                                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                        bottomPopupOption.dismiss();
                                        break;
                                }

                            }
                        });
                    }

                    //权限被用户禁止时调用
                    @Override
                    public void onPermissionDenied() {
                        XPermission.showTipsDialog(getActivity());
                    }
                });
                break;
        }
    }

    ArrayList<ImageItem> images = null;
    private ArrayList<ImageItem> selImageList = new ArrayList<>(); //当前选择的所有图片

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    File file = new File(selImageList.get(0).path);
                    updateHead(file);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    File file = new File(selImageList.get(0).path);
                    updateHead(file);
                }
            }
        }
    }

    @Subscribe
    public void onEvent(UserEvent user) {
        tv_user_name.setText(user.getNickname());
    }

    private void updateHead(File file) {
        OkGo.<HttpResponseData<Void>>post(Constants.API_UPLOAD_PHOTO)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .params("file", file)
                .execute(new DialogCallback<HttpResponseData<Void>>(getActivity(), "上传中") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/avator/" + loginInfo.getUserId() + ".png" + Constants.IMG_AVATOR, iv_user_head);
                            XToast.info("头像修改成功！");
                            EventBus.getDefault().post(new UserEvent(loginInfo.isInitNicknameed(), loginInfo.getNickname(), loginInfo.getUserId()));
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    private void loginOut() {
        OkGo.<HttpResponseData<Void>>post(Constants.API_USER_LogOut)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new DialogCallback<HttpResponseData<Void>>(getActivity(), "退出中") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            SPUtils.remove(getActivity(), "token");
                            EventBus.getDefault().post(new MainFragmentStartEvent(MainFragmentStartEvent.GoHomeIndex));
                            EventBus.getDefault().post(new LoginPageBusEven(LoginPageBusEven.LOGINOUT, ""));
                            XToast.error("您已退出登录");
                            _mActivity.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
