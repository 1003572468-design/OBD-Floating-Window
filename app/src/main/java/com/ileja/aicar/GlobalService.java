package com.ileja.aicar;

import android.app.Application;
import com.ileja.aibase.common.AILog;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.core.HFDataService;

/* JADX INFO: loaded from: classes.dex */
public class GlobalService {
    private static final String TAG = "GlobalService";
    private static HFDataService hfDataService;

    public static HFDataService getDataService() {
        return hfDataService;
    }

    public static void loadService(Application application) {
        hfDataService = (HFDataService) HudSystem.getService(application, HFService.HFServiceName.HFDataService);
        AILog.m4028d(TAG, "loadService");
    }

    public static void unloadService(Application application) {
        HudSystem.unload(application);
        AILog.m4028d(TAG, "unloadService");
    }
}