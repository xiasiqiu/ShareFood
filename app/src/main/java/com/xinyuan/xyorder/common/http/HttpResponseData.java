package com.xinyuan.xyorder.common.http;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description：返回数据
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/18
 */
public class HttpResponseData<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;
    public String message;
    public int errorCode;
    public boolean status;
    public List<String> exceptions;


    public T data;

    public T getData() {
        return data;
    }

    public void setData(T datas) {
        this.data = datas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    @Override
    public String toString() {
        return "HttpResponseData{" +
                "message='" + message + '\'' +
                ", errorCode=" + errorCode +
                ", status=" + status +
                ", exceptions=" + exceptions +
                ", data=" + data +
                '}';
    }
}
