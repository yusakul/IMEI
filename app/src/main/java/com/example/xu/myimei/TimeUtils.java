package com.example.xu.myimei;

/**
 * Created by heiyan on 2016/11/9.
 */

public class TimeUtils {

    private static String s_cur_day;
    private static long no_zero_cur_day;
    private static String s_total_hour;
    private static String s_cur_minute;
    private static String s_cur_seconds;

    public static String getTime(long time) {
        String s_time = "00小时00分00秒";
        if (time <= 0)
            return s_time;
        long total_seconds = time / 1000;
        long total_min = total_seconds / 60;
        long total_hour = total_min / 60;

        long cur_seconds = total_seconds % 60;
        long cur_minute = total_min % 60;
        long cur_hour = total_hour % 24;
        long cur_day = total_hour / 24;

        s_cur_day = cur_day + "";
        s_total_hour = cur_hour + "";
        s_cur_minute = cur_minute + "";
        s_cur_seconds = cur_seconds + "";
        no_zero_cur_day = cur_day;

        if (cur_day < 10)
            s_cur_day = "0" + s_cur_day;
        if (cur_hour < 10)
            s_total_hour = "0" + s_total_hour;
        if (cur_minute < 10)
            s_cur_minute = "0" + s_cur_minute;
        if (cur_seconds < 10)
            s_cur_seconds = "0" + s_cur_seconds;
        return s_cur_day + "" + s_total_hour + "" + s_cur_minute + "" + s_cur_seconds + "";
    }

    public static String getDay() {
        return s_cur_day;
    }

    public static long getNoZeroDay() {
        return no_zero_cur_day;
    }

    public static String getHour() {
        return s_total_hour;
    }

    public static String getMinute() {
        return s_cur_minute;
    }

    public static String getSeconds() {
        return s_cur_seconds;
    }
}
