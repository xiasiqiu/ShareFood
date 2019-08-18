package com.xinyuan.xyorder.common.http;

import android.support.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.vector.update_app.HttpManager;
import com.xinyuan.xyorder.common.bean.UpdateBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.youth.xframe.utils.XEmptyUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by f-x on 2017/11/218:04
 */

public class UpdateAppHttpUtil implements HttpManager {

    private static final long serialVersionUID = -746296911702013167L;

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {

    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkGo.<HttpResponseData<UpdateBean>>post(Constants.API_APP_UPDATE)
                .tag(this)
                .params(params)
                .params("testVersion", Constants.ISTEST)
                .execute(new JsonCallback<HttpResponseData<UpdateBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<UpdateBean>> response) {
                        if (!XEmptyUtils.isEmpty(response.body().getData())) {
                            callBack.onResponse(response.body().getData().toString());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<UpdateBean>> response) {
                        super.onError(response);
                        callBack.onError("异常");
                    }
                });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkGo.<File>get(url).execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                callback.onBefore();
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                callback.onError("异常");
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);

                callback.onProgress(progress.fraction, progress.totalSize);
            }
        });
    }
}
