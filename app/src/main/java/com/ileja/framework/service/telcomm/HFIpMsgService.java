package com.ileja.framework.service.telcomm;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aitelcomm.ipmsg.IDataReceiveListener;
import com.ileja.aitelcomm.ipmsg.IDataSendListener;
import com.ileja.aitelcomm.ipmsg.IDeviceStatusListener;
import com.ileja.aitelcomm.ipmsg.IIpMsgManager;
import com.ileja.aitelcomm.ipmsg.listener.DataReceiveListener;
import com.ileja.aitelcomm.ipmsg.listener.DataSendListener;
import com.ileja.aitelcomm.ipmsg.listener.DeviceStatusListener;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFIpMsgService extends HFService {
    private static final String CLS_NAME = "com.ileja.aitelcomm.ipmsg.IpMsgService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private static final String TAG = "HFIpMsgService";
    private IIpMsgManager ipMsgManager;
    private SparseArray<IDataReceiveListener> dataReceiveListenerMap = new SparseArray<>();
    private SparseArray<IDeviceStatusListener> deviceStatusListenerMap = new SparseArray<>();
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "ipmsg service connected", LogLevel.RELEASE);
            HFIpMsgService.this.ipMsgManager = IIpMsgManager.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFIpMsgService.this.ipMsgManager = null;
            HFIpMsgService.this.dataReceiveListenerMap.clear();
            HFIpMsgService.this.deviceStatusListenerMap.clear();
            AILog.m4029d(HudSystem.TAG, "ipmsg service disconnected", LogLevel.RELEASE);
        }
    };

    private static class HFDataReceiveRemoteListener extends IDataReceiveListener.Stub {
        private DataReceiveListener listener;

        public HFDataReceiveRemoteListener(DataReceiveListener dataReceiveListener) {
            this.listener = dataReceiveListener;
        }

        @Override // com.ileja.aitelcomm.ipmsg.IDataReceiveListener
        public void onCommandReceived(int i) {
            DataReceiveListener dataReceiveListener = this.listener;
            if (dataReceiveListener != null) {
                dataReceiveListener.onCommandReceived(i);
            }
        }

        @Override // com.ileja.aitelcomm.ipmsg.IDataReceiveListener
        public void onMessageReceived(String str) {
            AILog.m4029d(HFIpMsgService.TAG, "onMessageReceived : " + str, LogLevel.RELEASE);
            DataReceiveListener dataReceiveListener = this.listener;
            if (dataReceiveListener != null) {
                dataReceiveListener.onMessageReceived(str);
            }
        }
    }

    private static class HFDeviceStatusRemoteListener extends IDeviceStatusListener.Stub {
        private DeviceStatusListener listener;

        public HFDeviceStatusRemoteListener(DeviceStatusListener deviceStatusListener) {
            this.listener = deviceStatusListener;
        }

        @Override // com.ileja.aitelcomm.ipmsg.IDeviceStatusListener
        public void onDeviceOffline() {
            DeviceStatusListener deviceStatusListener = this.listener;
            if (deviceStatusListener != null) {
                deviceStatusListener.onDeviceOffline();
            }
        }

        @Override // com.ileja.aitelcomm.ipmsg.IDeviceStatusListener
        public void onDeviceOnline() {
            DeviceStatusListener deviceStatusListener = this.listener;
            if (deviceStatusListener != null) {
                deviceStatusListener.onDeviceOnline();
            }
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to bind ipmsg service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        if (this.ipMsgManager != null) {
            m4406g(application);
            this.dataReceiveListenerMap.clear();
            this.deviceStatusListenerMap.clear();
        }
        m4402c();
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFIpMsgService;
    }

    public boolean isConnected() {
        IIpMsgManager iIpMsgManager = this.ipMsgManager;
        if (iIpMsgManager != null) {
            try {
                return iIpMsgManager.isConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void registerDataReceiveListener(final DataReceiveListener dataReceiveListener) {
        AILog.m4029d(TAG, "registerDataReceiveListener", LogLevel.RELEASE);
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.7
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = dataReceiveListener.hashCode();
                if (HFIpMsgService.this.dataReceiveListenerMap.get(iHashCode) != null) {
                    return;
                }
                HFDataReceiveRemoteListener hFDataReceiveRemoteListener = new HFDataReceiveRemoteListener(dataReceiveListener);
                try {
                    if (HFIpMsgService.this.ipMsgManager != null) {
                        HFIpMsgService.this.ipMsgManager.registerDataReceiveListener(hFDataReceiveRemoteListener);
                    }
                    HFIpMsgService.this.dataReceiveListenerMap.put(iHashCode, hFDataReceiveRemoteListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.ipmsg.IpMsgService[register_data_action]" + dataReceiveListener.hashCode());
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ipMsgManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void registerDeviceStatusListener(final DeviceStatusListener deviceStatusListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.5
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = deviceStatusListener.hashCode();
                if (HFIpMsgService.this.deviceStatusListenerMap.get(iHashCode) != null) {
                    return;
                }
                HFDeviceStatusRemoteListener hFDeviceStatusRemoteListener = new HFDeviceStatusRemoteListener(deviceStatusListener);
                try {
                    if (HFIpMsgService.this.ipMsgManager != null) {
                        HFIpMsgService.this.ipMsgManager.registerDeviceStatusListener(hFDeviceStatusRemoteListener);
                    }
                    HFIpMsgService.this.deviceStatusListenerMap.put(iHashCode, hFDeviceStatusRemoteListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.ipmsg.IpMsgService[register_device_action]" + deviceStatusListener.hashCode());
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ipMsgManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void sendMessage(final String str, final DataSendListener dataSendListener) {
        AILog.m4028d(TAG, "sendMessage, msg:" + str + " ,listener:" + dataSendListener + " ,ipMsgManager:" + this.ipMsgManager);
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.4
            @Override // java.lang.Runnable
            public void run() {
                if (HFIpMsgService.this.ipMsgManager == null) {
                    return;
                }
                try {
                    HFIpMsgService.this.ipMsgManager.sendMessage(str, new IDataSendListener.Stub() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.4.1
                        @Override // com.ileja.aitelcomm.ipmsg.IDataSendListener
                        public void onError() {
                            DataSendListener dataSendListener2 = dataSendListener;
                            if (dataSendListener2 != null) {
                                dataSendListener2.onError();
                            }
                        }

                        @Override // com.ileja.aitelcomm.ipmsg.IDataSendListener
                        public void onProgress(int i) {
                            DataSendListener dataSendListener2 = dataSendListener;
                            if (dataSendListener2 != null) {
                                dataSendListener2.onProgress(i);
                            }
                        }

                        @Override // com.ileja.aitelcomm.ipmsg.IDataSendListener
                        public void onSuccess() {
                            DataSendListener dataSendListener2 = dataSendListener;
                            if (dataSendListener2 != null) {
                                dataSendListener2.onSuccess();
                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.ipMsgManager != null) {
            pendingAction.run();
        }
    }

    public void start() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (HFIpMsgService.this.ipMsgManager == null) {
                        return;
                    }
                    HFIpMsgService.this.ipMsgManager.start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.ipmsg.IpMsgService[start_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ipMsgManager != null) {
            AILog.m4029d(TAG, "really start udp server", LogLevel.RELEASE);
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void stop() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (HFIpMsgService.this.ipMsgManager == null) {
                        return;
                    }
                    HFIpMsgService.this.ipMsgManager.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.ipmsg.IpMsgService[start_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ipMsgManager != null) {
            AILog.m4029d(TAG, "really stop udp server", LogLevel.RELEASE);
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void unregisterDataReceiveListener(final DataReceiveListener dataReceiveListener) {
        AILog.m4029d(TAG, "unregisterDataReceiveListener", LogLevel.RELEASE);
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.8
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = dataReceiveListener.hashCode();
                HFDataReceiveRemoteListener hFDataReceiveRemoteListener = (HFDataReceiveRemoteListener) HFIpMsgService.this.dataReceiveListenerMap.get(iHashCode);
                if (hFDataReceiveRemoteListener == null || HFIpMsgService.this.ipMsgManager == null) {
                    return;
                }
                try {
                    HFIpMsgService.this.ipMsgManager.unregisterDataReceiveListener(hFDataReceiveRemoteListener);
                    HFIpMsgService.this.dataReceiveListenerMap.remove(iHashCode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.ipmsg.IpMsgService[register_data_action]" + dataReceiveListener.hashCode());
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ipMsgManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void unregisterDeviceStatusListener(final DeviceStatusListener deviceStatusListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFIpMsgService.6
            @Override // java.lang.Runnable
            public void run() {
                int iHashCode = deviceStatusListener.hashCode();
                HFDeviceStatusRemoteListener hFDeviceStatusRemoteListener = (HFDeviceStatusRemoteListener) HFIpMsgService.this.deviceStatusListenerMap.get(iHashCode);
                if (hFDeviceStatusRemoteListener == null || HFIpMsgService.this.ipMsgManager == null) {
                    return;
                }
                try {
                    HFIpMsgService.this.ipMsgManager.unregisterDeviceStatusListener(hFDeviceStatusRemoteListener);
                    HFIpMsgService.this.deviceStatusListenerMap.remove(iHashCode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.ipmsg.IpMsgService[register_device_action]" + deviceStatusListener.hashCode());
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.ipMsgManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}