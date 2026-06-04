package com.ileja.aibase.phone;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class MemoryRecycleUtils {
    /* JADX INFO: renamed from: gc */
    public static void m4069gc() {
        Runtime.getRuntime().gc();
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}