package com.ileja.aibase.common.logger;

/* JADX INFO: loaded from: classes.dex */
public final class Settings {
    private int methodCount = 2;
    private boolean showThreadInfo = true;
    private int methodOffset = 0;
    private LogLevel logLevel = LogLevel.FULL;

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public int getMethodCount() {
        return this.methodCount;
    }

    public int getMethodOffset() {
        return this.methodOffset;
    }

    public Settings hideThreadInfo() {
        this.showThreadInfo = false;
        return this;
    }

    public boolean isShowThreadInfo() {
        return this.showThreadInfo;
    }

    public Settings setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public Settings setMethodCount(int i) {
        if (i < 0) {
            i = 0;
        }
        this.methodCount = i;
        return this;
    }

    public Settings setMethodOffset(int i) {
        this.methodOffset = i;
        return this;
    }
}