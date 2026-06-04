package com.ileja.aibase.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.amap.api.services.core.AMapException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.config.HttpConfig;

/* JADX INFO: loaded from: classes.dex */
public class BatteryUtil {
    private static final String TAG = "BatteryUtil";
    private int BatteryN;
    private String BatteryStatus;
    private double BatteryT;
    private String BatteryTemp;
    private int BatteryV;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() { // from class: com.ileja.aibase.phone.BatteryUtil.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                BatteryUtil.this.BatteryN = intent.getIntExtra("level", 0);
                BatteryUtil.this.BatteryV = intent.getIntExtra(HttpConfig.TAG.TP_VOLTAGE, 0);
                BatteryUtil.this.BatteryT = intent.getIntExtra(HttpConfig.TAG.TP_TEMPERATURE, 0);
                int intExtra = intent.getIntExtra("status", 1);
                if (intExtra == 1) {
                    BatteryUtil.this.BatteryStatus = "未知道状态";
                } else if (intExtra == 2) {
                    BatteryUtil.this.BatteryStatus = "充电状态";
                } else if (intExtra == 3) {
                    BatteryUtil.this.BatteryStatus = "放电状态";
                } else if (intExtra == 4) {
                    BatteryUtil.this.BatteryStatus = "未充电";
                } else if (intExtra == 5) {
                    BatteryUtil.this.BatteryStatus = "充满电";
                }
                int intExtra2 = intent.getIntExtra("health", 1);
                if (intExtra2 == 1) {
                    BatteryUtil.this.BatteryTemp = AMapException.AMAP_CLIENT_UNKNOWN_ERROR;
                    return;
                }
                if (intExtra2 == 2) {
                    BatteryUtil.this.BatteryTemp = "状态良好";
                    return;
                }
                if (intExtra2 == 3) {
                    BatteryUtil.this.BatteryTemp = "电池过热";
                } else if (intExtra2 == 4) {
                    BatteryUtil.this.BatteryTemp = "电池没有电";
                } else {
                    if (intExtra2 != 5) {
                        return;
                    }
                    BatteryUtil.this.BatteryTemp = "电池电压过高";
                }
            }
        }
    };
    private Context mContext;

    public int getBatteryN() {
        return this.BatteryN;
    }

    public void register(Context context) {
        if (context != null) {
            AILog.m4028d(TAG, "register()");
            this.mContext = context;
            context.registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }
    }

    public void unregister() {
        if (this.mContext != null) {
            AILog.m4028d(TAG, "unregister()");
            this.mContext.unregisterReceiver(this.mBatInfoReceiver);
        }
    }
}