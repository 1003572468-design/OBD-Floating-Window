package com.ileja.aicar.obd.impl.miyuan;

import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.data.AIObdDrivingStatisticsData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdHistoryData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;
import com.ileja.module.BaseModuleApplication;
import com.miyuan.obd.serial.FaultCode;
import com.miyuan.obd.serial.OBDCore;
import com.miyuan.obd.serial.PanelBoardInfo;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class AIMYObdReadThread extends Thread {
    private static final long MAX_READ_TIME = 300000;
    private static final String TAG = "AIMYObd";
    private Set<AIObdListener> aiObdListeners;
    private long lastFaultReadTime = 0;
    private AIObdStatus aiObdStatus = new AIObdStatus();
    private volatile boolean running = false;

    public AIMYObdReadThread(Set<AIObdListener> set) {
        this.aiObdListeners = set;
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

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.running) {
            AILog.m4031e(TAG, "read myobd version:" + OBDCore.getInstance(BaseModuleApplication.getContext()).getVersion(), LogLevel.RELEASE);
        }
        while (this.running) {
            long status = this.aiObdStatus.getStatus();
            boolean zIsConnect = OBDCore.getInstance(BaseModuleApplication.getContext()).isConnect();
            OBDCore.getInstance(BaseModuleApplication.getContext()).isLaunched();
            if (zIsConnect) {
                PanelBoardInfo fixedData = OBDCore.getInstance(BaseModuleApplication.getContext()).getFixedData();
                if (fixedData != null) {
                    this.aiObdStatus.setStatus(0L);
                    AIObdRealtimeData aIObdRealtimeData = new AIObdRealtimeData();
                    aIObdRealtimeData.type = (byte) 1;
                    aIObdRealtimeData.f5953id = "";
                    aIObdRealtimeData.obdBattery = Float.parseFloat(fixedData.getVoltage());
                    aIObdRealtimeData.obdSpeed = Integer.parseInt(fixedData.getSpeed());
                    aIObdRealtimeData.obdRotateSpeed = Integer.parseInt(fixedData.getRotationRate());
                    aIObdRealtimeData.obdCoolantTemprature = Integer.parseInt(fixedData.getTemperature());
                    aIObdRealtimeData.obdOilLeft = Integer.parseInt(fixedData.getResidualFuel());
                    aIObdRealtimeData.obdAverageFuelConsume = Float.parseFloat(fixedData.getAverageFuelConsumption());
                    aIObdRealtimeData.obdRealtimeFuelConsume = Float.parseFloat(fixedData.getInstantaneousFuelConsumption());
                    aIObdRealtimeData.obdFuelConsume = Float.parseFloat(fixedData.getTotalFuelConsumptionDuringThisTrip());
                    aIObdRealtimeData.obdDrivingRange = Float.parseFloat(fixedData.getMileageOfTrip());
                    AILog.m4029d(TAG, fixedData.toString(), LogLevel.RELEASE);
                    notifyRealtimeListener(aIObdRealtimeData);
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (jCurrentTimeMillis - this.lastFaultReadTime > MAX_READ_TIME) {
                    this.lastFaultReadTime = jCurrentTimeMillis;
                    List<FaultCode> faultCode = OBDCore.getInstance(BaseModuleApplication.getContext()).getFaultCode();
                    if (faultCode == null || faultCode.size() <= 0) {
                        notifyErrorDataListener(null);
                    } else {
                        AIObdErrorData aIObdErrorData = new AIObdErrorData();
                        int size = faultCode.size();
                        aIObdErrorData.errCount = size;
                        aIObdErrorData.errCodes = new String[size];
                        for (int i = 0; i < aIObdErrorData.errCount; i++) {
                            aIObdErrorData.errCodes[i] = faultCode.get(i).getId();
                        }
                        AILog.m4029d(TAG, aIObdErrorData.toString(), LogLevel.RELEASE);
                        notifyErrorDataListener(aIObdErrorData);
                        OBDCore.getInstance(BaseModuleApplication.getContext()).cleanFaultCode();
                    }
                }
            } else {
                this.aiObdStatus.setStatus(-1L);
            }
            if (this.aiObdStatus.getStatus() != status) {
                notifyStatusChangeListener(this.aiObdStatus);
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    public synchronized void start_() {
        this.running = true;
        start();
        boolean zOpen = OBDCore.getInstance(BaseModuleApplication.getContext()).open("/dev/ttyMT1");
        this.aiObdStatus.setStatus(-1L);
        AILog.m4031e(TAG, "start open myobod tty ,open:" + zOpen, LogLevel.RELEASE);
    }

    public synchronized boolean stop_() {
        if (!this.running) {
            return false;
        }
        OBDCore.getInstance(BaseModuleApplication.getContext()).close();
        this.running = false;
        interrupt();
        this.aiObdStatus.setStatus(-1L);
        return true;
    }
}