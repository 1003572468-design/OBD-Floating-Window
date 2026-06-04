package com.ileja.aicar.obd.impl.trd;

import android.content.Context;
import com.ileja.aicar.obd.AIObdListener;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class AITrdObdManger {
    private static final String TAG = "AIObd";
    private Set<AIObdListener> aiObdListeners = new HashSet();
    private AITrdObdReader aiObdReadThread;

    public AITrdObdManger(Context context) {
    }

    public void close() {
        AITrdObdReader aITrdObdReader = this.aiObdReadThread;
        if (aITrdObdReader != null) {
            aITrdObdReader.stop();
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void open() {
        if (this.aiObdReadThread != null) {
            return;
        }
        AITrdObdReader aITrdObdReader = new AITrdObdReader(this.aiObdListeners);
        this.aiObdReadThread = aITrdObdReader;
        try {
            aITrdObdReader.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerObdListener(AIObdListener aIObdListener) {
        if (aIObdListener != null) {
            this.aiObdListeners.add(aIObdListener);
        }
    }

    public void sendCommand(String str) {
        AITrdObdReader aITrdObdReader = this.aiObdReadThread;
        if (aITrdObdReader != null) {
            aITrdObdReader.sendCmd(str);
        }
    }

    public void unregisterObdListener(AIObdListener aIObdListener) {
        if (aIObdListener != null) {
            this.aiObdListeners.remove(aIObdListener);
        }
    }
}