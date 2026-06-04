package com.ileja.aibase.common;

import android.content.Context;
import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/* JADX INFO: loaded from: classes.dex */
public class DateUtil {
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA).format(new Date());
    }

    public static int getDateFields(long j, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        if (i == 1) {
            return calendar.get(1);
        }
        if (i == 2) {
            return calendar.get(2);
        }
        if (i != 5) {
            return 0;
        }
        return calendar.get(5);
    }

    public static String getDateStr(Context context) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(new Date()));
        sb.append(StringUtils.SPACE);
        switch (Calendar.getInstance().get(7)) {
            case 1:
                str = "周日";
                break;
            case 2:
                str = "周一";
                break;
            case 3:
                str = "周二";
                break;
            case 4:
                str = "周三";
                break;
            case 5:
                str = "周四";
                break;
            case 6:
                str = "周五";
                break;
            case 7:
                str = "周六";
                break;
            default:
                str = null;
                break;
        }
        sb.append(str);
        return sb.toString();
    }

    public static String getDateString(String str) {
        try {
            return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateString2(String str) {
        try {
            return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getDayTimeSpan(long j) {
        return j * DateUtils.MILLIS_PER_DAY;
    }

    public static String getNowDateString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getTimeFromMS(long j) {
        long j2 = j / 3600000;
        long j3 = (j % 3600000) / DateUtils.MILLIS_PER_MINUTE;
        long j4 = (j % DateUtils.MILLIS_PER_MINUTE) / 1000;
        return j2 > 0 ? String.format(Locale.US, "%d:%02d:%02d", Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4)) : String.format(Locale.US, "%02d:%02d", Long.valueOf(j3), Long.valueOf(j4));
    }

    public static long getTimeSecondFrom2011() {
        long time;
        Date date = new Date();
        try {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2011-01-01 00:00:00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            time = 0;
        }
        return (date.getTime() - time) / 1000;
    }

    public static String getTimeStr(Context context) {
        return (DateFormat.is24HourFormat(context) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh:mm", Locale.getDefault())).format(new Date());
    }

    public static String getTodayDateStr() {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
    }
}