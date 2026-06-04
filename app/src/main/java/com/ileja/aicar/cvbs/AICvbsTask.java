package com.ileja.aicar.cvbs;

import android.os.RemoteException;
import android.text.TextUtils;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class AICvbsTask implements Runnable {
    private static final String TAG = "CVBSClient";
    private AICvbsCallback callback;
    private String cmd;
    private CVBSServer server;

    public AICvbsTask(CVBSServer cVBSServer, String str, AICvbsCallback aICvbsCallback) {
        this.server = cVBSServer;
        this.cmd = str;
        this.callback = aICvbsCallback;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            AILog.m4029d(TAG, "send command = " + this.cmd, LogLevel.RELEASE);
            if (this.server != null) {
                this.server.sendCmd(this.cmd);
                this.server.flush();
            }
            boolean z = true;
            int i = 0;
            while (z) {
                try {
                    strRecvMsg = this.server != null ? this.server.recvMsg() : null;
                    AILog.m4029d(TAG, "recv result = " + strRecvMsg, LogLevel.RELEASE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if ((TextUtils.isEmpty(strRecvMsg) || TextUtils.equals("-1", strRecvMsg)) && i < 5) {
                    i++;
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                } else if (this.callback != null) {
                    AILog.m4029d(TAG, "callback result = " + strRecvMsg, LogLevel.RELEASE);
                    this.callback.onResult(strRecvMsg);
                    z = false;
                }
            }
        } catch (RemoteException e3) {
            e3.printStackTrace();
        }
    }
}