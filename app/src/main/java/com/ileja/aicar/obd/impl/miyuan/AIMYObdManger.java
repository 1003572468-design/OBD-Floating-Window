package com.ileja.aicar.obd.impl.miyuan;

import android.content.Context;
import com.ileja.aicar.obd.AIObdListener;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class AIMYObdManger {
    private static final String TAG = "AIObd";
    private Set<AIObdListener> aiObdListeners = new HashSet();
    private AIMYObdReadThread aiObdReadThread;

    public AIMYObdManger(Context context) {
    }

    public void close() {
        AIMYObdReadThread aIMYObdReadThread = this.aiObdReadThread;
        if (aIMYObdReadThread != null) {
            aIMYObdReadThread.stop_();
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
        AIMYObdReadThread aIMYObdReadThread = new AIMYObdReadThread(this.aiObdListeners);
        this.aiObdReadThread = aIMYObdReadThread;
        try {
            aIMYObdReadThread.start_();
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
    }

    public void unregisterObdListener(AIObdListener aIObdListener) {
        if (aIObdListener != null) {
            this.aiObdListeners.remove(aIObdListener);
        }
    }
}