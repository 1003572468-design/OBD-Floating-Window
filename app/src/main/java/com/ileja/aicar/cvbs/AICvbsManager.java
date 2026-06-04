package com.ileja.aicar.cvbs;

import android.p000os.ServiceManager;
import android.util.Log;
import com.ileja.aicar.cvbs.CVBSServer;

/* JADX INFO: loaded from: classes.dex */
public class AICvbsManager {
    private static final String CVBS_SERVICE = "cvbs_service";
    private static volatile AICvbsManager inst;
    private CVBSServer cvbsServer;

    private AICvbsManager() {
        bindServer();
    }

    private void bindServer() {
        try {
            this.cvbsServer = CVBSServer.Stub.asInterface(ServiceManager.getService(CVBS_SERVICE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AICvbsManager getInst() {
        if (inst == null) {
            synchronized (AICvbsManager.class) {
                if (inst == null) {
                    inst = new AICvbsManager();
                }
            }
        }
        return inst;
    }

    public void sendCommand(String str, AICvbsCallback aICvbsCallback) {
        AICvbsTaskExecutor.getInst().addCvbsTask(new AICvbsTask(this.cvbsServer, str, aICvbsCallback));
    }

    public void testcommand() {
        new Thread(new AICvbsTask(this.cvbsServer, AICvbsCommand.CMD_IMAGE, new AICvbsCallback(this) { // from class: com.ileja.aicar.cvbs.AICvbsManager.1
            @Override // com.ileja.aicar.cvbs.AICvbsCallback
            public void onResult(String str) {
                Log.d("CVBSClient", "got result = " + str);
            }
        })).start();
    }
}