package com.ileja.framework.service;

import android.content.ComponentName;
import android.os.IBinder;

/* JADX INFO: loaded from: classes.dex */
public interface HFServiceCallback {
    void onServiceConnected(ComponentName componentName, IBinder iBinder);

    void onServiceDisconnected(ComponentName componentName);
}