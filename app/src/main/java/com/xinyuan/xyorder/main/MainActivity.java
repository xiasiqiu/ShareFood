package com.xinyuan.xyorder.main;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;

import com.taobao.sophix.SophixManager;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.xinyuan.xyorder.BuildConfig;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.UpdateAppHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity<BasePresenter> {

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.main_content, MainFragment.newInstance());
        }

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
    }


    @Override
    public void initData() {
        checkAppVersion(); //检查是否有新版本
    }

    /**
     * 启动检查版本
     */
    private void checkAppVersion() {
        String path = Constants.APK_DIR;
        Map<String, String> params = new HashMap<String, String>();
        params.put("serverVersion", BuildConfig.VERSION_NAME);
        params.put("serverPackage", getPackageName());

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(this)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public interface MyOnTouchListener {
        public boolean onTouchEvent(MotionEvent ev);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if (listener != null) {
                listener.onTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }


}
