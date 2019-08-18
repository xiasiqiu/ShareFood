package com.xinyuan.xyorder.common.http;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.XToast;


public class HttpUtil {

    public static <T> boolean handleResponse(Context context, HttpResponseData<T> response) {
        boolean flag = false;
        if (XEmptyUtils.isEmpty(response)) {
            flag = false;
        } else {
            boolean status = false;
            status = response.isStatus();
            if (status) {
                flag = true;
            } else {
                flag = false;
                XToast.error(response.getMessage());
            }
        }

        return flag;
    }

    /**
     * 错误判断 1：网络问题 2：服务器问题
     *
     * @param context
     * @param response
     * @param <T>
     * @return
     */
    public static <T> int handleError(Context context, Response<T> response) {
        int state = 0;
        if (response == null) {
            state = 1;
        } else if (response.getException() != null) {
            response.getException().printStackTrace();
            okhttp3.Response rawResponse = response.getRawResponse();
            if (rawResponse != null) {
                XLog.v("网络状态码：" + rawResponse.code());
                XLog.v(rawResponse.toString());
                switch (rawResponse.code()) {
                    case 400:
                        state = 2;
                        XToast.error("404-与服务器连接错误！");
                        break;
                    case 404:
                        state = 2;
                        XToast.error("404-与服务器连接错误！");
                        break;
                    case 500:
                        state = 2;
                        XToast.error("500-与服务器连接错误！");
                        break;
                    case 505:
                        state = 2;
                        XToast.error("505-与服务器连接错误！");
                        break;

                }
            } else {
                state = 2;
                XToast.error("与服务器连接错误！");

            }

        }
        return state;
    }

}
