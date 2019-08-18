package com.xinyuan.xyorder.common.http;

import java.io.Serializable;

/**
 * Created by fx on 2017/5/3 0003.
 */

public class SimpleResponse implements Serializable {

	private static final long serialVersionUID = -1477609349345966116L;
	public int errorCode;
	public boolean status;
	
	public HttpResponseData toHttpResponse() {
		HttpResponseData httpResponse = new HttpResponseData();
		httpResponse.errorCode = errorCode;
		httpResponse.status=status;
		return httpResponse;
	}
}
