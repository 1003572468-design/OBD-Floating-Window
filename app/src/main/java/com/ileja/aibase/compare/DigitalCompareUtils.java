package com.ileja.aibase.compare;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class DigitalCompareUtils {
    private static final String TAG = "DigitalCompareUtils";

    public static boolean compareDouble(double d, double d2, int i) {
        if (d == d2) {
            return true;
        }
        double d3 = i;
        int iPow = (int) (d * Math.pow(10.0d, d3));
        int iPow2 = (int) (d2 * Math.pow(10.0d, d3));
        Log.d(TAG, "compareDouble: scaleD1=" + iPow + " ,scaleD2=" + iPow2);
        return iPow == iPow2;
    }
}