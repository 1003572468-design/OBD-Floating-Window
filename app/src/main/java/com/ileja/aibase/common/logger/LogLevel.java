package com.ileja.aibase.common.logger;

/* JADX INFO: loaded from: classes.dex */
public enum LogLevel {
    NONE(0),
    RELEASE(1),
    FULL(2);

    int loglevel;

    LogLevel(int i) {
        this.loglevel = i;
    }

    public boolean canLog(LogLevel logLevel) {
        return this.loglevel >= logLevel.loglevel;
    }
}