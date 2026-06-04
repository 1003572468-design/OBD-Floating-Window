package com.ileja.aibase.common;

import com.ileja.aibase.common.logger.LogLevel;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class AITimer extends Timer {
    private static final String TAG = "AITimer";
    private static Map<String, TimerTask> mTaskMap = new HashMap();
    private static AITimer mTimer;

    private AITimer() {
        super("aitimer_used_thread");
    }

    public static AITimer getInstance() {
        if (mTimer == null) {
            mTimer = new AITimer();
        }
        return mTimer;
    }

    public void cancelTimer(String str) {
        AILog.m4035i(TAG, "cancelTimer,taskName:" + str, LogLevel.RELEASE);
        TimerTask timerTask = mTaskMap.get(str);
        if (timerTask != null) {
            timerTask.cancel();
            mTaskMap.remove(str);
        }
    }

    public void startTimer(TimerTask timerTask, String str, long j) {
        AILog.m4035i(TAG, "startTimer,taskName:" + str, LogLevel.RELEASE);
        TimerTask timerTask2 = mTaskMap.get(str);
        if (timerTask2 != null) {
            timerTask2.cancel();
            mTaskMap.remove(str);
        }
        mTaskMap.put(str, timerTask);
        if (timerTask != null) {
            try {
                mTimer.schedule(timerTask, j);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTimer(TimerTask timerTask, String str, int i, int i2) {
        AILog.m4035i(TAG, "startTimer,timer num" + mTaskMap.size(), LogLevel.RELEASE);
        TimerTask timerTask2 = mTaskMap.get(str);
        if (timerTask2 != null) {
            timerTask2.cancel();
            mTaskMap.remove(str);
        }
        mTaskMap.put(str, timerTask);
        if (timerTask != null) {
            try {
                mTimer.schedule(timerTask, i, i2);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}