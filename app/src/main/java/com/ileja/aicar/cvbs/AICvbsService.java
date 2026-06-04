package com.ileja.aicar.cvbs;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.service.BaseRemoteService;
import com.ileja.aicar.C1103R;
import com.ileja.aicar.cvbs.ICvbsService;

/* JADX INFO: loaded from: classes.dex */
public class AICvbsService extends BaseRemoteService {
    private static final String TAG = "AICvbsService";
    private AICvbsManager cvbsManager;

    private class ICvbsServiceBinder extends ICvbsService.Stub {
        private ICvbsServiceBinder() {
        }

        @Override // com.ileja.aicar.cvbs.ICvbsService
        public void send(String str, final ICvbsCallback iCvbsCallback) {
            if (AICvbsService.this.cvbsManager != null) {
                AICvbsService.this.cvbsManager.sendCommand(str, new AICvbsCallback(this) { // from class: com.ileja.aicar.cvbs.AICvbsService.ICvbsServiceBinder.1
                    @Override // com.ileja.aicar.cvbs.AICvbsCallback
                    public void onResult(String str2) {
                        ICvbsCallback iCvbsCallback2 = iCvbsCallback;
                        if (iCvbsCallback2 != null) {
                            try {
                                iCvbsCallback2.onResult(str2);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
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
        return new ICvbsServiceBinder();
    }

    @Override // com.ileja.aibase.service.BaseRemoteService, com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.cvbsManager = AICvbsManager.getInst();
        AILog.m4029d(TAG, "onCreate()", LogLevel.RELEASE);
    }

    @Override // com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        AILog.m4029d(TAG, "onDestroy()", LogLevel.RELEASE);
    }
}