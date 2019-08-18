package com.xinyuan.xyorder.ui.mine;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.xinyuan.xyorder.BuildConfig;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.UpdateBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.common.http.UpdateAppHttpUtil;
import com.xinyuan.xyorder.util.DataCleanManager;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.NormalDialog;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/1
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.tv_header_center)
    TextView tv_header_center;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cache)
    TextView tv_cache;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_new)
    TextView tv_new;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("设置");
        checkVersion();
        tv_version.setText(BuildConfig.VERSION_NAME);

    }

    @Override
    public void initData() {
        tv_cache.setText(getCacheSize());
    }

    @OnClick({R.id.iv_header_left, R.id.tv_about, R.id.rl_clear, R.id.tv_new})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                _mActivity.onBackPressed();
                break;
            case R.id.tv_about:
                start(AboutFragment.newInstance());
                break;
            case R.id.rl_clear:
                final NormalDialog dialog = new NormalDialog(getContext());
                dialog.setMessage("确定要清理APP图片缓存吗？");
                dialog.setEnterText("清理");
                dialog.setCancleText("取消");
                dialog.setClickListener(new NormalDialog.DialogClickListener() {
                    @Override
                    public void enterListener() {
                        DataCleanManager.cleanCacheData(_mActivity, "");
//                        Glide.get(getActivity()).clearMemory();
                        tv_cache.setText(getCacheSize());
                        dialog.dismiss();
                        XToast.info("清除缓存成功");
                    }

                    @Override
                    public void cancelListener() {

                    }
                });
                dialog.show();
                break;
            case R.id.tv_new:
                if (isUpdate) {
                    update();
                }

                break;

            default:
                break;
        }
    }

    private boolean isUpdate = false;

    private void checkVersion() {
        OkGo.<HttpResponseData<UpdateBean>>post(Constants.API_APP_UPDATE)
                .tag(this)
                .params("serverVersion", BuildConfig.VERSION_NAME)
                .params("serverPackage", getActivity().getPackageName())
                .params("testVersion", Constants.ISTEST)
                .execute(new JsonCallback<HttpResponseData<UpdateBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<UpdateBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            if (XEmptyUtils.isEmpty(response.body().getData())) {
                                isUpdate = false;
                                tv_new.setVisibility(View.GONE);

                            } else {
                                isUpdate = true;
                                tv_new.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<UpdateBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }

    private void update() {
        String path = Constants.APK_DIR;
        Map<String, String> params = new HashMap<String, String>();
        params.put("serverVersion", BuildConfig.VERSION_NAME);
        params.put("serverPackage", getActivity().getPackageName());
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(getActivity())
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(Constants.API_APP_UPDATE)
                .setPost(true)
                .setParams(params)
                .hideDialogOnDownloading(false)
                .setTopPic(R.drawable.update)
                .setThemeColor(0xffffac5d)
                .setTargetPath(path)
                .build()
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(jsonObject.optBoolean("isUpdate") ? "Yes" : "No")
                                    //（必须）新版本号，
                                    .setNewVersion(jsonObject.optString("serverVersion"))
                                    //（必须）下载地址


                                    .setApkFileUrl(Constants.APK_DOWN + jsonObject.optString("updateUrl"))
//                                  //更新日志
                                    .setUpdateLog(jsonObject.optString("updateContent"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(jsonObject.optBoolean("forcedUpdating"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        //CProgressDialogUtils.showProgressDialog(MainActivity.this);
                    }

                    /**
                     *
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        //CProgressDialogUtils.cancelProgressDialog(MainActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp() {
                    }
                });


    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public String getCacheSize() {
        String temp = "";
        try {
            temp = DataCleanManager.getAfinalSize(_mActivity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return temp;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_setting;
    }
}
