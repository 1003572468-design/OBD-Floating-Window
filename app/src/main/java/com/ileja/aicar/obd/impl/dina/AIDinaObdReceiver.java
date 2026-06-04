package com.ileja.aicar.obd.impl.dina;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.cpsdna.obdports.ports.OBDAttachKey;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.GlobalService;
import com.ileja.aicar.obd.data.AIObdDataMessage;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.ailbs.location.provider.AIBaseLocationProvider;
import com.ileja.core.data.CommonConfig;
import com.ileja.core.p007sp.BasePrefProvider;
import com.ileja.module.BaseModuleApplication;

/* JADX INFO: loaded from: classes.dex */
public class AIDinaObdReceiver extends BroadcastReceiver {
    private static final String ACTION_DATA = "com.cpsdna.obdport.data";
    private static final String ACTION_VIN = "OBD_VIN";
    private static final int MAX_INVALID_COUNT = 5;
    public static final int OIL_METHOD_TIME = 1;
    public static final int OIL_METHOD_TOTAL = 0;
    private static float averageOilConsume = 0.0f;
    private static boolean hasSendError = false;
    private static int invalidRotateSpeedCount = 0;
    private static int invalidSpeedCount = 0;
    private static long lastCalcTime = 0;
    private static int lastRoateSpeed = 0;
    private static int lastSpeed = 0;
    private static int obdErrCount = -1;
    private static String obdID = null;
    public static int oilMethod = 0;
    private static int totalMiles = -1;
    private static int totalOil = -1;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!ACTION_DATA.equals(action)) {
            if (TextUtils.equals("OBD_VIN", action)) {
                String stringExtra = intent.getStringExtra("OBD_VIN");
                if (!TextUtils.isEmpty(stringExtra)) {
                    BasePrefProvider.setCarVinCode(BaseModuleApplication.getApplication(), stringExtra);
                }
                AILog.m4029d("AIOBD", "广播发送 OBD_VIN_CODE = " + stringExtra, LogLevel.RELEASE);
                return;
            }
            return;
        }
        Bundle extras = intent.getExtras();
        if (obdID == null) {
            obdID = BasePrefProvider.getOBDId(context);
        }
        String string = extras.getString(OBDAttachKey.ObdId);
        AILog.m4029d("AIOBD", "OBD=" + string, LogLevel.RELEASE);
        if (!TextUtils.equals(obdID, string) && !TextUtils.isEmpty(string)) {
            obdID = string;
            totalMiles = -1;
            totalOil = -1;
            BasePrefProvider.setOBDId(context, string);
        }
        String string2 = extras.getString(OBDAttachKey.OBDRequirements);
        AILog.m4029d("AIOBD", "OBD类型=" + string2, LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "故障码状态=" + extras.getInt(OBDAttachKey.MalfunctionIndicatorLampStatus), LogLevel.RELEASE);
        int i = extras.getInt(OBDAttachKey.DTCStoredInthisECU);
        AILog.m4029d("AIOBD", "发动机故障码个数=" + i, LogLevel.RELEASE);
        String[] stringArray = extras.getStringArray(OBDAttachKey.TroubleCodeArray);
        if (stringArray != null && stringArray.length > 0) {
            for (String str : stringArray) {
                AILog.m4029d("AIOBD", "发动机故障码=" + str, LogLevel.RELEASE);
            }
        }
        Float fValueOf = Float.valueOf(extras.getFloat(OBDAttachKey.VehicleVoltage));
        AILog.m4029d("AIOBD", "电瓶电压=" + fValueOf, LogLevel.RELEASE);
        int i2 = extras.getInt(OBDAttachKey.EngineRPM);
        AILog.m4029d("AIOBD", "发动机转速=" + i2, LogLevel.RELEASE);
        if (i2 == 0) {
            int i3 = invalidRotateSpeedCount + 1;
            invalidRotateSpeedCount = i3;
            if (i3 <= 5) {
                int i4 = lastRoateSpeed;
                if (i4 > 0) {
                    i2 = i4;
                }
            } else {
                lastRoateSpeed = i2;
            }
        } else {
            invalidRotateSpeedCount = 0;
            lastRoateSpeed = i2;
        }
        int i5 = extras.getInt(OBDAttachKey.VehicleSpeed);
        AILog.m4029d("AIOBD", "车辆速度=" + i5, LogLevel.RELEASE);
        if (i5 == 0) {
            int i6 = invalidSpeedCount + 1;
            invalidSpeedCount = i6;
            if (i6 <= 5) {
                int i7 = lastSpeed;
                if (i7 > 0) {
                    i5 = i7;
                }
            } else {
                lastSpeed = i5;
            }
        } else {
            invalidSpeedCount = 0;
            lastSpeed = i5;
        }
        AILog.m4029d("AIOBD", "进气口温度=" + extras.getInt(OBDAttachKey.IntakeAirTemperature), LogLevel.RELEASE);
        int i8 = extras.getInt(OBDAttachKey.EngineCoolantTemperature);
        AILog.m4029d("AIOBD", "水箱温度=" + i8, LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "车辆环境温度=" + extras.getInt(OBDAttachKey.EnvironmentTemperature), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "进气歧管压力=" + extras.getInt(OBDAttachKey.IntakeManifoldPressure), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "燃油压力=" + extras.getInt(OBDAttachKey.FuelPressure), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "大气压力=" + extras.getInt(OBDAttachKey.BarometricPressure), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "空气流量=" + Float.valueOf(extras.getFloat(OBDAttachKey.AirFlowSensor)), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "绝对节气门位置传感器=" + Float.valueOf(extras.getFloat(OBDAttachKey.ThrottlePositionSensor)), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "油门踏板位置=" + Float.valueOf(extras.getFloat(OBDAttachKey.AcceleratorPedalPosition)), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "发动机启动后运行时间=" + extras.getInt(OBDAttachKey.EngineRunTime), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "故障行驶里程=" + extras.getInt(OBDAttachKey.FaultVehicleMileage), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "剩余油量=" + Float.valueOf(extras.getFloat(OBDAttachKey.OilMassFuelLevel)), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "发动机负荷=" + extras.getInt(OBDAttachKey.CalculatedEngineLoad), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "长期燃油修正=" + Float.valueOf(extras.getFloat(OBDAttachKey.LongTermFuelTrimBank1)), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "点火提前角=" + Float.valueOf(extras.getFloat(OBDAttachKey.SparkAngleBeforeTDC)), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "左前胎压=" + extras.getInt(OBDAttachKey.TirePressueLF), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "左前胎压=" + extras.getInt(OBDAttachKey.TirePressueRF), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "左后胎压=" + extras.getInt(OBDAttachKey.TirePressueLB), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "右后胎压=" + extras.getInt(OBDAttachKey.TirePressueRB), LogLevel.RELEASE);
        AILog.m4029d("AIOBD", "总里程类型=" + extras.getInt(OBDAttachKey.MileageFuelFlag), LogLevel.RELEASE);
        int i9 = extras.getInt(OBDAttachKey.TotalMileage);
        AILog.m4029d("AIOBD", "行驶里程=" + i9, LogLevel.RELEASE);
        int i10 = extras.getInt(OBDAttachKey.TotalFuelConsumption);
        AILog.m4029d("AIOBD", "总的燃油消耗量=" + i10, LogLevel.RELEASE);
        if (totalMiles == -1 && i9 > 0) {
            totalMiles = i9;
            AILog.m4029d("AIOBD", "初始总里程=" + totalMiles, LogLevel.RELEASE);
        }
        if (totalOil == -1 && i10 > 0) {
            totalOil = i10;
            AILog.m4029d("AIOBD", "初始总油耗=" + totalOil, LogLevel.RELEASE);
        }
        if (System.currentTimeMillis() - lastCalcTime > AIBaseLocationProvider.LOCATION_INVALID_TIME) {
            lastCalcTime = System.currentTimeMillis();
            i9 -= totalMiles;
            i10 -= totalOil;
            AILog.m4029d("AIOBD", "计算后的总油耗=" + i10 + ",计算后的总里程=" + i9, LogLevel.RELEASE);
            averageOilConsume = extras.getFloat(OBDAttachKey.AVERAGE_FUEL);
            if (i9 > 0) {
                float f = i10 / (i9 / 100.0f);
                AILog.m4029d("AIOBD", "计算的平均油耗=" + f + ",方法=" + oilMethod, LogLevel.RELEASE);
                if (oilMethod == 1) {
                    averageOilConsume = f;
                }
            }
            AILog.m4029d("AIOBD", "平均油耗=" + averageOilConsume, LogLevel.RELEASE);
            if (averageOilConsume > 39.0f) {
                averageOilConsume = 39.0f;
            }
        }
        Float fValueOf2 = Float.valueOf(extras.getFloat(OBDAttachKey.INSTANT_FUEL));
        AILog.m4029d("AIOBD", "瞬时油耗=" + fValueOf2, LogLevel.RELEASE);
        if (fValueOf2.floatValue() > 99.0f) {
            fValueOf2 = Float.valueOf(99.0f);
        }
        if (string2 == null && i5 <= 0 && i2 <= 0) {
            AILog.m4029d("AIOBD", "无效OBD数据!!!!", LogLevel.RELEASE);
            return;
        }
        AILog.m4029d("AIOBD", "=============================", LogLevel.RELEASE);
        AIObdRealtimeData aIObdRealtimeData = new AIObdRealtimeData();
        aIObdRealtimeData.type = (byte) 0;
        aIObdRealtimeData.f5953id = string;
        aIObdRealtimeData.obdBattery = fValueOf.floatValue();
        aIObdRealtimeData.obdSpeed = i5;
        aIObdRealtimeData.obdRotateSpeed = i2;
        aIObdRealtimeData.obdCoolantTemprature = i8;
        aIObdRealtimeData.obdAverageFuelConsume = averageOilConsume;
        aIObdRealtimeData.obdRealtimeFuelConsume = fValueOf2.floatValue();
        aIObdRealtimeData.obdDrivingRange = i9;
        aIObdRealtimeData.obdFuelConsume = i10;
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConfig.Data, aIObdRealtimeData);
        GlobalService.getDataService().send(AIObdDataMessage.OBD_REAL_MESSAGE_FILTER, bundle, true);
        if (obdErrCount != i) {
            if (i == 0) {
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable(CommonConfig.Data, null);
                GlobalService.getDataService().send(AIObdDataMessage.OBD_ERROR_MESSAGE_FILTER, bundle2, true);
            } else if (stringArray != null && stringArray.length > 0 && !hasSendError) {
                hasSendError = true;
                AIObdErrorData aIObdErrorData = new AIObdErrorData();
                aIObdErrorData.errCount = i;
                aIObdErrorData.errCodes = stringArray;
                Bundle bundle3 = new Bundle();
                bundle3.putParcelable(CommonConfig.Data, aIObdErrorData);
                GlobalService.getDataService().send(AIObdDataMessage.OBD_ERROR_MESSAGE_FILTER, bundle3, true);
            }
            obdErrCount = i;
        }
    }
}