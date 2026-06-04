package com.ileja.aibase.phone;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.ileja.aibase.common.AILog;

/* JADX INFO: loaded from: classes.dex */
public class SMSSender {
    private String SMS_SEND_ACTIOIN = "com.aispeech.navos.launcher.SMS_SEND_ACTIOIN";
    private Context mContext;
    private SMSStateListener mSMSStateListener;
    private PendingIntent mSentIntent;
    private BroadcastReceiver mSentReceiver;

    public interface SMSStateListener {
        void onSMSStateChanged(boolean z, int i);
    }

    public SMSSender(Context context, SMSStateListener sMSStateListener) {
        this.mContext = context;
        this.mSMSStateListener = sMSStateListener;
        this.mSentIntent = PendingIntent.getActivity(context, 0, new Intent(this.SMS_SEND_ACTIOIN), AMapEngineUtils.HALF_MAX_P20_WIDTH);
    }

    public void registerReceiver() {
        if (this.mSentReceiver == null) {
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.ileja.aibase.phone.SMSSender.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    SMSSender.this.unregisterReceiver();
                    AILog.m4028d("SMSStateMonitor", "onReceive result code==" + getResultCode());
                    int resultCode = getResultCode();
                    if (resultCode == -1) {
                        if (SMSSender.this.mSMSStateListener != null) {
                            SMSSender.this.mSMSStateListener.onSMSStateChanged(true, -1);
                        }
                    } else if ((resultCode == 1 || resultCode == 2 || resultCode == 3 || resultCode == 4) && SMSSender.this.mSMSStateListener != null) {
                        SMSSender.this.mSMSStateListener.onSMSStateChanged(false, getResultCode());
                    }
                }
            };
            this.mSentReceiver = broadcastReceiver;
            this.mContext.registerReceiver(broadcastReceiver, new IntentFilter(this.SMS_SEND_ACTIOIN));
        }
    }

    public void sendSMS(String str, String str2) {
        SMSStateListener sMSStateListener;
        if ((TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) && (sMSStateListener = this.mSMSStateListener) != null) {
            sMSStateListener.onSMSStateChanged(false, 0);
        }
        registerReceiver();
        SmsManager.getDefault().sendTextMessage(str, null, str2, this.mSentIntent, null);
    }

    public void unregisterReceiver() {
        BroadcastReceiver broadcastReceiver = this.mSentReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mSentReceiver = null;
        }
    }
}