package com.ileja.framework.service;

import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public abstract class PendingAction implements Runnable {
    private PendingActionType pendingActionType = PendingActionType.Normal;
    private String key = UUID.randomUUID().toString();

    public enum PendingActionType {
        Normal,
        Replace
    }

    public String getKey() {
        return this.key;
    }

    public PendingActionType getType() {
        return this.pendingActionType;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public void setType(PendingActionType pendingActionType) {
        this.pendingActionType = pendingActionType;
    }
}