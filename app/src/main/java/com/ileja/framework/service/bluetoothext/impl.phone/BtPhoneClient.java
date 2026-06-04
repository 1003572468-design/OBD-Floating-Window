package com.ileja.framework.service.bluetoothext.impl.phone;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.bluetoothext.BtConstants;
import com.ileja.bluetoothext.listener.PhoneCallStateChangeListener;
import com.ileja.bluetoothext.listener.SmsStateChangeListener;
import com.ileja.bluetoothext.provider.communication.ICommunicationBinder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class BtPhoneClient {
    private static final String TAG = "BtPhoneClient";
    private boolean mBindServiceSuccess;
    private ICommunicationBinder mCommunicationBinder;
    private Context mContext;
    private long mIPhoneCallListenerToken = 0;
    private IPhoneCallListenerImpl mIPhoneCallListenerImpl = new IPhoneCallListenerImpl();
    private long mISMSListenerToken = 0;
    private ISMSListenerImpl mISMSListenerImpl = new ISMSListenerImpl();

    private boolean canReject(int i) {
        return i == 4 || i == 5 || i == 1 || i == 6;
    }

    private boolean canTerminate(int i) {
        return i == 0 || i == 3 || i == 1 || i == 2;
    }

    private boolean checkServiceConnected() {
        boolean z = this.mCommunicationBinder != null;
        if (!z) {
            AILog.m4031e(TAG, "蓝牙服务绑定失败", LogLevel.RELEASE);
        }
        return z;
    }

    private void registerCallBacks() {
        if (checkServiceConnected()) {
            try {
                this.mIPhoneCallListenerImpl.resetData();
                this.mCommunicationBinder.registerPhoneCallBack(this.mIPhoneCallListenerImpl);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AILog.m4029d(TAG, "registerCallBacks", LogLevel.RELEASE);
        }
    }

    private void unregisterCallBacks() {
        if (checkServiceConnected()) {
            try {
                this.mIPhoneCallListenerImpl.resetData();
                this.mCommunicationBinder.unregisterPhoneCallBack(this.mIPhoneCallListenerImpl);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AILog.m4029d(TAG, "unregisterCallBacks", LogLevel.RELEASE);
        }
    }

    public boolean acceptCall() {
        if (!checkServiceConnected()) {
            return false;
        }
        try {
            boolean zAcceptCall = this.mCommunicationBinder.acceptCall();
            if (!zAcceptCall) {
                AILog.m4031e(TAG, "接通失败，请检查是否链接蓝牙", LogLevel.RELEASE);
            }
            return zAcceptCall;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkIsCallActive() {
        if (!checkServiceConnected()) {
            return false;
        }
        try {
            return this.mCommunicationBinder.checkIsCallActive();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dial(String str) {
        if (!checkServiceConnected()) {
            return false;
        }
        try {
            boolean zDial = this.mCommunicationBinder.dial(str);
            if (!zDial) {
                AILog.m4031e(TAG, "拨号失败，请检查是否链接蓝牙", LogLevel.RELEASE);
            }
            return zDial;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disableHfpBT() {
        AILog.m4035i(TAG, "disableHfpBT" + this.mCommunicationBinder, LogLevel.RELEASE);
        ICommunicationBinder iCommunicationBinder = this.mCommunicationBinder;
        if (iCommunicationBinder != null) {
            try {
                iCommunicationBinder.disableHfpBT();
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4033e(TAG, "disableHfpBT exception: ", e, LogLevel.RELEASE);
            }
        }
    }

    public void enableHfpBT() {
        AILog.m4035i(TAG, "enableHfpBT" + this.mCommunicationBinder, LogLevel.RELEASE);
        ICommunicationBinder iCommunicationBinder = this.mCommunicationBinder;
        if (iCommunicationBinder != null) {
            try {
                iCommunicationBinder.enableHfpBT();
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4033e(TAG, "enableHfpBT exception: ", e, LogLevel.RELEASE);
            }
        }
    }

    public boolean endCall() {
        boolean zBooleanValue;
        int i;
        boolean zTerminateCall;
        if (checkServiceConnected()) {
            try {
                if (this.mIPhoneCallListenerImpl.getCallIdList().size() == 0) {
                    AILog.m4031e(TAG, "当前没有通话，无法挂断电话", LogLevel.RELEASE);
                    return false;
                }
                Iterator<Map.Entry<Integer, Boolean>> it = this.mIPhoneCallListenerImpl.getCallIdList().entrySet().iterator();
                AILog.m4035i(TAG, "!!!callIdList is " + this.mIPhoneCallListenerImpl.getCallIdList(), LogLevel.RELEASE);
                int i2 = -1;
                if (it.hasNext()) {
                    Map.Entry<Integer, Boolean> next = it.next();
                    int iIntValue = next.getKey().intValue();
                    int callState = this.mCommunicationBinder.getCallState(iIntValue);
                    zBooleanValue = next.getValue().booleanValue();
                    AILog.m4029d(TAG, "callstate:" + callState + " ,isOutGoing:" + zBooleanValue, LogLevel.RELEASE);
                    i2 = callState;
                    i = iIntValue;
                } else {
                    zBooleanValue = false;
                    i = -1;
                }
                if (!zBooleanValue) {
                    if (canReject(i2)) {
                        zTerminateCall = this.mCommunicationBinder.rejectCall();
                        AILog.m4029d(TAG, "incoming , rejectCall: id:" + i + " , end:" + zTerminateCall, LogLevel.RELEASE);
                    } else if (canTerminate(i2)) {
                        zTerminateCall = this.mCommunicationBinder.terminateCall(0);
                        AILog.m4029d(TAG, "incoming , terminateCall: id:" + i + " , end:" + zTerminateCall + " , index:0", LogLevel.RELEASE);
                    } else {
                        zTerminateCall = false;
                    }
                    if (zTerminateCall) {
                        this.mIPhoneCallListenerImpl.setRejectCallID(Integer.valueOf(i).intValue());
                    } else {
                        AILog.m4031e(TAG, "来电挂断失败, state:" + i2, LogLevel.RELEASE);
                    }
                } else if (canTerminate(i2)) {
                    zTerminateCall = this.mCommunicationBinder.terminateCall(0);
                    this.mIPhoneCallListenerImpl.setRejectCallID(Integer.valueOf(i).intValue());
                    AILog.m4029d(TAG, "outgoing , terminateCall: id:" + i + " , end:" + zTerminateCall + " , index:0", LogLevel.RELEASE);
                } else {
                    AILog.m4031e(TAG, "去电挂断失败，不处于挂断状态, state:" + i2, LogLevel.RELEASE);
                    zTerminateCall = false;
                }
                AILog.m4029d(TAG, "endCall: id:" + i + " , end:" + zTerminateCall, LogLevel.RELEASE);
                return zTerminateCall;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public BluetoothDevice isHFPConnected() {
        BluetoothDevice bluetoothDeviceIsHFPConnected = null;
        if (!this.mBindServiceSuccess) {
            AILog.m4029d(TAG, "isHFPConnected, disconnected !!!", LogLevel.RELEASE);
            return null;
        }
        try {
            bluetoothDeviceIsHFPConnected = this.mCommunicationBinder.isHFPConnected();
            AILog.m4029d(TAG, "isHFPConnected, connect success bluetooth device is " + bluetoothDeviceIsHFPConnected, LogLevel.RELEASE);
            return bluetoothDeviceIsHFPConnected;
        } catch (RemoteException e) {
            AILog.m4029d(TAG, "isHFPConnected, get bluetooth device err: " + e.toString(), LogLevel.RELEASE);
            e.printStackTrace();
            return bluetoothDeviceIsHFPConnected;
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mBindServiceSuccess = true;
        this.mCommunicationBinder = ICommunicationBinder.Stub.asInterface(iBinder);
        registerCallBacks();
        AILog.m4029d(TAG, "onServiceConnected AIBT", LogLevel.RELEASE);
    }

    public void onServiceDisconnected(ComponentName componentName) {
        unregisterCallBacks();
        this.mBindServiceSuccess = false;
        this.mCommunicationBinder = null;
        AILog.m4029d(TAG, "onServiceDisconnected AIBT", LogLevel.RELEASE);
    }

    public void registerPhoneListener(PhoneCallStateChangeListener phoneCallStateChangeListener) {
        this.mIPhoneCallListenerImpl.registerPhoneListener(phoneCallStateChangeListener);
    }

    public void registerSMSListener(SmsStateChangeListener smsStateChangeListener) {
        this.mISMSListenerImpl.registerSMSListener(smsStateChangeListener);
    }

    public void reset() {
        AILog.m4035i(TAG, "reset data", LogLevel.RELEASE);
        IPhoneCallListenerImpl iPhoneCallListenerImpl = this.mIPhoneCallListenerImpl;
        if (iPhoneCallListenerImpl != null) {
            iPhoneCallListenerImpl.resetData();
        }
    }

    public boolean sendSms(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return sendSms(arrayList, str2);
    }

    public boolean sendSms(List<String> list, String str) {
        return false;
    }

    public void simulateSyncContacts(Context context) {
        Intent intent = new Intent(BtConstants.ACTION_SIMULATE_SYNC_CONTACTS);
        intent.setClassName(BtConstants.AIBLUETOOTHEXT_BIND_PKG_NAME, BtConstants.AIBLUETOOTHEXT_SIMULATE_SYNC_CLASS_NAME);
        context.getApplicationContext().startService(intent);
    }

    public void swichHFPOrBLE(boolean z) {
        AILog.m4035i(TAG, "swichHFPOrBLE" + this.mCommunicationBinder + ", isHFP = " + z, LogLevel.RELEASE);
        ICommunicationBinder iCommunicationBinder = this.mCommunicationBinder;
        if (iCommunicationBinder != null) {
            try {
                iCommunicationBinder.swichHFPOrBLE(z);
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4033e(TAG, "swichHFPOrBLE exception: ", e, LogLevel.RELEASE);
            }
        }
    }

    public void syncBtState() {
        ICommunicationBinder iCommunicationBinder = this.mCommunicationBinder;
        if (iCommunicationBinder != null) {
            try {
                iCommunicationBinder.syncHFPState();
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4033e(TAG, "syncBtState exception: ", e, LogLevel.RELEASE);
            }
        }
    }

    public boolean syncContacts() {
        try {
            if (checkServiceConnected()) {
                return this.mCommunicationBinder.syncContacts();
            }
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unRegisterPhoneListener(PhoneCallStateChangeListener phoneCallStateChangeListener) {
        this.mIPhoneCallListenerImpl.unRegisterPhoneListener(phoneCallStateChangeListener);
    }

    public void unRegisterSMSListener(SmsStateChangeListener smsStateChangeListener) {
        this.mISMSListenerImpl.unRegisterSMSListener(smsStateChangeListener);
    }
}