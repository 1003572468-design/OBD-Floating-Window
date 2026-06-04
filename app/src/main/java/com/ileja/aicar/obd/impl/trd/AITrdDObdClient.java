package com.ileja.aicar.obd.impl.trd;

import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.impl.AIObdClient;
import com.ileja.module.BaseModuleApplication;

/* JADX INFO: loaded from: classes.dex */
public class AITrdDObdClient extends AIObdClient {
    private AITrdObdManger aiObdManger;

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void close() {
        AITrdObdManger aITrdObdManger = this.aiObdManger;
        if (aITrdObdManger != null) {
            aITrdObdManger.close();
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void create() {
        this.aiObdManger = new AITrdObdManger(BaseModuleApplication.getContext());
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void destroy() {
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void open() {
        AITrdObdManger aITrdObdManger = this.aiObdManger;
        if (aITrdObdManger != null) {
            aITrdObdManger.open();
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void registerListener(AIObdListener aIObdListener) {
        AITrdObdManger aITrdObdManger = this.aiObdManger;
        if (aITrdObdManger != null) {
            aITrdObdManger.registerObdListener(aIObdListener);
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void sendCommand(String str) {
        AITrdObdManger aITrdObdManger = this.aiObdManger;
        if (aITrdObdManger != null) {
            aITrdObdManger.sendCommand(str);
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void unregisterListener(AIObdListener aIObdListener) {
        AITrdObdManger aITrdObdManger = this.aiObdManger;
        if (aITrdObdManger != null) {
            aITrdObdManger.unregisterObdListener(aIObdListener);
        }
    }
}