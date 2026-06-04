package com.ileja.framework.service.lbs;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.ailbs.bean.AILocation;
import com.ileja.ailbs.location.AILocationListener;
import com.ileja.ailbs.location.IAILocationManager;
import com.ileja.ailbs.location.listener.IAILocationListener;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public final class HFLocationService extends HFService {
    private static final String CLS_NAME = "com.ileja.ailbs.location.AILocationService";
    public static final String GPS_PROVIDER = "gps";
    public static final String LBS_PROVIDER = "lbs";
    public static final String MIX_PROVIDER = "mix";
    private static final String PKG_NAME = "com.ileja.aicore";
    private IAILocationManager iaiLocationManager;
    private SparseArray<IAILocationListener> listenerMap = new SparseArray<>();
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.lbs.HFLocationService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "location service connected", LogLevel.RELEASE);
            HFLocationService.this.iaiLocationManager = IAILocationManager.Stub.asInterface(iBinder);
            if (HFLocationService.this.iaiLocationManager != null) {
                try {
                    HFLocationService.this.iaiLocationManager.init();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFLocationService.this.iaiLocationManager = null;
            HFLocationService.this.listenerMap.clear();
            AILog.m4029d(HudSystem.TAG, "location service disconnected", LogLevel.RELEASE);
        }
    };

    private static class HFLocationRemoteListener extends IAILocationListener.Stub {
        private AILocationListener listener;

        public HFLocationRemoteListener(AILocationListener aILocationListener) {
            this.listener = aILocationListener;
        }

        @Override // com.ileja.ailbs.location.listener.IAILocationListener
        public void onFixSatellitesNumChange(int i) {
            AILocationListener aILocationListener = this.listener;
            if (aILocationListener != null) {
                aILocationListener.onFixSatellitesNumChange(i);
            }
        }

        @Override // com.ileja.ailbs.location.listener.IAILocationListener
        public void onGPSSignalLoss(int i, long j, double d, double d2, long j2) {
            AILocationListener aILocationListener = this.listener;
            if (aILocationListener != null) {
                aILocationListener.onGPSSignalLoss(i, j, d, d2, j2);
            }
        }

        @Override // com.ileja.ailbs.location.listener.IAILocationListener
        public void onLocationChange(AILocation aILocation) {
            AILocationListener aILocationListener = this.listener;
            if (aILocationListener != null) {
                aILocationListener.onLocationChange(aILocation);
            }
        }

        @Override // com.ileja.ailbs.location.listener.IAILocationListener
        public void onLocationTimeConsuming(long j) {
            AILocationListener aILocationListener = this.listener;
            if (aILocationListener != null) {
                aILocationListener.onLocationTimeConsuming(j);
            }
        }

        @Override // com.ileja.ailbs.location.listener.IAILocationListener
        public void onNmeaReceived(String str) {
            AILocationListener aILocationListener = this.listener;
            if (aILocationListener != null) {
                aILocationListener.onNmeaReceived(str);
            }
        }

        @Override // com.ileja.ailbs.location.listener.IAILocationListener
        public void onStatusChange(int i) {
            AILocationListener aILocationListener = this.listener;
            if (aILocationListener != null) {
                aILocationListener.onStatusChange(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void realRemoveLocationUpdate(AILocationListener aILocationListener) {
        AILog.m4029d(HudSystem.TAG, "real remove location action", LogLevel.RELEASE);
        int iHashCode = aILocationListener.hashCode();
        IAILocationListener iAILocationListener = this.listenerMap.get(iHashCode);
        if (iAILocationListener != null) {
            try {
                this.iaiLocationManager.removeLocationUpdate(iAILocationListener);
                this.listenerMap.remove(iHashCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void realRequestLocationUpdate(String str, AILocationListener aILocationListener) {
        AILog.m4029d(HudSystem.TAG, "real request location action, provider=" + str, LogLevel.RELEASE);
        int iHashCode = aILocationListener.hashCode();
        if (this.listenerMap.get(iHashCode) == null) {
            HFLocationRemoteListener hFLocationRemoteListener = new HFLocationRemoteListener(aILocationListener);
            try {
                this.iaiLocationManager.requestLocationUpdate(str, hFLocationRemoteListener);
                this.listenerMap.put(iHashCode, hFLocationRemoteListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to bind location service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        IAILocationManager iAILocationManager = this.iaiLocationManager;
        if (iAILocationManager != null) {
            try {
                iAILocationManager.destroy();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            m4406g(application);
            this.listenerMap.clear();
        }
        m4402c();
    }

    public AILocation getLastFixLocation() {
        IAILocationManager iAILocationManager = this.iaiLocationManager;
        if (iAILocationManager == null) {
            return null;
        }
        try {
            return iAILocationManager.getLastFixLocation();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFLocationService;
    }

    public void removeLocationUpdate(final AILocationListener aILocationListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFLocationService.3
            @Override // java.lang.Runnable
            public void run() {
                HFLocationService.this.realRemoveLocationUpdate(aILocationListener);
            }
        };
        pendingAction.setKey("com.ileja.ailbs.location.AILocationService[request_action]" + aILocationListener.hashCode());
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.iaiLocationManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void requestLocationUpdate(final String str, final AILocationListener aILocationListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFLocationService.2
            @Override // java.lang.Runnable
            public void run() {
                HFLocationService.this.realRequestLocationUpdate(str, aILocationListener);
            }
        };
        pendingAction.setKey("com.ileja.ailbs.location.AILocationService[request_action]" + aILocationListener.hashCode());
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.iaiLocationManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void setEnableNmeaLog(boolean z) {
        IAILocationManager iAILocationManager = this.iaiLocationManager;
        if (iAILocationManager != null) {
            try {
                iAILocationManager.setEnableNmeaLog(z);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}