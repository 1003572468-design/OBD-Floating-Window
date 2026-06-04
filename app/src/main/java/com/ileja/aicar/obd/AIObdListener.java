package com.ileja.aicar.obd;

import com.ileja.aicar.obd.data.AIObdDrivingStatisticsData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdFuelData;
import com.ileja.aicar.obd.data.AIObdHistoryData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;

/* JADX INFO: loaded from: classes.dex */
public interface AIObdListener {
    void onDrivingStatisticsMessage(AIObdDrivingStatisticsData aIObdDrivingStatisticsData);

    void onHistoryMessage(AIObdHistoryData aIObdHistoryData);

    void onOBDUpgradeMessage(String str);

    void onOBDVersionMessage(String str);

    void onObdErrorMessage(AIObdErrorData aIObdErrorData);

    void onObdStatusChange(AIObdStatus aIObdStatus);

    void onRealtimeMessage(AIObdRealtimeData aIObdRealtimeData);

    void onRemainFuelData(AIObdFuelData aIObdFuelData);
}