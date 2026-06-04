package com.ileja.aibase;

import android.app.Application;
import android.content.Context;
import com.ileja.aibase.common.ThreadPoolManager;
import com.ileja.aibase.config.DebugConfig;
import com.ileja.aibase.http.http.HttpTrigger;
import com.tencent.mmkv.MMKV;

/* JADX INFO: loaded from: classes.dex */
public class AiBase {
    private static final String FILE = "/sdcard/Android/shared_prefs/";
    private static AiBase inst;
    private String channel;
    private Application context;
    private boolean isPhone = false;
    private double latitude;
    private double longitude;

    private AiBase() {
    }

    public static synchronized AiBase getInst() {
        if (inst == null) {
            inst = new AiBase();
        }
        return inst;
    }

    public void Init(final Application application) {
        this.context = application;
        MMKV.initialize(FILE);
        ThreadPoolManager.getInstance().addAsyncTask(new Runnable(this) { // from class: com.ileja.aibase.AiBase.1
            @Override // java.lang.Runnable
            public void run() {
                HttpTrigger.init(application);
            }
        });
        DebugConfig.MOCK_GPS_CALLBACK = AiBasePrefHelper.getGPSCallback(application);
    }

    public void clearCache(final Context context) {
        ThreadPoolManager.getInstance().addAsyncTask(new Runnable(this) { // from class: com.ileja.aibase.AiBase.2
            @Override // java.lang.Runnable
            public void run() {
                HttpTrigger.clearCache(context);
            }
        });
    }

    public String getChannel() {
        return this.channel;
    }

    public Application getContext() {
        Application application = this.context;
        if (application != null) {
            return application;
        }
        throw new RuntimeException("aibase not inited");
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public boolean isPhone() {
        return this.isPhone;
    }

    public void setChannel(String str) {
        this.channel = str;
    }

    public void setIsPhone(boolean z) {
        this.isPhone = z;
    }

    public void setLatLon(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }
}