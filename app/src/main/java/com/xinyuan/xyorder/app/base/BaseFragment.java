package com.xinyuan.xyorder.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinyuan.xyorder.app.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */

public abstract class BaseFragment<T extends BasePresenter> extends SupportFragment {
    protected T mPresenter;
    private View rootView;
    Unbinder mUnbinder;
    public static final String TAG = "LIFE:";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);
            rootView.setClickable(true);// 防止点击穿透，底层的fragment响应上层点击触摸事件
            ButterKnife.bind(this, rootView);
            mUnbinder = ButterKnife.bind(this, rootView);
            initView(rootView);
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;


    }

    public abstract void initView(View rootView);

    public abstract void initData();

    public void initListener() {

    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();


    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     */
    public View getStateViewRoot() {
        return rootView;
    }


    @Override
    public void onDestroy() {
        Log.e(TAG + "OnDestroy", getClass().getSimpleName());
        // TODO: 2017/10/16 UnBind 重复问题待解决
        //mUnbinder.unbind();
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        rootView = null;

    }

    @Override
    public void onDestroyView() {
        Log.e(TAG + "OnDestroyView", getClass().getSimpleName());
        //移除当前视图，防止重复加载相同视图使得程序闪退
        ((ViewGroup) rootView.getParent()).removeView(rootView);
        super.onDestroyView();
    }


    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }


    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    protected void saveStateToArguments() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(TAG + "OnStart", getClass().getSimpleName());
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(TAG + "OnResume", getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG + "OnPause", getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(TAG + "OnStop", getClass().getSimpleName());
        super.onStop();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
