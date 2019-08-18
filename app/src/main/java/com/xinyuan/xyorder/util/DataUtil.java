package com.xinyuan.xyorder.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.ChoeseTimeBean;
import com.xinyuan.xyorder.common.bean.buy.ChoeseTimeContentBean;
import com.xinyuan.xyorder.common.bean.buy.NumBean;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeBean;
import com.xinyuan.xyorder.common.bean.buy.SelectTimeContentBean;
import com.xinyuan.xyorder.common.bean.home.LocationBean;
import com.xinyuan.xyorder.common.bean.store.StoreActivityShowTextBean;
import com.xinyuan.xyorder.common.constant.Constants;
import com.youth.xframe.cache.XCaches;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by f-x on 2017/10/1820:15
 * 测试数据填充
 */

public class DataUtil {


    public static List<SelectTimeBean> getSendTime(String type, List<SelectTimeBean> selectTimeBean, BigDecimal meefee) {
        if (!XEmptyUtils.isEmpty(selectTimeBean)) {
            for (int j = 0; j < selectTimeBean.size(); j++) {
                String currentday = XDateUtils.format(XDateUtils.millis2Date(selectTimeBean.get(j).getDate()), "MM-dd");
                String currentweek = XDateUtils.getWeek(XDateUtils.millis2Date(selectTimeBean.get(j).getDate()));
                String time_title = "";
                if (j == 0) {
                    time_title = "今天(" + currentweek + ")";
                } else {
                    time_title = currentday + "(" + currentweek + ")";
                }
                List<SelectTimeContentBean> contentBeans = new ArrayList<SelectTimeContentBean>();
                for (int i = 0; i < selectTimeBean.get(j).getTimes().size(); i++) {
                    SelectTimeContentBean contentBean;
                    long time = XDateUtils.string2Millis(selectTimeBean.get(j).getTimes().get(i), "HH:mm");
                    if (i == 0 && j == 0) {
                        if (type.equals(Constants.ORDER_TAKEOUT)) {
                            contentBean = new SelectTimeContentBean("尽快送达|预计" + selectTimeBean.get(j).getTimes().get(i), false, selectTimeBean.get(j).getDate(), time + selectTimeBean.get(j).getDate() + (8 * 1000 * 60 * 60), CommUtil.getPriceString(meefee));

                        } else {
                            contentBean = new SelectTimeContentBean("立即预定" + selectTimeBean.get(j).getTimes().get(i), false, selectTimeBean.get(j).getDate(), time + selectTimeBean.get(j).getDate() + (8 * 1000 * 60 * 60), CommUtil.getPriceString(meefee));

                        }

                    } else {
                        contentBean = new SelectTimeContentBean(selectTimeBean.get(j).getTimes().get(i), false, selectTimeBean.get(j).getDate(), time + selectTimeBean.get(j).getDate() + (8 * 1000 * 60 * 60), CommUtil.getPriceString(meefee));

                    }
                    contentBeans.add(contentBean);
                }
                selectTimeBean.get(j).setDay_name(time_title);
                selectTimeBean.get(j).setContentTime(contentBeans);
            }
            selectTimeBean.get(0).setCheck(true);
            selectTimeBean.get(0).getContentTime().get(0).setCheck(true);

            return selectTimeBean;
        }
        return selectTimeBean;

    }


    /**
     * 获取预定订单时间
     *
     * @param context
     * @param stratTime
     * @param endTime
     * @return
     */
    public static List<ChoeseTimeBean> getReOrderTime(Context context, String stratTime, String endTime) {

        long start_time = XDateUtils.string2Millis(stratTime, "HH:mm");
        long end_time = XDateUtils.string2Millis(endTime, "HH:mm");
        List<ChoeseTimeBean> list = getSendOrderTime(context, stratTime, endTime, new BigDecimal(-1));
        Date date = list.get(0).getTimeContentList().get(0).getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(year, month, day);

        Date date1 = cal.getTime();

        long firstTime = XDateUtils.date2Millis(date1);

        for (int i = 1; i < 8; i++) {
            long current_long_day = firstTime + i * 24 * 60 * 60 * 1000;

            String currentday = XDateUtils.format(XDateUtils.millis2Date(current_long_day), "MM-dd");
            String currentweek = XDateUtils.getWeek(XDateUtils.millis2Date(current_long_day));
            List<ChoeseTimeContentBean> contentBeans = new ArrayList<ChoeseTimeContentBean>();


            for (int j = 1; j < 48; j++) {
                long current_long_time = start_time + current_long_day + j * 3 * 60 * 10000;
                if (current_long_time > current_long_day + end_time) {
                    break;
                }

                ChoeseTimeContentBean choeseTimeContentBean = new ChoeseTimeContentBean(XDateUtils.millis2String(current_long_time), XDateUtils.millis2Date(current_long_time), "-1", i + j);


                contentBeans.add(choeseTimeContentBean);
            }

            ChoeseTimeBean choeseTimeBean = new ChoeseTimeBean(currentday + "(" + currentweek + ")", contentBeans, i);
            list.add(choeseTimeBean);
        }
        return list;

    }

    /**
     * 获取外卖配送时间
     *
     * @param context
     * @param mealFee
     * @return
     */
    public static List<ChoeseTimeBean> getSendOrderTime(Context context, String stratTime, String endTime, BigDecimal mealFee) {
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(XDateUtils.string2Millis(XDateUtils.getCurrentDate()));
        int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts = mCalendar.get(Calendar.MINUTE);
        String str_hour = mHour < 10 ? "0" + mHour : String.valueOf(mHour);
        String str_minut = mMinuts < 10 ? "0" + mMinuts : String.valueOf(mMinuts);

        String currentTime = str_hour + ":" + str_minut;


        long start_time = XDateUtils.string2Millis(stratTime, "HH:mm");
        long end_time = XDateUtils.string2Millis(endTime, "HH:mm");
        long current_Time = XDateUtils.string2Millis(currentTime, "HH:mm");


        long time_num = (end_time - current_Time) / (1000 * 60);
        int hour = (int) time_num / 60;
        int min = (int) time_num - hour * 60;
        int num = 0;
        if (min < 0) {
            XLog.v("已过时间" + hour + ":" + min);
        } else if (min > 30 && hour == 0) {
            XLog.v("不足一小时满半小时" + hour + ":" + min);
            num = 1;
        } else if (hour > 1) {
            if (min > 30) {
                num = hour * 2 + 1;
            } else {
                num = hour * 2;
            }
        }

        List<ChoeseTimeBean> choeseTimeBeans = new ArrayList<ChoeseTimeBean>();
        List<ChoeseTimeContentBean> choeseTimeContentBeans = new ArrayList<ChoeseTimeContentBean>();
        for (int i = 1; i < num + 1; i++) {
            mCalendar.add(Calendar.MINUTE, 30);
            int Hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int Minut = mCalendar.get(Calendar.MINUTE);
            String str_hour2 = Hour < 10 ? "0" + Hour : String.valueOf(Hour);
            String str_minut2 = Minut < 10 ? "0" + Minut : String.valueOf(Minut);
            if (i == 1) {
                if (mealFee.compareTo(BigDecimal.ZERO) == -1) {
                    choeseTimeContentBeans.add(new ChoeseTimeContentBean("立即预定|" + str_hour + ":" + str_minut, mCalendar.getTime(), CommUtil.getPriceString(mealFee), i));
                } else {
                    choeseTimeContentBeans.add(new ChoeseTimeContentBean("尽快送达|预计" + str_hour + ":" + str_minut, mCalendar.getTime(), CommUtil.getPriceString(mealFee), i));
                }
            } else {
                choeseTimeContentBeans.add(new ChoeseTimeContentBean(str_hour2 + ":" + str_minut2, mCalendar.getTime(), CommUtil.getPriceString(mealFee), i));
            }
        }

        choeseTimeContentBeans.get(0).setChecked(true);
        ChoeseTimeBean choeseTimeBean = new ChoeseTimeBean("今日（" + XDateUtils.getWeek(XDateUtils.getCurrentDate()) + ")", choeseTimeContentBeans, 1);
        choeseTimeBeans.add(choeseTimeBean);
        choeseTimeBeans.get(0).setChecked(true);
        return choeseTimeBeans;

    }

    public static int isOpen(String startTime, String endTime) {
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(XDateUtils.string2Millis(XDateUtils.getCurrentDate()));
        int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts = mCalendar.get(Calendar.MINUTE);
        String str_hour = mHour < 10 ? "0" + mHour : String.valueOf(mHour);
        String str_minut = mMinuts < 10 ? "0" + mMinuts : String.valueOf(mMinuts);
        String currentTime = str_hour + ":" + str_minut;
        long current_Time = XDateUtils.string2Millis(currentTime, "HH:mm");

        long start_time = XDateUtils.string2Millis(startTime, "HH:mm");
        long end_time = XDateUtils.string2Millis(endTime, "HH:mm");
        if (current_Time < start_time) {
            return 1;
        } else if (current_Time > end_time) {
            return 2;
        } else {
            return 0;
        }


    }

    public static String listToSpecString(LinkedList<String> specList) {
        String spec = "";
        if (XEmptyUtils.isEmpty(specList)) {
            spec = "";
        } else {
            spec = specList.toString().replace(",", "/");
            spec = spec.substring(1, spec.length() - 1);
        }
        return spec;

    }

    public static String getGender(String gender) {
        switch (gender) {
            case "MALE":
                return "先生";
            case "FEMALE":
                return "女士";
            default:
                return "";
        }
    }


    public static Object getEvaStar(Context context, int num) {
        int drawable = 0;
        switch (num) {
            case 0:
                drawable = R.drawable.ic_eva_leve0;
                break;
            case 1:
                drawable = R.drawable.ic_eva_leve1;
                break;
            case 2:
                drawable = R.drawable.ic_eva_leve2;
                break;
            case 3:
                drawable = R.drawable.ic_eva_leve3;
                break;
            case 4:
                drawable = R.drawable.ic_eva_leve4;
                break;
            case 5:
                drawable = R.drawable.ic_eva_leve5;
                break;

        }
        return drawable;

    }

    public static List<NumBean> getManNum() {
        List<NumBean> datas = new ArrayList<NumBean>();
        datas.add(new NumBean("1人", 1, true));
        datas.add(new NumBean("2人", 2, false));
        datas.add(new NumBean("3人", 3, false));
        datas.add(new NumBean("4人", 4, false));
        datas.add(new NumBean("5人", 5, false));
        datas.add(new NumBean("6人", 6, false));
        datas.add(new NumBean("7人", 7, false));
        datas.add(new NumBean("8人", 8, false));
        datas.add(new NumBean("9人", 9, false));
        datas.add(new NumBean("10人以上", 10, false));
        return datas;
    }

    public static String getDistance(BigDecimal distance) {
        String str = "";
        int int_distance = distance.intValue();
        DecimalFormat df = new DecimalFormat("0.0");

        if (int_distance < 1000) {
            str = int_distance + "m";
        } else {

            distance = distance.divide(new BigDecimal(1000));
            str = df.format(distance) + "km";

        }

        return str;
    }

    public static StoreActivityShowTextBean getCouponText(Context context, String type) {
        StoreActivityShowTextBean storeActivityShowTextBean = new StoreActivityShowTextBean();
        switch (type) {
            case Constants.SHOP_ACTIVE_SALE:
                storeActivityShowTextBean.setAcitvityColor(R.drawable.ic_coupon_discount);
                storeActivityShowTextBean.setActivityType("折");
                break;
            case Constants.SHOP_ACTIVE_FIRST:
                storeActivityShowTextBean.setAcitvityColor(R.drawable.ic_coupon_first);
                storeActivityShowTextBean.setActivityType("首");
                break;
            case Constants.SHOP_ACTIVE_DELGOLD:
                storeActivityShowTextBean.setAcitvityColor(R.drawable.ic_coupon_reduce);
                storeActivityShowTextBean.setActivityType("减");
                break;
            case Constants.SHOP_ACTIVE_COMPLIMENTARY:
                storeActivityShowTextBean.setAcitvityColor(R.drawable.ic_coupon_coupon);
                storeActivityShowTextBean.setActivityType("惠");
                break;
            case Constants.SHOP_ACTIVE_SPECIALPRICES:
                storeActivityShowTextBean.setAcitvityColor(R.drawable.ic_coupon_spec);
                storeActivityShowTextBean.setActivityType("特");
                break;
            case Constants.SHOP_ACTIVE_SPECIFIC:
                storeActivityShowTextBean.setAcitvityColor(R.drawable.ic_coupon_coupon);
                storeActivityShowTextBean.setActivityType("惠");
                break;
        }
        return storeActivityShowTextBean;
    }

    /**
     * 获取优惠活动icon
     *
     * @param context
     * @param type
     * @return
     */

    public static Drawable getCouponLsogo(Context context, String type) {
        Drawable drawable = null;
        switch (type) {
            case Constants.SHOP_ACTIVE_SALE:
                drawable = context.getResources().getDrawable(R.drawable.ic_coupon_coupon);
                break;
            case Constants.SHOP_ACTIVE_FIRST:
                drawable = context.getResources().getDrawable(R.drawable.ic_coupon_first);
                break;
            case Constants.SHOP_ACTIVE_DELGOLD:
                drawable = context.getResources().getDrawable(R.drawable.ic_coupon_reduce);
                break;
            case Constants.SHOP_ACTIVE_COMPLIMENTARY:
                drawable = context.getResources().getDrawable(R.drawable.ic_coupon_coupon);
                break;
            case Constants.SHOP_ACTIVE_SPECIALPRICES:
                drawable = context.getResources().getDrawable(R.drawable.ic_coupon_spec);
                break;
            case Constants.SHOP_ACTIVE_SPECIFIC:
                drawable = context.getResources().getDrawable(R.drawable.ic_coupon_spec);
                break;
        }
        return drawable;
    }

    /**
     * 获取通知图标
     *
     * @param context
     * @param type
     * @return
     */

    public static Drawable getNoticeLogo(Context context, String type) {
        Drawable drawable = null;
        if (XEmptyUtils.isEmpty(type)) {
            drawable = context.getResources().getDrawable(R.drawable.notice);

        } else {
            switch (type) {
                case Constants.NOTICE_ORDER_CONFIRM:
                    drawable = context.getResources().getDrawable(R.drawable.ic_notice_order_receive);
                    break;
                case Constants.NOTICE_ORDER_SHIPPING:
                    drawable = context.getResources().getDrawable(R.drawable.ic_notice_order_send);
                    break;
                case Constants.NOTICE_ORDER_DELIVERED:
                    drawable = context.getResources().getDrawable(R.drawable.ic_notice_order_complete);
                    break;
                case Constants.NOTICE_ORDER_CANCELLATION:
                    drawable = context.getResources().getDrawable(R.drawable.ic_notice_order_cancel);
                    break;
                case Constants.NOTICE_ORDER_CREATE:
                    drawable = context.getResources().getDrawable(R.drawable.ic_notice_create);
                    break;
                case Constants.NOTICE_ORDER_FINISHED:
                    drawable = context.getResources().getDrawable(R.drawable.ic_notice_finish);
                    break;
                default:
                    drawable = context.getResources().getDrawable(R.drawable.notice);
                    break;

            }
        }

        return drawable;
    }

    public static String getCityCode(String code) {
        return code.substring(0, 4) + "00";

    }

    public static String getgoodsRateApprise(BigDecimal num) {
        String v = "";
        int str_num = num.multiply(new BigDecimal(100)).intValue();
        v = str_num + "%";
        return v;
    }

    /**
     * 檢查Token
     *
     * @param context
     * @return
     */
    public static boolean checkToken(Context context) {
        if (getToken(context) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        return (String) SPUtils.get(context, "token", "");
    }

    /**
     * 设置Token
     *
     * @param context
     * @param token
     */
    public static void setToken(Context context, String token) {
        SPUtils.put(context, "token", token);
    }

    /**
     * 檢查定位
     *
     * @param context
     * @return
     */
    public static boolean checkLocation(Context context) {
        if (getLocation(context) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置定位信息
     *
     * @param context
     * @param locationBean
     */
    public static void setLocation(Context context, LocationBean locationBean) {
        XCaches.get(context).put("location", locationBean);

    }

    /**
     * 获取定位信息
     *
     * @param context
     */
    public static LocationBean getLocation(Context context) {
        return (LocationBean) XCaches.get(context).getAsObject("location");
    }

}
