package com.ileja.aicar.tirepressure;

import android.content.Intent;
import android.os.IBinder;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.service.BaseRemoteService;
import com.ileja.aicar.C1103R;
import com.ileja.aicar.tire.ITirePressureService;
import com.ileja.aicar.tire.listener.ITirePressureListener;

/* JADX INFO: loaded from: classes.dex */
public class TirePressureService extends BaseRemoteService {
    private static final String TAG = "TirePressureService";

    private class TPServiceBinder extends ITirePressureService.Stub {
        private TPServiceBinder(TirePressureService tirePressureService) {
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void bindTireDevice(String str) {
            AILog.m4028d(TirePressureService.TAG, "bindTireDevice");
            TirePressureManager.getInstance().bindTireDevice(str);
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void requestTirePressure(ITirePressureListener iTirePressureListener) {
            TirePressureManager.getInstance().start(iTirePressureListener);
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void stopTirePressure() {
            TirePressureManager.getInstance().stop();
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void unbindTireDevice() {
            TirePressureManager.getInstance().unbindTireDevice();
        }
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
        return new TPServiceBinder();
    }

    @Override // com.ileja.aibase.service.BaseRemoteService, com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.ileja.aibase.service.KeepLiveService, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    @Override // com.ileja.aibase.service.BaseRemoteService, android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}