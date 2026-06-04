package com.ileja.framework;

import android.app.Application;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.bluetoothext.HFBluetoothExtService;
import com.ileja.framework.service.car.HFObdService;
import com.ileja.framework.service.car.HFTirePressureService;
import com.ileja.framework.service.core.HFBeepPlayerService;
import com.ileja.framework.service.core.HFDataService;
import com.ileja.framework.service.core.HFLogService;
import com.ileja.framework.service.core.HFPageAnalyseService;
import com.ileja.framework.service.lbs.HFEDogService;
import com.ileja.framework.service.lbs.HFLocationService;
import com.ileja.framework.service.lbs.HFQueryService;
import com.ileja.framework.service.lbs.HFRouteService;
import com.ileja.framework.service.telcomm.HFFmTxService;
import com.ileja.framework.service.telcomm.HFIpMsgService;
import com.ileja.framework.service.telcomm.HFMusicPlayService;
import com.ileja.framework.service.telcomm.HFMusicQueryService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class HudSystem {
    public static final String TAG = "HudSystem";
    private static HashMap<HFService.HFServiceName, HFService> services = new HashMap<>();

    private static final HFService createService(Application application, HFService.HFServiceName hFServiceName) {
        AILog.m4028d(TAG, "start service");
        if (HFService.HFServiceName.HFLocationService.equals(hFServiceName)) {
            HFLocationService hFLocationService = new HFLocationService();
            AILog.m4028d(TAG, "start location service");
            hFLocationService.create(application);
            services.put(hFLocationService.getName(), hFLocationService);
            return hFLocationService;
        }
        if (HFService.HFServiceName.HFEDogService.equals(hFServiceName)) {
            HFEDogService hFEDogService = new HFEDogService();
            AILog.m4028d(TAG, "start edog service");
            hFEDogService.create(application);
            services.put(hFEDogService.getName(), hFEDogService);
            return hFEDogService;
        }
        if (HFService.HFServiceName.HFBeepPlayerService.equals(hFServiceName)) {
            HFBeepPlayerService hFBeepPlayerService = new HFBeepPlayerService();
            AILog.m4028d(TAG, "start beepplayer service");
            hFBeepPlayerService.create(application);
            services.put(hFBeepPlayerService.getName(), hFBeepPlayerService);
            return hFBeepPlayerService;
        }
        if (HFService.HFServiceName.HFQueryService.equals(hFServiceName)) {
            HFQueryService hFQueryService = new HFQueryService();
            AILog.m4028d(TAG, "start query service");
            hFQueryService.create(application);
            services.put(hFQueryService.getName(), hFQueryService);
            return hFQueryService;
        }
        if (HFService.HFServiceName.HFRouteService.equals(hFServiceName)) {
            HFRouteService hFRouteService = new HFRouteService();
            AILog.m4028d(TAG, "start route service");
            hFRouteService.create(application);
            services.put(hFRouteService.getName(), hFRouteService);
            return hFRouteService;
        }
        if (HFService.HFServiceName.HFDataService.equals(hFServiceName)) {
            HFDataService hFDataService = new HFDataService();
            AILog.m4028d(TAG, "start data service");
            hFDataService.create(application);
            services.put(hFDataService.getName(), hFDataService);
            return hFDataService;
        }
        if (HFService.HFServiceName.HFObdService.equals(hFServiceName)) {
            HFObdService hFObdService = new HFObdService();
            AILog.m4028d(TAG, "start obd service");
            hFObdService.create(application);
            services.put(hFObdService.getName(), hFObdService);
            return hFObdService;
        }
        if (HFService.HFServiceName.HFIpMsgService.equals(hFServiceName)) {
            HFIpMsgService hFIpMsgService = new HFIpMsgService();
            AILog.m4028d(TAG, "start ipmsg service");
            hFIpMsgService.create(application);
            services.put(hFIpMsgService.getName(), hFIpMsgService);
            return hFIpMsgService;
        }
        if (HFService.HFServiceName.HFLogService.equals(hFServiceName)) {
            HFLogService hFLogService = new HFLogService();
            AILog.m4028d(TAG, "start log service");
            hFLogService.create(application);
            services.put(hFLogService.getName(), hFLogService);
            return hFLogService;
        }
        if (HFService.HFServiceName.HFFmTxService.equals(hFServiceName)) {
            HFFmTxService hFFmTxService = new HFFmTxService();
            AILog.m4028d(TAG, "start fmtx service");
            hFFmTxService.create(application);
            services.put(hFFmTxService.getName(), hFFmTxService);
            return hFFmTxService;
        }
        if (HFService.HFServiceName.HFPageAnalyseService.equals(hFServiceName)) {
            HFPageAnalyseService hFPageAnalyseService = new HFPageAnalyseService();
            AILog.m4028d(TAG, "start page analyse service");
            hFPageAnalyseService.create(application);
            services.put(hFPageAnalyseService.getName(), hFPageAnalyseService);
            return hFPageAnalyseService;
        }
        if (HFService.HFServiceName.HFBluetoothExtService.equals(hFServiceName)) {
            HFBluetoothExtService hFBluetoothExtService = new HFBluetoothExtService();
            AILog.m4028d(TAG, "start aibt service");
            hFBluetoothExtService.create(application);
            services.put(hFBluetoothExtService.getName(), hFBluetoothExtService);
            return hFBluetoothExtService;
        }
        if (HFService.HFServiceName.HFMusicQueryService.equals(hFServiceName)) {
            HFMusicQueryService hFMusicQueryService = new HFMusicQueryService();
            AILog.m4028d(TAG, "start music query service");
            hFMusicQueryService.create(application);
            services.put(hFMusicQueryService.getName(), hFMusicQueryService);
            return hFMusicQueryService;
        }
        if (HFService.HFServiceName.HFMusicPlayService.equals(hFServiceName)) {
            HFMusicPlayService hFMusicPlayService = new HFMusicPlayService();
            AILog.m4034i(TAG, "start music play service");
            hFMusicPlayService.create(application);
            services.put(hFMusicPlayService.getName(), hFMusicPlayService);
            return hFMusicPlayService;
        }
        if (!HFService.HFServiceName.HFTirePressureService.equals(hFServiceName)) {
            return null;
        }
        HFTirePressureService hFTirePressureService = new HFTirePressureService();
        AILog.m4034i(TAG, "start tirePressureService");
        hFTirePressureService.create(application);
        services.put(hFTirePressureService.getName(), hFTirePressureService);
        return hFTirePressureService;
    }

    public static final HFService getService(Application application, HFService.HFServiceName hFServiceName) {
        HFService hFService = services.get(hFServiceName);
        return hFService != null ? hFService : createService(application, hFServiceName);
    }

    public static void load(Application application) {
        AILog.m4028d(TAG, "on system load");
        synchronized (services) {
            createService(application, HFService.HFServiceName.HFLocationService);
            createService(application, HFService.HFServiceName.HFDataService);
        }
        AILog.m4029d(TAG, "on system load over", LogLevel.RELEASE);
    }

    public static void unload(Application application) {
        AILog.m4029d(TAG, "on system unload", LogLevel.RELEASE);
        synchronized (services) {
            Iterator<Map.Entry<HFService.HFServiceName, HFService>> it = services.entrySet().iterator();
            while (it.hasNext()) {
                HFService value = it.next().getValue();
                if (value != null) {
                    value.destroy(application);
                }
            }
            services.clear();
        }
    }
}