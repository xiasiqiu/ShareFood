package com.xinyuan.xyorder.common.even;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2017/6/26.
 */

public class MainFragmentStartEvent {

    public static final String ClassSearch = "ClassSearch";
    public static final String GoFragment = "GoFragment";
    public static final String GoHomeIndex = "GoHomeIndex";

    private SupportFragment targetFragment;
    private String flag;
    private Object obj;

    public MainFragmentStartEvent(String flag) {
        this.flag = flag;
    }

    public MainFragmentStartEvent(String flag, SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
        this.flag = flag;
    }


    public SupportFragment getTargetFragment() {
        return targetFragment;
    }

    public String getFlag() {
        return flag;
    }

    public Object getObj() {
        return obj;
    }
}
