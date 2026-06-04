package com.ileja.framework.service.core;

import android.app.Application;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aicore.log.ILogService;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFLogService extends HFService {
    private static final String CLS_NAME = "com.ileja.aicore.log.LogService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private ILogService logService;
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.core.HFLogService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4028d(HudSystem.TAG, "log service connected");
            HFLogService.this.logService = ILogService.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFLogService.this.logService = null;
            AILog.m4028d(HudSystem.TAG, "log service disconnected");
        }
    };

    public void addLogAsyn(final Bundle bundle) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.core.HFLogService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFLogService.this.logService.addLogAsny(bundle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.logService != null) {
            pendingAction.run();
        } else {
            m4403d(pendingAction);
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4028d(HudSystem.TAG, "try to bind log service");
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        if (this.logService != null) {
            m4406g(application);
        }
        m4402c();
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFLogService;
    }

    public void setEnable(final boolean z) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.core.HFLogService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFLogService.this.logService.enable(z);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicore.log.LogService[enable_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.logService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void setMaxLen(final long j) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.core.HFLogService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFLogService.this.logService.setMaxLen(j);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicore.log.LogService[len_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.logService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}