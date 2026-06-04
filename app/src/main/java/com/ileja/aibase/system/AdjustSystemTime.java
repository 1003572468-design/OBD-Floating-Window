package com.ileja.aibase.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import com.ileja.aibase.common.AILog;

/* JADX INFO: loaded from: classes.dex */
public class AdjustSystemTime {
    public static final String ACTION_RESET_SYSTEM_TIME = "com.aispeech.receiver.reset.time";
    public static final String EXTRA_KEY_TIME_MILLISECONDS = "KEY_TIME_MILLISECONDS";
    private static final String TAG = "AdjustSystemTime";

    public static void resetSysTime(Context context, int i, int i2) {
        Intent intent = new Intent(ACTION_RESET_SYSTEM_TIME);
        intent.putExtra("KEY_HOUR", i);
        intent.putExtra("KEY_MINUTE", i2);
        context.sendBroadcast(intent);
    }

    @SuppressLint({"WrongConstant"})
    public static void timeUpdateTrigger(Context context) {
        Intent intent = new Intent("com.android.server.NetworkTimeUpdateService.action.TIME_UPDATE_TRIGGER");
        intent.setClassName("com.android.settings", "com.aispeech.receiver.AISpeechReceiver");
        intent.setFlags(ViewCompat.MEASURED_STATE_TOO_SMALL);
        context.sendBroadcast(intent);
        AILog.m4028d(TAG, "timeUpdateTrigger");
    }

    public static void resetSysTime(Context context, long j) {
        Intent intent = new Intent(ACTION_RESET_SYSTEM_TIME);
        intent.putExtra(EXTRA_KEY_TIME_MILLISECONDS, j);
        context.sendBroadcast(intent);
        AILog.m4028d(TAG, "reset system time");
    }
}