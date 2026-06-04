package com.ileja.framework.service.lbs;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.ailbs.bean.AILocation;
import com.ileja.ailbs.bean.edog.EDogEventInfo;
import com.ileja.ailbs.edog.EDogEventListener;
import com.ileja.ailbs.edog.IEDogManager;
import com.ileja.ailbs.edog.listener.IEDogListener;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFEDogService extends HFService {
    private static final String CLS_NAME = "com.ileja.ailbs.edog.AIEDogService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private static final String TAG = "EDogService";
    private IEDogManager ieDogManager;
    private SparseArray<IEDogListener> listenerMap = new SparseArray<>();
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.lbs.HFEDogService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "edog service connected", LogLevel.RELEASE);
            HFEDogService.this.ieDogManager = IEDogManager.Stub.asInterface(iBinder);
            if (HFEDogService.this.ieDogManager != null) {
                try {
                    HFEDogService.this.ieDogManager.init();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFEDogService.this.ieDogManager = null;
            HFEDogService.this.listenerMap.clear();
            AILog.m4029d(HudSystem.TAG, "edog service disconnected", LogLevel.RELEASE);
        }
    };

    private class HFEDogRemoteListener extends IEDogListener.Stub {
        private EDogEventListener listener;

        public HFEDogRemoteListener(HFEDogService hFEDogService, EDogEventListener eDogEventListener) {
            this.listener = eDogEventListener;
        }

        @Override // com.ileja.ailbs.edog.listener.IEDogListener
        public void onEDogEvent(EDogEventInfo eDogEventInfo) {
            AILog.m4029d(HudSystem.TAG, "onEDogEvent, eDogEventInfo:" + eDogEventInfo, LogLevel.RELEASE);
            EDogEventListener eDogEventListener = this.listener;
            if (eDogEventListener != null) {
                eDogEventListener.onEDogEvent(eDogEventInfo);
                return;
            }
            AILog.m4031e(HudSystem.TAG, "onEDogEvent, eDogEventInfo:" + eDogEventInfo + " ,but listener is null!!!!", LogLevel.RELEASE);
        }

        @Override // com.ileja.ailbs.edog.listener.IEDogListener
        public void onEDogText(String str) {
            AILog.m4029d(HudSystem.TAG, "onEDogText, text:" + str, LogLevel.RELEASE);
            EDogEventListener eDogEventListener = this.listener;
            if (eDogEventListener != null) {
                eDogEventListener.onEDogText(str);
                return;
            }
            AILog.m4031e(HudSystem.TAG, "onEDogText, text:" + str + " ,but listener is null!!!!", LogLevel.RELEASE);
        }

        @Override // com.ileja.ailbs.edog.listener.IEDogListener
        public void onTip() {
            AILog.m4028d(HudSystem.TAG, "onTip");
            EDogEventListener eDogEventListener = this.listener;
            if (eDogEventListener != null) {
                eDogEventListener.onTip();
            } else {
                AILog.m4031e(HudSystem.TAG, "onTip ,but listener is null!!!!", LogLevel.RELEASE);
            }
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to bind edog service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        IEDogManager iEDogManager = this.ieDogManager;
        if (iEDogManager != null) {
            try {
                iEDogManager.destroy();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            m4406g(application);
            this.listenerMap.clear();
            m4402c();
        }
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFEDogService;
    }

    public void onLocationChange(final AILocation aILocation) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.8
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFEDogService.this.ieDogManager.onLocationChange(aILocation);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[location_change_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void registerEdogListener(final EDogEventListener eDogEventListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.6
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = eDogEventListener.hashCode();
                if (((IEDogListener) HFEDogService.this.listenerMap.get(iHashCode)) == null) {
                    HFEDogRemoteListener hFEDogRemoteListener = new HFEDogRemoteListener(HFEDogService.this, eDogEventListener);
                    try {
                        HFEDogService.this.ieDogManager.registerEDogEventListener(hFEDogRemoteListener);
                        HFEDogService.this.listenerMap.put(iHashCode, hFEDogRemoteListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[register_edog_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void setCameraState(final boolean z) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFEDogService.this.ieDogManager.setCameraEDogState(z);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[camera_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void setTrafficState(final boolean z) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFEDogService.this.ieDogManager.setTrafficEDogState(z);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[traffic_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void start() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFEDogService.this.ieDogManager.openEDogService();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[start_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void stop() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFEDogService.this.ieDogManager.closeEDogService();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[start_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void unregisterEdogListener(final EDogEventListener eDogEventListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFEDogService.7
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = eDogEventListener.hashCode();
                IEDogListener iEDogListener = (IEDogListener) HFEDogService.this.listenerMap.get(iHashCode);
                if (iEDogListener != null) {
                    try {
                        HFEDogService.this.ieDogManager.unregisterEDogEventListener(iEDogListener);
                        HFEDogService.this.listenerMap.remove(iHashCode);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        pendingAction.setKey("com.ileja.ailbs.edog.AIEDogService[register_edog_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ieDogManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}