package com.ileja.framework.service.bluetoothext;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.os.IBinder;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.bluetoothext.listener.PhoneCallStateChangeListener;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;
import com.ileja.framework.service.bluetoothext.a2dp.BtA2dpClient;
import com.ileja.framework.service.bluetoothext.impl.phone.BtPhoneClient;

/* JADX INFO: loaded from: classes.dex */
public class HFBluetoothExtService extends HFService {
    private static final String CLS_NAME = "com.ileja.bluetoothext.BluetoothConnectionService";
    private static final String PKG_NAME = "com.ileja.carrobot";
    private static final String TAG = "HFBluetoothExtService";
    private BtA2dpClient a2dpClient;
    private AIBTRemoteServiceConnectionListener mAIBTRemoteServiceConnectionListener;
    private BtPhoneClient phoneClient;
    private HFServiceCallback serviceCallback = new HFServiceCallback() { // from class: com.ileja.framework.service.bluetoothext.HFBluetoothExtService.3
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HFBluetoothExtService.TAG, "aibt ServiceConnected", LogLevel.RELEASE);
            if (HFBluetoothExtService.this.phoneClient != null) {
                HFBluetoothExtService.this.phoneClient.onServiceConnected(componentName, iBinder);
            }
            if (HFBluetoothExtService.this.a2dpClient != null) {
                HFBluetoothExtService.this.a2dpClient.onServiceConnected(componentName, iBinder);
            }
            if (HFBluetoothExtService.this.mAIBTRemoteServiceConnectionListener == null || iBinder == null) {
                return;
            }
            HFBluetoothExtService.this.mAIBTRemoteServiceConnectionListener.onServiceConnected(componentName, iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            AILog.m4029d(HFBluetoothExtService.TAG, "aibt ServiceDisconnected", LogLevel.RELEASE);
            if (HFBluetoothExtService.this.phoneClient != null) {
                HFBluetoothExtService.this.phoneClient.onServiceDisconnected(componentName);
            }
            if (HFBluetoothExtService.this.a2dpClient != null) {
                HFBluetoothExtService.this.a2dpClient.onServiceDisconnected(componentName);
            }
            if (HFBluetoothExtService.this.mAIBTRemoteServiceConnectionListener != null) {
                HFBluetoothExtService.this.mAIBTRemoteServiceConnectionListener.onServiceDisconnected();
            }
        }
    };

    public interface AIBTRemoteServiceConnectionListener {
        void onServiceConnected(ComponentName componentName, IBinder iBinder);

        void onServiceDisconnected();
    }

    public boolean acceptCall() {
        AILog.m4031e(TAG, "acceptCall, phoneClient=" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            return btPhoneClient.acceptCall();
        }
        return false;
    }

    public boolean checkIsCallActive() {
        AILog.m4031e(TAG, "checkIsCallActive, phoneClient=" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            return btPhoneClient.checkIsCallActive();
        }
        return false;
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(TAG, "try to bind aibt service", LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient == null) {
            this.phoneClient = new BtPhoneClient();
        } else {
            btPhoneClient.reset();
        }
        if (this.a2dpClient == null) {
            this.a2dpClient = new BtA2dpClient();
        }
        m4401b(application, "com.ileja.carrobot", "com.ileja.bluetoothext.BluetoothConnectionService", this.serviceCallback);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        AILog.m4029d(TAG, "unbind aibt service", LogLevel.RELEASE);
        m4406g(application);
        m4402c();
        this.phoneClient = null;
    }

    public boolean dial(String str) {
        AILog.m4031e(TAG, "dial, phoneClient=" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            return btPhoneClient.dial(str);
        }
        return false;
    }

    public void disableHfpBT() {
        AILog.m4035i(TAG, "disableHfpBT" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            btPhoneClient.disableHfpBT();
        }
    }

    public void enableHfpBT() {
        AILog.m4035i(TAG, "enableHfpBT" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            btPhoneClient.enableHfpBT();
        }
    }

    public boolean endCall() {
        AILog.m4031e(TAG, "endCall, phoneClient=" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            return btPhoneClient.endCall();
        }
        return false;
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFBluetoothExtService;
    }

    public BluetoothDevice isA2dpConnected() {
        BtA2dpClient btA2dpClient = this.a2dpClient;
        if (btA2dpClient != null) {
            return btA2dpClient.isA2dpConnected();
        }
        return null;
    }

    public BluetoothDevice isHFPConnected() {
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            return btPhoneClient.isHFPConnected();
        }
        return null;
    }

    public void rebindServer(Application application) {
        AILog.m4035i(TAG, "rebindServer", LogLevel.RELEASE);
        m4406g(application);
        m4401b(application, "com.ileja.carrobot", "com.ileja.bluetoothext.BluetoothConnectionService", this.serviceCallback);
    }

    public void registerPhoneListener(final PhoneCallStateChangeListener phoneCallStateChangeListener) {
        AILog.m4031e(TAG, "registerPhoneListener enqueueAction, phoneClient=" + this.phoneClient + " ,listener=" + phoneCallStateChangeListener, LogLevel.RELEASE);
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.bluetoothext.HFBluetoothExtService.1
            @Override // java.lang.Runnable
            public void run() {
                if (HFBluetoothExtService.this.phoneClient != null) {
                    HFBluetoothExtService.this.phoneClient.registerPhoneListener(phoneCallStateChangeListener);
                }
                AILog.m4035i(HFBluetoothExtService.TAG, "registerPhoneListener execActions, phoneClient:" + HFBluetoothExtService.this.phoneClient, LogLevel.RELEASE);
            }
        };
        pendingAction.setKey("com.ileja.bluetoothext.BluetoothConnectionService_registerPhoneListener");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        pendingAction.run();
        m4403d(pendingAction);
    }

    public void registerRemoteServiceConnectionListener(AIBTRemoteServiceConnectionListener aIBTRemoteServiceConnectionListener) {
        this.mAIBTRemoteServiceConnectionListener = aIBTRemoteServiceConnectionListener;
        AILog.m4029d(TAG, "registerRemoteServiceConnectionListener, listener = " + aIBTRemoteServiceConnectionListener, LogLevel.RELEASE);
    }

    public void reset() {
        AILog.m4035i(TAG, "bt disconnected reset", LogLevel.RELEASE);
        this.phoneClient.reset();
    }

    public void swichHFPOrBLE(boolean z) {
        AILog.m4035i(TAG, "swichHFPOrBLE" + this.phoneClient + ", isHFP = " + z, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            btPhoneClient.swichHFPOrBLE(z);
        }
    }

    public void syncBtState() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.bluetoothext.HFBluetoothExtService.2
            @Override // java.lang.Runnable
            public void run() {
                if (HFBluetoothExtService.this.phoneClient != null) {
                    HFBluetoothExtService.this.phoneClient.syncBtState();
                }
                AILog.m4035i(HFBluetoothExtService.TAG, "syncBtState, phoneClient:" + HFBluetoothExtService.this.phoneClient, LogLevel.RELEASE);
            }
        };
        pendingAction.setKey("com.ileja.bluetoothext.BluetoothConnectionService_sync_bt_state");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        pendingAction.run();
        m4403d(pendingAction);
    }

    public boolean syncContacts() {
        AILog.m4031e(TAG, "syncContacts, phoneClient=" + this.phoneClient, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            return btPhoneClient.syncContacts();
        }
        return false;
    }

    public void unRegisterPhoneListener(PhoneCallStateChangeListener phoneCallStateChangeListener) {
        AILog.m4031e(TAG, "unRegisterPhoneListener, phoneClient=" + this.phoneClient + " ,listener=" + phoneCallStateChangeListener, LogLevel.RELEASE);
        BtPhoneClient btPhoneClient = this.phoneClient;
        if (btPhoneClient != null) {
            btPhoneClient.unRegisterPhoneListener(phoneCallStateChangeListener);
        }
    }

    public void unregisterRemoteServiceConnectionListener() {
        if (this.mAIBTRemoteServiceConnectionListener != null) {
            this.mAIBTRemoteServiceConnectionListener = null;
            AILog.m4029d(TAG, "unregisterRemoteServiceConnectionListener", LogLevel.RELEASE);
        }
    }
}