package com.ileja.aibase.common;

import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes.dex */
public class UnitConversionUtil {

    /* JADX INFO: renamed from: df */
    public static DecimalFormat f5896df = new DecimalFormat("#.#");

    public static String format(double d) {
        return f5896df.format(d);
    }

    public static int[] formatMeter(float f) {
        int[] iArr = new int[2];
        if (f >= 1000.0f) {
            iArr[0] = (int) (f / 1000.0f);
        }
        iArr[1] = (int) (f % 1000.0f);
        return iArr;
    }

    public static String formatMeterToCnStr(float f) {
        return formatMeterToStr(f, "公里", "米");
    }

    public static String formatMeterToEnStr(float f) {
        return formatMeterToStr(f, "kilometer", "meter");
    }

    public static String formatMeterToEnUnitStr(float f) {
        return formatMeterToStr(f, "km", "m");
    }

    public static String formatMeterToStr(float f, String str, String str2) {
        int[] meter = formatMeter(f);
        int i = meter[0];
        int i2 = meter[1];
        StringBuffer stringBuffer = new StringBuffer();
        if (i > 0) {
            stringBuffer.append(format(i + (i2 / 1000.0f)));
            stringBuffer.append(str);
        } else {
            stringBuffer.append(i2);
            stringBuffer.append(str2);
        }
        return stringBuffer.toString();
    }

    public static int[] formatSecond(int i) {
        int i2;
        int[] iArr = new int[3];
        if (i >= 3600) {
            iArr[0] = i / 3600;
            int i3 = i % 3600;
            if (i3 != 0 && (i2 = i3 / 60) != 0) {
                iArr[1] = i2;
            }
        } else if (i >= 60) {
            iArr[1] = i / 60;
        }
        iArr[2] = i % 60;
        return iArr;
    }

    public static String formatSecondToCnStr(int i) {
        return formatSecondToStr(i, "小时", "分钟", "秒");
    }

    public static String formatSecondToEnStr(int i) {
        return formatSecondToStr(i, "hours", "minutes", "seconds");
    }

    public static String formatSecondToStr(int i, String str, String str2, String str3) {
        int[] second = formatSecond(i);
        int i2 = second[0];
        int i3 = second[1];
        int i4 = second[2];
        StringBuffer stringBuffer = new StringBuffer();
        if (i2 <= 0 || i3 <= 0) {
            if (i2 > 0) {
                stringBuffer.append(i2);
                stringBuffer.append(str);
            }
            if (i3 > 0) {
                stringBuffer.append(i3);
                stringBuffer.append(str2);
            }
            if (i2 == 0 && i3 == 0) {
                stringBuffer.append(i4);
                stringBuffer.append(str3);
            }
        } else {
            stringBuffer.append(i2);
            stringBuffer.append(str);
            stringBuffer.append(i3);
            stringBuffer.append(str2);
        }
        return stringBuffer.toString();
    }

    public static String formatSpeedToCnStr(float f) {
        return formatSpeedToStr(f, "公里/小时");
    }

    public static String formatSpeedToEnStr(float f) {
        return formatSpeedToStr(f, "km/h");
    }

    public static String formatSpeedToStr(float f, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(format(f));
        stringBuffer.append(str);
        return stringBuffer.toString();
    }
}