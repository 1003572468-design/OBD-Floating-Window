package com.ileja.aicar;

import com.ileja.module.BaseModuleApplication;

/* JADX INFO: loaded from: classes.dex */
public class AICarApplication extends BaseModuleApplication {
    @Override // com.ileja.module.BaseModuleApplication
    public void onCreate() {
        GlobalService.loadService(BaseModuleApplication.f6581a);
    }

    @Override // com.ileja.module.BaseModuleApplication
    public void onTerminate() {
        GlobalService.unloadService(BaseModuleApplication.f6581a);
    }
}