package com.ileja.aicar.obd.impl.leja;

import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.impl.AIObdClient;
import com.ileja.module.BaseModuleApplication;

/* JADX INFO: loaded from: classes.dex */
public class AILejaObdClient extends AIObdClient {
    private static final String TAG = "AILejaObcClient";
    private AILejaObdManger aiObdManger;

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void close() {
        AILejaObdManger aILejaObdManger = this.aiObdManger;
        if (aILejaObdManger != null) {
            aILejaObdManger.close();
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void create() {
        this.aiObdManger = new AILejaObdManger(BaseModuleApplication.getContext());
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void destroy() {
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void open() {
        AILejaObdManger aILejaObdManger = this.aiObdManger;
        if (aILejaObdManger != null) {
            aILejaObdManger.open();
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void registerListener(AIObdListener aIObdListener) {
        AILejaObdManger aILejaObdManger = this.aiObdManger;
        if (aILejaObdManger != null) {
            aILejaObdManger.registerObdListener(aIObdListener);
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void sendCommand(String str) {
        AILejaObdManger aILejaObdManger = this.aiObdManger;
        if (aILejaObdManger != null) {
            aILejaObdManger.sendCommand(str);
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void unregisterListener(AIObdListener aIObdListener) {
        AILejaObdManger aILejaObdManger = this.aiObdManger;
        if (aILejaObdManger != null) {
            aILejaObdManger.unregisterObdListener(aIObdListener);
        }
    }
}