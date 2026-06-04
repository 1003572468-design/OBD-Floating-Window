package com.ileja.framework.service.core;

import android.app.Application;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import com.ileja.aibase.common.AILog;
import com.ileja.aicore.data.IDataCallback;
import com.ileja.aicore.data.IDataRepertory;
import com.ileja.core.data.DataCallback;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class HFDataService extends HFService {
    private static final String CLS_NAME = "com.ileja.aicore.data.DataRepertoryService";
    private static final String PKG_NAME = "com.ileja.aicore";

    /* JADX INFO: renamed from: d */
    IDataRepertory f6364d;
    private SparseArray<ArrayList<RemoteDataCallback>> listenerMap = new SparseArray<>();
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.core.HFDataService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4028d(HudSystem.TAG, "data service connected");
            HFDataService.this.f6364d = IDataRepertory.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFDataService hFDataService = HFDataService.this;
            hFDataService.f6364d = null;
            hFDataService.listenerMap.clear();
            AILog.m4028d(HudSystem.TAG, "data service disconnected");
        }
    };

    private class RemoteDataCallback extends IDataCallback.Stub {
        private DataCallback callbackWeakReference;

        public RemoteDataCallback(HFDataService hFDataService, DataCallback dataCallback) {
            this.callbackWeakReference = dataCallback;
        }

        @Override // com.ileja.aicore.data.IDataCallback
        public void callback(String str, Bundle bundle) {
            if (get() == null || bundle == null) {
                return;
            }
            bundle.setClassLoader(RemoteDataCallback.class.getClassLoader());
            this.callbackWeakReference.callback(str, bundle);
        }

        public DataCallback get() {
            DataCallback dataCallback = this.callbackWeakReference;
            if (dataCallback != null) {
                return dataCallback;
            }
            return null;
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4028d(HudSystem.TAG, "try to bind data service");
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        if (this.f6364d != null) {
            m4406g(application);
            this.listenerMap.clear();
        }
        m4402c();
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFDataService;
    }

    public Bundle getValue(String str) {
        IDataRepertory iDataRepertory = this.f6364d;
        Bundle bundle = null;
        if (iDataRepertory != null) {
            try {
                bundle = iDataRepertory.get(str);
                if (bundle != null) {
                    bundle.setClassLoader(getClass().getClassLoader());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            AILog.m4040w(HudSystem.TAG, "getValue(" + str + ") failed");
        }
        return bundle;
    }

    public synchronized void register(final String str, final DataCallback dataCallback) {
        String str2 = "com.ileja.aicore.data.DataRepertoryService[register_action]" + str + dataCallback.hashCode();
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.core.HFDataService.3
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = str.hashCode();
                ArrayList arrayList = (ArrayList) HFDataService.this.listenerMap.get(iHashCode);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    HFDataService.this.listenerMap.put(iHashCode, arrayList);
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (dataCallback == ((RemoteDataCallback) it.next())) {
                        return;
                    }
                }
                RemoteDataCallback remoteDataCallback = new RemoteDataCallback(HFDataService.this, dataCallback);
                arrayList.add(remoteDataCallback);
                try {
                    AILog.m4028d(HudSystem.TAG, "register filter:" + str + " ,callback:" + dataCallback);
                    if (HFDataService.this.f6364d != null) {
                        HFDataService.this.f6364d.register(str, remoteDataCallback);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        pendingAction.setKey(str2);
        if (this.f6364d != null) {
            AILog.m4028d(HudSystem.TAG, "real register data service");
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void send(final String str, final Bundle bundle, final boolean z) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.core.HFDataService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    AILog.m4028d(HudSystem.TAG, "send, data service send filter:" + str + " ,data:" + bundle + " ,notify: " + z);
                    if (HFDataService.this.f6364d != null) {
                        HFDataService.this.f6364d.send(str, bundle, z);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.f6364d != null) {
            AILog.m4028d(HudSystem.TAG, "real send, data service send filter:" + str + " ,data:" + bundle + " ,notify: " + z);
            pendingAction.run();
            return;
        }
        AILog.m4028d(HudSystem.TAG, "queue send, data service send filter:" + str + " ,data:" + bundle + " ,notify: " + z);
        m4403d(pendingAction);
    }

    public synchronized void unregister(final String str, final DataCallback dataCallback) {
        String str2 = "com.ileja.aicore.data.DataRepertoryService[register_action]" + str + dataCallback.hashCode();
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.core.HFDataService.4
            @Override // java.lang.Runnable
            public void run() {
                ArrayList<RemoteDataCallback> arrayList = (ArrayList) HFDataService.this.listenerMap.get(str.hashCode());
                if (arrayList != null) {
                    for (RemoteDataCallback remoteDataCallback : arrayList) {
                        if (dataCallback == remoteDataCallback) {
                            break;
                        }
                    }
                    remoteDataCallback = null;
                } else {
                    remoteDataCallback = null;
                }
                if (remoteDataCallback != null) {
                    arrayList.remove(remoteDataCallback);
                    try {
                        AILog.m4028d(HudSystem.TAG, "unregister filter:" + str + " ,callback:" + dataCallback);
                        if (HFDataService.this.f6364d != null) {
                            HFDataService.this.f6364d.unregister(str, remoteDataCallback);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        pendingAction.setKey(str2);
        if (this.f6364d != null) {
            AILog.m4028d(HudSystem.TAG, "real unregister data service");
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}