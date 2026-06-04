package com.ileja.aicar.obd;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.phone.DeviceUtil;
import com.ileja.aibase.service.BaseRemoteService;
import com.ileja.aicar.C1103R;
import com.ileja.aicar.GlobalService;
import com.ileja.aicar.obd.IObdService;
import com.ileja.aicar.obd.data.AIObdCmd;
import com.ileja.aicar.obd.data.AIObdDataMessage;
import com.ileja.aicar.obd.data.AIObdDrivingStatisticsData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdFuelData;
import com.ileja.aicar.obd.data.AIObdHistoryData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;
import com.ileja.core.data.CommonConfig;

/* JADX INFO: loaded from: classes.dex */
public class AIObdService extends BaseRemoteService implements AIObdListener {
    private static final String TAG = "AIObdService";
    private AIObdFactory obdFactory;

    private class IObdServiceBinder extends IObdService.Stub {
        private IObdServiceBinder() {
        }

        @Override // com.ileja.aicar.obd.IObdService
        public void checkObdUpgrade(String str) {
            if (DeviceUtil.isSupportTesla()) {
                return;
            }
            if (TextUtils.isEmpty(str)) {
                sendCommand(AIObdCmd.OBD_LEJA_MOCK_UPGRADE);
                return;
            }
            sendCommand("AT+MOCKVERSION_" + str);
        }

        @Override // com.ileja.aicar.obd.IObdService
        public void close() {
            if (DeviceUtil.isSupportTesla() || AIObdService.this.obdFactory == null) {
                return;
            }
            AIObdService.this.obdFactory.close();
        }

        @Override // com.ileja.aicar.obd.IObdService
        public void open() {
            if (DeviceUtil.isSupportTesla() || AIObdService.this.obdFactory == null) {
                return;
            }
            AIObdService.this.obdFactory.open();
        }

        @Override // com.ileja.aicar.obd.IObdService
        public void sendCommand(String str) {
            if (DeviceUtil.isSupportTesla() || AIObdService.this.obdFactory == null) {
                return;
            }
            AIObdService.this.obdFactory.sendCommand(str);
        }
    }

    private void resetData() {
        AILog.m4029d(TAG, "obd disconnected, resetData.", LogLevel.RELEASE);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_REAL_MESSAGE_FILTER, null, true);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_HISTORY_MESSAGE_FILTER, null, true);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_DRIVING_MESSAGE_FILTER, null, true);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_ERROR_MESSAGE_FILTER, null, true);
    }

    @Override // com.ileja.aibase.service.KeepLiveService
    public int getAppNameID() {
        return C1103R.string.app_name;
    }

    @Override // com.ileja.aibase.service.KeepLiveService
    public int getIconID() {
        return C1103R.mipmap.ic_launcher;
    }

    @Override // com.ileja.aibase.service.BaseRemoteService, com.ileja.aibase.service.KeepLiveService, android.app.Service
    public IBinder onBind(Intent intent) {
        return new IObdServiceBinder();
    }

    @Override // com.ileja.aibase.service.BaseRemoteService, com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onCreate() {
        super.onCreate();
        AIObdFactory aIObdFactory = new AIObdFactory();
        this.obdFactory = aIObdFactory;
        aIObdFactory.create();
        this.obdFactory.registerListener(this);
        AILog.m4029d(TAG, "onCreate()", LogLevel.RELEASE);
    }

    @Override // com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onDestroy() {
        AIObdFactory aIObdFactory = this.obdFactory;
        if (aIObdFactory != null) {
            aIObdFactory.unregisterListener(this);
            this.obdFactory.close();
            this.obdFactory = null;
        }
        super.onDestroy();
        AILog.m4029d(TAG, "onDestroy()", LogLevel.RELEASE);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onDrivingStatisticsMessage(AIObdDrivingStatisticsData aIObdDrivingStatisticsData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdDrivingStatisticsData);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_DRIVING_MESSAGE_FILTER, bundle, true);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onHistoryMessage(AIObdHistoryData aIObdHistoryData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdHistoryData);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_HISTORY_MESSAGE_FILTER, bundle, true);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onOBDUpgradeMessage(String str) {
        AILog.m4029d(TAG, "onOBDUpgradeMessage message =>" + str, LogLevel.RELEASE);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onOBDVersionMessage(String str) {
        AILog.m4029d(TAG, "onOBDVersionMessage version =>" + str, LogLevel.RELEASE);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onObdErrorMessage(AIObdErrorData aIObdErrorData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdErrorData);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_ERROR_MESSAGE_FILTER, bundle, true);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onObdStatusChange(AIObdStatus aIObdStatus) {
        if (aIObdStatus.getStatus() == -1) {
            resetData();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdStatus);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_STATUS_MESSAGE_FILTER, bundle, true);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onRealtimeMessage(AIObdRealtimeData aIObdRealtimeData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdRealtimeData);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_REAL_MESSAGE_FILTER, bundle, true);
    }

    @Override // com.ileja.aicar.obd.AIObdListener
    public void onRemainFuelData(AIObdFuelData aIObdFuelData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdFuelData);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_FUEL_MESSAGE_FILTER, bundle, true);
    }
}