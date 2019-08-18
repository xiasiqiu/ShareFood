package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/714:28
 *
 * @author f-x
 */

public class NoticeData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
