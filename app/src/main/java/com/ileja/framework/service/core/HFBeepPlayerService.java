package com.ileja.framework.service.core;

import android.app.Application;
import android.content.Context;
import android.media.SoundPool;
import android.util.SparseIntArray;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.ThreadPoolManager;
import com.ileja.framework.C1303R;
import com.ileja.framework.service.HFService;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class HFBeepPlayerService extends HFService {
    public static final int ALARM_DISTRACTION = 2000;
    private static final int CAPACITY = 9;
    private static final String TAG = "BeepPlayerService";
    public static final int TIP_CALL = 1009;
    public static final int TIP_CMD = 1007;
    public static final int TIP_GOODBYE = 3;
    public static final int TIP_NAVI_DOG = 4;
    public static final int TIP_RECORD_END = 2;
    public static final int TIP_SPEAK = 1;
    public static final int TIP_START = 1004;
    public static final int TIP_STOP = 1005;
    public static final int TIP_WEATHER = 1200;
    public static final int TIP_WX = 1003;
    public static final int TIP_WX_QR = 1101;
    public static final int TIP_WX_SEND = 1102;
    public static final int TIP_WX_TIP = 1103;
    private ReentrantLock mLock = new ReentrantLock();
    private SparseIntArray mSoundIdMap = new SparseIntArray(9);
    private SoundPool mSoundPool;

    /* JADX INFO: Access modifiers changed from: private */
    public void loadRes(Context context) {
        if (this.mSoundPool == null) {
            this.mSoundPool = new SoundPool(9, 3, 0);
            this.mSoundIdMap.clear();
            this.mSoundIdMap.append(1, this.mSoundPool.load(context, C1303R.raw.wakeup, 1));
            this.mSoundIdMap.append(2, this.mSoundPool.load(context, C1303R.raw.success, 1));
            this.mSoundIdMap.append(3, this.mSoundPool.load(context, C1303R.raw.no_input, 1));
            this.mSoundIdMap.append(4, this.mSoundPool.load(context, C1303R.raw.navi_warning, 1));
            this.mSoundIdMap.append(1003, this.mSoundPool.load(context, C1303R.raw.beep_03, 1));
            this.mSoundIdMap.append(1004, this.mSoundPool.load(context, C1303R.raw.beep_04, 1));
            this.mSoundIdMap.append(1005, this.mSoundPool.load(context, C1303R.raw.beep_05, 1));
            this.mSoundIdMap.append(1007, this.mSoundPool.load(context, C1303R.raw.beep_07, 1));
            this.mSoundIdMap.append(1009, this.mSoundPool.load(context, C1303R.raw.beep_09, 1));
            this.mSoundIdMap.append(1101, this.mSoundPool.load(context, C1303R.raw.qrcode_completed, 1));
            this.mSoundIdMap.append(1102, this.mSoundPool.load(context, C1303R.raw.sent_message, 1));
            this.mSoundIdMap.append(1103, this.mSoundPool.load(context, C1303R.raw.wx_tip, 1));
            this.mSoundIdMap.append(1200, this.mSoundPool.load(context, C1303R.raw.weather_tip, 1));
            this.mSoundIdMap.append(2000, this.mSoundPool.load(context, C1303R.raw.alarm_distraction, 1));
        }
    }

    private void loadSounds(final Context context) {
        ThreadPoolManager.getInstance().addAsyncTask(new Runnable() { // from class: com.ileja.framework.service.core.HFBeepPlayerService.1
            @Override // java.lang.Runnable
            public void run() {
                HFBeepPlayerService.this.mLock.lock();
                try {
                    HFBeepPlayerService.this.loadRes(context);
                    AILog.m4028d(HFBeepPlayerService.TAG, "load sounds, streamType=3");
                } finally {
                    HFBeepPlayerService.this.mLock.unlock();
                }
            }
        });
    }

    private void release() {
        ThreadPoolManager.getInstance().addAsyncTask(new Runnable() { // from class: com.ileja.framework.service.core.HFBeepPlayerService.2
            @Override // java.lang.Runnable
            public void run() {
                HFBeepPlayerService.this.mLock.lock();
                try {
                    if (HFBeepPlayerService.this.mSoundPool != null) {
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(2));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(3));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(4));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1003));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1004));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1005));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1007));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1009));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1101));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1102));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(1200));
                        HFBeepPlayerService.this.mSoundPool.unload(HFBeepPlayerService.this.mSoundIdMap.get(2000));
                        HFBeepPlayerService.this.mSoundPool.release();
                        HFBeepPlayerService.this.mSoundIdMap.clear();
                        HFBeepPlayerService.this.mSoundPool = null;
                    }
                } finally {
                    HFBeepPlayerService.this.mLock.unlock();
                }
            }
        });
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        loadSounds(application);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        release();
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFBeepPlayerService;
    }

    public void playBeep(Context context, int i) {
        this.mLock.lock();
        try {
            if (this.mSoundPool == null) {
                loadRes(context);
            }
            AILog.m4028d(TAG, "play beep prepared, id:" + i);
            this.mSoundPool.play(this.mSoundIdMap.get(i), 1.0f, 1.0f, 1, 0, 1.0f);
            AILog.m4028d(TAG, "play beep start, id:" + i);
        } finally {
            this.mLock.unlock();
        }
    }

    public void playBeepAsync(final Context context, final int i) {
        ThreadPoolManager.getInstance().addAsyncTask(new Runnable() { // from class: com.ileja.framework.service.core.HFBeepPlayerService.3
            @Override // java.lang.Runnable
            public void run() {
                HFBeepPlayerService.this.playBeep(context, i);
            }
        });
    }
}