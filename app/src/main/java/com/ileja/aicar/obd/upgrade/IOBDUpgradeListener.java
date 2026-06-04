package com.ileja.aicar.obd.upgrade;

/* JADX INFO: loaded from: classes.dex */
public interface IOBDUpgradeListener {
    void onDownloadFinished(String str, String str2, String str3);

    void onDownloadProgress(long j, long j2);

    void onDownloadStart();

    void onError(String str, String str2);

    void onHasNewVersion(boolean z, String str);

    void onUnzipFinished(String str, String str2, String str3);
}