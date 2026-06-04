package com.ileja.aicar.obd.impl.miyuan;

import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.impl.AIObdClient;
import com.ileja.module.BaseModuleApplication;

/* JADX INFO: loaded from: classes.dex */
public class AIMYObdClient extends AIObdClient {
    private AIMYObdManger aiObdManger;

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void close() {
        AIMYObdManger aIMYObdManger = this.aiObdManger;
        if (aIMYObdManger != null) {
            aIMYObdManger.close();
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void create() {
        this.aiObdManger = new AIMYObdManger(BaseModuleApplication.getContext());
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void destroy() {
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void open() {
        AIMYObdManger aIMYObdManger = this.aiObdManger;
        if (aIMYObdManger != null) {
            aIMYObdManger.open();
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void registerListener(AIObdListener aIObdListener) {
        AIMYObdManger aIMYObdManger = this.aiObdManger;
        if (aIMYObdManger != null) {
            aIMYObdManger.registerObdListener(aIObdListener);
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void sendCommand(String str) {
        AIMYObdManger aIMYObdManger = this.aiObdManger;
        if (aIMYObdManger != null) {
            aIMYObdManger.sendCommand(str);
        }
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void unregisterListener(AIObdListener aIObdListener) {
        AIMYObdManger aIMYObdManger = this.aiObdManger;
        if (aIMYObdManger != null) {
            aIMYObdManger.unregisterObdListener(aIObdListener);
        }
    }
}