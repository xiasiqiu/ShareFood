package com.xinyuan.xyorder.ui.mine;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.widget.XLoadingDialog;

import butterknife.BindView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/15
 */
public class WebviewActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void initView() {
        SystemBarHelper.immersiveStatusBar(this, 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(this, toolbar);

        XLoadingDialog.with(WebviewActivity.this)
                .setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框
        CommUtil.setBack(this, iv_header_left);
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            if ("login".equals(getIntent().getStringExtra("flag"))) {//从登陆页面跳转
                tv_header_center.setText("用户注册协议");
                web.loadUrl(Constants.userRegistrationProtocol);
            }else if("banner".equals(getIntent().getStringExtra("flag"))){//从Banner页面跳转
                String url=getIntent().getStringExtra("url");
                web.loadUrl(url);
            } else {//从关于共享点餐页面跳转
                tv_header_center.setText("免责声明");
                web.loadUrl(Constants.disclaimer);
            }
        }
        web.setInitialScale(50);//这里一定要设置，数值可以根据各人的需求而定，我这里设置的是50%的缩放
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
        // 设置WebView属性，能够执行JavaScript脚本
        web.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    XLoadingDialog.with(WebviewActivity.this).dismiss();
                }
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_web;
    }

}
