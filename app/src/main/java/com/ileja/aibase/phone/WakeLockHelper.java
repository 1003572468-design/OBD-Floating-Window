package com.ileja.aibase.phone;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes.dex */
public class WakeLockHelper {
    public static PowerManager.WakeLock acquireBrightWakeLock(Context context) {
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(268435466, "carrobot:ileja-bright");
        wakeLockNewWakeLock.acquire();
        return wakeLockNewWakeLock;
    }

    public static PowerManager.WakeLock acquireKeepCPURunningWakeLockWhenScreenOff(Context context) {
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(536870913, "carrobot:keepRunning");
        wakeLockNewWakeLock.acquire();
        return wakeLockNewWakeLock;
    }

    public static void releaseBrightLockUnCheck(Context context) {
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(268435466, "carrobot:ileja-bright");
        if (wakeLockNewWakeLock.isHeld()) {
            wakeLockNewWakeLock.release();
        }
    }

    public static void releaseWakeLock(PowerManager.WakeLock wakeLock) {
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        wakeLock.release();
    }

    public static void setUnlocked(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.flags |= 4718592;
        window.setAttributes(attributes);
    }
}