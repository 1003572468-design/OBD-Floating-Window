// AIObdListener.aidl
package com.ileja.aicar.obd;

import com.ileja.aicar.obd.data.AIObdDrivingStatisticsData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdFuelData;
import com.ileja.aicar.obd.data.AIObdHistoryData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;

interface AIObdListener {
    void onDrivingStatisticsMessage(in AIObdDrivingStatisticsData data);
    void onHistoryMessage(in AIObdHistoryData data);
    void onOBDUpgradeMessage(String str);
    void onOBDVersionMessage(String str);
    void onObdErrorMessage(in AIObdErrorData data);
    void onObdStatusChange(in AIObdStatus status);
    void onRealtimeMessage(in AIObdRealtimeData data);
    void onRemainFuelData(in AIObdFuelData data);
}
