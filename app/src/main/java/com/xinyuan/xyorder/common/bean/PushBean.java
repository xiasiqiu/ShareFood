package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/611:29
 */


public class PushBean implements Serializable {
    private static final long serialVersionUID = 4808165216141937721L;
    private PushData pushData;
    private NoticeData  notice;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public PushData getPushData() {
        return pushData;
    }

    public NoticeData getNotice() {
        return notice;
    }
}
