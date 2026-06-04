package com.ileja.aibase.common;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class CalcSunRiseAndSunSetTools {
    private static final double UTo = 180.0d;
    private static int[] days_of_month_1 = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static int[] days_of_month_2 = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /* JADX INFO: renamed from: h */
    private static final double f5879h = -0.833d;

    public static double GHA(double d, double d2, double d3) {
        return ((((d - 180.0d) - (Math.sin((d2 * 3.141592653589793d) / 180.0d) * 1.915d)) - (Math.sin(((d2 * 2.0d) * 3.141592653589793d) / 180.0d) * 0.02d)) + (Math.sin(((2.0d * d3) * 3.141592653589793d) / 180.0d) * 2.466d)) - (Math.sin(((d3 * 4.0d) * 3.141592653589793d) / 180.0d) * 0.053d);
    }

    public static double G_sun(double d) {
        return (d * 35999.05d) + 357.528d;
    }

    public static double L_sun(double d) {
        return (d * 36000.77d) + 280.46d;
    }

    public static double UT_rise(double d, double d2, double d3, double d4) {
        return d - ((d2 + d3) + d4);
    }

    public static double UT_set(double d, double d2, double d3, double d4) {
        return d - ((d2 + d3) - d4);
    }

    public static int Zone(double d) {
        return d >= 0.0d ? ((int) (d / 15.0d)) + 1 : ((int) (d / 15.0d)) - 1;
    }

    public static int days(int i, int i2, int i3) {
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 2000; i6 < i; i6++) {
            i5 = leap_year(i6) ? i5 + 366 : i5 + 365;
        }
        if (leap_year(i)) {
            while (i4 < i2 - 1) {
                i5 += days_of_month_2[i4];
                i4++;
            }
        } else {
            while (i4 < i2 - 1) {
                i5 += days_of_month_1[i4];
                i4++;
            }
        }
        return i5 + i3;
    }

    /* JADX INFO: renamed from: e */
    public static double m4044e(double d, double d2, double d3) {
        double d4 = (d2 * 3.141592653589793d) / 180.0d;
        double d5 = (d3 * 3.141592653589793d) / 180.0d;
        return Math.acos((Math.sin((d * 3.141592653589793d) / 180.0d) - (Math.sin(d4) * Math.sin(d5))) / (Math.cos(d4) * Math.cos(d5))) * 57.29577951308232d;
    }

    public static double earth_tilt(double d) {
        return 23.4393d - (d * 0.013d);
    }

    public static double ecliptic_longitude(double d, double d2) {
        return d + (Math.sin((d2 * 3.141592653589793d) / 180.0d) * 1.915d) + (Math.sin(((d2 * 2.0d) * 3.141592653589793d) / 180.0d) * 0.02d);
    }

    public static void getSunrise(double d, double d2, int i, int i2, int i3) {
        double dResult_rise = result_rise(UT_rise(180.0d, GHA(180.0d, G_sun(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))), d, m4044e(f5879h, d2, sun_deviation(earth_tilt(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))))), 180.0d, d, d2, i, i2, i3);
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Sunrise is: ");
        double d3 = (dResult_rise / 15.0d) + 8.0d;
        int i4 = (int) d3;
        sb.append(i4);
        sb.append(":");
        sb.append((int) ((d3 - ((double) i4)) * 60.0d));
        sb.append(" .\n");
        printStream.println(sb.toString());
    }

    public static int getSunriseTime(double d, double d2, int i, int i2, int i3) {
        double dResult_rise = (result_rise(UT_rise(180.0d, GHA(180.0d, G_sun(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))), d, m4044e(f5879h, d2, sun_deviation(earth_tilt(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))))), 180.0d, d, d2, i, i2, i3) / 15.0d) + 8.0d;
        int i4 = (int) dResult_rise;
        return (i4 * 60) + ((int) ((dResult_rise - ((double) i4)) * 60.0d));
    }

    public static void getSunset(double d, double d2, int i, int i2, int i3) {
        double dResult_set = result_set(UT_set(180.0d, GHA(180.0d, G_sun(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))), d, m4044e(f5879h, d2, sun_deviation(earth_tilt(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))))), 180.0d, d, d2, i, i2, i3);
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Sunset is: ");
        double d3 = (dResult_set / 15.0d) + 8.0d;
        int i4 = (int) d3;
        sb.append(i4);
        sb.append(":");
        sb.append((int) ((d3 - ((double) i4)) * 60.0d));
        sb.append(" .\n");
        printStream.println(sb.toString());
    }

    public static int getSunsetTime(double d, double d2, int i, int i2, int i3) {
        double dResult_set = (result_set(UT_set(180.0d, GHA(180.0d, G_sun(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))), d, m4044e(f5879h, d2, sun_deviation(earth_tilt(t_century(days(i, i2, i3), 180.0d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), 180.0d)), G_sun(t_century(days(i, i2, i3), 180.0d)))))), 180.0d, d, d2, i, i2, i3) / 15.0d) + 8.0d;
        int i4 = (int) dResult_set;
        return (i4 * 60) + ((int) ((dResult_set - ((double) i4)) * 60.0d));
    }

    static boolean isDay(double d, double d2, long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(j));
        int i = calendar.get(1);
        int i2 = calendar.get(2) + 1;
        int i3 = calendar.get(5);
        int i4 = calendar.get(11);
        int i5 = calendar.get(12);
        if (i < 2020) {
            return true;
        }
        int i6 = (i4 * 60) + i5;
        return i6 >= getSunriseTime(d, d2, i, i2, i3) && i6 < getSunsetTime(d, d2, i, i2, i3);
    }

    public static boolean leap_year(int i) {
        if (i % 400 != 0) {
            return i % 100 != 0 && i % 4 == 0;
        }
        return true;
    }

    public static double result_rise(double d, double d2, double d3, double d4, int i, int i2, int i3) {
        if ((d >= d2 ? d - d2 : d2 - d) < 0.1d) {
            return d;
        }
        double dUT_rise = UT_rise(d, GHA(d, G_sun(t_century(days(i, i2, i3), d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), d)), G_sun(t_century(days(i, i2, i3), d)))), d3, m4044e(f5879h, d4, sun_deviation(earth_tilt(t_century(days(i, i2, i3), d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), d)), G_sun(t_century(days(i, i2, i3), d))))));
        result_rise(dUT_rise, d, d3, d4, i, i2, i3);
        return dUT_rise;
    }

    public static double result_set(double d, double d2, double d3, double d4, int i, int i2, int i3) {
        if ((d >= d2 ? d - d2 : d2 - d) < 0.1d) {
            return d;
        }
        double dUT_set = UT_set(d, GHA(d, G_sun(t_century(days(i, i2, i3), d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), d)), G_sun(t_century(days(i, i2, i3), d)))), d3, m4044e(f5879h, d4, sun_deviation(earth_tilt(t_century(days(i, i2, i3), d)), ecliptic_longitude(L_sun(t_century(days(i, i2, i3), d)), G_sun(t_century(days(i, i2, i3), d))))));
        result_set(dUT_set, d, d3, d4, i, i2, i3);
        return dUT_set;
    }

    public static double sun_deviation(double d, double d2) {
        return Math.asin(Math.sin(d * 0.017453292519943295d) * Math.sin(d2 * 0.017453292519943295d)) * 57.29577951308232d;
    }

    public static double t_century(int i, double d) {
        return (((double) i) + (d / 360.0d)) / 36525.0d;
    }
}