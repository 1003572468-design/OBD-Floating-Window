package com.ileja.framework.service.bluetoothext.impl.phone;

import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.bluetoothext.listener.SmsStateChangeListener;
import com.ileja.bluetoothext.provider.communication.ISMSListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ISMSListenerImpl extends ISMSListener.Stub {
    private static final String TAG = "ISMSListenerImpl";
    private List<SmsStateChangeListener> mSmsStateChangeListenerList = Collections.synchronizedList(new ArrayList());

    @Override // com.ileja.bluetoothext.provider.communication.ISMSListener
    public void onNewReceived(String str, String str2, String str3) {
        AILog.m4029d(TAG, "onNewReceived,  number:" + str + " , name:" + str2 + " , content:" + str3, LogLevel.RELEASE);
        Iterator<SmsStateChangeListener> it = this.mSmsStateChangeListenerList.iterator();
        while (it.hasNext()) {
            it.next().onNewReceived(str, str2, str3);
        }
    }

    @Override // com.ileja.bluetoothext.provider.communication.ISMSListener
    public void onSendResult(boolean z) {
        AILog.m4029d(TAG, "on SMS SendResult: " + z, LogLevel.RELEASE);
        Iterator<SmsStateChangeListener> it = this.mSmsStateChangeListenerList.iterator();
        while (it.hasNext()) {
            it.next().onSendResult(z);
        }
    }

    public void registerSMSListener(SmsStateChangeListener smsStateChangeListener) {
        if (smsStateChangeListener == null || this.mSmsStateChangeListenerList.contains(smsStateChangeListener)) {
            return;
        }
        this.mSmsStateChangeListenerList.add(smsStateChangeListener);
    }

    public void unRegisterSMSListener(SmsStateChangeListener smsStateChangeListener) {
        if (smsStateChangeListener == null || !this.mSmsStateChangeListenerList.contains(smsStateChangeListener)) {
            return;
        }
        this.mSmsStateChangeListenerList.remove(smsStateChangeListener);
    }
}