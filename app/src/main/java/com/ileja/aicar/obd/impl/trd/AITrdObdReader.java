package com.ileja.aicar.obd.impl.trd;

import android.text.TextUtils;
import com.cpsdna.obdports.ports.OBDAction;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.data.AIObdCmd;
import com.ileja.aicar.obd.data.AIObdData;
import com.ileja.aicar.obd.data.AIObdDrivingStatisticsData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdHistoryData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;
import com.ileja.core.p007sp.BasePrefProvider;
import com.ileja.module.BaseModuleApplication;
import com.ileja.serialport.SerialPortCallback;
import com.ileja.serialport.SerialPortHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class AITrdObdReader implements SerialPortCallback {
    private static final long MAX_RECV_COUNT = 30;
    private static final String SLEEP_MSG = "System sleeping";
    private static final String TAG = "AIObd";
    private Set<AIObdListener> aiObdListeners;
    private long notRecvHistoryDataCount = 0;
    private boolean hasRecvHistoryData = false;
    private boolean hasSendError = false;
    private int obdErrCount = 0;
    private long notRecvDataCount = 0;
    private boolean rtMsgHandled = false;
    private boolean hbMsgHandled = false;
    private boolean ttMsgHandled = false;
    private boolean autoStopTimeSet = false;
    private ArrayList<AIObdRealtimeData> realtimeDataArrayList = new ArrayList<>();
    private AIObdStatus aiObdStatus = new AIObdStatus();
    private volatile boolean running = false;

    public AITrdObdReader(Set<AIObdListener> set) {
        this.aiObdListeners = set;
    }

    private void addRealtimeHistory(AIObdRealtimeData aIObdRealtimeData) {
        synchronized (this.realtimeDataArrayList) {
            if (this.realtimeDataArrayList.size() >= 2) {
                this.realtimeDataArrayList.remove(0);
            }
            this.realtimeDataArrayList.add(aIObdRealtimeData);
        }
    }

    private void bindServer() {
        SerialPortHandler.getInstance().setSerialPortCallback(this);
        SerialPortHandler.getInstance().openSerialPort(19200);
    }

    private synchronized void doSendCmd(String str) {
        AILog.m4029d(TAG, "begin send cmd " + str, LogLevel.RELEASE);
        long jCurrentTimeMillis = System.currentTimeMillis();
        SerialPortHandler.getInstance().sendSerialPort(str.getBytes(), null);
        AILog.m4028d(TAG, "end send cmd time comsume = " + (System.currentTimeMillis() - jCurrentTimeMillis));
    }

    private void handleMessage(String str) {
        if (str.startsWith(AIObdRealtimeData.RealtimeObdTag) && !this.rtMsgHandled) {
            this.rtMsgHandled = true;
            AIObdRealtimeData aIObdRealtimeData = new AIObdRealtimeData();
            aIObdRealtimeData.fromMessage(str);
            if (aIObdRealtimeData.isValid()) {
                addRealtimeHistory(aIObdRealtimeData);
                if (this.obdErrCount != aIObdRealtimeData.obdErrorCount || !this.hasSendError) {
                    int i = aIObdRealtimeData.obdErrorCount;
                    this.obdErrCount = i;
                    if (i > 0) {
                        send400Cmd();
                    } else if (!this.hasSendError) {
                        this.hasSendError = true;
                        notifyErrorDataListener(null);
                    }
                }
                notifyRealtimeListener(aIObdRealtimeData);
                return;
            }
            return;
        }
        if (str.startsWith(AIObdHistoryData.HistoryDataTag) && !this.hbMsgHandled) {
            this.hasRecvHistoryData = true;
            this.hbMsgHandled = true;
            AIObdHistoryData aIObdHistoryData = new AIObdHistoryData();
            aIObdHistoryData.fromMessage(str);
            if (aIObdHistoryData.isValid()) {
                BasePrefProvider.setObdTimeStamp(BaseModuleApplication.getContext(), System.currentTimeMillis());
                BasePrefProvider.setObdHBT(BaseModuleApplication.getContext(), aIObdHistoryData.message);
                notifyHistoryListener(aIObdHistoryData);
                return;
            }
            return;
        }
        if (str.startsWith(AIObdDrivingStatisticsData.DrivingStatisticsDataTag) && !this.ttMsgHandled) {
            this.ttMsgHandled = true;
            AIObdDrivingStatisticsData aIObdDrivingStatisticsData = new AIObdDrivingStatisticsData();
            aIObdDrivingStatisticsData.fromMessage(str);
            if (aIObdDrivingStatisticsData.isValid()) {
                BasePrefProvider.setObdTimeStamp(BaseModuleApplication.getContext(), System.currentTimeMillis());
                BasePrefProvider.setObdTT(BaseModuleApplication.getContext(), aIObdDrivingStatisticsData.message);
                notifyDrivingStatisticsListener(aIObdDrivingStatisticsData);
                return;
            }
            return;
        }
        if (!str.startsWith(AIObdErrorData.ErrorObdTag) || this.ttMsgHandled) {
            return;
        }
        send401Cmd();
        this.ttMsgHandled = true;
        this.hasSendError = true;
        AIObdErrorData aIObdErrorData = new AIObdErrorData();
        aIObdErrorData.fromMessage(str);
        if (aIObdErrorData.isValid()) {
            notifyErrorDataListener(aIObdErrorData);
        }
    }

    private boolean isSleepMessage(String str) {
        return str.contains(SLEEP_MSG);
    }

    private boolean isValidMsg(String str) {
        return !TextUtils.isEmpty(str) && str.indexOf(AIObdData.Obd_Msg_Start) >= 0 && str.length() >= 5;
    }

    private void notifyDrivingStatisticsListener(AIObdDrivingStatisticsData aIObdDrivingStatisticsData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onDrivingStatisticsMessage(aIObdDrivingStatisticsData);
        }
    }

    private void notifyErrorDataListener(AIObdErrorData aIObdErrorData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onObdErrorMessage(aIObdErrorData);
        }
    }

    private void notifyHistoryListener(AIObdHistoryData aIObdHistoryData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onHistoryMessage(aIObdHistoryData);
        }
    }

    private void notifyMessage(String str) {
        String[] strArrSplit = str.replace("\r\n", StringUtils.f8344LF).replace("\r\r", StringUtils.f8344LF).split(StringUtils.f8344LF);
        this.rtMsgHandled = false;
        this.hbMsgHandled = false;
        this.ttMsgHandled = false;
        if (strArrSplit == null || strArrSplit.length == 0) {
            return;
        }
        for (int i = 0; i < strArrSplit.length; i++) {
            if (!TextUtils.isEmpty(strArrSplit[i])) {
                handleMessage(strArrSplit[i]);
            }
            if (this.rtMsgHandled && this.hbMsgHandled && this.ttMsgHandled) {
                return;
            }
        }
    }

    private void notifyRealtimeListener(AIObdRealtimeData aIObdRealtimeData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onRealtimeMessage(aIObdRealtimeData);
        }
    }

    private void notifyStatusChangeListener(AIObdStatus aIObdStatus) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onObdStatusChange(aIObdStatus);
        }
    }

    private void sendAutoHoldCmd() {
        doSendCmd(AIObdCmd.OBD_AUTO_HOLD_CMD);
    }

    private AIObdRealtimeData simulateRealtime() {
        if (this.realtimeDataArrayList.size() < 2) {
            return null;
        }
        AIObdRealtimeData aIObdRealtimeData = this.realtimeDataArrayList.get(0);
        AIObdRealtimeData aIObdRealtimeData2 = this.realtimeDataArrayList.get(1);
        AIObdRealtimeData aIObdRealtimeData3 = new AIObdRealtimeData();
        aIObdRealtimeData3.obdBattery = aIObdRealtimeData2.obdBattery;
        aIObdRealtimeData3.obdRotateSpeed = aIObdRealtimeData2.obdRotateSpeed;
        float f = aIObdRealtimeData2.obdSpeed;
        aIObdRealtimeData3.obdSpeed = f + ((f - aIObdRealtimeData.obdSpeed) / 2.0f);
        AILog.m4028d("ObdData", "speed=" + aIObdRealtimeData.obdSpeed + "," + aIObdRealtimeData2.obdSpeed + "," + aIObdRealtimeData3.obdSpeed);
        aIObdRealtimeData3.obdThrottleOpenRatio = aIObdRealtimeData2.obdThrottleOpenRatio;
        aIObdRealtimeData3.obdEngineLoadRatio = aIObdRealtimeData2.obdEngineLoadRatio;
        aIObdRealtimeData3.obdCoolantTemprature = aIObdRealtimeData2.obdCoolantTemprature;
        aIObdRealtimeData3.obdRealtimeFuelConsume = aIObdRealtimeData2.obdRealtimeFuelConsume;
        aIObdRealtimeData3.obdAverageFuelConsume = aIObdRealtimeData2.obdAverageFuelConsume;
        aIObdRealtimeData3.obdDrivingRange = aIObdRealtimeData2.obdDrivingRange;
        aIObdRealtimeData3.obdTotalDrivingRange = aIObdRealtimeData2.obdTotalDrivingRange;
        aIObdRealtimeData3.obdFuelConsume = aIObdRealtimeData2.obdFuelConsume;
        aIObdRealtimeData3.obdTotalFuelConsume = aIObdRealtimeData2.obdTotalFuelConsume;
        aIObdRealtimeData3.obdErrorCount = aIObdRealtimeData2.obdErrorCount;
        return aIObdRealtimeData3;
    }

    @Override // com.ileja.serialport.SerialPortCallback
    public void fail() {
    }

    @Override // com.ileja.serialport.SerialPortCallback
    public void readData(byte[] bArr, int i) {
        String str = new String(bArr, 0, i);
        AILog.m4029d("ObdData", str, LogLevel.RELEASE);
        if (!isValidMsg(str)) {
            AIObdRealtimeData aIObdRealtimeDataSimulateRealtime = simulateRealtime();
            if (aIObdRealtimeDataSimulateRealtime != null) {
                notifyRealtimeListener(aIObdRealtimeDataSimulateRealtime);
            }
        } else if (isSleepMessage(str)) {
            this.notRecvDataCount = 0L;
        } else {
            try {
                notifyMessage(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.notRecvDataCount = MAX_RECV_COUNT;
            if (this.aiObdStatus.getStatus() == -1) {
                this.aiObdStatus.setStatus(0L);
                notifyStatusChangeListener(this.aiObdStatus);
                AILog.m4029d("ObdData", OBDAction.OBD_STATUS_CONNECTED, LogLevel.RELEASE);
                sendAutoHoldCmd();
            }
        }
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        if (!this.hasRecvHistoryData && this.aiObdStatus.getStatus() == 0) {
            long j = this.notRecvHistoryDataCount - 1;
            this.notRecvHistoryDataCount = j;
            if (j <= 0) {
                sendHsCmd();
            }
        }
        long j2 = this.notRecvDataCount - 1;
        this.notRecvDataCount = j2;
        if (j2 <= 0) {
            this.notRecvDataCount = 0L;
            if (this.aiObdStatus.getStatus() == 0) {
                this.realtimeDataArrayList.clear();
                this.aiObdStatus.setStatus(-1L);
                notifyStatusChangeListener(this.aiObdStatus);
                AILog.m4029d("ObdData", "OBD_STATUS_DISCONNECTED", LogLevel.RELEASE);
            }
        }
    }

    public void send400Cmd() {
        doSendCmd(AIObdCmd.OBD_ERROR_READ_CMD);
    }

    public void send401Cmd() {
        doSendCmd(AIObdCmd.OBD_ERROR_CLEAR_CMD);
    }

    public void sendCmd(String str) {
        if (this.autoStopTimeSet) {
            return;
        }
        doSendCmd(str);
    }

    public void sendHsCmd() {
        this.notRecvHistoryDataCount = MAX_RECV_COUNT;
        doSendCmd(AIObdCmd.OBD_HS_CMD);
    }

    public void sendRtOffCmd() {
    }

    public void sendRtOnCmd() {
    }

    public synchronized void start() {
        this.running = true;
        bindServer();
        this.autoStopTimeSet = false;
        this.hasSendError = false;
        this.notRecvHistoryDataCount = 0L;
        this.notRecvDataCount = MAX_RECV_COUNT;
        this.hasRecvHistoryData = false;
        this.aiObdStatus.setStatus(-1L);
        this.realtimeDataArrayList.clear();
    }

    public synchronized boolean stop() {
        if (!this.running) {
            return false;
        }
        SerialPortHandler.getInstance().setSerialPortCallback(null);
        sendRtOffCmd();
        this.running = false;
        this.aiObdStatus.setStatus(-1L);
        this.realtimeDataArrayList.clear();
        SerialPortHandler.getInstance().closeSerialPort();
        return true;
    }

    @Override // com.ileja.serialport.SerialPortCallback
    public void success() {
        SerialPortHandler.getInstance().startReceiveMsg();
    }
}