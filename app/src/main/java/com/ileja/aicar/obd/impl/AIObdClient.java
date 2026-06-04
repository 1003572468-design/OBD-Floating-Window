package com.ileja.aicar.obd.impl;

import com.ileja.aicar.obd.AIObdListener;

/* JADX INFO: loaded from: classes.dex */
public abstract class AIObdClient {

    /* JADX INFO: renamed from: a */
    protected String f5954a = "abstract";

    public abstract void close();

    public abstract void create();

    public abstract void destroy();

    public String getName() {
        return this.f5954a;
    }

    public abstract void open();

    public abstract void registerListener(AIObdListener aIObdListener);

    public abstract void sendCommand(String str);

    public void setName(String str) {
        this.f5954a = str;
    }

    public abstract void unregisterListener(AIObdListener aIObdListener);
}