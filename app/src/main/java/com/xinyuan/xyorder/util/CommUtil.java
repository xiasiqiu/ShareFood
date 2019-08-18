package com.xinyuan.xyorder.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.constant.Constants;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by f-x on 2017/10/2416:36
 */

public class CommUtil {
    public static File logDir;

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void gotoActivity(Context poFrom, Class<?> poTo, boolean pbFinish, Map<String, String> pmExtra) {
        Intent loIntent = new Intent(poFrom, poTo);
        loIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!(pmExtra == null || pmExtra.isEmpty())) {
            for (String lsKey : pmExtra.keySet()) {
                loIntent.putExtra(lsKey, (String) pmExtra.get(lsKey));
            }
        }
        if (pbFinish) {
            ((Activity) poFrom).finish();
        }
        poFrom.startActivity(loIntent);
    }

    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    public static void goActivity(Context poFrom, Class<?> poTo, boolean pbFinish, Map<String, Long> pmExtra) {
        Intent loIntent = new Intent(poFrom, poTo);
        loIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!(pmExtra == null || pmExtra.isEmpty())) {
            for (String lsKey : pmExtra.keySet()) {
                loIntent.putExtra(lsKey, (Long) pmExtra.get(lsKey));
            }
        }
        if (pbFinish) {
            ((Activity) poFrom).finish();
        }
        poFrom.startActivity(loIntent);
    }


    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String getText(EditText view) {
        return view.getText().toString();
    }

    public static String getText(TextView view) {
        return view.getText().toString();
    }

    public static String getText(Button view) {
        return view.getText().toString().trim();
    }

    public static boolean isNotEmpty(CharSequence... cs) {
        for (CharSequence c : cs) {
            if (isEmpty(c)) {
                return false;
            }
        }
        return true;
    }

    public static void createDir() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File rootDir = new File(Constants.APP_DIR);
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
            File cacheDir = new File(Constants.CACHE_DIR);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            logDir = new File(Constants.LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
        }
    }


    public static int getScreenWeight(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }


    public static void addTvAnim(View fromView, int[] carLoc, Context context, final CoordinatorLayout rootview) {
        int[] addLoc = new int[2];
        fromView.getLocationInWindow(addLoc);
        Path path = new Path();
        path.moveTo(addLoc[0], addLoc[1]);
        path.quadTo(carLoc[0], addLoc[1] - 100, carLoc[0], carLoc[1]);

        final TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.circle_green);
        textView.setText("1");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(XDensityUtils.dp2px(20), XDensityUtils.dp2px(20));
        rootview.addView(textView, lp);
        ViewAnimator.animate(textView).path(path).accelerate().duration(300).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                rootview.removeView(textView);
            }
        }).start();
    }

    public static String getSubString(Context context, TextView tv, String content, int maxLine) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        float width = tv.getPaint().measureText(content);
        //这里只是为了方便，用屏幕宽度代替了textview控件宽度，如果需要精准控制，可以换成控件宽度
        float tvWidth = wm.getDefaultDisplay().getWidth();
        if (width / tvWidth > (maxLine + 0.3)) {
            return content.substring(0, (int) (content.length() / (width / tvWidth) / (maxLine + 0.3))) + "...";
        }
        return content;
    }

    //抖动动画CycleTimes动画重复的次数
    public static Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 6, 0, 6);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    public static Point getScreenSize(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    public static AnimationSet getInAnimationTest(Context context) {
        AnimationSet out = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(150);
        ScaleAnimation scale = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(150);
        out.addAnimation(alpha);
        out.addAnimation(scale);
        return out;
    }

    public static AnimationSet getOutAnimationTest(Context context) {
        AnimationSet out = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(150);
        ScaleAnimation scale = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(150);
        out.addAnimation(alpha);
        out.addAnimation(scale);
        return out;
    }


    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize1(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return new Point(display.getWidth(), display.getHeight());
        } else {
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }

    public static String getPriceString(BigDecimal price) {
        if (price != null) {
            return BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(price)))
                    .stripTrailingZeros().toPlainString();

        }
        return "0.00";
    }

    private static long lastClickTime = 0;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isCarFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 600) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setBack(final Activity activity, ImageView iv_header_left) {
        if (iv_header_left != null) {
            iv_header_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFastClick()) {
                        ScreenUtils.hideSoftInput(activity);
                        activity.onBackPressed();

                    }
                }
            });
        }


    }


    public static List<String> distinctList(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            if (!newList.contains(s)) {
                newList.add(s);
            }
        }
        return newList;
    }


    public static int chose;

    /**
     * 服务器图片地址包含\字符，此方法进行转换
     *
     * @param po
     * @return
     */
    public static String getUrl(String po) {
        if (!XEmptyUtils.isEmpty(po)) {
            String url = po.replace("\\", "/");
            return url;

        } else {
            return "";
        }

    }


    /**
     * @param content      内容
     * @param startIndex   对应内容索引start
     * @param endIndex     对应内容索引end
     * @param selectedSize 对应的大小
     * @return
     */
    public static SpannableString changeTextSize(String content, int startIndex, int endIndex, int selectedSize) {
        SpannableString s = new SpannableString(content);
        try {
            s.setSpan(new AbsoluteSizeSpan(selectedSize), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            s.setSpan(new ForegroundColorSpan(selectedColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * @param content       内容
     * @param startIndex    对应内容索引start
     * @param endIndex      对应内容索引end
     * @param selectedColor 对应的颜色
     * @return
     */
    public static SpannableString changeTextColor(String content, int startIndex, int endIndex, int selectedColor) {
        SpannableString s = new SpannableString(content);
        try {
            s.setSpan(new ForegroundColorSpan(selectedColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 通过包名检查apk是否安装
     *
     * @param packageName 要检测的apk包名
     * @return
     */
    public static boolean isInstallByPackageName(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
