package com.ileja.aicar.obd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.core.p007sp.BasePrefProvider;
import com.ileja.core.p007sp.IMPSharedPreference;

/* JADX INFO: loaded from: classes.dex */
public class SpeedAdjustUtil {

    /* JADX INFO: renamed from: a */
    SharedDataChangeReceiver f5950a;
    private Context mContext;
    private String TAG = "SpeedAdjustUtil";
    private int adjustSpeed = Integer.MAX_VALUE;

    public class SharedDataChangeReceiver extends BroadcastReceiver {
        public SharedDataChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("key");
            AILog.m4035i(SpeedAdjustUtil.this.TAG, "onSharedPreferenceChanged " + stringExtra, LogLevel.RELEASE);
            if ("setAdjustSpeed".equals(stringExtra)) {
                SpeedAdjustUtil speedAdjustUtil = SpeedAdjustUtil.this;
                speedAdjustUtil.adjustSpeed = BasePrefProvider.getAdjustSpeed(speedAdjustUtil.mContext);
                AILog.m4034i(SpeedAdjustUtil.this.TAG, "adjustspeed changed " + SpeedAdjustUtil.this.adjustSpeed);
            }
        }
    }

    public SpeedAdjustUtil(Context context) {
        this.mContext = context;
    }

    public float adjustSpeedToAMAP(float f) {
        if (f > 30.0f) {
            f -= 3.0f;
        }
        return f / 3.6f;
    }

    public int adjustSpeedValue(double d) {
        if (this.adjustSpeed == Integer.MAX_VALUE) {
            this.adjustSpeed = BasePrefProvider.getAdjustSpeed(this.mContext);
        }
        return d > 20.0d ? (int) (d * (((double) (this.adjustSpeed * 0.01f)) + 1.05d)) : (int) d;
    }

    public float adjustSpeedValueByMetersPerSecond(float f) {
        if (this.adjustSpeed == Integer.MAX_VALUE) {
            this.adjustSpeed = BasePrefProvider.getAdjustSpeed(this.mContext);
        }
        double d = f;
        int i = (int) (3.6d * d);
        if (i <= 20) {
            return f;
        }
        float f2 = (float) (d * (((double) (this.adjustSpeed * 0.01f)) + 1.05d));
        return i > 30 ? (float) (((double) f2) - 0.8333333554091282d) : f2;
    }

    public void destory() {
        SharedDataChangeReceiver sharedDataChangeReceiver = this.f5950a;
        if (sharedDataChangeReceiver != null) {
            this.mContext.unregisterReceiver(sharedDataChangeReceiver);
            this.f5950a = null;
            AILog.m4035i(this.TAG, "unregisterSharedDataChangeReceiver", LogLevel.RELEASE);
        }
    }

    public void init() {
        if (this.f5950a == null) {
            this.f5950a = new SharedDataChangeReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IMPSharedPreference.INTENT_ACTION_SHAREDSP_STATUS);
            this.mContext.registerReceiver(this.f5950a, intentFilter);
            AILog.m4035i(this.TAG, "registerSharedDataChangeReceiver", LogLevel.RELEASE);
        }
    }
}