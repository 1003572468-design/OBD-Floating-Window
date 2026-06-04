package com.ileja.framework.service.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.ileja.aibase.config.ServerConfig;
import com.ileja.core.data.CommonConfig;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.log.event.EventLogBody;
import com.tendcloud.tenddata.TCAgent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class HFPageAnalyseService extends HFService {
    private static final String TAG = "HFPageAnalyseService";
    private static final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    /* JADX INFO: Access modifiers changed from: private */
    public void addLocalEvent(Application application, String str, String str2, Map<String, String> map) {
        EventLogBody eventLogBody = new EventLogBody(application);
        if (!TextUtils.isEmpty(str)) {
            try {
                eventLogBody.event = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            try {
                eventLogBody.label = URLEncoder.encode(str2, "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        if (map != null) {
            JSONObject jSONObject = new JSONObject();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                try {
                    jSONObject.put(entry.getKey(), entry.getValue());
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
            String string = jSONObject.toString();
            if (!TextUtils.isEmpty(string)) {
                try {
                    eventLogBody.param = URLEncoder.encode(string, "UTF-8");
                } catch (UnsupportedEncodingException e4) {
                    e4.printStackTrace();
                }
            }
        }
        eventLogBody.time = System.currentTimeMillis();
        HFLogService hFLogService = (HFLogService) HudSystem.getService(application, HFService.HFServiceName.HFLogService);
        if (hFLogService != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CommonConfig.Data, eventLogBody);
            hFLogService.addLogAsyn(bundle);
        }
    }

    private void init(final Context context, final String str) {
        mExecutorService.submit(new Runnable(this) { // from class: com.ileja.framework.service.core.HFPageAnalyseService.1
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.LOG_ON = false;
                TCAgent.init(context, ServerConfig.TALK_APP_ID, str);
            }
        });
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        init(application, ServerConfig.CHANNEL_ID);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFPageAnalyseService;
    }

    public void onPause(final Activity activity) {
        mExecutorService.submit(new Runnable(this) { // from class: com.ileja.framework.service.core.HFPageAnalyseService.5
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onPause(activity);
            }
        });
    }

    public void onResume(final Activity activity) {
        mExecutorService.submit(new Runnable(this) { // from class: com.ileja.framework.service.core.HFPageAnalyseService.4
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onResume(activity);
            }
        });
    }

    public void pageEnd(final Context context, final String str) {
        mExecutorService.submit(new Runnable(this) { // from class: com.ileja.framework.service.core.HFPageAnalyseService.3
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onPageEnd(context, str);
            }
        });
    }

    public void pageStart(final Context context, final String str) {
        mExecutorService.submit(new Runnable(this) { // from class: com.ileja.framework.service.core.HFPageAnalyseService.2
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onPageStart(context, str);
            }
        });
    }

    public void recordEvent(final Application application, final String str) {
        mExecutorService.submit(new Runnable() { // from class: com.ileja.framework.service.core.HFPageAnalyseService.6
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onEvent(application, str);
                HFPageAnalyseService.this.addLocalEvent(application, str, null, null);
            }
        });
    }

    public void recordEvent(final Application application, final String str, final String str2) {
        mExecutorService.submit(new Runnable() { // from class: com.ileja.framework.service.core.HFPageAnalyseService.7
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onEvent(application, str, str2);
                HFPageAnalyseService.this.addLocalEvent(application, str, str2, null);
            }
        });
    }

    public void recordEvent(final Application application, final String str, final Map<String, String> map) {
        mExecutorService.submit(new Runnable() { // from class: com.ileja.framework.service.core.HFPageAnalyseService.8
            @Override // java.lang.Runnable
            public void run() {
                Application application2 = application;
                String str2 = str;
                TCAgent.onEvent(application2, str2, str2, map);
                HFPageAnalyseService.this.addLocalEvent(application, str, null, map);
            }
        });
    }

    public void recordEvent(final Application application, final String str, final String str2, final Map<String, String> map) {
        mExecutorService.submit(new Runnable() { // from class: com.ileja.framework.service.core.HFPageAnalyseService.9
            @Override // java.lang.Runnable
            public void run() {
                TCAgent.onEvent(application, str, str2, map);
                HFPageAnalyseService.this.addLocalEvent(application, str, str2, map);
            }
        });
    }
}