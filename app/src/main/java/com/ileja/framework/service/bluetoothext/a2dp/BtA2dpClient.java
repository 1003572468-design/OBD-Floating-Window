package com.ileja.framework.service.bluetoothext.a2dp;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.bluetoothext.provider.communication.ICommunicationBinder;

/* JADX INFO: loaded from: classes.dex */
public class BtA2dpClient {
    private static final String TAG = "BtA2dpClient";
    private boolean mBindServiceSuccess;
    private ICommunicationBinder mCommunicationBinder;

    public BluetoothDevice isA2dpConnected() {
        BluetoothDevice bluetoothDeviceIsA2dpConnected = null;
        if (!this.mBindServiceSuccess) {
            AILog.m4029d(TAG, "isA2dpConnected,  disconnected !!!", LogLevel.RELEASE);
            return null;
        }
        try {
            bluetoothDeviceIsA2dpConnected = this.mCommunicationBinder.isA2dpConnected();
            AILog.m4029d(TAG, "isA2dpConnected,  connect success bluetooth device is " + bluetoothDeviceIsA2dpConnected, LogLevel.RELEASE);
            return bluetoothDeviceIsA2dpConnected;
        } catch (RemoteException e) {
            AILog.m4029d(TAG, "get bluetooth device err: " + e.toString(), LogLevel.RELEASE);
            e.printStackTrace();
            return bluetoothDeviceIsA2dpConnected;
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        AILog.m4029d(TAG, "onServiceConnected AIBT", LogLevel.RELEASE);
        this.mCommunicationBinder = ICommunicationBinder.Stub.asInterface(iBinder);
        this.mBindServiceSuccess = true;
    }

    public void onServiceDisconnected(ComponentName componentName) {
        this.mBindServiceSuccess = false;
        this.mCommunicationBinder = null;
        AILog.m4029d(TAG, "onServiceDisconnected AIBT", LogLevel.RELEASE);
    }
}