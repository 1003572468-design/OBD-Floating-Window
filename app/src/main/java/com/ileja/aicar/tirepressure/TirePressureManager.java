package com.ileja.aicar.tirepressure;

import android.app.Application;
import android.os.RemoteException;
import com.honeywell.tire.TireManager;
import com.honeywell.tire.bean.TirePressure;
import com.honeywell.tire.util.FileSaveUtil;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.tire.listener.ITirePressureListener;
import com.ileja.ailbs.utils.LBSConfig;
import com.ileja.module.BaseModuleApplication;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.time.DateUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/* JADX INFO: loaded from: classes.dex */
public class TirePressureManager {
    private static String TAG = "TirePressureManager";

    /* JADX INFO: renamed from: a */
    TireManager f5963a;

    /* JADX INFO: renamed from: b */
    EventBus f5964b;

    /* JADX INFO: renamed from: c */
    ITirePressureListener f5965c;

    /* JADX INFO: renamed from: d */
    boolean f5966d;

    /* JADX INFO: renamed from: e */
    Timer f5967e;

    class RestartTimerTask extends TimerTask {
        RestartTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TirePressureManager.this.f5963a.stop();
            TirePressureManager.this.f5963a.start();
        }
    }

    private static class TirePressureManagerHolder {
        private static final TirePressureManager INSTANCE = new TirePressureManager();

        private TirePressureManagerHolder() {
        }
    }

    public static TirePressureManager getInstance() {
        return TirePressureManagerHolder.INSTANCE;
    }

    public void bindTireDevice(String str) {
        AILog.m4029d(TAG, "bindTireDevice " + str, LogLevel.RELEASE);
        this.f5963a.setCarName("default");
        String[] strArrSplit = str.split("&");
        for (int i = 0; i < strArrSplit.length; i++) {
            AILog.m4028d(TAG, "bindTireDevice : " + strArrSplit[i]);
            this.f5963a.bindTireDevice(i, strArrSplit[i]);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TirePressure tirePressure) {
        int type = tirePressure.getType();
        if (type == 0) {
            AILog.m4028d(TAG, "front left: " + tirePressure.toString());
            try {
                this.f5965c.onGetFLPressure(tirePressure.getPa(), tirePressure.getTempture());
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        if (type == 1) {
            AILog.m4028d(TAG, "front right: " + tirePressure.toString());
            try {
                this.f5965c.onGetFRPressure(tirePressure.getPa(), tirePressure.getTempture());
                return;
            } catch (RemoteException e2) {
                e2.printStackTrace();
                return;
            }
        }
        if (type == 2) {
            AILog.m4028d(TAG, "rear left: " + tirePressure.toString());
            try {
                this.f5965c.onGetLRPressure(tirePressure.getPa(), tirePressure.getTempture());
                return;
            } catch (RemoteException e3) {
                e3.printStackTrace();
                return;
            }
        }
        if (type != 3) {
            return;
        }
        AILog.m4028d(TAG, "rear right: " + tirePressure.toString());
        try {
            this.f5965c.onGetRRPressure(tirePressure.getPa(), tirePressure.getTempture());
        } catch (RemoteException e4) {
            e4.printStackTrace();
        }
    }

    public void start(ITirePressureListener iTirePressureListener) {
        if (!this.f5964b.isRegistered(this)) {
            this.f5964b.register(this);
        }
        if (this.f5966d) {
            AILog.m4029d(TAG, "tirescanservice has started, no need to start again", LogLevel.RELEASE);
            return;
        }
        this.f5967e = new Timer();
        this.f5965c = iTirePressureListener;
        this.f5963a.start();
        this.f5967e.scheduleAtFixedRate(new RestartTimerTask(), LBSConfig.CommonConst.QUERY_TIMEOUT, DateUtils.MILLIS_PER_MINUTE);
        this.f5966d = true;
    }

    public void stop() {
        if (this.f5966d) {
            this.f5967e.cancel();
        }
        this.f5965c = null;
        this.f5966d = false;
        this.f5963a.stop();
        if (this.f5964b.isRegistered(this)) {
            this.f5964b.unregister(this);
        }
        AILog.m4029d(TAG, "tirescanservice has stopped", LogLevel.RELEASE);
    }

    public void unbindTireDevice() {
        AILog.m4029d(TAG, "unbindTireDevice", LogLevel.RELEASE);
        this.f5963a.unbindTireDevice(0);
        this.f5963a.unbindTireDevice(1);
        this.f5963a.unbindTireDevice(2);
        this.f5963a.unbindTireDevice(3);
    }

    private TirePressureManager() {
        this.f5966d = false;
        this.f5963a = new TireManager();
        this.f5964b = EventBus.getDefault();
        this.f5963a.init((Application) BaseModuleApplication.getContext());
        FileSaveUtil.initDirectory();
    }
}