package com.ileja.framework.service.bluetoothext.impl.phone;

import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.bluetoothext.listener.PhoneCallStateChangeListener;
import com.ileja.bluetoothext.provider.communication.IPhoneCallListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class IPhoneCallListenerImpl extends IPhoneCallListener.Stub {
    private static final String TAG = "IPhoneCallListenerImpl";
    private List<PhoneCallStateChangeListener> mPhoneCallStateChangeListenerList = Collections.synchronizedList(new ArrayList());
    private Map<Integer, Boolean> mCallIdList = Collections.synchronizedMap(new LinkedHashMap());
    private List<Integer> mRejectCallIdList = Collections.synchronizedList(new ArrayList());

    public Map<Integer, Boolean> getCallIdList() {
        return this.mCallIdList;
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onAudioStateChange(int i, int i2) {
        AILog.m4029d(TAG, "onAudioStateChange state:" + i2, LogLevel.RELEASE);
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onAudioStateChange(i2);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onBluetoothConnectedState(boolean z, String str) {
        AILog.m4029d(TAG, "onBluetoothConnectedState isConnected:" + z + " , btMacAddress:" + str, LogLevel.RELEASE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onBluetoothConnectedState: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onBluetoothConnectedState(z, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onCallError(int i) {
        AILog.m4029d(TAG, "CallError cme:" + i, LogLevel.RELEASE);
        resetData();
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onCallError(i);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onIncomingCallEnded(int i, String str) {
        AILog.m4029d(TAG, "onIncomingCallEnded id:" + i + " ,number:" + str, LogLevel.RELEASE);
        if (this.mCallIdList.containsKey(Integer.valueOf(i))) {
            this.mCallIdList.remove(Integer.valueOf(i));
        }
        if (this.mRejectCallIdList.contains(Integer.valueOf(i))) {
            this.mRejectCallIdList.remove(Integer.valueOf(i));
        }
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onIncomingCallEnded: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onIncomingCallEnded(i, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onIncomingCallMissed(int i, String str) {
        boolean zContains = this.mRejectCallIdList.contains(Integer.valueOf(i));
        if (zContains) {
            this.mRejectCallIdList.remove(Integer.valueOf(i));
        }
        AILog.m4029d(TAG, "onIncomingCallMissed id:" + i + " ,number:" + str + " ,isReject:" + zContains, LogLevel.RELEASE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onIncomingCallMissed: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onIncomingCallMissed(i, str, zContains);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onIncomingCallStarted(int i, String str) {
        AILog.m4029d(TAG, "onIncomingCallStarted id:" + i + " ,number:" + str, LogLevel.RELEASE);
        this.mCallIdList.put(Integer.valueOf(i), Boolean.FALSE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onIncomingCallStarted: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            AILog.m4029d(TAG, "listener:" + phoneCallStateChangeListener, LogLevel.RELEASE);
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onIncomingCallStarted(i, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onIncomingHold(int i, String str) {
        AILog.m4029d(TAG, "onIncomingHold id:" + i + " ,number:" + str, LogLevel.RELEASE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onIncomingHold: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onIncomingHold(i, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onIncomingOffhook(int i, String str) {
        AILog.m4029d(TAG, "onIncomingOffhook id:" + i + " ,number:" + str, LogLevel.RELEASE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onIncomingOffhook: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onIncomingOffhook(i, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onLicenseStateChanged(int i) {
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onMobileStateInfo(int i, int i2) {
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onMobileStateInfo(i, i2);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onOutgoingCallEnded(int i, String str, boolean z) {
        boolean zContains = this.mRejectCallIdList.contains(Integer.valueOf(i));
        if (zContains) {
            this.mRejectCallIdList.remove(Integer.valueOf(i));
        }
        AILog.m4029d(TAG, "onOutgoingCallEnded id:" + i + " ,number:" + str + " ,offhooked:" + z + " ,isReject:" + zContains, LogLevel.RELEASE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onOutgoingCallEnded: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onOutgoingCallEnded(i, str, z, zContains);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onOutgoingCallStarted(int i, String str) {
        AILog.m4029d(TAG, "onOutgoingCallStarted id:" + i + " ,number:" + str, LogLevel.RELEASE);
        this.mCallIdList.put(Integer.valueOf(i), Boolean.TRUE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onOutgoingCallStarted: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            AILog.m4029d(TAG, "listener:" + phoneCallStateChangeListener, LogLevel.RELEASE);
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onOutgoingCallStarted(i, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onOutgoingHold(int i, String str) {
        AILog.m4028d(TAG, "onOutgoingHold id:" + i + " ,number:" + str);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onOutgoingHold: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onOutgoingHold(i, str);
            }
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.IPhoneCallListener
    public void onOutgoingOffhook(int i, String str) {
        AILog.m4029d(TAG, "onOutgoingOffhook id:" + i + " ,number:" + str, LogLevel.RELEASE);
        List<PhoneCallStateChangeListener> list = this.mPhoneCallStateChangeListenerList;
        if (list == null || list.size() == 0) {
            AILog.m4041w(TAG, "onOutgoingOffhook: listener not found", LogLevel.RELEASE);
        }
        for (PhoneCallStateChangeListener phoneCallStateChangeListener : this.mPhoneCallStateChangeListenerList) {
            if (phoneCallStateChangeListener != null) {
                phoneCallStateChangeListener.onOutgoingOffhook(i, str);
            }
        }
    }

    public void registerPhoneListener(PhoneCallStateChangeListener phoneCallStateChangeListener) {
        AILog.m4029d(TAG, "registerPhoneListener listener:" + phoneCallStateChangeListener, LogLevel.RELEASE);
        if (phoneCallStateChangeListener == null || this.mPhoneCallStateChangeListenerList.contains(phoneCallStateChangeListener)) {
            return;
        }
        this.mPhoneCallStateChangeListenerList.add(phoneCallStateChangeListener);
    }

    public void resetData() {
        this.mCallIdList.clear();
        this.mRejectCallIdList.clear();
    }

    public void setRejectCallID(int i) {
        if (this.mRejectCallIdList.contains(Integer.valueOf(i))) {
            return;
        }
        this.mRejectCallIdList.add(Integer.valueOf(i));
    }

    public void unRegisterPhoneListener(PhoneCallStateChangeListener phoneCallStateChangeListener) {
        AILog.m4029d(TAG, "unRegisterPhoneListener listener:" + phoneCallStateChangeListener, LogLevel.RELEASE);
        if (phoneCallStateChangeListener == null || !this.mPhoneCallStateChangeListenerList.contains(phoneCallStateChangeListener)) {
            return;
        }
        this.mPhoneCallStateChangeListenerList.remove(phoneCallStateChangeListener);
    }
}