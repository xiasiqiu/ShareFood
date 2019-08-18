package com.xinyuan.xyorder.app.base;

import android.content.Context;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */
public  class BasePresenter<V > {
    protected V mView;
    private Context context;
    public BasePresenter(V view) {
        attachView(view);
    }

    public void attachView(V view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
    }

    public V getmMvpView() {
        return mView;
    }
}
