package com.ileja.framework.service;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.ailbs.utils.LBSConfig;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.PendingAction;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class HFService {

    /* JADX INFO: renamed from: b */
    protected Application f6334b;

    /* JADX INFO: renamed from: c */
    protected HFServiceCallback f6335c;
    private HFServiceConnection serviceConnection;

    /* JADX INFO: renamed from: a */
    protected Handler f6333a = new Handler(Looper.getMainLooper());
    private ConcurrentHashMap<String, PendingAction> pendingActions = new ConcurrentHashMap<>();
    private HFServiceState serviceState = HFServiceState.HFInit;
    private boolean isInExecActions = false;
    private BindFailedRunnable mBindFailedRunnable = new BindFailedRunnable();

    private class BindFailedRunnable implements Runnable {
        private static final int TIMEOUT = 30000;
        private final int MAXCOUNT;
        private int retry;

        private BindFailedRunnable() {
            this.MAXCOUNT = 5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.retry = 0;
            AILog.m4030e(HudSystem.TAG, "reset bind retry count. " + HFService.this.getName());
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.retry >= 5) {
                AILog.m4031e(HudSystem.TAG, "bind Failed timeout!!!! " + HFService.this.getName(), LogLevel.RELEASE);
                return;
            }
            AILog.m4031e(HudSystem.TAG, "bind timeout, retry:" + this.retry + " ." + HFService.this.getName(), LogLevel.RELEASE);
            HFService.this.m4405f(HFServiceState.HFInit);
            this.retry = this.retry + 1;
        }
    }

    private class HFServiceConnection implements ServiceConnection {
        private HFServiceCallback callback;

        public HFServiceConnection(HFServiceCallback hFServiceCallback) {
            this.callback = hFServiceCallback;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HFService.this.m4405f(HFServiceState.HFStart);
            HFService.this.stopBindTimer(true);
            HFServiceCallback hFServiceCallback = this.callback;
            if (hFServiceCallback != null) {
                hFServiceCallback.onServiceConnected(componentName, iBinder);
            }
            HFServiceCallback hFServiceCallback2 = HFService.this.f6335c;
            if (hFServiceCallback2 != null) {
                hFServiceCallback2.onServiceConnected(componentName, iBinder);
            }
            HFService.this.m4404e();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            HFService.this.m4405f(HFServiceState.HFStop);
            HFService.this.stopBindTimer(true);
            HFServiceCallback hFServiceCallback = this.callback;
            if (hFServiceCallback != null) {
                hFServiceCallback.onServiceDisconnected(componentName);
            }
            HFServiceCallback hFServiceCallback2 = HFService.this.f6335c;
            if (hFServiceCallback2 != null) {
                hFServiceCallback2.onServiceDisconnected(componentName);
            }
            HFService.this.f6333a.post(new Runnable() { // from class: com.ileja.framework.service.HFService.HFServiceConnection.1
                @Override // java.lang.Runnable
                public void run() {
                    HFService hFService = HFService.this;
                    hFService.create(hFService.f6334b);
                }
            });
        }
    }

    public enum HFServiceName {
        HFLocationService,
        HFEDogService,
        HFBeepPlayerService,
        HFQueryService,
        HFRouteService,
        HFDataService,
        HFObdService,
        HFIpMsgService,
        HFLogService,
        HFFmTxService,
        HFPageAnalyseService,
        HFBluetoothExtService,
        HFMusicQueryService,
        HFMusicPlayService,
        HFTirePressureService
    }

    public enum HFServiceState {
        HFInit,
        HFBinding,
        HFStart,
        HFStop
    }

    private void startBindTimer() {
        this.f6333a.removeCallbacks(this.mBindFailedRunnable);
        this.f6333a.postDelayed(this.mBindFailedRunnable, LBSConfig.CommonConst.QUERY_TIMEOUT);
        AILog.m4028d(HudSystem.TAG, ">>start Bind Timer. " + getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopBindTimer(boolean z) {
        if (z) {
            this.mBindFailedRunnable.reset();
        }
        this.f6333a.removeCallbacks(this.mBindFailedRunnable);
        AILog.m4028d(HudSystem.TAG, "<<stop Bind Timer. " + getName());
    }

    /* JADX INFO: renamed from: b */
    protected boolean m4401b(Application application, String str, String str2, HFServiceCallback hFServiceCallback) {
        this.f6334b = application;
        m4405f(HFServiceState.HFBinding);
        startBindTimer();
        this.serviceConnection = new HFServiceConnection(hFServiceCallback);
        Intent intent = new Intent();
        intent.setClassName(str, str2);
        return application.bindService(intent, this.serviceConnection, 1);
    }

    /* JADX INFO: renamed from: c */
    protected void m4402c() {
        synchronized (this.pendingActions) {
            this.pendingActions.clear();
        }
    }

    public abstract void create(Application application);

    /* JADX INFO: renamed from: d */
    protected void m4403d(PendingAction pendingAction) {
        synchronized (this.pendingActions) {
            String key = pendingAction.getKey();
            PendingAction.PendingActionType type = pendingAction.getType();
            AILog.m4034i(HudSystem.TAG, "enqueue action key = " + key + ", type = " + type + ", state = " + getServiceState());
            if (type == PendingAction.PendingActionType.Replace && this.pendingActions.containsKey(key)) {
                this.pendingActions.remove(key);
            }
            this.pendingActions.put(key, pendingAction);
            if (getServiceState() == HFServiceState.HFStop || getServiceState() == HFServiceState.HFInit) {
                AILog.m4041w(HudSystem.TAG, "=========try rebind , state = " + getServiceState() + "=========", LogLevel.RELEASE);
                create(this.f6334b);
            }
        }
    }

    public abstract void destroy(Application application);

    /* JADX INFO: renamed from: e */
    protected void m4404e() {
        if (this.isInExecActions) {
            AILog.m4030e(HudSystem.TAG, "Recursive entry to executePendingActions");
            return;
        }
        synchronized (this.pendingActions) {
            if (this.pendingActions != null && this.pendingActions.size() != 0) {
                this.isInExecActions = true;
                Iterator<Map.Entry<String, PendingAction>> it = this.pendingActions.entrySet().iterator();
                while (it.hasNext()) {
                    it.next().getValue().run();
                }
                this.isInExecActions = false;
            }
        }
    }

    /* JADX INFO: renamed from: f */
    protected void m4405f(HFServiceState hFServiceState) {
        this.serviceState = hFServiceState;
    }

    /* JADX INFO: renamed from: g */
    protected void m4406g(Context context) {
        this.mBindFailedRunnable.reset();
        stopBindTimer(true);
        context.unbindService(this.serviceConnection);
    }

    public abstract HFServiceName getName();

    public final HFServiceState getServiceState() {
        return this.serviceState;
    }

    public void setRemoteServiceConnectionListener(HFServiceCallback hFServiceCallback) {
        this.f6335c = hFServiceCallback;
        AILog.m4029d(HudSystem.TAG, "setRemoteServiceConnectionListener, listener = " + hFServiceCallback, LogLevel.RELEASE);
    }
}