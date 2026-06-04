package com.ileja.aibase.view;

import android.content.Context;
import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
public class ToastUtils {
    private static boolean ENABLE = false;
    private static Toast mToast;

    private ToastUtils() {
    }

    public static void cancel() {
        Toast toast;
        if (ENABLE && (toast = mToast) != null) {
            toast.cancel();
        }
    }

    private static void init(Context context, String str) {
        if (ENABLE) {
            mToast = Toast.makeText(context.getApplicationContext(), str, 0);
        }
    }

    public static synchronized void release() {
        if (ENABLE) {
            cancel();
            mToast = null;
        }
    }

    public static void showToast(Context context, String str) {
        if (ENABLE) {
            try {
                if (mToast == null) {
                    synchronized (ToastUtils.class) {
                        if (mToast == null) {
                            init(context, str);
                        }
                    }
                } else {
                    mToast.setText(str);
                }
                mToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}