package com.ileja.aicar.obd.impl.leja;

import android.content.Context;
import com.ileja.aicar.obd.AIObdListener;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class AILejaObdManger {
    private static final String TAG = "AILejaObdManger";
    private Set<AIObdListener> aiObdListeners = new HashSet();
    private AILejaObdReader aiObdReadThread;

    public AILejaObdManger(Context context) {
    }

    public void close() {
        AILejaObdReader aILejaObdReader = this.aiObdReadThread;
        if (aILejaObdReader != null) {
            aILejaObdReader.stop();
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
        AILejaObdReader aILejaObdReader = new AILejaObdReader(this.aiObdListeners);
        this.aiObdReadThread = aILejaObdReader;
        try {
            aILejaObdReader.start();
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
        AILejaObdReader aILejaObdReader = this.aiObdReadThread;
        if (aILejaObdReader != null) {
            aILejaObdReader.sendCmd(str);
        }
    }

    public void unregisterObdListener(AIObdListener aIObdListener) {
        if (aIObdListener != null) {
            this.aiObdListeners.remove(aIObdListener);
        }
    }
}