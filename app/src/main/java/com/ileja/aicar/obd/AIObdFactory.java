package com.ileja.aicar.obd;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.phone.DeviceUtil;
import com.ileja.aicar.obd.impl.AIObdClient;
import com.ileja.aicar.obd.impl.dina.AIDinaObdClient;
import com.ileja.aicar.obd.impl.leja.AILejaObdClient;
import com.ileja.aicar.obd.impl.miyuan.AIMYObdClient;
import com.ileja.aicar.obd.impl.trd.AITrdDObdClient;

/* JADX INFO: loaded from: classes.dex */
public class AIObdFactory extends AIObdClient {
    private static final String LEJA = "carrobot";
    private static final String MIYUANOBD = "miyuan";
    private static final String TAG = "AIObdFactory";
    private static final String TRD = "suruide";

    /* JADX INFO: renamed from: b */
    AIObdClient f5937b;
    private Handler mHandler;
    private HandlerThread mThreadHandler;

    public AIObdFactory() {
        AILog.m4028d(TAG, "create obd factory");
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.1
            @Override // java.lang.Runnable
            public void run() {
                String oBDType = AIObdFactory.this.getOBDType();
                if (AIObdFactory.MIYUANOBD.equals(oBDType)) {
                    AILog.m4029d(AIObdFactory.TAG, "create obd factory is miyuan", LogLevel.RELEASE);
                    AIObdFactory.this.f5937b = new AIMYObdClient();
                } else if (AIObdFactory.TRD.equals(oBDType)) {
                    AILog.m4029d(AIObdFactory.TAG, "create obd factory is trd", LogLevel.RELEASE);
                    AIObdFactory.this.f5937b = new AITrdDObdClient();
                } else if (AIObdFactory.LEJA.equals(oBDType)) {
                    AILog.m4029d(AIObdFactory.TAG, "create obd factory is leja", LogLevel.RELEASE);
                    AIObdFactory.this.f5937b = new AILejaObdClient();
                } else {
                    AILog.m4029d(AIObdFactory.TAG, "create obd factory is dina", LogLevel.RELEASE);
                    AIObdFactory.this.f5937b = new AIDinaObdClient();
                }
            }
        });
    }

    private void createOBDHandler() {
        if (this.mThreadHandler == null) {
            HandlerThread handlerThread = new HandlerThread("Carrobot_OBD");
            this.mThreadHandler = handlerThread;
            handlerThread.start();
            this.mHandler = new Handler(this.mThreadHandler.getLooper());
            AILog.m4035i(TAG, "createOBDHandler", LogLevel.RELEASE);
        }
    }

    private void destroyOBDHandler() {
        HandlerThread handlerThread = this.mThreadHandler;
        if (handlerThread == null || handlerThread.getLooper() == null) {
            return;
        }
        this.mThreadHandler.getLooper().quit();
        this.mThreadHandler = null;
        AILog.m4035i(TAG, "destroyOBDHandler", LogLevel.RELEASE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getOBDType() {
        String systemConf = "";
        while (TextUtils.isEmpty(systemConf)) {
            systemConf = DeviceUtil.getSystemConf("leja.obd.type");
            AILog.m4029d(TAG, "read obd = " + systemConf, LogLevel.RELEASE);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return systemConf;
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void close() {
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.7
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.close();
                }
                AILog.m4035i(AIObdFactory.TAG, "close obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
        destroyOBDHandler();
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void create() {
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.2
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.create();
                }
                AILog.m4035i(AIObdFactory.TAG, "create obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void destroy() {
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.3
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.destroy();
                }
                AILog.m4035i(AIObdFactory.TAG, "destroy obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void open() {
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.6
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.open();
                }
                AILog.m4035i(AIObdFactory.TAG, "open obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void registerListener(final AIObdListener aIObdListener) {
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.4
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.registerListener(aIObdListener);
                }
                AILog.m4035i(AIObdFactory.TAG, "registerListener obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void sendCommand(final String str) {
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.8
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.sendCommand(str);
                }
                AILog.m4035i(AIObdFactory.TAG, "sendCommand obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void unregisterListener(final AIObdListener aIObdListener) {
        createOBDHandler();
        this.mHandler.post(new Runnable() { // from class: com.ileja.aicar.obd.AIObdFactory.5
            @Override // java.lang.Runnable
            public void run() {
                AIObdClient aIObdClient = AIObdFactory.this.f5937b;
                if (aIObdClient != null) {
                    aIObdClient.unregisterListener(aIObdListener);
                }
                AILog.m4035i(AIObdFactory.TAG, "unregisterListener obdClient : " + AIObdFactory.this.f5937b, LogLevel.RELEASE);
            }
        });
    }
}