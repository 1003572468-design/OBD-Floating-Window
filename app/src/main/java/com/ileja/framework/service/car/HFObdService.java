package com.ileja.framework.service.car;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.obd.IObdService;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFObdService extends HFService {
    private static final String CLS_NAME = "com.ileja.aicar.obd.AIObdService";
    private static final String DEVICESTATUS = "com.ileja.aicar.obd.AIObdService[open|close]";
    private static final String PKG_NAME = "com.ileja.aicore";
    private IObdService iObdService;
    private HFServiceCallback serviceCallback = new HFServiceCallback() { // from class: com.ileja.framework.service.car.HFObdService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HFObdService.this.iObdService = IObdService.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFObdService.this.iObdService = null;
        }
    };

    public void checkObdUpgrade(final String str) {
        AILog.m4029d(HudSystem.TAG, "checkObdUpgrade  upgradeReason=> " + str, LogLevel.RELEASE);
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFObdService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFObdService.this.iObdService.checkObdUpgrade(str);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicar.obd.AIObdServicecheckObdUpgrade");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.iObdService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void close() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFObdService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFObdService.this.iObdService.close();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey(DEVICESTATUS);
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.iObdService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4028d(HudSystem.TAG, "try to bind obd service");
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceCallback);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        if (this.iObdService != null) {
            m4406g(application);
        }
        m4402c();
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFObdService;
    }

    public void open() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFObdService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFObdService.this.iObdService.open();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey(DEVICESTATUS);
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.iObdService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void sendCommand(final String str) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFObdService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFObdService.this.iObdService.sendCommand(str);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey(str);
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.iObdService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}