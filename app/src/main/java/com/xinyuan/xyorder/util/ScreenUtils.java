package com.xinyuan.xyorder.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/22
 */
public class ScreenUtils {

    /**
     * 隐藏软键盘
     * @param activity
     * @return
     */
    public static boolean hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() == null || activity.getCurrentFocus()
                .getWindowToken() == null)
            return false;
        return imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
