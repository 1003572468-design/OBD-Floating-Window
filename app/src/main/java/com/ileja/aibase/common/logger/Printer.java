package com.ileja.aibase.common.logger;

/* JADX INFO: loaded from: classes.dex */
public interface Printer {
    /* JADX INFO: renamed from: d */
    void mo4056d(String str, Object... objArr);

    /* JADX INFO: renamed from: e */
    void mo4057e(String str, Object... objArr);

    /* JADX INFO: renamed from: e */
    void mo4058e(Throwable th, String str, Object... objArr);

    Settings getSettings();

    /* JADX INFO: renamed from: i */
    void mo4059i(String str, Object... objArr);

    Settings init(String str);

    void json(String str);

    /* JADX INFO: renamed from: t */
    Printer mo4060t(String str, int i);

    /* JADX INFO: renamed from: v */
    void mo4061v(String str, Object... objArr);

    /* JADX INFO: renamed from: w */
    void mo4062w(String str, Object... objArr);

    void wtf(String str, Object... objArr);

    void xml(String str);
}