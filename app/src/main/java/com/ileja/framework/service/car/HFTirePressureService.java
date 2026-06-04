package com.ileja.framework.service.car;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aicar.tire.ITirePressureService;
import com.ileja.aicar.tire.listener.ITirePressureListener;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;
import com.ileja.framework.service.telcomm.listener.TirePressureListener;

/* JADX INFO: loaded from: classes.dex */
public class HFTirePressureService extends HFService {
    private static final String CLS_NAME = "com.ileja.aicar.tirepressure.TirePressureService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private static final String TAG = "HFTirePressureService";
    private HFServiceCallback serviceCallback = new HFServiceCallback() { // from class: com.ileja.framework.service.car.HFTirePressureService.5
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HFTirePressureService.this.tirePressureService = ITirePressureService.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFTirePressureService.this.tirePressureService = null;
        }
    };
    private ITirePressureService tirePressureService;

    private class TirePressureListenerImp extends ITirePressureListener.Stub {

        /* JADX INFO: renamed from: a */
        TirePressureListener f6357a;

        public TirePressureListenerImp(HFTirePressureService hFTirePressureService, TirePressureListener tirePressureListener) {
            this.f6357a = tirePressureListener;
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetFLPressure(double d, int i) {
            this.f6357a.onGetFLPressure(d, i);
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetFRPressure(double d, int i) {
            this.f6357a.onGetFRPressure(d, i);
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetLRPressure(double d, int i) {
            this.f6357a.onGetLRPressure(d, i);
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetRRPressure(double d, int i) {
            this.f6357a.onGetRRPressure(d, i);
        }
    }

    public void bindTireDevice(final String str) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFTirePressureService.1
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFTirePressureService.TAG, "bindTireDevices: " + str);
                try {
                    HFTirePressureService.this.tirePressureService.bindTireDevice(str);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicar.tirepressure.TirePressureServicebindTireDevice");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.tirePressureService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceCallback);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFTirePressureService;
    }

    public void startDetectTirePressure(final TirePressureListener tirePressureListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFTirePressureService.3
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFTirePressureService.TAG, "requestTirePressure: ");
                try {
                    HFTirePressureService.this.tirePressureService.requestTirePressure(new TirePressureListenerImp(HFTirePressureService.this, tirePressureListener));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicar.tirepressure.TirePressureServicerequestTirePressure");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.tirePressureService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void stopTirePressure() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFTirePressureService.4
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFTirePressureService.TAG, "stopTirePressure: ");
                try {
                    HFTirePressureService.this.tirePressureService.stopTirePressure();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicar.tirepressure.TirePressureServicestopTirePressure");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.tirePressureService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void unbindTireDevice() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.car.HFTirePressureService.2
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFTirePressureService.TAG, "unbindTireDevice: ");
                try {
                    HFTirePressureService.this.tirePressureService.unbindTireDevice();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aicar.tirepressure.TirePressureServiceunbindTireDevice");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.tirePressureService != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}