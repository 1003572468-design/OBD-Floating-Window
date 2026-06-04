package com.ileja.framework;

import android.app.Application;
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

/* JADX INFO: loaded from: classes.dex */
public class HudModuleSystem {
    private static HFBeepPlayerService hfBeepPlayerService;
    private static HFBluetoothExtService hfBluetoothExtService;
    private static HFDataService hfDataService;
    private static HFFmTxService hfFmTxService;
    private static HFIpMsgService hfIpMsgService;
    private static HFLocationService hfLocationService;
    private static HFLogService hfLogService;
    private static HFMusicPlayService hfMusicPlayService;
    private static HFMusicQueryService hfMusicQueryService;
    private static HFObdService hfObdService;
    private static HFPageAnalyseService hfPageAnalyseService;
    private static HFQueryService hfQueryService;
    private static HFRouteService hfRouteService;
    private static HFTirePressureService hfTirePressureService;
    private static HFEDogService hfeDogService;
    private static Object lock = new Object();
    private static Application outContext;

    public static HFBeepPlayerService getBeepPlayerService() {
        HFBeepPlayerService hFBeepPlayerService;
        HFBeepPlayerService hFBeepPlayerService2 = hfBeepPlayerService;
        if (hFBeepPlayerService2 != null) {
            return hFBeepPlayerService2;
        }
        synchronized (lock) {
            hFBeepPlayerService = (HFBeepPlayerService) HudSystem.getService(outContext, HFService.HFServiceName.HFBeepPlayerService);
            hfBeepPlayerService = hFBeepPlayerService;
        }
        return hFBeepPlayerService;
    }

    public static HFBluetoothExtService getBluetoothExtService() {
        HFBluetoothExtService hFBluetoothExtService;
        HFBluetoothExtService hFBluetoothExtService2 = hfBluetoothExtService;
        if (hFBluetoothExtService2 != null) {
            return hFBluetoothExtService2;
        }
        synchronized (lock) {
            hFBluetoothExtService = (HFBluetoothExtService) HudSystem.getService(outContext, HFService.HFServiceName.HFBluetoothExtService);
            hfBluetoothExtService = hFBluetoothExtService;
        }
        return hFBluetoothExtService;
    }

    public static HFDataService getDataService() {
        HFDataService hFDataService;
        HFDataService hFDataService2 = hfDataService;
        if (hFDataService2 != null) {
            return hFDataService2;
        }
        synchronized (lock) {
            hFDataService = (HFDataService) HudSystem.getService(outContext, HFService.HFServiceName.HFDataService);
            hfDataService = hFDataService;
        }
        return hFDataService;
    }

    public static HFEDogService getEDogService() {
        HFEDogService hFEDogService;
        HFEDogService hFEDogService2 = hfeDogService;
        if (hFEDogService2 != null) {
            return hFEDogService2;
        }
        synchronized (lock) {
            hFEDogService = (HFEDogService) HudSystem.getService(outContext, HFService.HFServiceName.HFEDogService);
            hfeDogService = hFEDogService;
        }
        return hFEDogService;
    }

    public static HFFmTxService getFmTxService() {
        HFFmTxService hFFmTxService;
        HFFmTxService hFFmTxService2 = hfFmTxService;
        if (hFFmTxService2 != null) {
            return hFFmTxService2;
        }
        synchronized (lock) {
            hFFmTxService = (HFFmTxService) HudSystem.getService(outContext, HFService.HFServiceName.HFFmTxService);
            hfFmTxService = hFFmTxService;
        }
        return hFFmTxService;
    }

    public static HFIpMsgService getIpMsgService() {
        HFIpMsgService hFIpMsgService;
        HFIpMsgService hFIpMsgService2 = hfIpMsgService;
        if (hFIpMsgService2 != null) {
            return hFIpMsgService2;
        }
        synchronized (lock) {
            hFIpMsgService = (HFIpMsgService) HudSystem.getService(outContext, HFService.HFServiceName.HFIpMsgService);
            hfIpMsgService = hFIpMsgService;
        }
        return hFIpMsgService;
    }

    public static HFLocationService getLocationService() {
        HFLocationService hFLocationService;
        HFLocationService hFLocationService2 = hfLocationService;
        if (hFLocationService2 != null) {
            return hFLocationService2;
        }
        synchronized (lock) {
            hFLocationService = (HFLocationService) HudSystem.getService(outContext, HFService.HFServiceName.HFLocationService);
            hfLocationService = hFLocationService;
        }
        return hFLocationService;
    }

    public static HFLogService getLogService() {
        HFLogService hFLogService;
        HFLogService hFLogService2 = hfLogService;
        if (hFLogService2 != null) {
            return hFLogService2;
        }
        synchronized (lock) {
            hFLogService = (HFLogService) HudSystem.getService(outContext, HFService.HFServiceName.HFLogService);
            hfLogService = hFLogService;
        }
        return hFLogService;
    }

    public static HFMusicPlayService getMusicPlayService() {
        HFMusicPlayService hFMusicPlayService;
        HFMusicPlayService hFMusicPlayService2 = hfMusicPlayService;
        if (hFMusicPlayService2 != null) {
            return hFMusicPlayService2;
        }
        synchronized (lock) {
            hFMusicPlayService = (HFMusicPlayService) HudSystem.getService(outContext, HFService.HFServiceName.HFMusicPlayService);
            hfMusicPlayService = hFMusicPlayService;
        }
        return hFMusicPlayService;
    }

    public static HFMusicQueryService getMusicQueryService() {
        HFMusicQueryService hFMusicQueryService;
        HFMusicQueryService hFMusicQueryService2 = hfMusicQueryService;
        if (hFMusicQueryService2 != null) {
            return hFMusicQueryService2;
        }
        synchronized (lock) {
            hFMusicQueryService = (HFMusicQueryService) HudSystem.getService(outContext, HFService.HFServiceName.HFMusicQueryService);
            hfMusicQueryService = hFMusicQueryService;
        }
        return hFMusicQueryService;
    }

    public static HFObdService getObdService() {
        HFObdService hFObdService;
        HFObdService hFObdService2 = hfObdService;
        if (hFObdService2 != null) {
            return hFObdService2;
        }
        synchronized (lock) {
            hFObdService = (HFObdService) HudSystem.getService(outContext, HFService.HFServiceName.HFObdService);
            hfObdService = hFObdService;
        }
        return hFObdService;
    }

    public static HFPageAnalyseService getPageAnalyseService() {
        HFPageAnalyseService hFPageAnalyseService;
        HFPageAnalyseService hFPageAnalyseService2 = hfPageAnalyseService;
        if (hFPageAnalyseService2 != null) {
            return hFPageAnalyseService2;
        }
        synchronized (lock) {
            hFPageAnalyseService = (HFPageAnalyseService) HudSystem.getService(outContext, HFService.HFServiceName.HFPageAnalyseService);
            hfPageAnalyseService = hFPageAnalyseService;
        }
        return hFPageAnalyseService;
    }

    public static HFQueryService getQueryService() {
        HFQueryService hFQueryService;
        HFQueryService hFQueryService2 = hfQueryService;
        if (hFQueryService2 != null) {
            return hFQueryService2;
        }
        synchronized (lock) {
            hFQueryService = (HFQueryService) HudSystem.getService(outContext, HFService.HFServiceName.HFQueryService);
            hfQueryService = hFQueryService;
        }
        return hFQueryService;
    }

    public static HFRouteService getRouteService() {
        HFRouteService hFRouteService;
        HFRouteService hFRouteService2 = hfRouteService;
        if (hFRouteService2 != null) {
            return hFRouteService2;
        }
        synchronized (lock) {
            hFRouteService = (HFRouteService) HudSystem.getService(outContext, HFService.HFServiceName.HFRouteService);
            hfRouteService = hFRouteService;
        }
        return hFRouteService;
    }

    public static HFTirePressureService getTirePressureService() {
        HFTirePressureService hFTirePressureService;
        HFTirePressureService hFTirePressureService2 = hfTirePressureService;
        if (hFTirePressureService2 != null) {
            return hFTirePressureService2;
        }
        synchronized (lock) {
            hFTirePressureService = (HFTirePressureService) HudSystem.getService(outContext, HFService.HFServiceName.HFTirePressureService);
            hfTirePressureService = hFTirePressureService;
        }
        return hFTirePressureService;
    }

    public static void init(Application application) {
        outContext = application;
    }

    public static void loadMainModuleService(Application application) {
        outContext = application;
        synchronized (lock) {
            HudSystem.load(application);
        }
        getEDogService();
        getBeepPlayerService();
        getLocationService();
        getQueryService();
        getObdService();
        getDataService();
        getLogService();
        getBluetoothExtService();
        getPageAnalyseService();
        getTirePressureService();
    }

    public static void loadNaviModuleService(Application application) {
        outContext = application;
        synchronized (lock) {
            HudSystem.load(application);
        }
        getEDogService();
        getBeepPlayerService();
        getLocationService();
        getQueryService();
        getRouteService();
        getObdService();
        getDataService();
        getLogService();
        getBluetoothExtService();
        getPageAnalyseService();
    }

    public static void unloadAllSystemService(Application application) {
        synchronized (lock) {
            HudSystem.unload(application);
        }
    }
}